package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.author.BaseHelper;
import com.ahxbapp.jsqb.author.Constants;
import com.ahxbapp.jsqb.author.MobileSecurePayer;
import com.ahxbapp.jsqb.model.PersonDataModel;
import com.ahxbapp.jsqb.utils.MatchUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * 银行卡认证  暂未用到
 */
@EActivity(R.layout.activity_bank)
public class BankActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;

    @ViewById
    Button mSubmitBN;

    @ViewById
    EditText editbank, editname;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("银行卡认证");
        loadInfoData();
    }

    @Click
    void mToolbarLeftIB() {
        setResult(RESULT_OK);
        finish();
    }

    void loadInfoData() {
        showBlackLoading();
        APIManager.getInstance().member_getPerData(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                PersonDataModel model1 = (PersonDataModel) model;
                if (model1.getRelaName() != null) {
                    editname.setText(model1.getTrueName());
                    editname.setFocusable(false);
                }
                if (model1.getCardNo() != null) {
                    editbank.setText(model1.getCardNo());
                    editbank.setFocusable(false);
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    @Click
    void mSubmitBN() {
        if (TextUtils.isEmpty(editbank.getText().toString())) {
            showButtomToast("请输入银行卡号！");
            return;
        } else {
            if (!MatchUtil.isBankCardChecked(editbank.getText().toString())) {
                showButtomToast("请输入正确的银行卡号");
                return;
            }
        }
//        BankCodeActivity_.intent(this).start();
        showBlackLoading();
        mSubmitBN.setClickable(false);
        APIManager.getInstance().requestBankAuthor(this, new APIManager.APIManagerInterface.aliPay() {
            @Override
            public void Success(Context context, String content4Pay, String ordernumber) {
                mSubmitBN.setClickable(true);
                hideProgressDialog();
                MobileSecurePayer msp = new MobileSecurePayer();
                msp.payRepaySign(content4Pay, mHandler, payFlag, BankActivity.this, false);
            }

            @Override
            public void Failure(Context context, String msg) {
                mSubmitBN.setClickable(true);
                hideProgressDialog();
            }
        });
    }

    private final int payFlag = 310;
    private Handler mHandler = createHandler();

    private Handler createHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {
                String strRet = (String) msg.obj;
                switch (msg.what) {
                    case payFlag: {
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");

                        // 成功
                        if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
//                            LLSignModel model = com.alibaba.fastjson.JSONObject.parseObject(strRet, LLSignModel.class);
//                            if (model != null) {
//                            }
                            requestSign(strRet);
//                            // TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
//                            BaseHelper.showDialog(BankActivity.this, "提示",
//                                    "支付成功，交易状态码：" + retCode + " 返回报文:" + strRet,
//                                    android.R.drawable.ic_dialog_alert);
                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                            // TODO 处理中，掉单的情形
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING
                                    .equalsIgnoreCase(resulPay)) {
                                showButtomToast(objContent.optString("ret_msg"));
//                                BaseHelper.showDialog(BankActivity.this, "提示",
//                                        objContent.optString("ret_msg") + "交易状态码："
//                                                + retCode + " 返回报文:" + strRet,
//                                        android.R.drawable.ic_dialog_alert);
                            }
                        } else {
                            // TODO 失败
                            showButtomToast(retMsg);
//                            BaseHelper.showDialog(BankActivity.this, "提示", retMsg
//                                            + "，交易状态码:" + retCode + " 返回报文:" + strRet,
//                                    android.R.drawable.ic_dialog_alert);
                        }
                    }
                    break;
                }
                super.handleMessage(msg);
            }
        };

    }

    private void requestSign(String json) {
        showBlackLoading();
        APIManager.getInstance().requestBankSign(json, this, new APIManager.APIManagerInterface.aliPay() {
            @Override
            public void Success(Context context, String content4Pay, String ordernumber) {
                hideProgressDialog();
                showButtomToast("认证成功!");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void Failure(Context context, String msg) {
                hideProgressDialog();
            }
        });
    }

}
