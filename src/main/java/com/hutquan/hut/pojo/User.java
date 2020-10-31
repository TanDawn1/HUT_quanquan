package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 */
@Data
public class User implements Serializable {

    private Integer userId;

    private String tele;

    private String username;

    private String passwd;
    //头像地址
    private String avatarPicture;
    //个性签名
    private String signature;

    private String sex;
    //学号
    private String xh;

    private Long time;
    //附近的人-> 距离字段
    private Double distance;
    //关注该用户的人数
    private Double followCount;
    //该用户关注的人数
    private Double selfFollowCount;
    //是否被查首页的用户关注
    private Boolean followed;

}
