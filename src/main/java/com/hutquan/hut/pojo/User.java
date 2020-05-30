package com.hutquan.hut.pojo;

import lombok.Data;

@Data
public class User {

    private int userId;
    //学号
    private String stuNumber;

    private String username;

    private String passwd;

    private String account;
    //头像地址
    private String avatar_picture;
    //个性签名
    private String signature;

    private int followers;

}
