package com.hutquan.hut.vo;

import lombok.Data;

@Data
public class ResponseBean {

    private long code;
    private String msg;
    private String enCode;
    private Object data;

    public ResponseBean(){
    }

    //需要加密参数的构造函数
    public ResponseBean(long code, String msg, String enCode, Object data) {
        this.code = code;
        this.msg = msg;
        this.enCode = enCode;
        this.data = data;
    }

    //无需加密参数的构造函数
    public ResponseBean(long code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
