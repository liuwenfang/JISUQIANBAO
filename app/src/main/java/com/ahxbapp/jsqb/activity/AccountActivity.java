package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.graphics.Paint;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ImageLoadTool;
import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 认证  ???
 */
@EActivity(R.layout.activity_account)
public class AccountActivity extends BaseActivity {

    @Extra
    int flag;
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, text_agree;
    @ViewById
    LoginEditText editName, editPassword, editCode;
    @ViewById
    Button define_btn;
    @ViewById
    CheckBox btn_agree;
    @ViewById
    ImageView codeImage;
    @ViewById
    LinearLayout codeLinear;

    String _yzm, _lt;


    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        text_agree.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        switch (flag) {
            case 1:
                mToolbarTitleTV.setText("学信网认证");
                editName.setHint("请输入学信网账号");
                editPassword.setHint("请输入学信网密码");
                codeLinear.setVisibility(View.VISIBLE);
                getCodeImage();
                break;
            case 2:
                mToolbarTitleTV.setText("身份证认证");
                editName.setLeftIcon(R.mipmap.icon_bgname);
                editName.setHint("请输入真实姓名");
                editPassword.setLeftIcon(R.mipmap.icon_bgshen);
                editPassword.setHint("请输入身份证号");
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 3:
                mToolbarTitleTV.setText("支付宝认证");
                editName.setHint("请输入支付宝账号");
                editPassword.setHint("请输入支付宝密码");
                text_agree.setText("《支付宝信息授权协议》");
                break;
            case 4:
                mToolbarTitleTV.setText("芝麻信用认证");
                editName.setHint("请输入支付宝账号");
                editPassword.setHint("请输入支付宝密码");
                text_agree.setText("《芝麻分信用信息授权协议》");
                break;
            case 5:
                mToolbarTitleTV.setText("淘宝认证");
                editName.setHint("请输入淘宝网账号");
                editPassword.setHint("请输入淘宝网密码");
                text_agree.setText("《淘宝网信息授权协议》");
                break;
        }
    }

    void getCodeImage() {

        showBlackLoading();
        APIManager.getInstance().home_getcodeImageUrl(this, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    _lt = response.getString("lt").toString();
                    Log.e("imageCode", Global.HOST + response.getString("yzm").toString());
                    ImageLoader.getInstance().clearMemoryCache();
                    ImageLoader.getInstance().clearDiskCache();
                    ImageLoader.getInstance().displayImage(Global.HOST + response.getString("yzm").toString(), codeImage, ImageLoadTool.options);


                    //codeImage.setImageURI(Uri.parse(Global.HOST+response.getString("yzm").toString()));
                } catch (JSONException e) {

                }

            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    void xunXinCer() {

        showBlackLoading();
        APIManager.getInstance().home_IsLNet(this, editName.getText().toString(), editPassword.getText().toString(), editCode.getText().toString(), _lt, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    int result = response.getInt("result");
                    if (result == 1) {
                        showMiddleToast(response.getString("message"));
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        showMiddleToast("账号、密码或验证码错误！请重新输入");
                    }
                } catch (JSONException e) {

                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    @Click
    void codeImage() {
        getCodeImage();
    }

    @Click
    void define_btn() {


        if (TextUtils.isEmpty(editName.getText().toString().trim())) {
            showMiddleToast(editName.getHint().toString());
            return;
        }
        if (TextUtils.isEmpty(editPassword.getText().toString().trim())) {
            showMiddleToast(editPassword.getHint().toString());
            return;
        }

        if (flag == 1) {
            if (TextUtils.isEmpty(editCode.getText().toString().trim())) {
                showMiddleToast(editCode.getHint().toString());
                return;
            }
        }

        if (!btn_agree.isChecked()) {
            showMiddleToast("请先同意" + text_agree.getText().toString());
            return;
        }

        if (flag == 2) {
            if (!MatchUtil.IDCardValidate(editPassword.getText().toString())) {
                showMiddleToast("请输入正确的身份证号码");
                return;
            }
        }
        if (flag == 1) {
            xunXinCer();
            return;
        }
        String account = editName.getText().toString();
        String password = editPassword.getText().toString();
        APIManager.getInstance().Home_AccountCer(this, flag, account, password, "", "", "", "", new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                if (result == 1) {
                    showMiddleToast(message);
                    // MyToast.showToast(AccountActivity.this, message);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    if (flag == 2) {
                        showMiddleToast("身份证号和真实姓名不匹配，请重新输入");
                        // MyToast.showToast(AccountActivity.this, "身份证号和真实姓名不匹配，请重新输入");
                    } else {
                        showMiddleToast("请稍后重试");
                        //MyToast.showToast(AccountActivity.this, "请稍后重试");
                    }
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    @Click
    void text_agree() {
        switch (flag) {
            case 1:
                SetWebActivity_.intent(this).flag(5).start();
                break;
            case 2:
                SetWebActivity_.intent(this).flag(5).start();
                break;
            case 3:
                SetWebActivity_.intent(this).flag(9).start();
                break;
            case 4:
                SetWebActivity_.intent(this).flag(10).start();

                break;
            case 5:
                SetWebActivity_.intent(this).flag(8).start();

                break;
        }
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
