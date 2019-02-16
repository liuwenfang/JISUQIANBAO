package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.author.BaseHelper;
import com.ahxbapp.jsqb.author.Constants;
import com.ahxbapp.jsqb.author.MobileSecurePayer;
import com.ahxbapp.jsqb.dialog.AuthPromptDialog;
import com.ahxbapp.jsqb.model.BankModel;
import com.ahxbapp.jsqb.model.JobMedel;
import com.ahxbapp.jsqb.model.PersonDataModel;
import com.ahxbapp.jsqb.utils.DeviceUtil;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.wheel.OnWheelChangedListener;
import com.ahxbapp.jsqb.wheel.WheelView;
import com.ahxbapp.jsqb.wheel.adapters.ArrayWheelAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 借款
 * 基本认证
 */

@EActivity(R.layout.activity_first_cer)
public class FirstCerActivity extends BaseActivity implements OnWheelChangedListener {

    //    @Extra
//    Boolean isZMXY = false;
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, mJobTV, mIncomeTV, tv_cancel, tv_ok, tvFirstPrompt;

    @ViewById
    WheelView wheel_job;
    @ViewById
    LinearLayout lin_area;

    @ViewById
    Button mSubmitBN;

    @ViewById
    EditText editName, editsfz, editbank;

    @ViewById
    RelativeLayout mJobRL, mInComeRL;

    @ViewById
    View mView;

    @Extra
    int tag = 0;  //  1  芝麻信用   3  已认证  2 认证
    private List<BankModel> jobModels = new ArrayList<>();  //职位
    private List<BankModel> incomeModels = new ArrayList<>();  //月收入

    private List<BankModel> models = new ArrayList<>();

    private int flag = 0;


    TextView textView;
    private AuthPromptDialog promptDialog;
    private Context mContext;

    @AfterViews
    void init() {
        mContext = this;
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        if (tag == 1) {
            loadInfoData();
            mToolbarTitleTV.setText("芝麻信用认证");
        } else {
            mToolbarTitleTV.setText("实名认证");
            if (tag == 3) {//已实名
                loadInfoData();
            } else {
                loadPromptData();
            }
        }
    }

    void loadInfoData() {
        APIManager.getInstance().member_getPerData(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                PersonDataModel model1 = (PersonDataModel) model;
                if (model1.getTrueName() != null) {
                    editName.setText(model1.getTrueName());
                    editName.setEnabled(false);
                }
                if (model1.getIdentity() != null) {
                    editsfz.setText(model1.getIdentity());
                    editsfz.setEnabled(false);
                }
                if (model1.getCardNo() != null) {
                    editbank.setText(model1.getCardNo());
                    editbank.setEnabled(false);
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    void loadPromptData() {
        APIManager.getInstance().requestPrompt(this, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, final JSONObject response) {
                int result = 0;
//                String llpalt = "";
                String llpstr = "";
                try {
                    result = response.getInt("result");
                    llpstr = response.getString("llpstr");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 1) {
                    if (llpstr != null && !llpstr.equals("")) {//对话框
                        showPromptDialog(llpstr);
                    }
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
            }
        });
    }

    @Click
    void mJobRL() {
        DeviceUtil.hideSoftInput(mJobTV, this);
        DeviceUtil.hideSoftInput(mIncomeTV, this);
        flag = 0;
        wheel_job.setCurrentItem(0);
        mSubmitBN.setVisibility(View.GONE);
        mView.setOnClickListener(null);
        if (jobModels != null && jobModels.size() > 0) {
            String[] strs = new String[jobModels.size()];
            int inde = 0;
            for (BankModel m : jobModels) {
                strs[inde] = m.getName();
                inde++;
            }

            Log.e("Success", strs.length + "");
            textView = mJobTV;
            models = jobModels;
            wheel_job.setViewAdapter(new ArrayWheelAdapter<String>(FirstCerActivity.this, strs));
            lin_area.setVisibility(View.VISIBLE);
        }
    }

    @Click
    void mInComeRL() {
        DeviceUtil.hideSoftInput(mJobTV, this);
        DeviceUtil.hideSoftInput(mIncomeTV, this);
        flag = 1;
        wheel_job.setCurrentItem(0);
        mSubmitBN.setVisibility(View.GONE);
        mView.setOnClickListener(null);
        if (incomeModels != null && incomeModels.size() > 0) {
            String[] strs = new String[incomeModels.size()];
            int inde = 0;
            for (BankModel m : incomeModels) {
                strs[inde] = m.getName();
                inde++;
            }
            Log.e("Success", strs.length + "");
            textView = mIncomeTV;
            models = incomeModels;
            wheel_job.setViewAdapter(new ArrayWheelAdapter<String>(FirstCerActivity.this, strs));
            lin_area.setVisibility(View.VISIBLE);
        }
    }

    private void showPromptDialog(String normal) {
        if (promptDialog == null) {
            promptDialog = new AuthPromptDialog(this);
            promptDialog.tvContent.setText(normal);
        }
        promptDialog.show();
    }

    @Click
    void tv_cancel() {
        lin_area.setVisibility(View.GONE);
        mSubmitBN.setVisibility(View.VISIBLE);
    }

    @Click
    void tv_ok() {
        lin_area.setVisibility(View.GONE);
        mSubmitBN.setVisibility(View.VISIBLE);
        BankModel bankModel = models.get(wheel_job.getCurrentItem());
        String name = "";
        if (bankModel != null) {
            name += bankModel.getName() + " ";
            //textView.setText(name);
        }
        if (flag == 0) {
            mJobTV.setText(name);
        } else if (flag == 1) {
            mIncomeTV.setText(name);
        }

    }

    private int flag_zmxy;

    @Click
    void mSubmitBN() {
        mSubmitBN.setClickable(false);
        if (tag == 3) {
            mSubmitBN.setClickable(true);
            showMiddleToast("资料已提交成功");
            finish();
            return;
        }
        String name = editName.getText().toString();
        String sfzNo = editsfz.getText().toString();
        String cardNo = editbank.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mSubmitBN.setClickable(true);
            showButtomToast("请输入真实姓名！");
            return;
        }
        if (TextUtils.isEmpty(sfzNo)) {
            mSubmitBN.setClickable(true);
            showButtomToast("请输入身份证号码！");
            return;
        } else {
            if (!MatchUtil.IDCardValidate(sfzNo)) {
                mSubmitBN.setClickable(true);
                showMiddleToast("请输入正确的身份证号码");
                return;
            }
        }

        if (TextUtils.isEmpty(cardNo)) {
            mSubmitBN.setClickable(true);
            showButtomToast("请输入银行卡号！");
            return;
        } else {
            if (!MatchUtil.isBankCardChecked(cardNo)) {
                mSubmitBN.setClickable(true);
                showButtomToast("请输入正确的银行卡号");
                return;
            }
        }
        if (tag == 1) {
            flag_zmxy = 4;
            //芝麻信用认证
            requestAuthen();
        } else {
            flag_zmxy = 2;
            requestStatus();
        }
    }

    /**
     * 获取实名认证的方式
     */
    private void requestStatus() {
        showDialogLoading();
        APIManager.getInstance().requestIdStatus(mContext, new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String url) {
                hideProgressDialog();
                if (result == 1) {
                    WebActivity_.intent(mContext).url(url).title("实名认证").startForResult(100);
                } else if (result == 0) {
                    requestAuthenFrist();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                mSubmitBN.setClickable(true);
                hideProgressDialog();
            }
        });
    }

