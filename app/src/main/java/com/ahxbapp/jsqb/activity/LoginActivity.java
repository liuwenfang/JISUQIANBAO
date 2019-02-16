package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ImageLoadTool;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.CommonEnity;
import com.ahxbapp.jsqb.model.OtherLogin;
import com.ahxbapp.jsqb.model.User;
import com.ahxbapp.jsqb.utils.MyToast;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;

/**
 * 登录
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener {


//    private static UMShareAPI mShareAPI = null;
//    private SHARE_MEDIA platform = null;

    private OtherLogin login = new OtherLogin();

    final private int RESULT_CLOSE = 100;


    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    View layoutRoot;
    @ViewById
    EditText editName;
    @ViewById
    EditText editPassword;
    @ViewById
    View loginButton;
    @ViewById
    CheckBox btn_agree;
    @ViewById
    TextView text_agree, btn_register, btn_forget_pass;
    @ViewById
    Button button_qq, button_weixin, button_weibo;


    @AfterViews
    void init() {

//        mShareAPI = UMShareAPI.get(LoginActivity.this);

        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("登录");
        //注册通知
        EventBus.getDefault().register(this);

        text_agree.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_forget_pass.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        btn_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        button_qq.setOnClickListener(this);
        button_weixin.setOnClickListener(this);
        button_weibo.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onEvent(UserEvent.loginDestoryEvent event) {
        finish();
    }

    public void onEvent(UserEvent.forgetDestoryEvent event) {
        finish();
    }

    @Click
    void btn_register() {
        RegisterActivity_.intent(this).start();
    }


    private void settingBackground() {

    }


    @OnActivityResult(RESULT_CLOSE)
    void resultRegiter(int result) {

    }


    private void downloadValifyPhoto() {

    }

    //点击
    @Click
    void btn_forget_pass() {
        ForgetActivity_.intent(this).start();
    }

    @Click
    void loginButton(View v) {
        login();
    }

    private void login() {

        try {
            final String name = editName.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (name.isEmpty()) {
                hideProgressDialog();
                showMiddleToast("手机号码不能为空");
                return;
            }

            if (password.isEmpty()) {
                hideProgressDialog();
                showMiddleToast("密码不能为空");
                return;
            }
//            if (!btn_agree.isChecked()) {
//                hideProgressDialog();
//                showMiddleToast("请先同意《极速钱包用户协议》");
//                return;
//            }
            showBlackLoading();
            Log.e("log", "登录==>");
            APIManager manager = APIManager.getInstance();
            manager.login(this, name, password, new APIManager.APIManagerInterface.login_otherLogin() {
                @Override
                public void Success(Context context, int result, String token, String message) {
                    if (result == 1) {
                        MyToast.showToast(context, message);
                        PrefsUtil.setString(context, Global.TOKEN, token);
                        PrefsUtil.setString(context, "phone", name);
//                        setResult(RESULT_OK);
                       loadData();
                    }
                }

                @Override
                public void Failure(Context context, JSONObject response) {
                    hideProgressDialog();
                    Log.e("log", "111登录==>");
                }
            });
        } catch (Exception e) {
            Global.errorLog(e);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @FocusChange
    void editName(boolean hasFocus) {

    }

    private void upateLoginButton() {
        if (editName.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }

        if (editPassword.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }
        loginButton.setEnabled(true);
    }

    /**
     * 三方登录
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_qq:
//                platform = SHARE_MEDIA.QQ;
                login.setType(0);
                break;
            case R.id.button_weixin:
//                platform = SHARE_MEDIA.WEIXIN;
                login.setType(1);
                break;
            case R.id.button_weibo:
//                platform = SHARE_MEDIA.SINA;
                login.setType(2);
                break;
        }
//        if (mShareAPI.isInstall(this, platform)){
//            mShareAPI.doOauthVerify(this, platform, umAuthListener);
//        }else {
//            showButtomToast("请先安装对应客户端!");
//        }
//        mShareAPI.doOauthVerify(this, platform, umAuthListener);
    }

    private User user;

    /**
     * 网络请求个人信息
     */
    //下载数据
    void loadData() {
        APIManager.getInstance().user_getUserInfo(LoginActivity.this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                //用户信息
                user = (User) model;
                //存储数据库
                if (user != null) {
                    User user1 = DataSupport.find(User.class, user.getUid());
                    if (user1 == null) {
                        user.save();
                        PrefsUtil.setString(context, "user_id", String.valueOf(user.getUid()));
                        JPushInterface.setAlias(LoginActivity.this, String.valueOf(user.getID()), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                            }
                        });
                        JPushInterface.resumePush(LoginActivity.this);
                        Log.e("1", "添加1--------->> user" +user.getID());
                        EventBus.getDefault().post(new CommonEnity<>("loginSuccess"));
                        LoanMainActivity_.intent(LoginActivity.this).start();
                        finish();
                    } else {
                        user.update(user.getUid());
                        Log.e("1", "修改1-------->> user" + user.printUserInfo());
                    }
                }
                hideProgressDialog();

            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

//    private UMAuthListener info = new UMAuthListener() {
//        @Override
//        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            /**
//             * 给login赋值!
//             * type:0是QQ,1是微信,2是微博;
//             */
//            if (login.getType() == 0) {
//                //QQ
//                login.setOpenid(map.get("openid"));
//                login.setName(map.get("screen_name"));
//                login.setHead(map.get("profile_image_url"));
//            } else if (login.getType() == 1) {
//                //微信
//                login.setOpenid(map.get("openid"));
//                login.setName(map.get("screen_name"));
//                login.setHead(map.get("profile_image_url"));
//            } else if (login.getType() == 2) {
//                //微博
//                login.setOpenid(map.get("id"));
//                login.setName(map.get("screen_name"));
//                login.setHead(map.get("profile_image_url"));
//            }
//            APIManager.getInstance().login_otherLogin(LoginActivity.this, login, new APIManager.APIManagerInterface.login_otherLogin() {
//                @Override
//                public void Success(Context context, int result, String token, String message) {
//                    if (result == 1) {
//                        //已绑定,直接登录
//                        PrefsUtil.setString(context, Global.TOKEN, token);
//                        finish();
//                    } else if (result == 0) {
//
//                    }
//                }
//
//                @Override
//                public void Failure(Context context, JSONObject response) {
//                    try {
//                        if (response.getInt("result") == 0) {
//                            //未绑定,跳转到绑定页面
//                            BindingActivity_.intent(LoginActivity.this).login(login).start();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            for (String key : map.keySet()) {
//
//                System.out.println("key= " + key + " and value= " + map.get(key));
//            }
//            Log.e("用户信息", String.valueOf(map));
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA share_media, int i) {
//
//        }
//    };
//    private UMAuthListener umAuthListener = new UMAuthListener() {
//        @Override
//        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            mShareAPI.getPlatformInfo(LoginActivity.this, platform, info);
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            Log.e("WEIXIN D ASD A ", throwable.getMessage());
//
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA share_media, int i) {
//            Log.e("WEIXIN D ASD A11111111 ", "111111");
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    @Click
    void text_agree() {
        SetWebActivity_.intent(this).flag(5).start();
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }


}