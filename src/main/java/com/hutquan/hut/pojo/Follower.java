package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 关注者
 */
@Data
public class Follower implements Serializable {

    private Integer userId;

    private String username;
    //头像地址
    private String avatarPicture;
    //个性签名
    private String signature;

    private String sex;

}
