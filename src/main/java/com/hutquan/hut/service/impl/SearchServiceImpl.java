package com.hutquan.hut.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.mapper.ISearchMapper;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.Search;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ISearchService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {

    private static final String STAR = "star:";

    @Autowired
    private ISearchMapper iSearchMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public PageBean<Search> searchs(String message, int pageNum, int pageSize) {

        if(message.equals("")) return null;
        //敏感词限制
        PageHelper.startPage(pageNum,pageSize);
        List<Search> list = iSearchMapper.searchs(message);
        return new PageBean<>(list);
    }

    @Override
    public PageBean<Dynamic> dynamics(String message,int pageNum,int pageSize, User user) {
        if(message.equals("")) return null;
        PageHelper.startPage(pageNum,pageSize);
        List<Dynamic> list = iSearchMapper.dynamics(message);
        for(Dynamic dynamic: list){
            //为了提升效率，所以starCount和commentCount都是在Redis中保存的
            //starCount
            dynamic.setStarCount(redisUtils.zscore("dynamic_like",dynamic.getDynamicId()).intValue());
            //commentCount
            dynamic.setCommentCount((Integer) redisUtils.hget("dynamic_comment","d"+dynamic.getDynamicId()));
            if(user != null){
                if(user.getUserId().equals(dynamic.getUserId())) dynamic.setSelf(true);
                //通过查找Redis中的点赞列表，判断用户是否给该动态点赞 O(1)的效率
                if(redisUtils.zscore(STAR + user.getUserId(),dynamic.getDynamicId()) != null) dynamic.setLike(true);
            }
        }
        return new PageBean<>(list);
    }

}
