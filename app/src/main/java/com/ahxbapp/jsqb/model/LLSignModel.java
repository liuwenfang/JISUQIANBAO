package com.ahxbapp.jsqb.model;

/**
 * Created by zzx on 2017/7/28 0028.
 */

public class LLSignModel {

    /**
     * no_agree : 2017072776231566
     * oid_partner : 201608101001022519
     * result_sign : SUCCESS
     * ret_code : 0000
     * ret_msg : 交易成功
     * sign : f71Fg8Lxb0fA0oPv1C+3gi9auqXxBRleaRYnOnzWA3w2VBQfzYGjWM4Z0kQvOdAWu4I5EIahq4OnPglaPNLzulJPz5k8kcGkN/kMtup/T1KPSLdhfSVoDVgRIK7OhfcqsZMCy7xZkhUKF6qSXd9bwXzU0C6S3hX1a5UkwF7Ihsg=
     * sign_type : RSA
     * user_id : 41441#62246A56533DD3D9CC590F0A8D61E876s
     */

    private String no_agree;
    private String oid_partner;
    private String result_sign;
    private String ret_code;
    private String ret_msg;
    private String sign;
    private String sign_type;
    private String user_id;

    public String getNo_agree() {
        return no_agree;
    }

    public void setNo_agree(String no_agree) {
        this.no_agree = no_agree;
    }

    public String getOid_partner() {
        return oid_partner;
    }

    public void setOid_partner(String oid_partner) {
        this.oid_partner = oid_partner;
    }

    public String getResult_sign() {
        return result_sign;
    }

    public void setResult_sign(String result_sign) {
        this.result_sign = result_sign;
    }

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
