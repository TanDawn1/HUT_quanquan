package com.hutquan.hut.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.mapper.IWithFriensMapper;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IWithFriendsService;
import com.hutquan.hut.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WithFriendsServiceImpl implements IWithFriendsService {

    @Value("${uploade.dynamicPhoto.dir}")
    private  String dynamicPhoto;

    private static final String LIKE = "like:";

    private static final String FLLOW = "fllow:";

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
    public List<Dynamic> dynamicsByLike(int pageNum, int pageSize) {
        //PageHelper.startPage(pageNum,pageSize);
//        List<Dynamic> list = iWithFriensMapper.dynamicsByLike();
//        List<String> photoDir = new ArrayList<>();
//        for(int i = 0; i < list.size(); i++) {
//            list.get(i).setImagesList(JSON.parseArray(list.get(i).getImages(), String.class));
////            for(String image: list.get(i).getImagesList()){
////                photoDir.add(image);
////            }
////            list.get(i).setImagesList(photoDir);
//            list.get(i).setImages(null);
//        }
//        PageInfo<Dynamic> page = new PageInfo<>(list);
//
//        return page;
        return null;
    }

    /**
     * 按时间顺序查找动态
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @Override
    public PageInfo<Dynamic> dynamicsByTime(int pageNum, int pageSize, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String userID = LIKE +user.getUserId();

        List<Dynamic> list = iWithFriensMapper.dynamicsByTime();

        List<String> photoDir = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            list.get(i).setImagesList(JSON.parseArray(list.get(i).getImages(), String.class));
//            for(String image: list.get(i).getImagesList()){
//                photoDir.add(image);
//            }
//            list.get(i).setImagesList(photoDir);
            list.get(i).setLikeSum(redisUtils.zscore("Dynamic", list.get(i).getDynamicId()));
            list.get(i).setImages(null);
            //判断用户是否给该动态点赞
            list.get(i).setLike(redisUtils.sHasKey(userID,list.get(i).getDynamicId()));
            //判断是否是自己的动态
            list.get(i).setSelf(list.get(i).getUserId() == user.getUserId());
        }
        PageInfo<Dynamic> page = new PageInfo<>(list);

        return page;
    }

    /**
     * 关注的人的动态
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @Override
    public PageInfo<Dynamic> condynamic(int pageNum, int pageSize, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String userID = LIKE +user.getUserId();
        if(redisUtils.sCard(FLLOW +user.getUserId()) > 0){
            Set<Object> idSet = redisUtils.sGet(FLLOW +user.getUserId());

            List<Object> idList = new ArrayList<Object>(idSet);
            System.out.println(idList);

            List<Dynamic> list = iWithFriensMapper.condynamic(idList);

            List<String> photoDir = new ArrayList<>();
            for(int i = 0; i < list.size(); i++) {
                list.get(i).setImagesList(JSON.parseArray(list.get(i).getImages(), String.class));
//            for(String image: list.get(i).getImagesList()){
//                photoDir.add(image);
//            }
//            list.get(i).setImagesList(photoDir);
                list.get(i).setLikeSum(redisUtils.zscore("Dynamic", list.get(i).getDynamicId()));
                list.get(i).setImages(null);
                //判断用户是否给该动态点赞
                list.get(i).setLike(redisUtils.sHasKey(userID,list.get(i).getDynamicId()));
                //判断是否是自己的动态
                list.get(i).setSelf(list.get(i).getUserId() == user.getUserId());
            }
            PageInfo<Dynamic> page = new PageInfo<>(list);

            return page;
        }else {
            return null;
        }
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

    /**
     * 添加动态
     * @param user
     * @param dynamic
     * @param file
     * @return
     */
    @Override
    public boolean addDynamic(User user, Dynamic dynamic, List<MultipartFile> file) {
        String newFileName = null;
        List<String> dynamicPhotos = null;
        if(!file.isEmpty()){
            dynamicPhotos = new ArrayList<>();
            for(int i = 0; i < file.size(); i++) {
                String originalFileName = file.get(i).getOriginalFilename();
                newFileName = UUID.randomUUID() + "-user" + user.getUserId()
                        + "-dy" + dynamic.getDynamicId() + originalFileName.substring(originalFileName.lastIndexOf("."));
                File newfile = new File(dynamicPhoto + newFileName);
                dynamicPhotos.add(newFileName);
                try {
                    file.get(i).transferTo(newfile);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        dynamic.setTime(LocalDateTime.now());
        dynamic.setImages(JSON.toJSONString(dynamicPhotos));
        dynamic.setUserId(user.getUserId());

        if(1 == iWithFriensMapper.addDynamic(dynamic)){
            redisUtils.zAdd("Dynamic",dynamic.getDynamicId(),0);
            return true;
        }else{
            return false;
        }

    }

    /**
     * 喜爱的动态
     * @param user
     * @param dynamicId
     * @return
     */
    @Override
    public boolean likeDynamic(User user, int dynamicId) {
        //System.out.println(redisUtils.sSet(LIKE+user.getUserId(),dynamicId));
        if(redisUtils.sSet(LIKE+user.getUserId(),dynamicId) > 0){
            redisUtils.zInCrBy("Dynamic",dynamicId,1);

            return true;
        }
        return false;
    }
}
