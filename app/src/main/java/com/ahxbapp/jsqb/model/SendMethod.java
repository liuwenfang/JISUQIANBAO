package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by xp on 16/9/6.
 */
public class SendMethod implements Serializable{
    private int ID;
    private String Name;
    private float freight;

    public float getFreight() {
        return freight;
    }

    public void setFreight(float freight) {
        this.freight = freight;
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
}
