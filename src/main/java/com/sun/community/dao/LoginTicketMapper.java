package com.sun.community.dao;

import com.sun.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@Deprecated
public interface LoginTicketMapper {

    @Insert({
            "insert login_ticket (user_id, ticket, status, expired) ",
            "values (#{userId},#{ticket},#{status},#{expired})"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select("select id, user_id, ticket, status, expired from login_ticket where user_id=#{userId}")
    List<LoginTicket> selectByUserId(int userId);

    @Select("select id, user_id, ticket, status, expired from login_ticket where ticket=#{ticket}")
    LoginTicket selectByTicket(String ticket);

    @Update("update login_ticket set status=#{status} where ticket=#{ticket} and status=0")
    int updateStatus(String ticket, int status);

}
