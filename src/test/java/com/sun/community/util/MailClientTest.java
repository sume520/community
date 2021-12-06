package com.sun.community.util;

import com.sun.community.CommunityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailClientTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void sendMail() {
        mailClient.sendMail("sunxiaoABC@outlook.com","Test","Welcome.");
    }

    @Test
    public void testHtmlMail(){
        Context context=new Context();
        context.setVariable("username","sunday");

        String content=templateEngine.process("/mail/demo",context);
        System.out.println(content);

        mailClient.sendMail("sunxiaoABC@outlook.com","HTML",content);
    }
}