package com.ahxbapp.jsqb.model;

/**
 * Created by urnotxx on 2018/9/5.
 */

public class PersonalModel {
    private String type;
    private int res;

    public PersonalModel(String type, int res) {
        this.type = type;
        this.res = res;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
