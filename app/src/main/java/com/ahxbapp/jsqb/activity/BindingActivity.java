package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.common.widget.ValidePhoneView;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.OtherLogin;
import com.ahxbapp.jsqb.model.User;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.utils.MyToast;
import com.ahxbapp.jsqb.utils.PrefsUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import de.greenrobot.event.EventBus;

/**
 * 绑定    ???
 * 第三方登录失败  即从登录页面跳到该页面
 */
@EActivity(R.layout.activity_binding)
public class BindingActivity extends BaseActivity implements View.OnClickListener {

    @Extra
    OtherLogin login;
    @ViewById
    LoginEditText editPhone, editCode;
    @ViewById
    ValidePhoneView sendCode;
    @ViewById
    Button nextButton;
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;

    @AfterViews
    void init() {
        User user = DataSupport.findFirst(User.class);
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("绑定");

        nextButton.setOnClickListener(this);

        //注册通知
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(UserEvent.loginDestoryEvent event) {
        finish();
    }

    //点击发送验证码
    @Click
    void sendCode() {
        String phone = editPhone.getText().toString();
        if (phone.isEmpty()) {
            MyToast.showToast(this, "请输入手机号码!");
            return;
        }
        if (!MatchUtil.isPhone(phone)) {
            MyToast.showToast(this, "请输入正确的手机号码!");
            return;
        }

        APIManager.getInstance().verifyPhone3(this, phone, new APIManager.APIManagerInterface.checkPhonePsd_back() {
            @Override
            public void Success(Context context, int result) {
                if (result == 1) {
                    showMiddleToast("验证码发送成功，请注意查收！");
                    //开始倒计时
                    sendCode.startTimer();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        String phone = editPhone.getText().toString();
        final String code = editCode.getText().toString();
        if (phone.isEmpty()) {
            showMiddleToast("请输入要绑定的手机号");
            return;
        }
        if (code.isEmpty()) {
            showMiddleToast("请输入验证码");
            return;
        }
        login.setMobile(phone);
        login.setCode(code);
        showDialogLoading();
        APIManager.getInstance().login_binding(this, login, new APIManager.APIManagerInterface.login_otherLogin() {
            @Override
            public void Success(Context context, int result, String token, String message) {
                hideProgressDialog();
                showMiddleToast(message);
                if (result == 0) {


                } else if (result == 1) {
                    //已经注册，直接绑定并登陆
                    PrefsUtil.setString(context, Global.TOKEN, token);
                    EventBus.getDefault().post(new UserEvent.loginDestoryEvent());
                    finish();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    if (response.getInt("result") == 0) {
                        //未注册，注册，绑定并登陆
                        login.setCode(code);
                        OtherRegActivity_.intent(BindingActivity.this).login(login).start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
