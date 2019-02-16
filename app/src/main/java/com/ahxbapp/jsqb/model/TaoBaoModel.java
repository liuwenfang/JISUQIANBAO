package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Jayzhang on 16/11/17.
 */
public class TaoBaoModel implements Serializable {
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPostAdd() {
        return PostAdd;
    }

    public void setPostAdd(String postAdd) {
        PostAdd = postAdd;
    }

    private String UserName;
    private String Phone;
    private String Address;
    private String PostAdd;

}
