package com.sun.community.dao;

import com.sun.community.CommunityApplication;
import com.sun.community.entity.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class LoginTicketMapperTest {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void insertLoginTicket() {
        LoginTicket ticket=new LoginTicket();
        ticket.setTicket("Ticket");
        ticket.setStatus(0);
        ticket.setExpired(new Date());
        int rows=loginTicketMapper.insertLoginTicket(ticket);
        System.out.println(rows);
    }

    @Test
    public void selectByTicket() {
        LoginTicket ticket=loginTicketMapper.selectByTicket("c3a3170f38a84f3eb78b167c3afaf5b6");
        System.out.println(ticket);
    }

    @Test
    public void updateStatus() {
        int rows= loginTicketMapper.updateStatus("Ticket",1);
        System.out.println(rows);
    }
}