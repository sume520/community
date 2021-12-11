package com.sun.community.service;

import com.sun.community.CommunityApplication;
import com.sun.community.entity.User;
import com.sun.community.util.HostHolder;
import com.sun.community.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void like() {
    }

    @Test
    public void findEntityLikeCount() {
    }

    @Test
    public void findEntityLikeStatus() {
    }
}