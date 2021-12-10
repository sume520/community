package com.sun.community.dao;

import com.sun.community.CommunityApplication;
import com.sun.community.entity.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class
CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void selectCommentsByEntity() {
        List<Comment> comments=commentMapper.selectCommentsByEntity(1,280,0,100);
        System.out.println(comments.size());
    }

    @Test
    public void selectCountByEntity() {
        int count= commentMapper.selectCountByEntity(1,280);
        System.out.println(count);
    }
}