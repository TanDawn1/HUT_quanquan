package com.hutquan.hut.service.impl;

import com.alibaba.fastjson.JSON;
import com.hutquan.hut.mapper.IUserMapper;
import com.hutquan.hut.pojo.Follower;
import com.hutquan.hut.pojo.FollowerPage;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.pojo.Xh;
import com.hutquan.hut.service.IUserService;
import com.hutquan.hut.utils.FileUtil;
import com.hutquan.hut.utils.RedisPagination;
import com.hutquan.hut.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    private static final String FOLLOW = "follow:";

    private static final String SELFFOLLOW = "selfFollow:";

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private RedisPagination redisPagination;

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
        MultipartFile[] multipartFiles = {file};
        String dynamicPhotos ="";
//        String originalFileName = file.getOriginalFilename();
//        String newfileName = UUID.randomUUID() + "-" +user.getUserId()
//                +originalFileName.substring(originalFileName.lastIndexOf("."));
//        File newfile = new File(headPhoto + newfileName);
        if(file != null){
            //上传图片
            dynamicPhotos = FileUtil.fileUpload(user.getUserId(),multipartFiles,0);
            if( dynamicPhotos == null || dynamicPhotos.equals("")) return null;
        }
        try {
            iUserMapper.upHeadUrl(dynamicPhotos,user.getUserId());
            return dynamicPhotos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Follower> queryFollower(Integer userId, Long l, Long l1) {
        if(userId == null) return null;
        //Set<Object> followId = redisUtils.zRange(FOLLOW+userId,l, l1);
          List<Object> followId = redisUtils.lGet(SELFFOLLOW + userId,l,l1);
        return iUserMapper.queryFollower(followId);
    }

    @Override
    public FollowerPage queryFollowered(Integer userId, Long pageNum) {
        if(userId == null) return null;
        FollowerPage followerPage = redisPagination.redisPage(pageNum,userId);
        if (followerPage == null) return null;
        Set<Object> followedId = redisUtils.zRange(FOLLOW + userId,followerPage.getStart(),followerPage.getEnd());
        if(followedId == null || followedId.size() == 0) return null;
        List<Follower> list = iUserMapper.queryFollowered(followedId);
        followerPage.setSize(list.size());
        followerPage.setFollowers(list);
        return followerPage;
    }

    @Override
    public Long querySelfFollow(User user) {

        return redisUtils.llen(SELFFOLLOW + user.getUserId().toString());
    }

    @Override
    public Long querySelfFollowed(User user) {
        return redisUtils.zscard(FOLLOW + user.getUserId().toString());
    }

    @Override
    public Boolean followed(Integer user1Id, Integer user2Id) {
        //判断用户1是否关注用户2 返回null则说明没有关注
        return redisUtils.zscore(FOLLOW + user2Id, user1Id) != null;
    }


}
