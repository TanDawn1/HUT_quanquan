package com.hutquan.hut.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.mapper.IGeoFriendsCircleMapper;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IGeoFriendsCircleService;
import com.hutquan.hut.utils.RedisUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GeoFriendsCircleServiceImpl implements IGeoFriendsCircleService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IGeoFriendsCircleMapper iGeoFriendsCircleMapper;

    private static final String LOCATION = "location";

    private static final String STAR = "star:";


    /**
     * 附近的人的动态
     * @param user
     * @return
     */
    @Override
    public PageInfo<Dynamic> nearybyDynamic(User user,int pageNum, int pageSize) {
        //获取附近100KM内的用户Id
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults
                = redisUtils.georadiusbymember(LOCATION,user.getUserId().toString(),100D);
//        List<Dynamic> dynamics = new ArrayList<>();
        List<Integer> idList = new ArrayList<>();
        //获取100KM内用户的Id
        for(GeoResult<RedisGeoCommands.GeoLocation<Object>> geoResult
                : geoResults.getContent()){
            idList.add(Integer.valueOf(geoResult.getContent().getName().toString()));
        }

        if(idList.size() > 0) {
            //使用PageHelper分页
            PageHelper.startPage(pageNum, pageSize);

            //使用用户Id查询100KM内用户的动态
            List<Dynamic> list = iGeoFriendsCircleMapper.nearybyDynamic(idList);
            for (Dynamic dynamic : list) {
                //为了提升效率，所以starCount和commentCount都是在Redis中保存的
                //starCount
                dynamic.setStarCount(redisUtils.zscore("dynamic_like", dynamic.getDynamicId()).intValue());
                //commentCount
                dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment", "d" + dynamic.getDynamicId()));
                if (user != null) {
                    if (user.getUserId() == dynamic.getUserId()) dynamic.setSelf(true);
                    //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
                    if (redisUtils.zscore(STAR + user.getUserId(), dynamic.getDynamicId()) != null)
                        dynamic.setLike(true);
                }
            }
            return new PageInfo<>(list);
        }
        return  null;
    }
}
