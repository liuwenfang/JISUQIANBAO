package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/10/21.
 * Page
 */
public class JobMedel implements Serializable{
//    "PosID": 3,
//            "PosName": "测试",
//            "IncID": 2,
//            "IncName": "1000-2000元",
//            "Company": "我现在",

//            "ProID": 12,

//            "ProName": "安徽省",
//            "CityID": 98,
//            "CityName": "合肥市",
//            "ComAddr": "你一",
//            "ComMobile": "64649",
//            "IsPost": 1
    private int PosID;
    private String PosName;
    private int IncID;
    private String IncName;
    private String Company;
    private int ProID;


    private int CityID;
    private String ProName;
    private String CityName;
    private String ComAddr;
    private String ComMobile;
    private int IsPost;

    @Override
    public String toString() {
        return "JobMedel{" +
                "PosID=" + PosID +
                ", PosName='" + PosName + '\'' +
                ", IncID=" + IncID +
                ", IncName='" + IncName + '\'' +
                ", Company='" + Company + '\'' +
                ", ProID=" + ProID +
                ", CityID=" + CityID +
                ", ProName='" + ProName + '\'' +
                ", CityName='" + CityName + '\'' +
                ", ComAddr='" + ComAddr + '\'' +
                ", ComMobile='" + ComMobile + '\'' +
                ", IsPost=" + IsPost +
                '}';
    }

    public int getPosID() {
        return PosID;
    }

    public void setPosID(int posID) {
        PosID = posID;
    }

    public String getPosName() {
        return PosName;
    }

    public void setPosName(String posName) {
        PosName = posName;
    }

    public int getIncID() {
        return IncID;
    }

    public void setIncID(int incID) {
        IncID = incID;
    }

    public String getIncName() {
        return IncName;
    }

    public void setIncName(String incName) {
        IncName = incName;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public int getProID() {
        return ProID;
    }

    public void setProID(int proID) {
        ProID = proID;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getComAddr() {
        return ComAddr;
    }

    public void setComAddr(String comAddr) {
        ComAddr = comAddr;
    }

    public String getComMobile() {
        return ComMobile;
    }

    public void setComMobile(String comMobile) {
        ComMobile = comMobile;
    }

    public int getIsPost() {
        return IsPost;
    }

    public void setIsPost(int isPost) {
        IsPost = isPost;
    }
}
