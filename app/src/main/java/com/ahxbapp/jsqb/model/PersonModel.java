package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/10/21.
 * Page
 */
public class PersonModel implements Serializable {
//    "ID": 6,
//            "QQ": "66",
//            "Email": "h",

//            "EduID": 1,

//            "EduName": "小学",
//            "IsMarry": 0,

//            "ChildNumID": 1,

//            "ChildNum": 1,

//            "Addr": "握",

//            "LiveID": 1,

    //            "LiveName": "一个月",
//            "IsPerson": 1,
//            "IsPost": 1,
//            "IsContact": 1
    private int ID;
    private String QQ;
    private String Email;
    private int EduID;

    private String EduName;
    private int IsMarry;
    private int ChildNumID;
    private String ChildNum;

    private String Addr;
    private int LiveID;
    private String LiveName;
    private int IsPerson;

    private int IsPost;
    private int IsContact;

    @Override
    public String toString() {
        return "PersonModel{" +
                "ID=" + ID +
                ", QQ='" + QQ + '\'' +
                ", Email='" + Email + '\'' +
                ", EduID=" + EduID +
                ", EduName='" + EduName + '\'' +
                ", IsMarry=" + IsMarry +
                ", ChildNumID=" + ChildNumID +
                ", ChildNum='" + ChildNum + '\'' +
                ", Addr='" + Addr + '\'' +
                ", LiveID=" + LiveID +
                ", LiveName='" + LiveName + '\'' +
                ", IsPerson=" + IsPerson +
                ", IsPost=" + IsPost +
                ", IsContact=" + IsContact +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getEduID() {
        return EduID;
    }

    public void setEduID(int eduID) {
        EduID = eduID;
    }

    public String getEduName() {
        return EduName;
    }

    public void setEduName(String eduName) {
        EduName = eduName;
    }

    public int getIsMarry() {
        return IsMarry;
    }

    public void setIsMarry(int isMarry) {
        IsMarry = isMarry;
    }

    public int getChildNumID() {
        return ChildNumID;
    }

    public void setChildNumID(int childNumID) {
        ChildNumID = childNumID;
    }

    public String getChildNum() {
        return ChildNum;
    }

    public void setChildNum(String childNum) {
        ChildNum = childNum;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public int getLiveID() {
        return LiveID;
    }

    public void setLiveID(int liveID) {
        LiveID = liveID;
    }

    public String getLiveName() {
        return LiveName;
    }

    public void setLiveName(String liveName) {
        LiveName = liveName;
    }

    public int getIsPerson() {
        return IsPerson;
    }

    public void setIsPerson(int isPerson) {
        IsPerson = isPerson;
    }

    public int getIsPost() {
        return IsPost;
    }

    public void setIsPost(int isPost) {
        IsPost = isPost;
    }

    public int getIsContact() {
        return IsContact;
    }

    public void setIsContact(int isContact) {
        IsContact = isContact;
    }
}
