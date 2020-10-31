package com.hutquan.hut.service;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.pojo.Xh;
import com.hutquan.hut.vo.ResponseBean;

public interface ITeleService {

    boolean selectUser(String tele);

    User  teleLogin(String tele,String yzm);

    String sendTele(String tele);

    ResponseBean xhLogin(Xh xhl);
}
