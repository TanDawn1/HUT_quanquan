package com.hutquan.hut.service;

import com.hutquan.hut.pojo.Active;
import com.hutquan.hut.pojo.CaptchaVO;
import com.hutquan.hut.pojo.Comment;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.vo.PageBean;

public interface IActiveService {

    //返回剩余名额数
    Double signActive(User user, Integer activeId);

    //查找最新的活动
    PageBean<Active> actives(int pageNum);

    //活动详情
    Active activeDetailed(int activeId);

    //验证码
    CaptchaVO getCaptchaVO();
}
