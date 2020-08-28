package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 人脸管理
 */
@Data
public class UserFace implements Serializable {

    private Integer faceId;

    private Integer userId;

    private List<String> faceImages;

}
