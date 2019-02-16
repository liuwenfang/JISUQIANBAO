package com.ahxbapp.common.base;

import android.content.Context;
import android.util.Log;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.model.User;
import com.ahxbapp.jsqb.utils.MyToast;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.network.NetworkImpl;
import com.ahxbapp.common.util.SingleToast;

import org.apache.http.Header;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

/**
 * Created by chenchao on 15/10/11.
 */
public abstract class
MyJsonResponse extends JsonHttpResponseHandler {

    private long startTime;


    public void onMySuccess(JSONObject response) {
        int code = response.optInt("result", NetworkImpl.NETWORK_ERROR);

    }


    @Override
    public void onStart() {
        super.onStart();
        startTime = System.currentTimeMillis();
    }

    public void onMyFailure(JSONObject response) {
        if (response == null) {
            MyToast.showToast(mActivity, "发生错误,请刷新重试");
        } else {
            int code = response.optInt("result", NetworkImpl.NETWORK_ERROR);
            SingleToast.showErrorMsg(mActivity, code, response);
            if (code == -1) {
                String message = response.optString("message");
                if (message.contains("用户信息不存在")) {
                    PrefsUtil.remove(mActivity, Global.TOKEN);
                    //删除用户信息
                    DataSupport.deleteAll(User.class);
                }
            }
        }
    }

    private Context mActivity;

    public MyJsonResponse(Context activity) {
        super();
        mActivity = activity;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        Log.e("---------", response.toString());
        int code = response.optInt("result", 0);
        if (code == 1) {
            onMySuccess(response);
        } else {
            onMyFailure(response);
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
//        Log.e("MyJsonResponse", "onFinish API execute time = " + (System.currentTimeMillis() - startTime) + "ms");
    }

    // 没有网络的情况下，会调用这个回调函数，并且 errorResponse 为 null
    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        if (errorResponse == null) {
            errorResponse = sNetworkError;
        }
        onMyFailure(errorResponse);
    }

    // 服务器异常的时候会调用，这个时候返回的是 responseString，一般是 html 代码
    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        onMyFailure(sServiceError);
    }

    static JSONObject sNetworkError;
    static JSONObject sServiceError;

    static {
        try {
            String connectFailString = String.format("{\"code\":%d,\"msg\":{\"error\":\"%s\"}}",
                    NetworkImpl.NETWORK_ERROR, NetworkImpl.ERROR_MSG_CONNECT_FAIL);
            sNetworkError = new JSONObject(connectFailString);


            String serviceFailString = String.format("{\"code\":%d,\"msg\":{\"error\":\"%s\"}}",
                    NetworkImpl.NETWORK_ERROR_SERVICE, NetworkImpl.ERROR_MSG_SERVICE_ERROR);
            sServiceError = new JSONObject(serviceFailString);

        } catch (Exception e) {
            Global.errorLog(e);
        }
    }
}

