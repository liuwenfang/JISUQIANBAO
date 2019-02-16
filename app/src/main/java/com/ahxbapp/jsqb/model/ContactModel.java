package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/10/21.
 * Page
 */
public class ContactModel implements Serializable {
    //    "RelaID": 1,
//            "RelaName": "配偶",
//            "RelaMobile": null,
//            "SocID": 1,
//            "SocName": "领导",
//            "SocMobile": "18855973091",
//            "IsContact": 0
    private int RelaID;
    private String RelaName;
    private String RelaMobile;
    private int SocID;
    private String SocName;
    private String SocMobile;
    private int IsContact;
    private String RelaUserName;
    private String SocUserName;

    @Override
    public String toString() {
        return "ContactModel{" +
                "RelaID=" + RelaID +
                ", RelaName='" + RelaName + '\'' +
                ", RelaMobile='" + RelaMobile + '\'' +
                ", SocID=" + SocID +
                ", SocName='" + SocName + '\'' +
                ", SocMobile='" + SocMobile + '\'' +
                ", IsContact=" + IsContact +
                '}';
    }

    public int getRelaID() {
        return RelaID;
    }

    public void setRelaID(int relaID) {
        RelaID = relaID;
    }

    public String getRelaName() {
        return RelaName;
    }

    public void setRelaName(String relaName) {
        RelaName = relaName;
    }

    public String getRelaMobile() {
        return RelaMobile;
    }

    public void setRelaMobile(String relaMobile) {
        RelaMobile = relaMobile;
    }

    public int getSocID() {
        return SocID;
    }

    public void setSocID(int socID) {
        SocID = socID;
    }

    public String getSocName() {
        return SocName;
    }

    public void setSocName(String socName) {
        SocName = socName;
    }

    public String getSocMobile() {
        return SocMobile;
    }

    public void setSocMobile(String socMobile) {
        SocMobile = socMobile;
    }

    public int getIsContact() {
        return IsContact;
    }

    public void setIsContact(int isContact) {
        IsContact = isContact;
    }

    public String getRelaUserName() {
        return RelaUserName;
    }

    public void setRelaUserName(String relaUserName) {
        RelaUserName = relaUserName;
    }

    public String getSocUserName() {
        return SocUserName;
    }

    public void setSocUserName(String socUserName) {
        SocUserName = socUserName;
    }
}
