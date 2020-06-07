package com.hutquan.hut.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.mapper.IWithFriensMapper;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IWithFriendsService;
import com.hutquan.hut.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WithFriendsServiceImpl implements IWithFriendsService {

    @Autowired
    private IWithFriensMapper iWithFriensMapper;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 通过like数来排序查找
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<Dynamic> dynamicsByLike(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Dynamic> list = iWithFriensMapper.dynamicsByLike();
        List<String> photoDir = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            list.get(i).setImagesList(JSON.parseArray(list.get(i).getImages(), String.class));
//            for(String image: list.get(i).getImagesList()){
//                photoDir.add(image);
//            }
//            list.get(i).setImagesList(photoDir);
            list.get(i).setImages(null);
        }
        PageInfo<Dynamic> page = new PageInfo<>(list);

        return page;
    }

    /**
     * 添加关注
     * @param user
     * @param concernUserId
     * @return
     */
    @Override
    public boolean addConcern(User user, int concernUserId) {

        if(redisUtils.sSet("userConcernId"+user.getUserId(),concernUserId) != 0){
            return true;
        }
        return false;
    }

    /**
     * 取消关注
     * @param user
     * @param concernUserId
     * @return
     */
    @Override
    public boolean remConcern(User user, int concernUserId) {

        if(redisUtils.setRemove("userConcernId"+user.getUserId(),concernUserId) != 0){
            return true;
        }
        return false;
    }
}
