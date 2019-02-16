package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by xp on 16/8/30.
 */
public class MessageModel implements Serializable{
    private int ID;

    private int IsRead;

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int isRead) {
        IsRead = isRead;
    }

    private String Title;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    private String Content;
    private String AddTime;


}
