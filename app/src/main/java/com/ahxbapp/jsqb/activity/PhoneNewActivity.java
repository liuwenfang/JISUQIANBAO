//package com.ahxbapp.xjkd.activity.user;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.support.v7.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.ahxbapp.common.Global;
//import com.ahxbapp.common.ImageLoadTool;
//import com.ahxbapp.common.enter.SimpleTextWatcher;
//import com.ahxbapp.common.ui.BaseActivity;
//import com.ahxbapp.xjkd.R;
//import com.ahxbapp.xjkd.api.APIManager;
//import com.ahxbapp.xjkd.utils.MyToast;
//import com.ahxbapp.xjkd.utils.PrefsUtil;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.FocusChange;
//import org.androidannotations.annotations.OnActivityResult;
//import org.androidannotations.annotations.OptionsItem;
//import org.androidannotations.annotations.ViewById;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//@EActivity(R.layout.activity_phone)
//public class PhoneNewActivity extends BaseActivity {
//
//
//    final private int RESULT_CLOSE = 100;
//    @ViewById
//    View layoutRoot;
//    @ViewById
//    EditText editCode;
//    @ViewById
//    EditText editPassword;
//    @ViewById
//    View loginButton;
//    @ViewById
//    ImageView checkCodeImg;
//    @ViewById
//    RelativeLayout codeImgWrapper;
//    DisplayImageOptions options = new DisplayImageOptions.Builder()
//            .showImageForEmptyUri(R.mipmap.ic_default_image)
//            .showImageOnFail(R.mipmap.ic_default_image)
//            .resetViewBeforeLoading(true)
//            .cacheOnDisk(true)
//            .imageScaleType(ImageScaleType.EXACTLY)
//            .bitmapConfig(Bitmap.Config.RGB_565)
//            .considerExifParams(true)
//            .displayer(new FadeInBitmapDisplayer(300))
//            .build();
//    View androidContent;
//    TextWatcher textWatcher = new SimpleTextWatcher() {
//        @Override
//        public void afterTextChanged(Editable s) {
//            upateLoginButton();
//        }
//    };
//    TextWatcher textWatcherName = new SimpleTextWatcher() {
//        @Override
//        public void afterTextChanged(Editable s) {
////            userIcon.setImageResource(R.mipmap.ic_default_image);
////            userIcon.setBackgroundResource(R.drawable.icon_user_monkey);
//        }
//    };
//    private String globalKey = "";
//
//    @AfterViews
//    final void initAccountSetting() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar supportActionBar = getSupportActionBar();
//        supportActionBar.setDisplayHomeAsUpEnabled(true);
//    }
//
//    @OptionsItem(android.R.id.home)
//    protected final void annotaionClose() {
//        onBackPressed();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // 调用下，防止收到上次登陆账号的通知
//    }
//
//    @AfterViews
//    void init() {
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        ActionBar supportActionBar = getSupportActionBar();
//
//        getSupportActionBar().setTitle("手机号");
//      //  invalidateOptionsMenu();
//
//
//        editCode.addTextChangedListener(textWatcher);
//        editPassword.addTextChangedListener(textWatcher);
//
//        upateLoginButton();
//
//        editCode.addTextChangedListener(textWatcherName);
//
//        androidContent = findViewById(android.R.id.content);
//        androidContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int height = androidContent.getHeight();
//                if (height > 0) {
//                    ViewGroup.LayoutParams lp = layoutRoot.getLayoutParams();
//                    lp.height = height;
//                    layoutRoot.setLayoutParams(lp);
//                    androidContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//            }
//        });
//
//        checkConfig();
//
////        String lastLoginName = AccountInfo.loadLastLoginName(this);
////        if (!lastLoginName.isEmpty()) {
////            editName.setDisableAuto(true);
////            editName.setText(lastLoginName);
////            editName.setDisableAuto(false);
////            editPassword.requestFocus();
////            editName(false);
////        }
//    }
//
//
//    void checkConfig(){
//        new APIManager().login_new(this, new APIManager.APIManagerInterface.baseBlock() {
//            @Override
//            public void Success(Context context, JSONObject response) {
//                try {
//                    int s = response.getInt("flag");
//                    if (s == 1) {
//                        new APIManager().phoneConfigNew(PhoneNewActivity.this, PrefsUtil.getString(PhoneNewActivity.this, "phone"), new APIManager.APIManagerInterface.baseBlock() {
//                            @Override
//                            public void Success(Context context, JSONObject response) {
//                                try {
//                                    String imgUrl=response.getString("imgUrl");
//                                    if(imgUrl!=null&&imgUrl.length()>0){
//                                        codeImgWrapper.setVisibility(View.VISIBLE);
//                                        ImageLoader.getInstance().clearDiskCache();
//                                        ImageLoader.getInstance().clearMemoryCache();
//                                        ImageLoader.getInstance().displayImage(imgUrl, checkCodeImg, ImageLoadTool.options);
//                                    }else{
//                                        codeImgWrapper.setVisibility(View.GONE);
//
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void Failure(Context context, JSONObject response) {
//                                MyToast.showToast(context,"初始化失败");
//                            }
//                        });
//                    }
//                } catch (JSONException e) {
//                    MyToast.showToast(context,"初始化失败");
//                }
//            }
//
//            @Override
//            public void Failure(Context context, JSONObject response) {
//                MyToast.showToast(context,"初始化失败");
//            }
//        });
//
//    }
//
//    @OnActivityResult(RESULT_CLOSE)
//    void resultRegiter(int result) {
//
//    }
//
//
//    private void downloadValifyPhoto() {
//
//    }
//    @Click
//    void loginButton(View v){
//        login();
//    }
//    private void login() {
//        try {
//            String name = editCode.getText().toString();
//            String password = editPassword.getText().toString();
//            if (password.isEmpty()) {
//                showMiddleToast("服务密码不能为空");
//                return;
//            }
//
//            showDialogLoading();
//            APIManager manager=APIManager.getInstance();
//            manager.checkPhonePsdNew(this, PrefsUtil.getString(this, "phone"), password, name, new APIManager.APIManagerInterface.baseBlock() {
//                @Override
//                public void Success(Context context, JSONObject response) {
//                    try {
//                        if(response.getInt("status")==1){
//                            MyToast.showToast(context,"手机号码认证成功!");
//                            finish();
//                        }else{
//                            MyToast.showToast(context, "服务密码或验证码错误!");
//                            checkConfig();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    hideProgressDialog();
//                }
//
//                @Override
//                public void Failure(Context context, JSONObject response) {
//                    hideProgressDialog();
//                }
//            });
//        } catch (Exception e) {
//            Global.errorLog(e);
//        }
//    }
//
//    private void loginSuccess(JSONObject respanse) throws JSONException {
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//    }
//
//    @FocusChange
//    void editName(boolean hasFocus) {
//
//    }
//
//    private void upateLoginButton() {
////        if (editCode.getText().length() == 0) {
////            loginButton.setEnabled(false);
////            return;
////        }
//
//        if (editPassword.getText().length() == 0) {
//            loginButton.setEnabled(false);
//            return;
//        }
//        loginButton.setEnabled(true);
//    }
//}