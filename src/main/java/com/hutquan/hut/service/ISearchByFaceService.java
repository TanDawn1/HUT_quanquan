package com.hutquan.hut.service;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.pojo.UserSearchFace;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISearchByFaceService {

    List<UserSearchFace> searchFaces(MultipartFile face);

    Object  searchFaces1(MultipartFile face);
}
