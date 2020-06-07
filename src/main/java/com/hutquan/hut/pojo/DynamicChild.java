package com.hutquan.hut.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class DynamicChild implements Serializable {

    private int dynamicChildId;

    private String message;

    private int likeSum;

    private int userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    private String images;

    private List<String> imagesList;

}
