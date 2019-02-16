package com.ahxbapp.jsqb.utils;

import android.content.Context;

import org.xutils.ex.HttpException;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by lxq on 2015/12/3.
 */
public class ExceptionUtils {

    public static void getErrorMessage(Context context, Throwable ex) {
        if (ex instanceof HttpException) {
            if (((HttpException) ex).getCode() == 401) {
//                Intent intent = new Intent(context, TokenLoseActivity.class);

//                context.startActivity(intent);
            }
            MyToast.showToast(x.app(), ((HttpException) ex).getResult(), 2);
        }
        if (ex instanceof ConnectException) {
            MyToast.showToast(x.app(), "网络连接异常", 2);
        }
        if(ex instanceof SocketTimeoutException){
            MyToast.showToast(x.app(), "链接超时", 2);
        }
    }
}
