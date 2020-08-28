package com.hutquan.hut.pojo;

import lombok.Data;

/**
 * 人脸搜索后的结果存储
 * 为了区分
 */
@Data
public class UserSearchFace {

    private Integer userId;

    private String username;
    //头像地址
    private String avatarPicture;
    //个性签名
    private String signature;

    private String sex;
    //匹配值
    private Float score;
    //关注该用户的人数
    private Double followCount;
    //该用户关注的人数
    private Double selfFollowCount;
}
