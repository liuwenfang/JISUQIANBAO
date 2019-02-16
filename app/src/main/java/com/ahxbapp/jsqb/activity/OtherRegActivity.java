package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.OtherLogin;
import com.ahxbapp.jsqb.model.User;
import com.ahxbapp.jsqb.utils.PrefsUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import de.greenrobot.event.EventBus;

/**
 * 注册
 * 绑定失败  即从绑定页面跳到该页面
 */
@EActivity(R.layout.activity_other_reg)
public class OtherRegActivity extends BaseActivity implements View.OnClickListener {

    @Extra
    OtherLogin login;

    @ViewById
    Button regButton;
    @ViewById
    LoginEditText Pass, Passagain;

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("注册");
        User user = DataSupport.findFirst(User.class);
        regButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String pass = Pass.getText().toString();
        String passagain = Passagain.getText().toString();
        if (pass.isEmpty() || passagain.isEmpty()) {
            showMiddleToast("请输入密码");
            return;
        }
        if (!pass.equals(passagain)) {
            showMiddleToast("两次输入的密码不一致");
            return;
        }
        showDialogLoading();
        login.setPassword(pass);
        APIManager.getInstance().login_otherReg(this, login, new APIManager.APIManagerInterface.login_otherLogin() {
            @Override
            public void Success(Context context, int result, String token, String message) {
                hideProgressDialog();
                showMiddleToast(message);
                if (result == 1) {
                    PrefsUtil.setString(context, Global.TOKEN, token);
                    EventBus.getDefault().post(new UserEvent.loginDestoryEvent());
                    finish();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }
}
