package com.ahxbapp.jsqb.activity;

import android.content.Context;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.utils.MyToast;
import com.ahxbapp.jsqb.utils.PrefsUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 找回密码 2
 */
@EActivity(R.layout.activity_forget1)
public class ForgetActivity1 extends BaseActivity {

    @Extra
    String phone;
    @ViewById
    LoginEditText editPassword, editPassword1;

    @AfterViews
    void initView() {

    }

    @Click
    void btnConfirm() {
        final String phone = this.phone;
        final String pass = editPassword.getText().toString();
        String pass1 = editPassword1.getText().toString();

        if (pass.isEmpty()) {
            MyToast.showToast(this, "请输入新密码!");
            return;
        }

        if (!MatchUtil.isPasswordChecked(pass)) {
            MyToast.showToast(this, "密码长度应为6-20位!");
            return;
        }

        if (!pass.equals(pass1)) {
            MyToast.showToast(this, "两次输入密码不一致!");
            return;
        }

        showDialogLoading();
        APIManager.getInstance().resetPwd(this, phone, pass, new APIManager.APIManagerInterface.checkPhonePsd_back() {
            @Override
            public void Success(Context context, int result) {
                hideProgressDialog();
                if (result == 1) {
                    //修改密码成功，登录
                    APIManager.getInstance().login(ForgetActivity1.this, phone, pass, new APIManager.APIManagerInterface.login_otherLogin() {
                        @Override
                        public void Success(Context context, int result, String token, String message) {
                            hideProgressDialog();
                            if (result == 1) {
                                MyToast.showToast(context, message);
                                PrefsUtil.setString(context, Global.TOKEN, token);
                                finish();
                                EventBus.getDefault().post(new UserEvent.forgetDestoryEvent());
//                            PersonActivity_.intent(ForgetActivity1.this).start();
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
}
