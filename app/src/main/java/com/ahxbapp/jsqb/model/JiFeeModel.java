package com.ahxbapp.jsqb.model;

/**
 * Created by zzx on 2017/6/16 0016.
 */

public class JiFeeModel {

    /**
     * result : 1
     * message : 计算息费成功
     * Interest : 0.03
     * Userfee : 9
     * Applyfee : 20
     */

    private int result;
    private String message;
    private double Interest;
    private int Userfee;
    private int Applyfee;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getInterest() {
        return Interest;
    }

    public void setInterest(double Interest) {
        this.Interest = Interest;
    }

    public int getUserfee() {
        return Userfee;
    }

    public void setUserfee(int Userfee) {
        this.Userfee = Userfee;
    }

    public int getApplyfee() {
        return Applyfee;
    }

    public void setApplyfee(int Applyfee) {
        this.Applyfee = Applyfee;
    }
}
