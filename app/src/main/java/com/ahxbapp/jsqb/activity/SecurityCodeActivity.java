package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.utils.MatchUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * 修改密码
 */
@EActivity(R.layout.activity_securitycode)
public class SecurityCodeActivity extends BaseActivity implements View.OnClickListener {


    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    LoginEditText editCode, newPass, newPassagain;
    @ViewById
    Button modifyButton;

    @AfterViews
    void initView() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar supportActionBar = getSupportActionBar();
//
//        supportActionBar.setTitle("修改密码");

        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("修改密码");
        modifyButton.setOnClickListener(this);
    }

    /**
     * 修改密码
     */
    @Override
    public void onClick(View v) {
        //登录密码
        String oldPwd = editCode.getText().toString();
        String newPwd = newPass.getText().toString();
        String newPwdAgain = newPassagain.getText().toString();
        if (oldPwd.isEmpty()) {
            showMiddleToast("请输入原密码");

        } else if (newPwd.isEmpty() || newPwdAgain.isEmpty()) {
            showMiddleToast("新密码不能为空");

        } else if (!newPwd.equals(newPwdAgain)) {
            showMiddleToast("两次输入的新密码不同");

        } else if (!MatchUtil.isPasswordChecked(newPwd)) {
            showMiddleToast("请输入6-20位字符密码！");
        } else {
            showDialogLoading();
            APIManager.getInstance().person_updatePwd(this, oldPwd, newPwd, new APIManager.APIManagerInterface.no_object() {
                @Override
                public void Success(Context context, int result, String message, String enmessage) {
                    hideProgressDialog();
                    showMiddleToast(message);
                    finish();
                }

                @Override
                public void Failure(Context context, JSONObject response) {
                    hideProgressDialog();
                }
            });
        }
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
