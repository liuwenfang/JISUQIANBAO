package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jayzhang on 16/9/10.
 */
@EActivity(R.layout.activity_setweb)
public class SetWebActivity extends BaseActivity {
    @Extra
    int flag;
    @Extra
    int loanLogID;

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    WebView webView;
    @AfterViews
    void initView(){
        mToolbarLeftIB.setImageResource(R.mipmap.back);

        if (flag==4){
            mToolbarTitleTV.setText("法律责任");
        }else if (flag==5){
            mToolbarTitleTV.setText("极速钱包用户协议");
        }else if (flag==8){
            mToolbarTitleTV.setText("淘宝网信息授权协议");
        }else if (flag==9){
            mToolbarTitleTV.setText("支付宝信息授权协议");
        }else if (flag==11){
            mToolbarTitleTV.setText("极速钱包借款合同");
        }else {
            mToolbarTitleTV.setText("芝麻信用信息授权协议");
        }
        showDialogLoading();

        if (flag==11){
            APIManager.getInstance().Member_Contract(this, String.valueOf(loanLogID), new APIManager.APIManagerInterface.baseBlock() {
                @Override
                public void Success(Context context, JSONObject response) {
                    hideProgressDialog();
                    webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
                    try {
                        webView.loadDataWithBaseURL(null,  response.getString("data"), "text/html", "UTF-8", null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void Failure(Context context, JSONObject response) {
                    hideProgressDialog();
                }
            });
            return;
        }
        else {
            APIManager.getInstance().person_getNews(this, String.valueOf(flag), new APIManager.APIManagerInterface.config_getNews() {
                @Override
                public void Success(Context context, String result) {
                    hideProgressDialog();
                    Log.e("html",result);
                    webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
                    webView.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null);//loadData(result,"text/html","UTF-8");
                }
                @Override
                public void Failure(Context context, JSONObject response) {
                    hideProgressDialog();
                }
            });
        }

    }
    @Click
    void mToolbarLeftIB(){
        finish();
    }
}
