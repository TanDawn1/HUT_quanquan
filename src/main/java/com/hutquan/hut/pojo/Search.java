package com.hutquan.hut.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 寻物寻人
 */
@Data
public class Search implements Serializable {

    private Integer sId;

    //用户Id
    private Integer userId;

    //用户信息
    private User user;

    private String message;

    private String sPhoto;

    private String location;

    private Long time;

    //0为失物 1为招领
    private Integer type;

}
