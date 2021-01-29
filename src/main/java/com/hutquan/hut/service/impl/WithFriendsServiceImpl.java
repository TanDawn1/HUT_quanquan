package com.hutquan.hut.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.mapper.IWithFriensMapper;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IWithFriendsService;
import com.hutquan.hut.utils.FileUtil;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@Service
public class WithFriendsServiceImpl implements IWithFriendsService {

    private static final String FLLOW = "follow:";

    private static final String STAR = "star:";

    private static final String SELFFOLLOW = "selfFollow:";

    private static final String DYNAMICLIKE = "dynamic_like";

    private Logger logger = LoggerFactory.getLogger(WithFriendsServiceImpl.class);

    @Autowired
    private IWithFriensMapper iWithFriensMapper;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 按时间顺序查找动态
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Dynamic> dynamicsByTime(int pageNum, int pageSize, User user) {
        //使用PageHelper分页
        PageHelper.startPage(pageNum, pageSize);

        List<Dynamic> list = iWithFriensMapper.dynamicsByTime();

        for (Dynamic dynamic : list) {
            //为了提升效率，所以starCount和commentCount都是在Redis中保存的
            //starCount
            dynamic.setStarCount(redisUtils.zscore("dynamic_like", dynamic.getDynamicId()).intValue());
            //commentCount
            dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment", "d" + dynamic.getDynamicId()));

            if (user != null && user.getUserId().equals(dynamic.getUserId()))
                dynamic.setSelf(true);
            //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
            if (user != null && redisUtils.zscore(STAR + user.getUserId(), dynamic.getDynamicId()) != null)
                dynamic.setLike(true);
        }
        return new PageBean<>(list);
    }

