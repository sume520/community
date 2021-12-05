package com.sun.community.dao;

import com.sun.community.CommunityApplication;
import com.sun.community.entity.DiscussPost;
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
public class DiscussPostMapperTest {
    @Autowired
    private DiscussPostMapper mapper;

    @Test
    public void selectDiscussPosts() {
        List<DiscussPost> list=mapper.selectDiscussPosts(0,0,10);
        for (DiscussPost discussPost : list) {
            System.out.println(discussPost);
        }
    }

    @Test
    public void selectDiscussPostRows() {
        System.out.println(mapper.selectDiscussPostRows(101));
    }
}