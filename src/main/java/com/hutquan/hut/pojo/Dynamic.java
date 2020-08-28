package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 动态表
 */
@Data
public class Dynamic implements Serializable {

    private Integer dynamicId;

    private String message;

    private Integer userId;

    private Long time;

    private String images;

    //private List<String> imagesList;

    private String label;

    private Integer starCount;

    private Integer commentCount;

    private User user;

    //附近的人-> 距离字段
    private Double distance;

    //是否已经点赞
    private boolean like;

    //是否是自己的动态
    private boolean self;

}
