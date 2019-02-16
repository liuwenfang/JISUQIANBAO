package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/14.
 */

public class CommonEnity<T> implements Serializable {
    private String type;
    private T data;

    public CommonEnity(String type) {
        this.type = type;
    }

    public CommonEnity(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
