package com.hutquan.hut.service;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.vo.PageBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IWithFriendsService {

    PageBean<Dynamic> dynamicsByTime(int pageNum, int pageSize, User user);

    PageBean<Dynamic> condynamic(int pageNum, int pageSize, User user);

    boolean addConcern(User user,int concernUserId);

    boolean remConcern(User user,int concernUserId);

    boolean addDynamic(User user, Dynamic dynamic, MultipartFile[] file);

    Double likeDynamic(User user,int dynamicId);

    //dynamicsByLike的新接口
    List<Dynamic> dynamicsByHot(User user);

    Double cancellikeDynamic(User user, int dynamicId);

    PageBean<Dynamic> dynamicsBySelf(int pageNum, int pageSize, User user);

    PageBean<Dynamic> queryDynamic(int userId, int pageNum, int pageSize, User user);
}
