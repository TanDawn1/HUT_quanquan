package com.hutquan.hut.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.mapper.IWithFriensMapper;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IWithFriendsService;
import com.hutquan.hut.utils.FileUtil;
import com.hutquan.hut.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WithFriendsServiceImpl implements IWithFriendsService {

    @Value("${uploade.dynamicPhoto.dir}")
    private  String dynamicPhoto;

    private static final String LIKE = "like:";

    private static final String FLLOW = "fllow:";

    private static final String STAR = "star:";

    private static final String SELFFOLLOW = "selfFollow:";

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
            //list.get(i).setImagesList(JSON.parseArray(list.get(i).getImages(), String.class));
//            for(String image: list.get(i).getImagesList()){
//                photoDir.add(image);
//            }
//            list.get(i).setImagesList(photoDir);
            //list.get(i).setLikeSum(redisUtils.zscore("Dynamic", list.get(i).getDynamicId()));
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
    public PageInfo<Dynamic> condynamic(int pageNum, int pageSize, User user) {

        //String userID = LIKE +user.getUserId();
        List<Object>  idList = redisUtils.lGet(SELFFOLLOW + user.getUserId(),0, -1);

        if(idList.size() > 0){
            //使用PageHelper分页
            PageHelper.startPage(pageNum, pageSize);

            List<Dynamic> list = iWithFriensMapper.condynamic(idList);
            for(Dynamic dynamic : list) {
                //为了提升效率，所以starCount和commentCount都是在Redis中保存的
                //starCount
                dynamic.setStarCount(redisUtils.zscore("dynamic_like", dynamic.getDynamicId()).intValue());
                //commentCount
                dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment", "d" + dynamic.getDynamicId()));

                if (user.getUserId() == dynamic.getUserId())
                    dynamic.setSelf(true);
                //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
                if (redisUtils.zscore(STAR + user.getUserId(), dynamic.getDynamicId()) != null)
                    dynamic.setLike(true);
            }
            return new PageInfo<>(list);
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
     * @param
     * @return
     */
    @Override
    public boolean addDynamic(User user, Dynamic dynamic, MultipartFile[] files) {
        String newFileName = null;
        String dynamicPhotos = null;
        //上传了图片
        if(files != null){
            //上传图片
            dynamicPhotos = FileUtil.fileUpload(user.getUserId(),files,1);

            if(dynamicPhotos.equals("")) return false;
        }
        dynamic.setImages(dynamicPhotos);
        //时间戳 秒级
        dynamic.setTime(Instant.now().getEpochSecond());
        dynamic.setUserId(user.getUserId());
        //判断受影响的行数是否为1 不为1则return false
        if(iWithFriensMapper.addDynamic(dynamic) == 1){
            //在Redis中添加相应的字段
            //与点赞条数的对应关系
            redisUtils.zAdd("dynamic_like",dynamic.getDynamicId(),0D);
            //与评论条数的对应关系
            redisUtils.hset("dynamic_comment","d" + dynamic.getDynamicId(),0);

            return true;
        }
        return false;

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

    /**
     * 热点动态
     * @param user
     * @return
     */
    @Override
    public List<Dynamic> dynamicsByHot(User user) {
        //查找动态表中排名靠前的动态ID，一页20条
        Set<Object> idSet = redisUtils.zRevrange("dynamic_like",0L,19L);
        List<Dynamic> list = iWithFriensMapper.dynamicsByHot(idSet);
        for(Dynamic dynamic: list){
            //为了提升效率，所以starCount和commentCount都是在Redis中保存的
            //starCount
            dynamic.setStarCount(redisUtils.zscore("dynamic_like",dynamic.getDynamicId()).intValue());
            //commentCount
            dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment","d"+dynamic.getDynamicId()));
            if(user != null){
                if(user.getUserId() == dynamic.getUserId()) dynamic.setSelf(true);
                //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
                if(redisUtils.zscore(STAR+user.getUserId(),dynamic.getDynamicId()) != null) dynamic.setLike(true);
            }
        }
        return  list;
    }


}
