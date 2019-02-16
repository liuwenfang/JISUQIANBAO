package com.ahxbapp.jsqb.utils;

import android.content.Context;

import org.xutils.http.RequestParams;

public class UrlContents {

    public static final String BASE_URL = "http://www.hcroad.com/";
//    public static final String BASE_URL = "http://192.168.65.116:8888/";
    // public static final String BASE_URL = "http://hcroad.hicp.net:8888/";
    //  public static final String BASE_URL = "http://hcroad.hicp.net:8888/api/";

    /**
     * 公共的头布局
     */
    public static void addHead(RequestParams params, Context context) {
        params.addHeader("username", PrefsUtil.getString(context, "telPhone"));
        params.addHeader("token", PrefsUtil.getString(context, "token"));
        params.addHeader("Accept", "application/json");
        params.addHeader("Content-Type", "application/json");
    }

    /**
     * 登陆获取token
     */
    public static final String getToken = BASE_URL + "api/v0/login";
    /**
     * 找回密码验证码发送
     */
    public static final String FINDPWD = BASE_URL + "api/v0/findPwd";
    /**
     * 找回密码
     */
    public static final String CHANGEPWD = BASE_URL + "api/v0/changePwd";
    /**
     * 获取验证码
     */
    public static final String getSmCode = BASE_URL + "api/v0/smCode";

    /**
     * 注册
     */
    public static final String register = BASE_URL + "api/v0/register";

    /**
     * 规范诊疗
     */
    public static final String NEWS = BASE_URL + "api/v1/news";

    /**
     * 获取规范诊疗评论
     */
    public static final String getComment(int id) {
        return BASE_URL + "api/v1/news/" + id + "/comment";
    }

    /**
     * 增加评论
     */
    public static final String addComment = BASE_URL + "api/v1/comment";

    /**
     * 新闻详情
     */
    public static final String getDetialNews(int id) {
        return BASE_URL + "share/news/" + id;
    }

    /**
     * 点赞
     */
    public static final String upvoteComment(int id) {
        return BASE_URL + "api/v1/comment/" + id + "/upvote";
    }

    /**
     * 取消点赞
     */
    public static final String cancelUpvote(int id) {
        return BASE_URL + "api/v1/comment/" + id + "/cancel";
    }

    /**
     * 签到
     */
    public static final String SIGN = BASE_URL + "api/v1/sign";

    /**
     * 抽奖
     */
    public static final String AWARD = BASE_URL + "share/signGame";
    /**
     * 上传图片
     */
    public static final String UPLOADPIC = BASE_URL + "api/v1/uploadPic";

    /**
     * 登陆后修改密码
     */
    public static final String NEWPWD = BASE_URL + "api/v1/newPwd";

    /**
     * 认证
     */
    public static final String AUTHOR = BASE_URL + "api/v1/author";

    /**
     * 上传头像
     */
    public static final String AVATAR = BASE_URL + "api/v1/avatar";

    /**
     * 产品清单
     */
    public static final String PRODUCTION = BASE_URL + "api/v1/production";

    /**
     * 查询产品
     */
    public static final String QUERY = BASE_URL + "api/v1/production/query";

    /**
     * 查看产品
     */
    public static final String getProduction(int id) {
        return BASE_URL + "api/v1/production/" + id;
    }

    /**
     * 查看配送商
     */
    public static final String DISTRIBUTION = BASE_URL + "api/v1/distribution";

    /**
     * 查看进行中的订单
     */
    public static final String LIST = BASE_URL + "api/v1/order/list";
    /**
     * 查看进行中的订单
     */
    public static final String LIST2 = BASE_URL + "api/v1/order/list2";

    /**
     * 创建订单
     */
    public static final String CREATEORDER = BASE_URL + "api/v1/order/create";

    /**
     * 删除订单
     */
    public static final String deleteOrder(int id) {
        return BASE_URL + "api/v1/order/delete/" + id;
    }

    /**
     * 针对订单扫码
     */
    public static final String barcode(int id) {
        return BASE_URL + "api/v1/order/barcode/" + id;
    }

    /**
     * 没有订单直接扫码
     */
    public static final String BARCODE = BASE_URL + "api/v1/barcode/production";

    /**
     * 完成订单
     */
    public static final String complete(int id) {
        return BASE_URL + "api/v1/order/complete/" + id;
    }

    /**
     * 修改订单数量
     */
    public static final String update(int id) {
        return BASE_URL + "api/v1/order/update/" + id;
    }

    /**
     * 查看积分
     */
    public static final String BONUS = BASE_URL + "api/v1/bonus";

    /**
     * 站内短信
     */
    public static final String NOTICE = BASE_URL + "api/v1/notice";
    /**
     * 删除短信
     */
    public static final String DELETENOTICE = BASE_URL + "api/v1/notice/delete";
    /**
     * 意见反馈列表
     */
    public static final String FEEDBACK = BASE_URL + "api/v1/feedback";

    /**
     * 创建意见
     */
    public static final String CREATE_FEEDBACK = BASE_URL + "api/v1/feedback/create";

    /**
     * 意见删除
     */
    public static final String deleteOption(int id) {
        return BASE_URL + "api/v1/feedback/delete/" + id;
    }

    /**
     * 活动列表
     */
    public static final String ACTIVITY = BASE_URL + "api/v1/activity";

    /**
     * 活动列表
     */
    public static final String getActivity(int id) {
        return BASE_URL + "api/v1/activity/" + id;
    }

    /**
     * 查看评论
     */
    public static final String getRecord(int id) {
        return BASE_URL + "api/v1/activity/" + id + "/record";
    }

    /**
     * 报名参加活动
     */
    public static final String JOIN_ACTIVITY = BASE_URL + "api/v1/activity/join";

    /**
     * 兑换商品
     */
    public static final String EX_GOODS = BASE_URL + "api/v1/ex_goods";
    /**
     * 抽奖商品
     */
    public static final String LT_GOODS = BASE_URL + "api/v1/lt_goods";

    /**
     * 抽奖查看
     */
    public static final String getGoods(int id) {
        return BASE_URL + "api/v1/goods/" + id;
    }

    /**
     * 参加抽奖/兑换
     */
    public static final String EXCHANGE = BASE_URL + "api/v1/goods/exchange";

    /**
     * 刷新用户信息
     */
    public static final String REFRESHUSER = BASE_URL + "api/v1/user";

    /**
     * banner信息
     */
    public static final String BANNER = BASE_URL + "api/v1/banner";

    /**
     * 完善用户信息
     */
    public static final String COMPLETEUSER = BASE_URL + "api/v1/completeUser";

    /**
     * 关于我们
     */
    public static final String CONTENT = BASE_URL + "api/v1/content";

    /**
     * 版本更新
     */
    public static final String ANDROID = BASE_URL + "api/v0/android";

    /**
     * 自动补全配送商
     */
    public static final String DISTRI = BASE_URL + "api/v1/distri";

    /**
     * 获取七牛的token
     */
    public static final String getQNToken(String key) {
        return BASE_URL + "api/v1/pic/token/" + key;
    }
}
