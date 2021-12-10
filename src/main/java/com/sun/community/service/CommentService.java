package com.sun.community.service;

import com.sun.community.controller.DiscussPostController;
import com.sun.community.dao.CommentMapper;
import com.sun.community.entity.Comment;
import com.sun.community.util.CommunityConstant;
import com.sun.community.util.SensitiveFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements CommunityConstant {
    private final Logger logger = LoggerFactory.getLogger(DiscussPostController.class);

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        //更新帖子评论数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            logger.debug("count: "+count);
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }

        logger.debug("entity id " + comment.getEntityId() + " entity type" + comment.getEntityType() + " comment count " +
                commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId()));


        return rows;
    }
}
