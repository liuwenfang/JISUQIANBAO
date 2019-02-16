package com.ahxbapp.jsqb.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xp on 16/8/31.
 */
public class OrderModel implements Serializable {
    private int ID;
    private String OrderNO;
    private float totalPrice;
    private float freight;
    private float Price;
    private int OrderState;
    private int CoupID;
    private int CoupPrice;
    private List<OrderDetailModel> OrderDetail;
//    private float youhui;


    public int getCoupID() {
        return CoupID;
    }

    public void setCoupID(int coupID) {
        CoupID = coupID;
    }

    public int getCoupPrice() {
        return CoupPrice;
    }

    public void setCoupPrice(int coupPrice) {
        CoupPrice = coupPrice;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getOrderNO() {
        return OrderNO;
    }

    public void setOrderNO(String orderNO) {
        OrderNO = orderNO;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
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

    public int getOrderState() {
        return OrderState;
    }

    public void setOrderState(int orderState) {
        OrderState = orderState;
    }

    public List<OrderDetailModel> getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(List<OrderDetailModel> orderDetail) {
        OrderDetail = orderDetail;
    }

    //订单商品信息
    public class OrderDetailModel implements Serializable{
        /*
        * thumbnail : http://ut.ahceshi.com:80/UpLoad/Pic/2016-07/ae9b875a-b071-4c0c-b591-3a118c95d356.png,
	Size : S,
	Num : 1,
	Price : 10,
	Name : 连帽拉链卫衣,
	ID : 193,
	Subtotal : 10
        * */
        private int ID;
        private int Num;
        private String Size;
        private String Name;
        private String thumbnail;
        private float Price;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getNum() {
            return Num;
        }

        public void setNum(int num) {
            Num = num;
        }

        public String getSize() {
            return Size;
        }

        public void setSize(String size) {
            Size = size;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public float getPrice() {
            return Price;
        }

        public void setPrice(float price) {
            Price = price;
        }
    }
}
