package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.dialog.ConsumeTypeDialog;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.model.BankModel;
import com.ahxbapp.jsqb.model.BaseFeeModel;
import com.ahxbapp.jsqb.model.CouponCashModel;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.ahxbapp.jsqb.utils.StringUtils;
import com.ahxbapp.jsqb.wheel.WheelView;
import com.ahxbapp.jsqb.wheel.adapters.ArrayWheelAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 我要借款
 */
@EActivity(R.layout.activity_start_borrowings)
public class StartBorrowingsActivity extends BaseActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, filingFee_tv, interestFee_tv, managementFee_tv, couponFee_tv, total_tv, tvConsumeType;
    @ViewById
    RelativeLayout bg_Linear;
    //    @ViewById
//    GridView loan_gridView;
    @ViewById
    Button next_btn, coup_btn, mDayBn, mMoneyBn;

    @ViewById
    LinearLayout lin_area1;
    @ViewById
    View mView1;
    @ViewById
    TextView tv_cancel1, tv_ok1;
    @ViewById
    WheelView wheel_bank;

    List<BankModel> modelList = new ArrayList<>();

    List<BankModel> termList = new ArrayList<>();
    BaseFeeModel feeModel;
    int loanId, termId, index, coupId, termIndex;

    float lixiFee, coupFee, totalFee;

    private boolean select;

    LoanAdaper loanAdaper;

    CouponCashModel couponCashModel;

    @AfterViews
    void init() {

        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("我要借款");
        termIndex = 0;

        loadBaseFeeData();

        loadLoanData();
//        loanAdaper = new LoanAdaper(this, modelList, R.layout.loan_button);
//        loan_gridView.setAdapter(loanAdaper);

//        loan_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("index", index + "" + position);
//
//                if (index == position) {
//                    return;
//                }
//                index = position;
//
//                loanAdaper.notifyDataSetChanged();
//
//                if (couponCashModel!=null){
//                    if (totalFee+coupFee<couponCashModel.getFullmoney()){
//                        coupFee = 0;
//                        coupId = -1;
//                        coup_btn.setText("请选择优惠券 > ");
//                        couponFee_tv.setText("优惠券:0元");
//                    }
//                }
//
//                BankModel model = modelList.get(position);
//                loanId = model.getID();
//                countInterest();
//            }
//        });
    }


    //获取借款金额及天数
    void loadLoanData() {
        showDialogLoading();
        APIManager.getInstance().home_getLoanMoney(this, new APIManager.APIManagerInterface.common_FAQlist() {
            @Override
            public void Success(Context context, List list) {

                modelList.clear();

                modelList.addAll(list);
                BankModel model = modelList.get(0);

                loanId = model.getID();
                index = 0;
//                loanAdaper.notifyDataSetChanged();
                mMoneyBn.setText(model.getNum() + "");
                APIManager.getInstance().home_getLoanDays(StartBorrowingsActivity.this, new APIManager.APIManagerInterface.common_FAQlist() {
                    @Override
                    public void Success(Context context, List list) {
                        termList.clear();
                        termList.addAll(list);
                        BankModel model = termList.get(0);
                        termId = model.getID();
//                        days_textView.setText("请选择借款期限（天）> 已选择" + model.getNum() + "天");
                        mDayBn.setText(model.getNum() + "");
                        countInterest();
                    }

                    @Override
                    public void Failure(Context context, JSONObject response) {
                        hideProgressDialog();
                    }
                });

            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    //获取基本费用
    void loadBaseFeeData() {
        APIManager.getInstance().home_getBaseFee(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                feeModel = (BaseFeeModel) model;
                filingFee_tv.setText("快速申请费:" + feeModel.getApplyfee() + "元");
                managementFee_tv.setText("用户管理费:" + feeModel.getUserfee() + "元");
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //计算利息
    void countInterest() {
        showDialogLoading();
        APIManager.getInstance().home_getInterest(this, loanId, termId, new APIManager.APIManagerInterface.loanFeeBack() {
            @Override
            public void Success(Context context, float Interest, float Applyfee, float Userfee) {
                hideProgressDialog();
                DecimalFormat df = new DecimalFormat("0");
//                df.format(total);
                feeModel.setApplyfee(df.format(Applyfee) + "");
                feeModel.setUserfee(df.format(Userfee) + "");
                BankModel model = modelList.get(index == -1 ? 0 : index);
                BankModel termModel = termList.get(termIndex);
//                float inter = Interest * model.getNum() * termModel.getNum() / 100;
//                BigDecimal bigDec = new java.math.BigDecimal(inter);
//                double total = bigDec.setScale(0, java.math.BigDecimal.ROUND_HALF_UP)
//                        .doubleValue();
                lixiFee = Interest;

                countTotalPrice();
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    //计算总价
    void countTotalPrice() {
        BankModel model = modelList.get(index == -1 ? 0 : index);
        filingFee_tv.setText("快速申请费:" + feeModel.getApplyfee() + "元");
        managementFee_tv.setText("用户管理费:" + feeModel.getUserfee() + "元");
        DecimalFormat df = new DecimalFormat("0");
        interestFee_tv.setText("息费:" + df.format(lixiFee) + "元");

        totalFee = (float) model.getNum() + Float.parseFloat(feeModel.getApplyfee()) +
                Float.parseFloat(feeModel.getUserfee()) + lixiFee - coupFee;
        total_tv.setText("到期应还:" + df.format(model.getNum()) + "元");
    }

    private void createView(int num) {

    }

    //选择借款天数
    @Click
    void mDayBn() {
        select = false;
        next_btn.setVisibility(View.GONE);
        mView1.setOnClickListener(null);
        if (termList != null && termList.size() > 0) {
            String[] strs = new String[termList.size()];
            int inde = 0;
            for (BankModel m : termList) {
                strs[inde] = m.getNum() + "";
                inde++;
            }
            Log.e("Success", strs.length + "");
            wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(StartBorrowingsActivity.this, strs));
            lin_area1.setVisibility(View.VISIBLE);
        }
    }

    @Click
    void mMoneyBn() {
        select = true;
        next_btn.setVisibility(View.GONE);
        mView1.setOnClickListener(null);
        if (modelList != null && modelList.size() > 0) {
            String[] strs = new String[modelList.size()];
            int inde = 0;
            for (BankModel m : modelList) {
                strs[inde] = m.getNum() + "";
                inde++;
            }
            Log.e("Success", strs.length + "");
            wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(StartBorrowingsActivity.this, strs));
            lin_area1.setVisibility(View.VISIBLE);
        }
    }

    @Click
    void coup_btn() {
        CouponCashActivity_.intent(this).price(totalFee + coupFee).startForResult(1000);
    }

    @OnActivityResult(1000)
    void CoupOnResult(Intent data) {
        if (data != null) {
            Bundle bundle = data.getBundleExtra("couponModel");
            couponCashModel = (CouponCashModel) bundle.get("coupon");
            coupFee = couponCashModel.getMoney();
            coupId = couponCashModel.getID();
            coup_btn.setText("请选择优惠券 > 已选择减" + coupFee + "券");
            couponFee_tv.setText("优惠券:-" + coupFee + "元");
            countTotalPrice();
        }
    }

    private ConsumeTypeDialog consumeTypeDialog;

    private void showConsumeDialog() {
        if (consumeTypeDialog == null) {
            consumeTypeDialog = new ConsumeTypeDialog(this);
            consumeTypeDialog.setSelType(new ConsumeTypeDialog.selType() {
                @Override
                public void getSelType(String selType) {
                    consumeType = selType;
                    tvConsumeType.setText(consumeType);
                }
            });
        }
        consumeTypeDialog.show();
    }

    private String consumeType = "";

    //借款用途
    @Click
    void rlConsumeType() {
        showConsumeDialog();
    }

    // 下一步
    @Click
    void next_btn() {
        if (StringUtils.isEmpty(consumeType)) {
            showMiddleToast("请选择借款用途!");
            showConsumeDialog();
            return;
        }
        String token = PrefsUtil.getString(this, Global.TOKEN);
        if (token == null) {
            LoginActivity_.intent(this).start();
            return;
        }
        showBlackLoading();
        APIManager.getInstance().home_getAuthenStatus(this, 0, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                AuthenModel authenModel = (AuthenModel) model;
                if (authenModel.getIsID() == 1 && authenModel.getIsContact() == 1 && authenModel.getIsMobile() == 1
                        && authenModel.getIsId_dbr() == 1 && authenModel.getIsMobile_dbr() == 1 && authenModel.getIsTabao() == 1) {
                    requestJieKuan();
                } else {
                    showMiddleToast("请您完成认证!");
                    ConsummateInfoActivity_.intent(StartBorrowingsActivity.this).start();
                    finish();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });

    }

    private void requestJieKuan() {
        APIManager.getInstance().home_submitLoan(this, loanId, termId, coupId, feeModel, lixiFee, totalFee, new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                hideProgressDialog();
                if (result == 1) {//判断手机通话记录是否通过
                    //发送通知
                    showMiddleToast(message);
                    EventBus.getDefault().post(new UserEvent.changeStatus());
                    finish();
                } else {
                    showMiddleToast(message);
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    @Click
    void tv_cancel1() {
        lin_area1.setVisibility(View.GONE);
        next_btn.setVisibility(View.VISIBLE);
    }

    @Click
    void tv_ok1() {
        lin_area1.setVisibility(View.GONE);
        next_btn.setVisibility(View.VISIBLE);
        if (select == false) {
            BankModel bankModel = termList.get(wheel_bank.getCurrentItem());

            termId = bankModel.getID();
//        mDayBn.setText("请选择借款期限（天）> 已选择" + bankModel.getNum() + "天");
            mDayBn.setText(bankModel.getNum() + "");
            termIndex = wheel_bank.getCurrentItem();
            countInterest();
        } else {
            BankModel bankModel = modelList.get(wheel_bank.getCurrentItem());
            loanId = bankModel.getID();
            index = wheel_bank.getCurrentItem();
            mMoneyBn.setText(bankModel.getNum() + "");
            countInterest();
        }

    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    class LoanAdaper extends CommonAdapter<BankModel> {

        public LoanAdaper(Context context, List<BankModel> datas, int layoutId) {
            super(context, datas, layoutId);
        }

        @Override
        public void convert(ViewHolder holder, BankModel bankModel) {

            TextView view = (TextView) holder.getView(R.id.loan_btn);

            view.setBackground(getResources().getDrawable(R.drawable.loanbtn_cutgray_20dp));

            if (holder.getmPosition() == index) {
                view.setBackground(getResources().getDrawable(R.drawable.loanbtn_cut_20dp));
            }
            holder.setText(R.id.loan_btn, bankModel.getNum() + "");
        }
    }
}