    /**
     * 关注的人的动态
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Dynamic> condynamic(int pageNum, int pageSize, User user) {

        //String userID = LIKE +user.getUserId();
        List<Object> idList = redisUtils.lGet(SELFFOLLOW + user.getUserId(), 0, -1);

        if (idList.size() > 0) {
            //使用PageHelper分页
            PageHelper.startPage(pageNum, pageSize);

            List<Dynamic> list = iWithFriensMapper.condynamic(idList);
            for (Dynamic dynamic : list) {
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
            return new PageBean<>(list);
        } else {
            return null;
        }
    }

    /**
     * 添加关注
     *
     * @param user
     * @param concernUserId
     * @return
     */
    @Override
    public boolean addConcern(User user, int concernUserId) {
        try {
            // 查询是否已经关注过
            if(redisUtils.zscore(FLLOW + concernUserId,concernUserId) != null) return true;
            //存入用户的关注列表 关注的越早排序越靠前
            redisUtils.lSet(SELFFOLLOW + user.getUserId(), concernUserId);
            // 存入被关注用户列表
            redisUtils.zAdd(FLLOW + concernUserId, user.getUserId(), Instant.now().getEpochSecond());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取消关注
     *
     * @param user
     * @param concernUserId
     * @return
     */
    @Override
    public boolean remConcern(User user, int concernUserId) {
        try {
            //删除用户的关注列表指定用户  count = 0 : 移除表中所有与 VALUE 相等的值
            redisUtils.lRemove(SELFFOLLOW + user.getUserId(), 0, concernUserId);
            //删除被关注用户列表中的指定用户 TODO 其实可以不用移除 参考 wechat
            redisUtils.zrem(FLLOW + concernUserId, user.getUserId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加动态
     * 事务处理
     *
     * @param user
     * @param dynamic
     * @param
     * @return
     */
    @Override
    @Transactional
    public boolean addDynamic(User user, Dynamic dynamic, MultipartFile[] files) {
        //String newFileName = null;
        String dynamicPhotos = null;
        //上传了图片
        if (files != null) {
            //上传图片
            dynamicPhotos = FileUtil.fileUpload(user.getUserId(), files, 1);

            if (dynamicPhotos == null || dynamicPhotos.equals("")) return false;
        }
        dynamic.setImages(dynamicPhotos);
        //时间戳 秒级
        dynamic.setTime(Instant.now().getEpochSecond());
        dynamic.setUserId(user.getUserId());
        //判断受影响的行数是否为1 不为1则return false
        try {
            if (iWithFriensMapper.addDynamic(dynamic) == 1) {
                //在Redis中添加相应的字段
                //与点赞条数的对应关系
                redisUtils.zAdd("dynamic_like", dynamic.getDynamicId(), 0D);
                //与评论条数的对应关系
                redisUtils.hset("dynamic_comment", "d" + dynamic.getDynamicId(), 0);

                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    /**
     * 喜爱的动态
     *
     * @param user
     * @param dynamicId
     * @return
     */
    @Override
    public Double likeDynamic(User user, int dynamicId) {
        //System.out.println(redisUtils.sSet(LIKE+user.getUserId(),dynamicId));
        //判断是否已经点过赞了，未点过才能够点赞
        if (redisUtils.zscore(STAR + user.getUserId(), dynamicId) == null) {
            //增加进入点赞表
            redisUtils.zAdd(STAR + user.getUserId(), dynamicId, Instant.now().getEpochSecond());
            return redisUtils.zInCrBy(DYNAMICLIKE, dynamicId, 1);
        }
        return redisUtils.zscore(DYNAMICLIKE, dynamicId);
    }

    /**
     * 取消喜爱动态
     *
     * @param user
     * @param dynamicId
     * @return
     */
    @Override
    public Double cancellikeDynamic(User user, int dynamicId) {
        //判断是否已经点过赞了，点过才能取消
        if (redisUtils.zscore(STAR + user.getUserId(), dynamicId) != null) {
            redisUtils.zrem(STAR + user.getUserId(), dynamicId);
            return redisUtils.zInCrBy(DYNAMICLIKE, dynamicId, -1);
        }
        return redisUtils.zscore(DYNAMICLIKE, dynamicId);
    }

    /**
     * 查询自己的动态
     *
     * @param pageNum
     * @param pageSize
     * @param user
     * @return
     */
    @Override
    public PageBean<Dynamic> dynamicsBySelf(int pageNum, int pageSize, User user) {
        //使用PageHelper分页
        PageHelper.startPage(pageNum, pageSize);

        List<Dynamic> list = iWithFriensMapper.dynamicsBySelf(user.getUserId());
        List<String> photoDir = new ArrayList<>();
        for (Dynamic dynamic : list) {
            //为了提升效率，所以starCount和commentCount都是在Redis中保存的
            //starCount
            dynamic.setStarCount(redisUtils.zscore("dynamic_like", dynamic.getDynamicId()).intValue());
            //commentCount
            dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment", "d" + dynamic.getDynamicId()));

            if (user != null && user.getUserId().equals(dynamic.getUserId()))
                dynamic.setSelf(true);
            //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
            if (user != null && redisUtils.zscore(STAR + user.getUserId(), dynamic.getDynamicId()) != null)
                dynamic.setLike(true);
        }
        return new PageBean<>(list);
    }

    @Override
    public PageBean<Dynamic> queryDynamic(int userId, int pageNum, int pageSize, User user) {
        //使用PageHelper分页
        PageHelper.startPage(pageNum, pageSize);
        //TODO 与查询自己的动态共用同一个查询接口
        List<Dynamic> list = iWithFriensMapper.dynamicsBySelf(userId);
        List<String> photoDir = new ArrayList<>();
        for (Dynamic dynamic : list) {
            //为了提升效率，所以starCount和commentCount都是在Redis中保存的
            //starCount
            dynamic.setStarCount(redisUtils.zscore("dynamic_like", dynamic.getDynamicId()).intValue());
            //commentCount
            dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment", "d" + dynamic.getDynamicId()));

            if (user != null && user.getUserId().equals(dynamic.getUserId()))
                dynamic.setSelf(true);
            //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
            if (user != null && redisUtils.zscore(STAR + user.getUserId(), dynamic.getDynamicId()) != null)
                dynamic.setLike(true);
        }
        return new PageBean<>(list);
    }

    @Override
    public boolean delDynamic(int dynamicId, User user) {
        try {
            if (user.getUserId() >= 0) {
                if (iWithFriensMapper.queryUserId(dynamicId).equals(user.getUserId())) {
                    //移除点赞列表中的
//                    redisUtils.zrem(STAR + user.getUserId(),dynamicId);
                    redisUtils.zrem(DYNAMICLIKE, dynamicId);
                    return iWithFriensMapper.delDynamic(dynamicId) > 0;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Dynamic dynamicDetail(User user, int dynamicId) {

        Dynamic dynamic = iWithFriensMapper.queryDynamicDetail(dynamicId);
        //为了提升效率，所以starCount和commentCount都是在Redis中保存的
        if(dynamic == null) return dynamic;
        //starCount
        dynamic.setStarCount(redisUtils.zscore("dynamic_like", dynamic.getDynamicId()).intValue());
        //commentCount
        dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment", "d" + dynamic.getDynamicId()));
        if (user != null && user.getUserId().equals(dynamic.getUserId()))
            dynamic.setSelf(true);
        //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
        if (user != null && redisUtils.zscore(STAR + user.getUserId(), dynamic.getDynamicId()) != null)
            dynamic.setLike(true);

        return dynamic;
    }

    /**
     * 热点动态
     *
     * @param user
     * @return
     */
    @Override
    public List<Dynamic> dynamicsByHot(User user) {
        List<Dynamic> list = null;
        //因为是热点数据，首先查询redis中是否有数据，无则查询数据库
        //使用分布式锁，因为考虑会部署到多台服务器
        Object jsondata = redisUtils.get("hot");
        if (jsondata != null) {
            //jsondata不为null
            //将jsondata转换为list<Dynamic>
            list = new ArrayList<>();
            list = JSONObject.parseArray(JSON.toJSONString(jsondata), Dynamic.class);
        } else {
            //在判断一次redis中数据是否为null
            jsondata = redisUtils.get("hot");
            if (jsondata == null) {
                //加分布式锁，防止缓存失效之后数据一次性打到DB
                String uuid = UUID.randomUUID().toString();
                try {
                    while (!redisUtils.lock("lock", uuid)) {
                        //如果加锁失败  自旋
                        //直到加锁成功
                    }
                    //在进行一次判断
                    jsondata = redisUtils.get("hot");
                    if (jsondata == null) {
                        //查找动态表中排名靠前的动态ID，一页20条
                        Set<Object> idSet = redisUtils.zRevrange("dynamic_like", 0L, 19L);
                        list = iWithFriensMapper.dynamicsByHot(idSet);
                        //缓存入Redis 1小时刷新一次
                        redisUtils.set("hot", list, 60 * 60L);
                    }
                } catch (Exception e){
                    logger.info("加锁之后 处理过程出现异常");
                }finally {
                    //无论如何都需要 解锁 有线程安全问题，需要使用lua脚本解决
                    redisUtils.unlock("lock",uuid);
                }
            }
            if (list == null)
                list = JSONObject.parseArray(JSON.toJSONString(jsondata), Dynamic.class);
        }
        //给list中付取最新的值
        for (Dynamic dynamic : list) {
            //为了提升效率，所以starCount和commentCount都是在Redis中保存的
            //starCount
            dynamic.setStarCount(redisUtils.zscore("dynamic_like", dynamic.getDynamicId()).intValue());
            //commentCount
            dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment", "d" + dynamic.getDynamicId()));
            if (user != null) {
                if (user.getUserId().equals(dynamic.getUserId())) dynamic.setSelf(true);
                //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
                if (redisUtils.zscore(STAR + user.getUserId(), dynamic.getDynamicId()) != null)
                    dynamic.setLike(true);
            }
        }

        return list;
    }


}
