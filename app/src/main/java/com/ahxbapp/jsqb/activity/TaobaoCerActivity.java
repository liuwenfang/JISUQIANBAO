package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.TaoBaoModel;
import com.ahxbapp.jsqb.model.TaobaoRegex;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 淘宝认证 (暂未用到)
 */
@EActivity(R.layout.activity_taobao_cer)
public class TaobaoCerActivity extends BaseActivity {

    WebView webview;

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;

    private String loginUrl = "https://login.m.taobao.com/login.htm";

    private List<TaoBaoModel> orderList = new ArrayList<>();
    private int status = 0;    //0  登录页  1 订单列表   2 订单详情
    private long lastTime;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("淘宝网认证");
        webview = (WebView) findViewById(R.id.wv);

        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);

        webview.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        //设置支持缩放
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.getSettings().setDomStorageEnabled(true);
        webview.requestFocus();
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setBlockNetworkImage(true);
        initView();
    }

    void initView() {
        status = 0;
        webview.setVisibility(View.GONE);
        showProgressBar(true);
        //加载需要显示的网页
        webview.loadUrl(loginUrl);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d("31312",
                        newProgress + "===" + view.getProgress() + ":onProgressChanged URL" + view.getUrl());
                if (newProgress == 100) {
                    Log.e("--312312", "当前页面加载到100:>>>>>>>>onProgressChanged URL"
                            + view.getUrl());
                    //lastPage();
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                showProgressBar(true, "淘宝账户认证中...");
                if (status == 0) {
                    showProgressBar(false);
                    webview.loadUrl("javascript:function initStyle(){ var head=document.getElementsByClassName('head')[0];head.style.display='none';" +
                            "var reg=document.getElementsByClassName('other-link')[0];reg.style.display='none';" +
                            "var regButton=document.getElementById('submit-btn');regButton.style.backgroundColor='#0990DC';regButton.style.fontSize='16px';regButton.style.color='white';regButton.style.border='1px solid #0990DC';" +
                            "}");
                    webview.loadUrl("javascript:initStyle();");
                    webview.setVisibility(View.VISIBLE);
                } else {
                    if (url.lastIndexOf("my.m.taobao.com") != -1 ||
                            url.lastIndexOf("login.m.etao.com") != -1 ||
                            url.lastIndexOf("login.taobao.com") != -1 ||
                            url.lastIndexOf("login.m.tmall.com") != -1 ||
                            url.lastIndexOf("h5.m.taobao.com") != -1 ||
                            url.lastIndexOf("pass.tmall.com") != -1) {

                    } else {
                        if (status == 1) {
                            webview.loadUrl("javascript:window.local_obj.showSource(document.body.innerHTML);");
                        } else if (status == 2) {
//                            webview.loadUrl("javascript:window.local_obj.showSource(document.body.innerHTML);");
                        }
                    }
                }
//                timer.schedule(tasww, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("-----------",
                        url);
                if (status == 0 && url.lastIndexOf("http://h5.m.taobao.com/") != -1) {
                    lastTime = System.currentTimeMillis();
                    webview.setVisibility(View.INVISIBLE);
                    view.loadUrl("https://member1.taobao.com/member/fresh/deliver_address.htm");
                    status = 1;
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("WebView", "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//              super.onReceivedSslError(view, handler, error);

                Log.e("---", "onReceivedSslError...");
                Log.e("---", "Error: " + error);
                handler.proceed();
            }

        });
    }

    @OptionsItem(android.R.id.home)
    protected final void annotaionClose() {
        onBackPressed();
    }


    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            String ht = html;

            Matcher mAddressTotal = Pattern.compile(TaobaoRegex.Address_list_item).matcher(ht);
            Log.e("mAddressTotal++++", mAddressTotal + "");
            while (mAddressTotal.find()) {
                String data = mAddressTotal.group(1);
                Matcher mAddressdata = Pattern.compile(TaobaoRegex.Address_list_data).matcher(data);
                Log.e("mAddressdata++++", mAddressdata + "");

                while (mAddressdata.find()) {
                    TaoBaoModel model = new TaoBaoModel();
                    String username = mAddressdata.group(1);
                    String add1 = mAddressdata.group(2);
                    String add = mAddressdata.group(3);
                    String phone = mAddressdata.group(5);
                    String post = mAddressdata.group(4);
                    model.setUserName(username);
                    model.setAddress(add1 + add);
                    model.setPhone(phone);
                    model.setPostAdd(post);
                    orderList.add(model);
                }
            }
            new APIManager().taobao_add(TaobaoCerActivity.this, orderList, new APIManager.APIManagerInterface.baseBlock() {
                @Override
                public void Success(Context context, JSONObject response) {
                    String message;
                    try {
                        message = response.getString("message");
                        if (response.getInt("result") == 1) {
                            showMiddleToast(message);
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            showMiddleToast(message);
                            reloadTao();
                        }
                    } catch (JSONException e) {
                        showMiddleToast("网络异常，请重试");
                        reloadTao();
                    }
                }

                @Override
                public void Failure(Context context, JSONObject response) {
                    showMiddleToast("网络异常，请重试");
                    reloadTao();
                }
            });
        }
    }

    class SubThread extends Thread {
        private WebView webView;
        private String url;

        SubThread(WebView webView, String url) {
            this.webView = webView;
            this.url = url;
        }

        public void run() {
            webView.loadUrl(url);
        }
    }

    void reloadTao() {
        webview.post(new Runnable() {
            @Override
            public void run() {
                status = 0;
                //showBlackLoading();
                //加载需要显示的网页
                webview.loadUrl(loginUrl);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        webview.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();

        webview.pauseTimers();
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                webview.setVisibility(View.VISIBLE);
//                hideProgressDialog();

            } else if (msg.what == 2) {
            }
            super.handleMessage(msg);
        }

        ;
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
            task.cancel();
            timer.cancel();
        }
    };

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
