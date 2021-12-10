package com.sun.community.dao;

import com.sun.community.CommunityApplication;
import com.sun.community.entity.Message;
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
public class MessageMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void selectConversations() {
        List<Message> messages=messageMapper.selectConversations(111,0,100);
        System.out.println(messages.get(0));
    }

    @Test
    public void selectConversationCount() {
        int count= messageMapper.selectConversationCount(111);
        System.out.println(count);
    }

    @Test
    public void selectLetters() {
        List<Message> messages=messageMapper.selectLetters("111_112",0,100);
        System.out.println(messages.get(1));
    }

    @Test
    public void selectLetterCount() {
        int count= messageMapper.selectLetterCount("111_112");
        System.out.println(count);
    }

    @Test
    public void selectLetterUnreadCount() {
    }

    @Test
    public void insertMessage() {
    }

    @Test
    public void updateStatus() {
    }
}