package com.sun.community.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;
}
