package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ISearchService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

//TODO 搜索服务在查询速度显著降低时可考虑 el
@RestController
public class SearchController {

    @Autowired
    private ISearchService iSearchService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/search/lostfound/{message}/{pageNum}")
    @ApiOperation("在失物招领的数据中查询 无需登录")
    public ResponseBean searchLostAndFound(@PathVariable("message") String message,@PathVariable("pageNum") int pageNum){
        return new ResponseBean(200,"ok",iSearchService.searchs(message,pageNum,20));
    }

    @GetMapping("/search/dynamic/{message}/{pageNum}")
    @ApiOperation("在动态说说的数据中查询 无需登录")
    public ResponseBean searchDynamic(@PathVariable("message") String message, @PathVariable("pageNum") int pageNum, HttpServletRequest request){
        User user = (User) redisUtils.get(request.getHeader("token"));
        return new ResponseBean(200,"ok",iSearchService.dynamics(message,pageNum,20,user));
    }

}
