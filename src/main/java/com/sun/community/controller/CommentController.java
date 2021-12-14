package com.sun.community.controller;

import com.sun.community.entity.Comment;
import com.sun.community.entity.DiscussPost;
import com.sun.community.entity.Event;
import com.sun.community.event.EventProducer;
import com.sun.community.service.CommentService;
import com.sun.community.service.DiscussPostService;
import com.sun.community.util.CommunityConstant;
import com.sun.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @PostMapping("/add/{discussPost}")
    public String addComment(@PathVariable("discussPost") int discussId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityId(comment.getEntityId())
                .setEntityType(comment.getEntityType())
                .setEntityUserId(comment.getTargetId())
                .setData("postId", discussId); //记录评论所在的帖子

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());//获取所评论的帖子的用户id
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());//获取回复的评论的用户id
        }
        eventProducer.fireEvent(event);

        if(comment.getEntityType()==ENTITY_TYPE_POST){
            //触发发帖事件
            Event evetn=new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussId);
            eventProducer.fireEvent(evetn);
        }

        return "redirect:/discuss/detail/" + discussId;
    }
}
