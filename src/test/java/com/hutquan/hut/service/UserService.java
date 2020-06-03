package com.hutquan.hut.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserService {

    @Autowired
    private IUserService iUserService;

    @Test
    public void userService(){
        System.out.println(iUserService.selectUser(1));
    }

}
