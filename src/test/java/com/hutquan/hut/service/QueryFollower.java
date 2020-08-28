package com.hutquan.hut.service;

import com.hutquan.hut.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class QueryFollower {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IWithFriendsService iWithFriendsService;

    @Test
    public void setiUserService(){

        System.out.println(iUserService.queryFollower(0,0L,-1L));
    }

    @Test
    public void dynamicsByHot(){

        System.out.println(iWithFriendsService.dynamicsByHot(null));
        User user = new User();
        user.setUserId(0);
        System.out.println(iWithFriendsService.dynamicsByHot(user));
    }

}
