package com.ahxbapp.jsqb.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.htmltext.URLSpanNoUnderline;
import com.ahxbapp.common.photopick.CameraPhotoUtil;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.model.CommonEnity;
import com.ahxbapp.jsqb.utils.BitmapHelper;
import com.ahxbapp.jsqb.utils.StringUtils;
import com.nostra13.universalimageloader.utils.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.Date;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_web)
public class WebActivity extends BaseActivity {
    @Extra
    protected String url;
    @Extra
    int type;
    @Extra
    String title;
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV; //title

    @ViewById
    protected WebView webView;

    @AfterViews
    protected final void initWebActivity() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarLeftIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!LoanMainActivity.isStart) {//如果mainActivity没有打开
                    LoanMainActivity_.intent(WebActivity.this).start();
                }
                finish();
            }
        });
        mToolbarTitleTV.setText(title);
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
        webView.setWebViewClient(new webViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.d("TAG", "URL------:" + url);
                // 判断方法1 ,注释掉方法2再测试
                if (url.contains("platformapi/startapp") || url.contains("platformapi/startApp")) {
                    startAlipayActivity(url);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                L.d("TAG", "onPageStarted------:" + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String title = view.getTitle();
                L.d("TAG", "onPageFinished------:" + url + "============" + title);
                if (!StringUtils.isEmpty(title) && !isEnglish(title)) {
                    mToolbarTitleTV.setText(view.getTitle() + "");
                }
                super.onPageFinished(view, url);
            }
        });

        if (url != null) {
//            webView.loadUrl(url, MyAsyncHttpClient.getMapHeaders());
            webView.loadUrl(url);
        }
        EventBus.getDefault().register(this);
    }

    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final int RESULT_REQUEST_PHOTO = 1005;
    private Uri fileUri;

    @OptionsItem
    void action_takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = CameraPhotoUtil.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, RESULT_REQUEST_PHOTO);
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_REQUEST_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    fileUri = data.getData();
                    Bitmap bitmap = getBitmapFromUri(fileUri);
                    String name = new Date().getTime() + "";
                    File file = BitmapHelper.saveMyBitmap(bitmap, name);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    WebActivity.this.sendBroadcast(intent);
                }
            }

        }
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (!LoanMainActivity.isStart) {//如果mainActivity没有打开
                LoanMainActivity_.intent(this).start();
            }
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        webView = null;
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public static class CustomWebViewClient extends WebViewClient {

        Context mContext;

        public CustomWebViewClient(Context context) {
            mContext = context;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return URLSpanNoUnderline.openActivityByUri(mContext, url, false, false);
        }
    }

    public void onEvent(CommonEnity event) {
        if (event.getType().equals("isVip") || event.getType().equals("signSuccess")) {
            finish();
        }
    }

    public boolean isEnglish(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

}
