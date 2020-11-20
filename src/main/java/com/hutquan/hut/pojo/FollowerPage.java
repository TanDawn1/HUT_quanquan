package com.hutquan.hut.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerPage {

    private List<Follower> followers;

    private long total;

    private Long pageNum;

    private long pages;

    private int size;

    private Long start;

    private Long end;

}
