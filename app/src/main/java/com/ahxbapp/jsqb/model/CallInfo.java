package com.ahxbapp.jsqb.model;

/**
 * Created by zzx on 2017/8/31 0031.
 */

public class CallInfo {
    //    String callDurationStr = min + "分" + sec + "秒";
//    String callOne = "类型：" + callTypeStr + ", 称呼：" + callName + ", 号码："
//            + callNumber + ", 通话时长：" + callDurationStr + ", 时间:" + callDateStr
//            + "\n---------------------\n";
    String callDuration, callTypeStr, callName, callNum, callDate;

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallTypeStr() {
        return callTypeStr;
    }

    public void setCallTypeStr(String callTypeStr) {
        this.callTypeStr = callTypeStr;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCallNum() {
        return callNum;
    }

    public void setCallNum(String callNum) {
        this.callNum = callNum;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }
}
