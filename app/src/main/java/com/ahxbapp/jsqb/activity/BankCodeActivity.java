package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.common.widget.ValidePhoneView;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.author.BaseHelper;
import com.ahxbapp.jsqb.author.Constants;
import com.ahxbapp.jsqb.author.MobileSecurePayer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;
/**
 * 银行卡  (暂未用到)
 */
@EActivity(R.layout.activity_bank_code)
public class BankCodeActivity extends BaseActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;

    @ViewById
    ValidePhoneView sendCode;

    @ViewById
    EditText editPhone, editCode;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("银行卡认证");
        loadInfoData();
    }

    void loadInfoData() {

    }

    @Click
    void sendCode() {
        APIManager.getInstance().requestBankAuthor(this, new APIManager.APIManagerInterface.aliPay() {
            @Override
            public void Success(Context context, String content4Pay, String ordernumber) {
                MobileSecurePayer msp = new MobileSecurePayer();
                msp.payRepaySign(content4Pay,mHandler,payFlag, BankCodeActivity.this, false);
            }

            @Override
            public void Failure(Context context, String msg) {

            }
        });
    }
    private final int payFlag=310;
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

                            // TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
                            BaseHelper.showDialog(BankCodeActivity.this, "提示",
                                    "支付成功，交易状态码：" + retCode+" 返回报文:"+strRet,
                                    android.R.drawable.ic_dialog_alert);
                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                            // TODO 处理中，掉单的情形
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING
                                    .equalsIgnoreCase(resulPay)) {
                                BaseHelper.showDialog(BankCodeActivity.this, "提示",
                                        objContent.optString("ret_msg") + "交易状态码："
                                                + retCode+" 返回报文:"+strRet,
                                        android.R.drawable.ic_dialog_alert);
                            }

                        } else {
                            // TODO 失败
                            BaseHelper.showDialog(BankCodeActivity.this, "提示", retMsg
                                            + "，交易状态码:" + retCode+" 返回报文:"+strRet,
                                    android.R.drawable.ic_dialog_alert);
                        }
                    }
                    break;
                }
                super.handleMessage(msg);
            }
        };

    }
}
