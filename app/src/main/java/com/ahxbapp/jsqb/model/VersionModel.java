package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Jayzhang on 17/3/2.
 */

public class VersionModel implements Serializable {
    /*手机类型*/
    private int ID;
    private String VerSionNO;
    private String Addr;
    private String Type;
    private String Content;
    private String AddTime;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getVerSionNO() {
        return VerSionNO;
    }

    public void setVerSionNO(String verSionNO) {
        VerSionNO = verSionNO;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }
}
