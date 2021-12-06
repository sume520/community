package com.sun.community.dao;

import com.sun.community.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id=#{id}")
    User selectById(int id);

    @Select("SELECT * FROM user WHERE username=#{username}")
    User selectByName(String username);

    @Select("SELECT * FROM user WHERE email=#{email}")
    User selectByEmail(String email);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO user (username, password, salt, email, type, status, activation_code, header_url, create_time)" +
           "VALUES (#{username},#{password},#{salt},#{email},#{type},#{status}," +
          "#{activationCode},#{headerUrl},#{createTime})")
    int insertUser(User user);

    @Update("UPDATE user SET status=#{status} WHERE id=#{id}")
    int updateStatus(int id, int status);

    @Update("UPDATE user SET header_url=#{headerUrl} WHERE id=#{id}")
    int updateHeader(int id, String headerUrl);

    @Update("UPDATE user SET password=#{password} WHERE id=#{id}")
    int updatePassword(int id, String password);

}
