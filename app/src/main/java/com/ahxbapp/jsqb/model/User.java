package com.ahxbapp.jsqb.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class User extends DataSupport implements Serializable{
    private  String nickName;
    private  String Psd;
    private String TrueName;
    private String head;
    private String WeiXin;
    private String Email;
    private String Mobile;
    /**
     * ID : 110294
     */

    private String ID;


    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    private int Sex;



    private String birthTime;
    private int Province,City,Area;
    private int uid;

    public String getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(String birthTime) {
        this.birthTime = birthTime;
    }

    public int getProvince() {
        return Province;
    }
    public void setProvince(int province) {
        Province = province;
    }

    public int getCity() {
        return City;
    }
    public void setCity(int city) {
        City = city;
    }

    public int getArea() {
        return Area;
    }
    public void setArea(int area) {
        Area = area;
    }

    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickName() {return nickName;}
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPsd() {
        return Psd;
    }
    public void setPsd(String psd) {
        Psd = psd;
    }

    public String getHead() {
        return head;
    }
    public void setHead(String head) {
        this.head = head;
    }

    public String getTrueName() {
        return TrueName;
    }
    public void setTrueName(String trueName) {
        TrueName = trueName;
    }

    public String getWeiXin() {
        return WeiXin;
    }

    public void setWeiXin(String weiXin) {
        WeiXin = weiXin;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String printUserInfo(){
        return "uid:"+uid+",nickname:"+nickName+",truename:"+TrueName+",pad:"+Psd+",sex:"+Sex+",birth:"+birthTime+",province:"+Province+",city:"+City+",Area:"+Area+",head:"+head;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
