package com.ahxbapp.jsqb.model;

import java.io.Serializable;

/**
 * Created by Admin on 2016/10/22.
 * Page
 */
public class CashModel implements Serializable {
    //    "ID": 17,
//            "Type": "普通借款",
//            "LoanNO": "qySF-3727480030",
//            "LoanId": 500,
//            "TermId": 28,
//            "AddTime": "2016-10-22",
//            "PalyTime": "--",
//            "RepayTime": "2016-11-19",
//            "Status": 0
    private int ID;
    private String Type;
    private String LoanNO;
    private int LoanId;
    private int TermId;
    private String AddTime;
    private String PalyTime;
    private String RepayTime;
    private int Status;

    public String getApplyfee() {
        return Applyfee;
    }

    public void setApplyfee(String applyfee) {
        Applyfee = applyfee;
    }

    public String getUserfee() {
        return Userfee;
    }

    public void setUserfee(String userfee) {
        Userfee = userfee;
    }

    public String getInterest() {
        return Interest;
    }

    public void setInterest(String interest) {
        Interest = interest;
    }

    public String getCoupID() {
        return CoupID;
    }

    public void setCoupID(String coupID) {
        CoupID = coupID;
    }

    private String Applyfee;
    private String Userfee;
    private String Interest;
    private String CoupID;

    @Override
    public String toString() {
        return "CashModel{" +
                "ID=" + ID +
                ", Type='" + Type + '\'' +
                ", LoanNO='" + LoanNO + '\'' +
                ", LoanId=" + LoanId +
                ", TermId=" + TermId +
                ", AddTime='" + AddTime + '\'' +
                ", PalyTime='" + PalyTime + '\'' +
                ", RepayTime='" + RepayTime + '\'' +
                ", Status=" + Status +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLoanNO() {
        return LoanNO;
    }

    public void setLoanNO(String loanNO) {
        LoanNO = loanNO;
    }

    public int getLoanId() {
        return LoanId;
    }

    public void setLoanId(int loanId) {
        LoanId = loanId;
    }

    public int getTermId() {
        return TermId;
    }

    public void setTermId(int termId) {
        TermId = termId;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String addTime) {
        AddTime = addTime;
    }

    public String getPalyTime() {
        return PalyTime;
    }

    public void setPalyTime(String palyTime) {
        PalyTime = palyTime;
    }

    public String getRepayTime() {
        return RepayTime;
    }

    public void setRepayTime(String repayTime) {
        RepayTime = repayTime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
