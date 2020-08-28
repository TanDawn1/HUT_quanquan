package com.hutquan.hut.service.impl;

import com.hutquan.hut.mapper.IUserMapper;
import com.hutquan.hut.pojo.Follower;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IUserService;
import com.hutquan.hut.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Value("${upload.headPortrait.dir}")
    private  String headPhoto;

    private static final String FOLLOW = "follow:";

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public User selectUser(int userId) {
        return iUserMapper.selectUser(userId);
    }

    @Override
    public int insertUser(User user) {
        user.setTime(Instant.now().getEpochSecond());
        //存在相同手机号或者相同账号就return 0
        if(iUserMapper.selectTele(user.getTele(),user.getTele()) != null){
            return 0;
        }else{
            //返回主键的值
//            iUserMapper.insertUser(user);
//            //新建Redis数据库中的该用户的关注库并自动给官方账号添加关注
//            redisUtils.sSet("userConcernId:"+user.getUserId(),1);
            //这里直接返回1
            return iUserMapper.insertUser(user);
        }
    }

    @Override
    public User login(User user) {

        return iUserMapper.login(user);

    }

    @Override
    public Boolean updataUser(User user) {
        if(iUserMapper.updataUser(user) == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String updataHeadPhoto(User user, MultipartFile file) {

        String originalFileName = file.getOriginalFilename();
        String newfileName = UUID.randomUUID() + "-" +user.getUserId()
                +originalFileName.substring(originalFileName.lastIndexOf("."));
        File newfile = new File(headPhoto + newfileName);

        try {
            //写入磁盘
            file.transferTo(newfile);
            iUserMapper.upHeadUrl(newfileName,user.getUserId());
            return newfileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Follower> queryFollower(Integer userId, Long l, Long l1) {

        Set<Object> followId = redisUtils.zRange(FOLLOW+userId,l, l1);

        return iUserMapper.queryFollower(followId);
    }


}
