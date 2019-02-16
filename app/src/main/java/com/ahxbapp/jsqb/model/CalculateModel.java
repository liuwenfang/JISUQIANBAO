package com.ahxbapp.jsqb.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xp on 16/9/5.
 */
public class CalculateModel implements Serializable{
    private AddressModel addressModel;
    private CouponModel couponModel;
    private float sendMoney;
    private SendModel sendModel;
    List<CartModel> carts;
    private float price;
    private float totalPrice;

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }

    public CouponModel getCouponModel() {
        return couponModel;
    }

    public void setCouponModel(CouponModel couponModel) {
        this.couponModel = couponModel;
    }

    public float getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(float sendMoney) {
        this.sendMoney = sendMoney;
    }

    public SendModel getSendModel() {
        return sendModel;
    }

    public void setSendModel(SendModel sendModel) {
        this.sendModel = sendModel;
    }

    public List<CartModel> getCarts() {
        return carts;
    }

    public void setCarts(List<CartModel> carts) {
        this.carts = carts;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public class SendModel implements Serializable{
        private int ID;
        private String sendMethod;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getSendMethod() {
            return sendMethod;
        }

        public void setSendMethod(String sendMethod) {
            this.sendMethod = sendMethod;
        }
    }
}
