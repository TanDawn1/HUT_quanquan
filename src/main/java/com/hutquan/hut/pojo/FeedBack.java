package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 反馈表
 */
@Data
public class FeedBack implements Serializable {

    private Integer tId;

    private Integer userId;

    private String QQ;

    private String message;

    private String images;

    private Long time;

}
