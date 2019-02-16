package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.common.widget.ValidePhoneView;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.BaseModel;
import com.ahxbapp.jsqb.model.MobileModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;
/**
 * 手机号认证 (暂未用到)
 */
@EActivity(R.layout.activity_phone_ver)
public class PhoneVerActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, mPhoneTV, mOperatorsTV;
    @ViewById
    LoginEditText mOperatorsPassET, mEditCode;
    @ViewById
    ValidePhoneView mSendCode;
    @ViewById
    CheckBox mAgreeCB;
    @ViewById
    Button mNextBN;

    private MobileModel mobiles;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("手机认证");
        mNextBN.setBackgroundResource(R.drawable.btn_submit);
        //获取手机和运营商
        loadPhone();
    }

    @Click({R.id.mToolbarLeftIB, R.id.mSendCode, R.id.mAgreeCB, R.id.mNextBN})
    void phoneClick(View v) {
        switch (v.getId()) {
            case R.id.mToolbarLeftIB:
                finish();
                break;

            //TODO 发送短信验证码
            case R.id.mSendCode:
                loadCode();
                mSendCode.startTimer();
                break;

            case R.id.mAgreeCB:
                if (mAgreeCB.isChecked()) {
                    mNextBN.setEnabled(true);
                    mNextBN.setBackgroundResource(R.drawable.btn_submit);
                } else {
                    mNextBN.setEnabled(false);
                    mNextBN.setBackgroundResource(R.color.btn_gray);
                }
                break;

            //TODO 提交
            case R.id.mNextBN:
                loadSubmit();
                break;

            default:
                break;
        }
    }

    //提交
    void loadSubmit() {
        String mobile = mPhoneTV.getText().toString().trim();
        String code = mEditCode.getText().toString().trim();
        String serviPsd = mOperatorsPassET.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)){
            showButtomToast("手机号码不能为空！");
            return;
        }
        if (TextUtils.isEmpty(code)){
            showButtomToast("短信验证码不能为空！");
            return;
        }
        if (TextUtils.isEmpty(serviPsd)){
            showButtomToast("运营商服务密码不能为空！");
            return;
        }

        showDialogLoading();


        APIManager.getInstance().next_phone(this, serviPsd, code, mobile, new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                hideProgressDialog();
                if (result == 1) {
                    setResult(RESULT_OK);
                    finish();
                }
                showButtomToast(message);
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    //获取手机号码和运营商
    void loadPhone() {
        showDialogLoading();
        APIManager.getInstance().operators(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseModel<MobileModel> baseModel = (BaseModel<MobileModel>) model;
                if (baseModel.getResult() == 1) {
                    mPhoneTV.setText(baseModel.getData().getMobile());
                    mOperatorsTV.setText(baseModel.getData().getOperator());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    //获取验证码
    void loadCode() {
        String mobile = mPhoneTV.getText().toString().trim();
        APIManager.getInstance().phone_ver(this, mobile, new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                showButtomToast(message);
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

}
