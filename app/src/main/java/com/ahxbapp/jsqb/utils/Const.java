package com.ahxbapp.jsqb.utils;

public class Const {


    /**
     * app名字
     */
    public static final String APP_NAME = "#ylb#";


    /**
     * 电话的正则表达式
     */
    public static final String REGEXP_PHONE = "^((13[0-9])|(15[^4,\\D])|(18[0,0-9])|(14[0-9])|(17[0-9]))\\d{8}$";

    /**
     * 身份证的正则表达式
     */
    public static final String REGEXP_IDENTIFY = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

    /**
     * 20位数字正则表达式
     */
    public static final String REGEXP_NUM = "^\\d{20}$";

    /**
     * 相机 REQUEST_CODE
     */
    public static final int REQUEST_IMAGE = 2;

    /**
     * 裁剪
     */
    public static final int PHOTO_REQUEST_CUT = 3;

    /**
     * 七牛方式裁剪图片
     */
    public static String getMethod(int width, int hegiht) {
        return "?imageView2/4/w/" + width + "/h/" + hegiht;
    }

    /**
     * 没有订单直接扫码
     */
    public static final int TYPE_SCANNER = 0x121;

    /**
     * 只有一个订单
     */
    public static final int TYPE_ORDER_ONE = 0x122;
    /**
     * 有多个订单
     */
    public static final int TYPE_ORDER_MANY = 0x123;

    /**
     * 屏幕的宽
     */
    public static  int WIDTH;

    /**
     * 对话框通知升级
     */
    public static final int UPDATA_CLIENT = 0x124;

    /**
     * 服务器超时
     */
    public static final int GET_UNDATAINFO_ERROR = 0x125;

    /**
     * 下载apk失败
     */
    public static final int DOWN_ERROR = 0x126;

    /**
     * 返回评论数
     */
    public static final int COMMENT_NUM = 0x127;

}
