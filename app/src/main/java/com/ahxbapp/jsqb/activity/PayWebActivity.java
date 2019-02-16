package com.ahxbapp.jsqb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.utils.MD5;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 支付页面 还款  (暂未用到)
 */
@EActivity(R.layout.activity_pay_web)
public class PayWebActivity extends BaseActivity {

    //1为支付宝  2为微信
    @Extra
    int flag;
    @Extra
    String noOrder;
    @Extra
    String backM;

    @ViewById
    WebView pay_web;
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("还款");

        //设置WebView属性，能够执行Javascript脚本
        WebSettings settings = pay_web.getSettings();
        settings.setJavaScriptEnabled(true);
        String str = "";
        if (flag == 1) {
            //支付宝
            str = "customerid=101749&sdcustomno=" + noOrder + "&ordermoney=" + Float.parseFloat(backM) + "&cardno=52&faceno=zap&noticeurl=" + Global.HOST + "API/payNotifyUrl&key=0e7e7749a81e3b8735d32cf19a3b59bb";
        } else if (flag == 2) {
            //微信
            int back = (int) Float.parseFloat(backM) * 100;
            str = "customerid=101749&sdcustomno=" + noOrder + "&orderAmount=" + back + "&cardno=51&noticeurl=" + Global.HOST + "API/payNotifyUrl&backurl=https://www.baidu.com0e7e7749a81e3b8735d32cf19a3b59bb";
        }
        String MD5Str = MD5.getMD5(str);
        String payString = "";
        if (flag == 1) {
            //支付宝
            payString = "http://www.51card.cn/gateway/alipay/wap-alipay.asp?customerid=101749&sdcustomno=" + noOrder + "&ordermoney=" + Float.parseFloat(backM) + "&cardno=52&faceno=zap&noticeurl=" + Global.HOST + "API/payNotifyUrl&backurl=https://www.baidu.com&endcustomer=50221216&endip=36.5.241.79&remarks=xjkd&mark=yhk" + "0.01" + "&stype=1&ZFtype=1&sign=" + MD5Str.toUpperCase();
        } else if (flag == 2) {
            //微信
            int back = (int) Float.parseFloat(backM) * 100;
            payString = "http://www.51card.cn/gateway/weixinpay/wap-weixinpay.asp?customerid=101749&sdcustomno=" + noOrder + "&orderAmount=" + back + "&cardno=51&noticeurl=" + Global.HOST + "API/payNotifyUrl&backurl=https://www.baidu.com&sign=" + MD5Str.toUpperCase() + "&mark=xjkd";
        }

        Log.e("url====", payString);
        String strUTF8 = null;
        try {
            strUTF8 = URLDecoder.decode(payString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("strUTF8====", strUTF8);
        //showProgressBar(true, "加载支付中...");
        pay_web.loadUrl(strUTF8);
        showProgressBar(true, "加载中...");
        pay_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {

                    if (view.getUrl().lastIndexOf("www.baidu.com") != -1 && view.getUrl().lastIndexOf("state") != -1) {
                        pay_web.setVisibility(View.GONE);
                        if (view.getUrl().lastIndexOf("state=1") != -1) {
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                    finish();
                    showProgressBar(false);
                }
            }
        });
        pay_web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.lastIndexOf("www.baidu.com") != -1 && url.lastIndexOf("state") != -1) {
                    showProgressBar(true, "加载中...");
                }
                if (url.contains("platformapi/startapp")) {
                    startAlipayActivity(url);
                    // android  6.0 两种方式获取intent都可以跳转支付宝成功,7.1测试不成功
                } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                        && (url.contains("platformapi") && url.contains("startapp"))) {
                    startAlipayActivity(url);
                } else {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        return false;
                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                    pay_web.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();
            }

        });
    }

    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click
    void mToolbarLeftIB() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
