package com.ahxbapp.jsqb.model;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xp on 16/8/27.
 */
public class CommodityModel extends DataSupport implements Serializable{
    private int ID;
    private double WithHegthan;
    private double Areathan;
    private String Content;
    private String Name;
    private double Price;
    private List<ProDetail> Pro;
    private int currentColorIndex;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getWithHegthan() {
        return WithHegthan;
    }

    public void setWithHegthan(double withHegthan) {
        WithHegthan = withHegthan;
    }

    public double getAreathan() {
        return Areathan;
    }

    public void setAreathan(double areathan) {
        Areathan = areathan;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public List<ProDetail> getPro() {
        return Pro;
    }

    public void setPro(List<ProDetail> pro) {
        Pro = pro;
    }

    public int getCurrentColorIndex() {
        return currentColorIndex;
    }

    public void setCurrentColorIndex(int currentColorIndex) {
        this.currentColorIndex = currentColorIndex;
    }
}
