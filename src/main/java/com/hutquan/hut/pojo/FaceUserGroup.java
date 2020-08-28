package com.hutquan.hut.pojo;

import lombok.Data;

/**
 * 人脸与User对应关系表
 * 看情况用不用
 */
@Data
public class FaceUserGroup {

    private Integer faceUserGroupId;

    private Integer groupId;

    private Integer userId;
}
