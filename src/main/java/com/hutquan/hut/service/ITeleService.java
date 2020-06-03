package com.hutquan.hut.service;

import com.hutquan.hut.pojo.User;

public interface ITeleService {

    boolean selectUser(String tele);

    User  teleLogin(String tele,String yzm);

    String sendTele(String tele);
}
