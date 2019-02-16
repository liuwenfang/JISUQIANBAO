package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by xp on 16/8/29.
 */
public class DesignModel implements Serializable{
    int ID;
    String AddDate;

    public String getAddDate() {
        return AddDate;
    }

    public void setAddDate(String addDate) {
        AddDate = addDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getCustomID() {
        return CustomID;
    }

    public void setCustomID(int customID) {
        CustomID = customID;
    }

    String thumbnail;
    String Title;
    int CustomID;

}
