package com.ahxbapp.jsqb.activity;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * 支付页面 还款
 */
@EActivity(R.layout.activity_pay)
public class PayActivity extends BaseActivity {

    public static final String BUNDLE_PREPAYID = "prepay_id";
    public static final String BUNDLE_ORDERNUM = "ordernumber";

    private boolean isPrePay = false;
    private String url;
    @Extra
    String prepayId, ordernumber;


    @ViewById
    WebView pay_webView;
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;

    @AfterViews
    void init() {
        mToolbarTitleTV.setText("支付");
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarLeftIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        initWebSet();
        url = Global.aliPayHtml + prepayId + "&ordernumber=" + ordernumber + "&type=1";
        pay_webView.loadUrl(url);
    }

    private void initWebSet() {
        WebSettings webSetting = pay_webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        pay_webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        pay_webView.setWebChromeClient(new WebChromeClient() {
        });
        pay_webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("alipays://platformapi/startapp?")) {
                    try {
                        isPrePay = true;
                        Intent intent;
                        intent = Intent.parseUri(url,
                                Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {

                    }
                } else {
                    view.loadUrl(url);
                }
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受所有网站的证书
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPrePay) {
            url = Global.aliPayResultHtml + ordernumber;
            pay_webView.clearHistory();
            pay_webView.loadUrl(url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pay_webView != null) {
            pay_webView.removeAllViews();
            try {
                pay_webView.destroy();
            } catch (Throwable t) {
            }
            pay_webView = null;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
