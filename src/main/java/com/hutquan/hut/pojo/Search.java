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

    private Integer userId;

    private String message;

    private List<String> sPhoto;

    private String provine;

    private String city;

    private String district;

    private String address;

    private String lonLat;

    private Long time;

}