    /**
     * 实名认证
     */
    private void requestAuthenFrist() {
        showDialogLoading();
        APIManager.getInstance().Home_AuthenFrist(this, editName.getText().toString(), editsfz.getText().toString(),
                editbank.getText().toString(), new APIManager.APIManagerInterface.baseBlock() {
                    @Override
                    public void Success(Context context, final JSONObject response) {
                        mSubmitBN.setClickable(true);
                        hideProgressDialog();
                        int result = 0;
                        String msg = "";
                        try {
                            result = response.getInt("result");
                            msg = response.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result == 1) {
                            showButtomToast("认证成功!");
                            setResult(RESULT_OK);
                            finish();
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    MobileSecurePayer payer = new MobileSecurePayer();
//                                    payer.setCallbackHandler(mHandler, Constants.RQF_SIGN);
//                                    payer.setTestMode(false);
//                                    boolean bRet = payer.doTokenSign(response, FirstCerActivity.this);
//                                }
//                            }).start();
                        } else {
                            showMiddleToast(msg);
                        }
                    }

                    @Override
                    public void Failure(Context context, JSONObject response) {
                        mSubmitBN.setClickable(true);
                        hideProgressDialog();
                    }
                });
    }

    private Handler mHandler = createHandler();

    private Handler createHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {
                String strRet = (String) msg.obj;
                switch (msg.what) {
                    case Constants.RQF_SIGN: {
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");
                        // 成功
                        if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                            requestSign(objContent.toString());
                        } else {
                            // TODO 失败
                            showButtomToast(retMsg);
                        }
                    }
                    break;
                }
                super.handleMessage(msg);
            }
        };
    }

    /**
     * 银行卡认证
     */
    private void requestSign(String json) {
        showDialogLoading();
        APIManager.getInstance().requestBank(this, json, new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                mSubmitBN.setClickable(true);
                if (result == 1) {
                    countDown();
                } else {
                    hideProgressDialog();
                    showButtomToast(message);
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                mSubmitBN.setClickable(true);
            }
        });
    }

    private CountDownTimer countDownTimer;

    private void countDown() {
        countDownTimer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                requestCheckSign();
            }
        };
        // 调用start方法开始倒计时
        countDownTimer.start();
    }

    /**
     * 检查认证状态
     */
    private void requestCheckSign() {
        APIManager.getInstance().requestCheckBank(this, new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                hideProgressDialog();
                if (result == 1) {
                    showButtomToast("认证成功!");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showButtomToast(message);
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                mSubmitBN.setClickable(true);
            }
        });
    }


    private void requestAuthen() {
        showDialogLoading();
        APIManager.getInstance().Home_AccountCer(this, flag_zmxy, editName.getText().toString(), editsfz.getText().toString(),
                editbank.getText().toString(), "", String.valueOf(0), String.valueOf(0), new APIManager.APIManagerInterface.common_wordBack() {
                    @Override
                    public void Success(Context context, int result, String message) {
                        mSubmitBN.setClickable(true);
                        hideProgressDialog();
                        if (result == 1) {
                            showMiddleToast("资料提交成功");
                            if (tag == 1) {
                                //芝麻信用
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                //必备认证
//                        NecessaryCerActivity_.intent(FirstCerActivity.this).start();
//                        finish();
                                setResult(RESULT_OK);
                                finish();
                            }
                        } else {
                            showMiddleToast(message);
                        }
                    }

                    @Override
                    public void Failure(Context context, JSONObject response) {
                        mSubmitBN.setClickable(true);
                        hideProgressDialog();
                    }
                });
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {

    }
}
