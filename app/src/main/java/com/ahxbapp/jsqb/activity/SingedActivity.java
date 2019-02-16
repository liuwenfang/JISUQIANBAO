package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.CommonEnity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 签约
 */
@EActivity(R.layout.activity_singed)
public class SingedActivity extends BaseActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    TextView jiekuanren, jine, shichang, zongheFee, fangshi, riqi, zhengce, tvOther;
    @ViewById
    CheckBox HT_agree, cbOther;
    @ViewById
    Button singed_btn;
    @ViewById
    LinearLayout bg_black, xieyi_line, llOther;
    @ViewById
    TextView xinshenFee, guanliFee, lixiFee, youhuiquan, zongji, zhidaole;
    @ViewById
    ImageView fee_image;

    @Extra
    int LoanID;

    @AfterViews
    void init() {
        loadSingedData();
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("签约");
    }

    private boolean isShow;
    private String msg;
    private String agreementUrl = "";
    private String payUrl = "";


    @Click
    void mToolbarLeftIB() {
        finish();
    }

    //加载签约信息
    void loadSingedData() {
        showDialogLoading();
        APIManager.getInstance().singedShow1(this, LoanID, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    jiekuanren.setText(response.getJSONObject("data").getString("UserId"));
                    jine.setText(response.getJSONObject("data").getInt("LoanId") + "元");
                    shichang.setText(response.getJSONObject("data").getInt("TermId") + "天");
                    zongheFee.setText(response.getJSONObject("data").getString("ZCost") + "元");
                    int zongfee = response.getJSONObject("data").getInt("ZCost") + response.getJSONObject("data").getInt("LoanId") - response.getJSONObject("data").getInt("CoupID");
                    fangshi.setText("一次性还款" + response.getJSONObject("data").getInt("LoanId") + "元");
//                    fangshi.setText("一次性还款"+zongfee+"元");
                    riqi.setText(response.getJSONObject("data").getString("RepayTime"));
                    zhengce.setText("7天容时期," + response.getJSONObject("data").getString("RongP") + "元每天" + "\n" + "7天后逾期," + response.getJSONObject("data").getString("OverdueP") + "元每天");
                    xinshenFee.setText(response.getJSONObject("data").getInt("Applyfee") + "元");
                    guanliFee.setText(response.getJSONObject("data").getInt("Userfee") + "元");
                    lixiFee.setText(response.getJSONObject("data").getInt("Interest") + "元");
                    youhuiquan.setText("-" + response.getJSONObject("data").getInt("CoupID") + "元");
                    int zong = response.getJSONObject("data").getInt("ZCost") - response.getJSONObject("data").getInt("CoupID");
                    zongji.setText(zong + "元");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
        requestLoanlogKSQY();
    }

    /**
     * 快速签约费收取功能
     */
    private void requestLoanlogKSQY() {
        APIManager.getInstance().requestLoanlogKSQY(this, LoanID, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                try {
                    payUrl = response.getString("url");
                    agreementUrl = response.getString("agreementUrl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isShow = true;
                llOther.setVisibility(View.VISIBLE);
                requestParamValue();
            }

            @Override
            public void Failure(Context context, JSONObject response) {
//                try {
//                    payUrl = response.getString("url");
//                    agreementUrl = response.getString("agreementUrl");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                isShow = true;
//                llOther.setVisibility(View.VISIBLE);
//                requestParamValue();
            }
        });
    }

    /**
     * 签约信息展示
     */
    private void requestParamValue() {
        APIManager.getInstance().requestParamValue(this, new APIManager.APIManagerInterface.requestMobile() {
            @Override
            public void Success(Context context, String url) {//
                tvOther.setText(url + "");
                msg = url;
            }

            @Override
            public void Failure(Context context, String msg) {

            }
        });
    }

    //查看合同
    @Click
    void HT() {
        SetWebActivity_.intent(this).flag(11).loanLogID(LoanID).start();
    }

    //查看综合费用
    @Click
    void fee_image() {
        bg_black.setVisibility(View.VISIBLE);
        xieyi_line.setClickable(false);
    }

    //消失综合费用
    @Click
    void zhidaole() {
        bg_black.setVisibility(View.GONE);
        xieyi_line.setClickable(true);
    }

    @Click
    void tvOther() {
        WebActivity_.intent(this).url(agreementUrl).title("签约条款").start();
    }

    @Click
    void singed_btn() {
        if (!HT_agree.isChecked()) {
            showMiddleToast("请先同意《极速钱包借款合同》");
            return;
        }
        if (isShow && !cbOther.isChecked()) {
            showMiddleToast(msg);
            return;
        }
        if (isShow) {
            WebActivity_.intent(this).url(payUrl).title("支付").start();
            EventBus.getDefault().post(new CommonEnity<>("sign"));
            finish();
            return;
        }
        showDialogLoading();
        APIManager.getInstance().payment(this, LoanID, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    int result = response.getInt("result");
                    if (result == 1) {
                        setResult(RESULT_OK);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }
}
