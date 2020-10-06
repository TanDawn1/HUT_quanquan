package com.hutquan.hut.service;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.Search;
import com.hutquan.hut.vo.PageBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ILostAndFoundService {

    //查询
    PageBean<Search> selectAllLostAndFound(Integer type, Integer pageNum, Integer pageSize);

    boolean putLostOrFound(Search search, MultipartFile[] photos);


}
