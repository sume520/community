package com.sun.community.dao;

import com.sun.community.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id=#{id}")
    User selectById(int id);

    @Select("SELECT * FROM user WHERE username=#{username}")
    User selectByName(String username);

    @Select("SELECT * FROM user WHERE email=#{email}")
    User selectByEmail(String email);

    @Insert("INSERT INTO user (username, password, salt, email, type, status, activation_code, header_url, create_time)" +
            "VALUES (#{user.username},#{user.password},#{user.salt},#{user.email},#{user.type},#{user.status}," +
            "#{user.activationCode},#{user.headerUrl},#{user.createTime})")
    int insertUser(@Param("user") User user);

    @Update("UPDATE user SET status=#{status} WHERE id=#{id}")
    int updateStatus(int id, int status);

    @Update("UPDATE user SET header_url=#{headerUrl} WHERE id=#{id}")
    int updateHeader(int id, String headerUrl);

    @Update("UPDATE user SET password=#{password} WHERE id=#{id}")
    int updatePassword(int id, String password);

}
