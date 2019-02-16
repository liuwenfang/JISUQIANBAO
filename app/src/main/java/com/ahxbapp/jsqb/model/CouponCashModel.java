package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/10/25.
 * Page
 */
public class CouponCashModel implements Serializable {
    //    "ID": 6,
//            "money": 100,
//            "Fullmoney": 1,
//            "EndTime": "2016年10月29日 11:07",
//            "Des": "kadk"
    private int ID;
    private float money;
    private float Fullmoney;
    private String EndTime;
    private String Des;
    private int IsOver;

    @Override
    public String toString() {
        return "CouponCashModel{" +
                "ID=" + ID +
                ", money=" + money +
                ", Fullmoney=" + Fullmoney +
                ", EndTime='" + EndTime + '\'' +
                ", Des='" + Des + '\'' +
                ", IsOver=" + IsOver +
                '}';
    }

    public int getIsOver() {
        return IsOver;
    }

    public void setIsOver(int isOver) {
        IsOver = isOver;
    }

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
