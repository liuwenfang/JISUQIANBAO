package com.ahxbapp.jsqb.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xp on 16/9/1.
 */
public class OrderDetail implements Serializable{
    private String OrderNO;
    private String state;
    private String SendType;
    private String Name;
    private String Mobile;
    private String Zip;
    private String Address;


    private int FkState;
    private float freight,Price;
    private List<OrderDetailListModel> OrderDetail;

    public String getOrderNO() {
        return OrderNO;
    }

    public void setOrderNO(String orderNO) {
        OrderNO = orderNO;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSendType() {
        return SendType;
    }

    public void setSendType(String sendType) {
        SendType = sendType;
    }

    public int getFkState() {
        return FkState;
    }

    public void setFkState(int fkState) {
        FkState = fkState;
    }

    public float getFreight() {
        return freight;
    }

    public void setFreight(float freight) {
        this.freight = freight;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
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

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<OrderDetailListModel> getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(List<OrderDetailListModel> orderDetail) {
        OrderDetail = orderDetail;
    }

    public class OrderDetailListModel implements Serializable{
        private float Price;
        private String thumbnail,Name;
        private String Size;
        private int Num;

        public float getPrice() {
            return Price;
        }

        public void setPrice(float price) {
            Price = price;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String size) {
            Size = size;
        }

        public int getNum() {
            return Num;
        }

        public void setNum(int num) {
            Num = num;
        }
    }
}
