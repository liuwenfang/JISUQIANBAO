package com.ahxbapp.jsqb.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.common.widget.ValidePhoneView;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.utils.MyToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 找回密码  1
 */
@EActivity(R.layout.activity_findpass)
public class ForgetActivity extends BaseActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, kefu_text;
    @ViewById
    LoginEditText editPhone, editCode;
    @ViewById
    ValidePhoneView sendCode;

    boolean isSendCode;
    private String phone;

    @AfterViews
    void initView() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("找回密码");
        EventBus.getDefault().register(this);

        kefu_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        APIManager.getInstance().member_getSerMobile(this, new APIManager.APIManagerInterface.no_object() {
            @Override
            public void Success(Context context, int result, String message, String enmessage) {
                phone = enmessage;

            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onEvent(UserEvent.forgetDestoryEvent event) {
        finish();
    }

    //发送验证码
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

        APIManager.getInstance().verifyPhone1(this, phone, new APIManager.APIManagerInterface.checkPhonePsd_back() {
            @Override
            public void Success(Context context, int result) {
                if (result == 1) {
                    isSendCode = true;
                    showMiddleToast("验证码发送成功，请注意查收!");
                    sendCode.startTimer();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //下一步
    @Click
    void nextButton() {
        final String phone = editPhone.getText().toString();
        final String code = editCode.getText().toString();

        if (phone.isEmpty()) {
            MyToast.showToast(this, "请输入手机号码!");
            return;
        }

        if (!MatchUtil.isPhone(phone)) {
            MyToast.showToast(this, "请输入正确的手机号码!");
            return;
        }

        if (code.isEmpty()) {
            MyToast.showToast(this, "请输入验证码!");
            return;
        }

        if (!isSendCode) {
            MyToast.showToast(this, "请重新获取验证码!");
            return;
        }
        showDialogLoading();
        APIManager.getInstance().resetQR(this, phone, code, new APIManager.APIManagerInterface.checkPhonePsd_back() {
            @Override
            public void Success(Context context, int result) {
                hideProgressDialog();
                if (result == 1) {
                    ForgetActivity1_.intent(ForgetActivity.this).phone(phone).start();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    @Click
    void kefu_text() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("tel:" + phone).setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
            }
        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}


