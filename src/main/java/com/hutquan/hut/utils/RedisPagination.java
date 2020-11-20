package com.hutquan.hut.utils;

import com.hutquan.hut.pojo.FollowerPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用于查找Redis时进行物理分页
 */
@Component
public class RedisPagination {

    @Autowired
    private RedisUtils redisUtils;

    private static final String FOLLOW = "follow:";

    //20个一页
    public FollowerPage redisPage(Long pageNum,Integer userId){
        try {
            if (userId == null) return null;
            Long total = redisUtils.zscard(FOLLOW + userId);
            FollowerPage followerPage = new FollowerPage();
            followerPage.setTotal(total);
            followerPage.setPageNum(pageNum);
            Long Pages = 0L;
            Long num = total / 10;
            if(num != 0) {
                Pages = total % 20 + num;
            }else {
                Pages = 1L;
            }
            followerPage.setPages(Pages);
            followerPage.setStart((pageNum - 1) * 20);
            if(followerPage.getStart() < 0) followerPage.setStart(0L);
            followerPage.setEnd(followerPage.getStart() + 19);
            return followerPage;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
