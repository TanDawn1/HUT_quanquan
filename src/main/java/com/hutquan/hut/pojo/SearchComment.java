package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 寻物评论表
 */
@Data
public class SearchComment implements Serializable {

    private Integer searchCId;

    private Integer sId;
    //评论
    private String message;

    private Integer userId;

    private Integer toUserId;

    private Long time;

}
