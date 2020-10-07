package com.hutquan.hut.controller;

import com.hutquan.hut.mapper.ICommentMapper;
import com.hutquan.hut.pojo.Comment;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ICommentService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论
 */
@RestController
public class commentController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ICommentService iCommentService;

    @PostMapping("/comment/dynamic")
    @ApiOperation("对所发表动态的 评论接口 ->需要用户登录,commentId、userId、time后台写入,touserId无可不填")
    public ResponseBean commentDynamic(HttpServletRequest request, @RequestBody Comment comment){
        try {
            User user = (User) redisUtils.get(request.getHeader("token"));
            if (user == null) return new ResponseBean(401, "未登录，无权限评论", null);
            if (comment == null || comment.getMessage().equals("")) return new ResponseBean(403, "传入参数不合规范", null);
            return new ResponseBean(200, "ok", iCommentService.insertDynamicComment(user, comment) == 0 ? "未知错误" : "成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseBean(500,"未知错误",null);
        }
    }

}
