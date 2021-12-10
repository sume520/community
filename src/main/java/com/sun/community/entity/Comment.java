package com.sun.community.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
