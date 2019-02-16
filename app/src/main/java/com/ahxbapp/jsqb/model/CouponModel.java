package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by xp on 16/9/5.
 */
public class CouponModel implements Serializable {
    private int ID;
    private float money;
    private float Fullmoney;
    private String EndTime;
    private String Des;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getFullmoney() {
        return Fullmoney;
    }

    public void setFullmoney(float fullmoney) {
        Fullmoney = fullmoney;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }
}
