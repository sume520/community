package com.sun.community.service;

import com.sun.community.CommunityApplication;
import com.sun.community.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchServiceTest {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Test
    public void searchDiscussPost() throws IOException {
        Map<String, Object> discusses = elasticsearchService.searchDiscussPost("互联网寒冬", 1, 10);
        List<DiscussPost> postList = (List<DiscussPost>) discusses.get("posts");
        if(postList!=null){
            for (DiscussPost post:postList){
                System.out.println(post);
            }
        }
    }
}