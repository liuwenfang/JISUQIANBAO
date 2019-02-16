package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.graphics.Paint;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.common.widget.ValidePhoneView;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.utils.MyToast;
import com.ahxbapp.jsqb.utils.PrefsUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 注册
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    EditText editName, editCode, editPassword;
    @ViewById
    ValidePhoneView sendCode;
    @ViewById
    CheckBox btn_agree;
    @ViewById
    TextView text_agree;

    @AfterViews
    void initView() {

        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("注册");

        text_agree.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    //点击发送验证码
    @Click
    void sendCode() {
        String phone = editName.getText().toString();
        if (phone.isEmpty()) {
            MyToast.showToast(this, "请输入手机号码!");
            return;
        }
        if (!MatchUtil.isPhone(phone)) {
            MyToast.showToast(this, "请输入正确的手机号码!");
            return;
        }

        APIManager.getInstance().verifyPhone(this, phone, new APIManager.APIManagerInterface.checkPhonePsd_back() {
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

    //点击注册
    @Click
    void registerButton() {
        final String phone = editName.getText().toString();
        String code = editCode.getText().toString();
        final String password = editPassword.getText().toString();
        if (phone.isEmpty()) {
            MyToast.showToast(this, "电话号码不能为空!");
            return;
        }
        if (code.isEmpty()) {
            MyToast.showToast(this, "验证码不能为空!");
            return;
        }
        if (password.isEmpty()) {
            MyToast.showToast(this, "密码不能为空");
            return;
        }
        if (password.length() < 6 || password.length() > 20) {
            MyToast.showToast(this, "密码长度在6-20位字符");
            return;
        }
        if (!btn_agree.isChecked()) {
            MyToast.showToast(this, "请先同意《极速钱包用户协议》");
            return;
        }
        showDialogLoading();
        APIManager.getInstance().register(this, phone, code, password, new APIManager.APIManagerInterface.checkPhonePsd_back() {
            @Override
            public void Success(Context context, int result) {
                if (result == 1) {
                    //注册成功，登录
                    MyToast.showToast(context, "注册成功");
                    APIManager.getInstance().login(RegisterActivity.this, phone, password, new APIManager.APIManagerInterface.login_otherLogin() {
                        @Override
                        public void Success(Context context, int result, String token, String message) {
                            if (result == 1) {
                                hideProgressDialog();
                                PrefsUtil.setString(context, Global.TOKEN, token);
                                finish();

                                EventBus.getDefault().post(new UserEvent.loginDestoryEvent());
                            }
                        }

                        @Override
                        public void Failure(Context context, JSONObject response) {
                            hideProgressDialog();
                        }
                    });
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
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
