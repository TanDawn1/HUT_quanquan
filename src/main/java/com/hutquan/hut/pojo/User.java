package com.hutquan.hut.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {

    private int userId;
    //学号
    private String stuNumber;

    private String username;

    private String passwd;

    private String account;
    //头像地址
    private String avatarPicture;
    //个性签名
    private String signature;

    private int followers;

    private String sex;

    private String tele;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

}
