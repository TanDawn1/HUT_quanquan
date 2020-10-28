package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.Search;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ILostAndFoundService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LostAndFoundController {

    @Autowired
    private ILostAndFoundService iLostAndFoundService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/lostfound/{type}/{pageNum}")
    @ApiOperation("查询失物招领 type 为1则查询招领信息  0 则查询失物信息 无需登录")
    public ResponseBean searchLostOrFound(@PathVariable("type")Integer type,@PathVariable("pageNum") int pageName){
        //20条每页
        return new ResponseBean(200,"ok",
                iLostAndFoundService.selectAllLostAndFound(type,pageName,20));
    }

    @PostMapping("/lostfound/put")
    @ApiOperation("发布失物招领 search中的type 为1则查询招领信息  0 则查询失物信息 需要登录 传输表单数据")
    //此处content-type的类型为：multipart/form-data ，表示表单中进行文件上传。
    // 由于参数中的MultipartFile类型影响了requestbody本应所对应的content-type: application/json
    // 可能是MultipartFile的媒体类型优先级高，会覆盖application/json
    public ResponseBean putLostOrFound(Search search, HttpServletRequest request,@RequestParam(value = "photos",required = false) MultipartFile[] photos){
        if(search == null) return new ResponseBean(400,"请求信息错误",null);
        User user = (User)redisUtils.get(request.getHeader("token"));
        if(user == null){
            return new ResponseBean(403,"未登录，无权限发布",null);
        }
        search.setUserId(user.getUserId());
        if(search.getType() != 0 && search.getType() != 1){
            return new ResponseBean(406,"客户端信息错误,无type信息",null);
        }
        //判断search中的类型 1则发布为招领信息 0则发布为失物信息
        return new ResponseBean(200,"ok",iLostAndFoundService.putLostOrFound(search,photos));
    }

}
