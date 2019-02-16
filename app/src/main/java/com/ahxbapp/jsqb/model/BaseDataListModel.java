package com.ahxbapp.jsqb.model;

import java.util.List;

/**
 * Created by xp on 16/8/23.
 */
public class BaseDataListModel<T>{
    private  int result;
    private String message;
    private String enmessage;
    private List<T> datalist;
    private T model;

    public String getEnmessage() {
        return enmessage;
    }

    public void setEnmessage(String enmessage) {
        this.enmessage = enmessage;
    }

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

    public List<T> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<T> datalist) {
        this.datalist = datalist;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
