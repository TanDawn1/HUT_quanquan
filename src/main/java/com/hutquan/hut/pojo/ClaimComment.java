package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 寻物评论表
 */
@Data
public class ClaimComment implements Serializable {

    private Integer claimCId;

    private Integer cId;
    //评论
    private String message;

    private Integer userId;

    private Integer toUserId;

    private Long time;

}
