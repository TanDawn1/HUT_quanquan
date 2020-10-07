package com.hutquan.hut.service;

import com.hutquan.hut.pojo.FeedBack;
import org.springframework.web.multipart.MultipartFile;

public interface IErrorService {

    boolean feedBack(FeedBack feedBack, MultipartFile[] feedphotos);

}
