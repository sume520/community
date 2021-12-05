package com.sun.community.dao;

import com.sun.community.CommunityApplication;
import com.sun.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectById() {
        User user=userMapper.selectById(101);
        System.out.println(user);
    }

    @Test
    public void selectByName() {
        User user=userMapper.selectByName("liubei");
        System.out.println(user);
    }

    @Test
    public void selectByEmail() {
        User user=userMapper.selectByEmail("nowcoder145@sina.com");
        System.out.println(user);
    }

    @Test
    public void insertUser() {
        User user=new User();
        user.setUsername("张三");
        user.setPassword("12345");
        int id=userMapper.insertUser(user);
        System.out.println(id);
    }

    @Test
    public void updateStatus() {
        userMapper.updateStatus(150,1);
    }

    @Test
    public void updateHeader() {
        userMapper.updateHeader(150,"http://images.nowcoder.com/head/150t.png");
    }

    @Test
    public void updatePassword() {
        userMapper.updatePassword(150,"abc123");
    }
}