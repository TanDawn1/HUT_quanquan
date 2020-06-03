package com.hutquan.hut.service.impl;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ITeleService;
import org.springframework.stereotype.Service;

@Service
public class TeleServiceImpl implements ITeleService {


    @Override
    public boolean selectUser(String tele) {
        return false;
    }

    @Override
    public User teleLogin(String tele, String code) {
        return null;
    }
}
