package com.hutquan.hut.vo;

/**
 * 状态枚举类
 */
public enum  EnumStatus {

    SUCCESS(1,"success"),Fail(-1,"fail"),Repetition(0,"Repetition")
    ,RESNULL(0,"NOTRES");

    private int code;

    private String message;

    private EnumStatus(int code,String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
