package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by xp on 16/8/23.
 */
public class ReceiptAddrModel implements Serializable {
    private int ID;
    private String Address;
    private String Name;
    private int Isdefult;
    private String Zip;
    private String Mobile;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIsdefult() {
        return Isdefult;
    }

    public void setIsdefult(int isdefult) {
        Isdefult = isdefult;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }
}
