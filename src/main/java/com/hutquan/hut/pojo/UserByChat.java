package com.hutquan.hut.pojo;

import lombok.Data;

/**
 * 聊天的封装
 */
@Data
public class UserByChat {

    private Integer ucId;

    private User fromuser;

    private User touser;

    private String message;

    private Long time;

    private MessageType type;

    public enum MessageType{
        CHAT,
        JOIN,
        LEAVE
    }

}
