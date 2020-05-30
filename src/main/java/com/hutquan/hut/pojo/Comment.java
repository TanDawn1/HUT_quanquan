package com.hutquan.hut.pojo;

import lombok.Data;

@Data
public class Comment {

    private int commentId;
    //评论
    private String commentMessage;

    private int dynamicId;

    private int dynamicChildId;

    private int userId;

}
