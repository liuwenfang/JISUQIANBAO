package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Jayzhang on 16/11/17.
 */
public class AlipayModel implements Serializable {

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTBName() {
        return TBName;
    }

    public void setTBName(String TBName) {
        this.TBName = TBName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHBTotal() {
        return HBTotal;
    }

    public void setHBTotal(String HBTotal) {
        this.HBTotal = HBTotal;
    }

    public String getHBHKTotal() {
        return HBHKTotal;
    }

    public void setHBHKTotal(String HBHKTotal) {
        this.HBHKTotal = HBHKTotal;
    }

    public String getHBKYTotal() {
        return HBKYTotal;
    }

    public void setHBKYTotal(String HBKYTotal) {
        this.HBKYTotal = HBKYTotal;
    }

    private String Mobile;
    private String Name;
    private String TBName;
    private String Email;
    private String HBTotal;
    private String HBHKTotal;
    private String HBKYTotal;
}
