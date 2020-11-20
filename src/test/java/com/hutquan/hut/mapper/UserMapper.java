package com.hutquan.hut.mapper;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapper {

    @Autowired
    private IUserMapper iUserMapper;

    @Test
    public void selectUser(){
        System.out.println(iUserMapper.selectUser(1));
    }

}
