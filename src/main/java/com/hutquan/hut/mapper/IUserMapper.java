package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Follower;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.pojo.Xh;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


/**
 * 登录与注册
 */
@Repository
public interface IUserMapper {


    User selectUser(int userId);

    Integer teleSelectUser(String tele);

    //返回的是匹配行数 1
    int insertUser(User user);

    Integer selectTele(String tele,String account);

    User login(User user);

    User logina(String account);

    //验证码通过Redis判断了
    User teleLogin(String tele);

    int insertTele(String tele, String yzm, LocalDateTime time);
    //统一更新个人资料的接口
    int updataUser(User user);

    int upHeadUrl(String headUrl,int userId);

    List<Follower> queryFollower(Set<Object> idList);

    User selectXh(String xh);

    User selectXhAndPass(Xh xhl);
}
