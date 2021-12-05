package com.sun.community.service;

import com.sun.community.dao.DiscussPostMapper;
import com.sun.community.entity.DiscussPost;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    public int findDiscussPostsRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
