package com.hutquan.hut.pojo;

import lombok.Data;

@Data
public class Active {

    private Integer id;

    private String content;

    private Integer allPerson;

    private Integer lastPerson;

    private Long signStartTime;

    private Long signEndTime;

    private Long startTime;

    private Long endTime;

    private String images;

    private Long createTime;

}
