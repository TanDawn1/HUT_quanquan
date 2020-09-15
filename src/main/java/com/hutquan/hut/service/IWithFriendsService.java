package com.hutquan.hut.service;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IWithFriendsService {

    PageInfo<Dynamic> dynamicsByTime(int pageNum, int pageSize, User user);

    PageInfo<Dynamic> condynamic(int pageNum, int pageSize, User user);

    boolean addConcern(User user,int concernUserId);

    boolean remConcern(User user,int concernUserId);

    boolean addDynamic(User user, Dynamic dynamic, MultipartFile[] file);

    Double likeDynamic(User user,int dynamicId);

    //dynamicsByLike的新接口
    List<Dynamic> dynamicsByHot(User user);

    Double cancellikeDynamic(User user, int dynamicId);
}
