package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/10/22.
 * Page
 */
public class MobileModel implements Serializable{
//    Mobile":"18855973094","Operator":null,"Status":1
    private String Mobile;
    private String Operator;
    private int Status;

    @Override
    public String toString() {
        return "MobileModel{" +
                "Mobile='" + Mobile + '\'' +
                ", Operator='" + Operator + '\'' +
                ", Status=" + Status +
                '}';
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getOperator() {
        return Operator;
    }

    public void setOperator(String operator) {
        Operator = operator;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
