package com.sun.community.util;

import com.sun.community.entity.User;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

/**
 * 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {
    //根据线程存储数据
    private ThreadLocal<User> users=new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser() {

        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
