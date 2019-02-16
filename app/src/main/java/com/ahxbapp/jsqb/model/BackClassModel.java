package com.ahxbapp.jsqb.model;

/**
 * Created by gravel on 16/9/7.
 */
public class BackClassModel {
    private int ID;
    private String Name;

    private boolean isCheck;
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
}
