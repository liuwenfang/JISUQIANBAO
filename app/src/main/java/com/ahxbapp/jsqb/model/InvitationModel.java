package com.ahxbapp.jsqb.model;

public class InvitationModel {
    private String name;
    private int res;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public InvitationModel(String name, int res) {
        this.name = name;
        this.res = res;
    }
}
