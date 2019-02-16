package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
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
import com.ahxbapp.jsqb.model.AlipayModel;
import com.ahxbapp.jsqb.model.AlipayRegex;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
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
 * 支付宝认证
 */
@EActivity(R.layout.activity_alipay_cer)
public class AlipayCerActivity extends BaseActivity {

    WebView webview;

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, mToolbarRightIB;

    private String loginUrl = "https://auth.alipay.com/login/index.htm";

    private String checkCodeUrl = "https://authgtj.alipay.com/login/checkSecurity.htm";
    private String orderUrl = "https://consumeprod.alipay.com/record/standard.htm";
    //    private List<AlipayModel> orderList = new ArrayList<>();
    private int status = 0;    //0  登录页  1 订单列表   2 订单详情
    AlipayModel model = new AlipayModel();

    @AfterViews
    void init() {
        showMiddleToast("如需扫描二维码，请点击右上角帮助\n如出现白屏，请退出重新进入");
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("支付宝认证");

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
                super.onProgressChanged(view, newProgress);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
//                showProgressBar(true,"支付宝账户认证中...");
                showProgressBar(true, "若出现白屏现象，请返回再次进入...");
                if (status == 0) {
                    if (url.lastIndexOf(checkCodeUrl) != -1) {
                        showProgressBar(false);
                        webview.loadUrl("javascript:function initStyle(){ " +
                                "var head=document.getElementsByClassName('nav-wraper')[0];head.style.display='none';" +
                                "var reg=document.getElementsByClassName('ui-securitycore-tip')[0];reg.style.display='none';" +
                                "var hide1=document.getElementsByClassName('ui-tiptext-container')[0];hide1.style.display='none';" +
                                "var hide2=document.getElementsByClassName('authcenter-foot')[0];hide2.style.display='none';" +
                                "var container=document.getElementsByClassName('authcenter-body-container')[0];container.style.width='auto';container.style.border='none';" +
                                "var authcenterbody=document.getElementsByClassName('authcenter-body')[0];authcenterbody.style.width='auto';authcenterbody.style.padding='0px';" +
                                "var mobile=document.getElementsByClassName('ui-form-item-mobile')[0];mobile.style.paddingLeft='65px';" +
                                "var success=document.getElementsByClassName('ui-form-item-success')[0];success.style.paddingLeft='65px';" +
                                "var submit=document.getElementsByClassName('ui-form-item-submit')[0];submit.style.paddingLeft='65px';" +
//                            "var ui-form-other=document.getElementsByClassName('ui-form-other')[0];ui-form-other.style.display='none';"+
                                "}");
                        webview.loadUrl("javascript:initStyle();");
                        webview.setVisibility(View.VISIBLE);
                    } else {
                        showProgressBar(false);
                        webview.loadUrl("javascript:function initStyle(){ var head=document.getElementsByClassName('authcenter-head')[0];head.style.display='none';" +
                                "var title=document.getElementById('J-loginMethod-tabs');title.style.display='none';" +
//                                "var hide1=document.getElementById('J-authcenter-bg');hide1.style.background='#908d8d';"+
                                "var hide2=document.getElementById('J-authcenter-bgImg');hide2.style.display='none';" +
                                "var hide3=document.getElementsByClassName('ui-form-other')[1];hide3.style.display='none';" +
                                "var hide4=document.getElementsByClassName('authcenter-body-logo')[0];hide4.style.display='none';" +
//                                "var login=document.getElementById('J-login');login.style.background='none;';login.style.padding='0px';"+
//                                "var lblUser=document.getElementById('J-label-user');lblUser.style.display='none';"+
//                                "var txtUser=document.getElementsByClassName('ui-form-item')[0];txtUser.style.border='1px solid #cccccc';txtUser.style.width='100%';"+

//                                "var loginContainer=document.getElementsByClassName('authcenter-body-login')[0];loginContainer.style.width='100%';"+
                                "var authcenterBody=document.getElementsByClassName('authcenter-body')[0];authcenterBody.style.padding='0px';" +

                                "var uiForget=document.getElementsByClassName('ui-form-other-fg')[0];uiForget.style.display='none';" +
                                "}");
                        webview.loadUrl("javascript:initStyle();");
                        webview.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (url.lastIndexOf("my.m.taobao.com") != -1 || url.lastIndexOf("login.m.etao.com") != -1 ||
                            url.lastIndexOf("login.taobao.com") != -1 ||
                            url.lastIndexOf("login.m.tmall.com") != -1 ||
                            url.lastIndexOf("h5.m.taobao.com") != -1 ||
                            url.lastIndexOf("pass.tmall.com") != -1) {
                    } else {
                        if (status == 1) {
                            webview.loadUrl("javascript:window.local_obj.showSource(document.body.innerHTML);");
                        } else if (status == 2) {
                            webview.loadUrl("javascript:window.local_obj.showSource(document.body.innerHTML);");
                        }
//                        else if (status == 3) {
//                            webview.loadUrl("javascript:window.local_obj.showSource(document.body.innerHTML);");
//                        }
                    }
                }
//                timer.schedule(task, 1000, 1000); // 1s后执行task,经过1s再次执行
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (status == 0 && url.lastIndexOf("https://my.alipay.com/portal/i.htm") != -1) {
                    webview.setVisibility(View.INVISIBLE);
                    view.loadUrl("https://custweb.alipay.com/account/index.htm");
                    status = 1;
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//          super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });
    }

    @Click
    void mToolbarLeftIB() {
        onBackPressed();
    }

    @Click
    void mToolbarRightIB() {
        HelpActivity_.intent(this).start();
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            String ht = html;
            if (status == 1) {
//                htmlstr1 = html;
                Matcher emailRegex = Pattern.compile(AlipayRegex.AlipayEmailRegex).matcher(ht);
                while (emailRegex.find()) {
                    model.setEmail(emailRegex.group(1));
                }

                Matcher phoneRegex = Pattern.compile(AlipayRegex.AlipayPhoneRegex).matcher(ht);
                while (phoneRegex.find()) {
                    model.setMobile(phoneRegex.group(1));
                }

                Matcher realNameRegex = Pattern.compile(AlipayRegex.AlipayRealNameRegex).matcher(ht);
                while (realNameRegex.find()) {
                    model.setName(realNameRegex.group(1));
                }

                Matcher tbNameRegex = Pattern.compile(AlipayRegex.AlipayTaoBaoRegex).matcher(ht);
                while (tbNameRegex.find()) {
                    model.setTBName(tbNameRegex.group(1));
                }
                webview.post(new Runnable() {
                    @Override
                    public void run() {
                        status = 2;
                        webview.getSettings().setBlockNetworkLoads(false);
                        webview.loadUrl("https://consumeweb.alipay.com/record/huabei/index.htm");
                    }
                });
            } else if (status == 2) {
                Matcher huanRegex = Pattern.compile(AlipayRegex.AlipayHuabeiHuanRegex).matcher(ht);
                while (huanRegex.find()) {
                    model.setHBHKTotal(huanRegex.group(1));
                }

                Matcher keRegex = Pattern.compile(AlipayRegex.AlipayHuabeiYongRegex).matcher(ht);
                while (keRegex.find()) {
                    model.setHBKYTotal(keRegex.group(1));
                }

                Matcher hbZRegex = Pattern.compile(AlipayRegex.AlipayHuabeiZongRegex).matcher(ht);
                while (hbZRegex.find()) {
                    model.setHBTotal(hbZRegex.group(1));
                }
                subInfo();
            }
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

//    private String htmlstr, htmlstr1, htmlstr2;

    //    void subInfo() {//  花呗  个人信息   账单
//
//        new APIManager().alipay_add(this, htmlstr, htmlstr1, htmlstr2, new APIManager.APIManagerInterface.baseBlock() {
//            @Override
//            public void Success(Context context, JSONObject response) {
//                String message;
////                Log.e("tag", "支付宝认证==》" + response.toString());
//                try {
//                    message = response.getString("message");
//                    if (response.getInt("result") == 1) {
//                        showMiddleToast(message);
//                        setResult(RESULT_OK);
//                        finish();
//                    } else {
//                        showMiddleToast(message);
//                        reloadTao();
//                    }
//                } catch (JSONException e) {
//                    showMiddleToast("网络异常，请重试");
//                    reloadTao();
//                }
//            }
//
//            @Override
//            public void Failure(Context context, JSONObject response) {
//                showMiddleToast("网络异常，请重试");
//                reloadTao();
//            }
//        });
//    }
    private List<AlipayModel> orderList = new ArrayList<>();

    void subInfo() {
        orderList.clear();
        orderList.add(model);
        new APIManager().alipay_add(this, orderList, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                String message;
//                Log.e("tag", "支付宝认证==》" + response.toString());
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

    void reloadTao() {
        webview.post(new Runnable() {
            @Override
            public void run() {
                status = 0;
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

}


//    new Thread(new Runnable() {
//@Override
//public void run() {
//        try {
//        htmlstr = Jsoup.connect("https://consumeweb.alipay.com/record/huabei/index.htm").get().toString();
//        htmlstr1 = Jsoup.connect("https://custweb.alipay.com/account/index.htm").get().toString();
//        htmlstr2 = Jsoup.connect(orderUrl).get().toString();
//        runOnUiThread(new Runnable() {
//@Override
//public void run() {
//        subInfo();
//        }
//        });
//        } catch (IOException e) {
//        e.printStackTrace();
//        }
//        }
//        }).start();
