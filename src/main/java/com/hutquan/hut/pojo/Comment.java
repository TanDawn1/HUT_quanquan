package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Comment implements Serializable {

    private int commentId;
    //评论
    private String commentMessage;

    private int dynamicId;

    private int dynamicChildId;

    private int userId;

    private User user;

}
