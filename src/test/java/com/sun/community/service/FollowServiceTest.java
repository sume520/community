package com.sun.community.service;

import com.sun.community.CommunityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class FollowServiceTest {

    @Autowired
    private FollowService followService;

    @Test
    public void follow() {
        int userId=10;
        int entityType=3;
        int entityId=111;
        followService.follow(userId,entityType,entityId);
    }

    @Test
    public void unfollow() {
    }
}