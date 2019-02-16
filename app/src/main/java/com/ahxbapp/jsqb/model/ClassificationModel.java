package com.ahxbapp.jsqb.model;

/**
 * Created by urnotxx on 2018/9/4.
 */

public class ClassificationModel {
    private String title,detail;
    private int res;

    public ClassificationModel(String title, String detail, int res) {
        this.title = title;
        this.detail = detail;
        this.res = res;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
