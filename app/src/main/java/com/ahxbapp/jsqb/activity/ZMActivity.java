package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 芝麻信用认证 by zzx
 */
@EActivity(R.layout.activity_zm)
public class ZMActivity extends BaseActivity {
    @ViewById
    WebView webview;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    ImageButton mToolbarLeftIB;

    @AfterViews
    void initView() {
        mToolbarTitleTV.setText("芝麻信用");
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);

        //设置支持缩放
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setDomStorageEnabled(true);
        webview.requestFocus();
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
        });
        requestZM();
    }

    private void requestZM() {
        APIManager.getInstance().requestZM(this, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                int result = 0;
//                String llpalt = "";
                String url = "";
                String message = "";
                try {
                    result = response.getInt("result");
                    url = response.getString("url");
                    message = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 1) {
                    webview.loadUrl(url);
                } else {
                    showButtomToast(message);
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }


    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
