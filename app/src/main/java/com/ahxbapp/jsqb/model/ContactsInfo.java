package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by lqfang on 2017/4/11.
 * 通讯录
 */

public class ContactsInfo implements Serializable{
    private String name;
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
