package com.sun.community.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

    //权限集合
 /*   @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list=new ArrayList<>();
        list.add((GrantedAuthority) () -> {
            switch(type){
                case 1:
                    return "ADMIN";
                default:
                    return "USER";
            }
        });
        return null;
    }*/

//    //true:账号未过期
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    //true:账号未锁定
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    //true:凭证未过期
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
