package com.sun.community.dao;

import com.sun.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // offset 每页起始行 limit 每页帖子数量
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit,int orderMode);

    int selectDiscussPostRows(int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id,int commentCount);

    int updateType(int id,int type);

    int updateStatus(int id,int status);

    int updateScore(int id, double score);
}
