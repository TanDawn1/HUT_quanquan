package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 动态评论表
 */
@Data
public class Comment implements Serializable {

    private Integer commentId;
    //评论
    private String message;

    private Integer dynamicId;

    private Integer userId;

    private Integer toUserId;

    private Long time;

    private String userName;

    private String toUserName;

}
