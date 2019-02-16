package com.ahxbapp.jsqb.model;

/**
 * Created by zzx on 2017/6/13 0013.
 */

public class MoneyModel {
    private int count;
    private int money;
    private boolean isClock;

    public MoneyModel(int count, int money, boolean isClock) {
        this.count = count;
        this.money = money;
        this.isClock = isClock;
    }

    public boolean isClock() {
        return isClock;
    }

    public void setClock(boolean clock) {
        isClock = clock;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
