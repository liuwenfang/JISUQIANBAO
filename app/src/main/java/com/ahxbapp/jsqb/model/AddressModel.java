package com.ahxbapp.jsqb.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by xp on 16/8/27.
 */
public class AddressModel extends DataSupport implements Serializable{
    private int AID;
    private int ID;
    private String Name;
    private String Mobile;
    private int ProID;
    private int CityID;
    private int DisID;
    private int Isdefult;
    private String Address;
    private String Zip;

    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public int getProID() {
        return ProID;
    }

    public void setProID(int proID) {
        ProID = proID;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public int getDisID() {
        return DisID;
    }

    public void setDisID(int disID) {
        DisID = disID;
    }

    public int getIsdefult() {
        return Isdefult;
    }

    public void setIsdefult(int isdefult) {
        Isdefult = isdefult;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String checkAddrStr(){
        if (Name.length()<=0){
            return "请填写收货人姓名";
        }
        if (Mobile.length()<=0){
            return "请填写手机号码";
        }
        if (ProID == 0 && CityID == 0 && DisID == 0){
            return "请选择省市区";
        }
        if (Address.length() <= 0){
            return "请填写详细地址";
        }
        return null;
    }
}
