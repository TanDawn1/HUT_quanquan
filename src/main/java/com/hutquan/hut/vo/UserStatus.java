package com.hutquan.hut.vo;

import com.hutquan.hut.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatus {

    private int code;

    private String message;

    private User user;



}
