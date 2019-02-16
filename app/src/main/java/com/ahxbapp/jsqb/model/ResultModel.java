package com.ahxbapp.jsqb.model;

/**
 * Created by zzx on 2017/6/15 0015.
 */

public class ResultModel {

    /**
     * result : 1
     * message : 获取优惠劵成功
     * data : 0
     */

    private int result;
    private String message;
    private String data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
