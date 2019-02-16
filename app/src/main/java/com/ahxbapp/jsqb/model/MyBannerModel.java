package com.ahxbapp.jsqb.model;

/**
 * Created by zzx on 2017/8/28 0028.
 */

public class MyBannerModel {


    /**
     * PicName : 图二
     * PhoneType : 2
     * Pic_1xUrl : http://img.910ok.com/1a790dc4-e01f-4e7f-958f-0b63a0b78499.png
     * Pic_2xUrl : http://img.910ok.com/d113b157-097d-46f3-ab35-0d2537ba27b3.jpg
     * Pic_3xUrl : http://img.910ok.com/30b809c2-3906-43db-b185-37409b551487.jpg
     * LinkUrl : www.qq.com
     * Sort : 2
     */

    private String PicName;
    private String PhoneType;
    private String Pic_1xUrl;
    private String Pic_2xUrl;
    private String Pic_3xUrl;
    private String LinkUrl;
    private int Sort;

    public String getPicName() {
        return PicName;
    }

    public void setPicName(String PicName) {
        this.PicName = PicName;
    }

    public String getPhoneType() {
        return PhoneType;
    }

    public void setPhoneType(String PhoneType) {
        this.PhoneType = PhoneType;
    }

    public String getPic_1xUrl() {
        return Pic_1xUrl;
    }

    public void setPic_1xUrl(String Pic_1xUrl) {
        this.Pic_1xUrl = Pic_1xUrl;
    }

    public String getPic_2xUrl() {
        return Pic_2xUrl;
    }

    public void setPic_2xUrl(String Pic_2xUrl) {
        this.Pic_2xUrl = Pic_2xUrl;
    }

    public String getPic_3xUrl() {
        return Pic_3xUrl;
    }

    public void setPic_3xUrl(String Pic_3xUrl) {
        this.Pic_3xUrl = Pic_3xUrl;
    }

    public String getLinkUrl() {
        return LinkUrl;
    }

    public void setLinkUrl(String LinkUrl) {
        this.LinkUrl = LinkUrl;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int Sort) {
        this.Sort = Sort;
    }
}
