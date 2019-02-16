package com.ahxbapp.jsqb.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseWebFragment;
import com.ahxbapp.jsqb.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zzx on 2017/12/20 0020.
 */
@EFragment(R.layout.fragment_web)
public class LoanSuperFragment extends BaseWebFragment implements PullToRefreshScrollView.OnRefreshListener {
    @ViewById
    TextView mToolbarTitleTV; //title
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    protected WebView webView;
    @ViewById
    PullToRefreshScrollView scrollView;

    private String webUrl;

    public void setUrl(String url) {
        this.webUrl = url;
        webView.loadUrl(url);
    }

    @AfterViews
    void initView() {
        mToolbarTitleTV.setText("贷款超市");
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //加载需要显示的网页
//        webView.loadUrl(UrlContents.AWARD, map);
        //设置Web视图
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (webUrl.equals(url)) {
                    mToolbarLeftIB.setVisibility(View.GONE);
                } else {
                    mToolbarLeftIB.setVisibility(View.VISIBLE);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                scrollView.onRefreshComplete();
                super.onPageFinished(view, url);
            }
        });

        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarLeftIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.goBack();
            }
        });
        scrollView.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        webView.reload();
    }

    @Override
    public boolean onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }else{
            return false;
        }
    }
}
