package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/10/19.
 * Page
 */
public class BankModel implements Serializable{
    private int ID;
    private String Name;
    private int Num;
    private int Sort;
    private String BankNOQZ;


    @Override
    public String toString() {
        return "BankModel{" +
                "ID=" + ID +
                ", Name='" + Name + '\'' +
                ", Num=" + Num +
                ", Sort=" + Sort +
                '}';
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

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public String getBankNOQZ() {
        return BankNOQZ;
    }

    public void setBankNOQZ(String bankNOQZ) {
        BankNOQZ = bankNOQZ;
    }
}
