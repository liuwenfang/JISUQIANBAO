package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.CashModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
/**
 * 借款订单详情
 */
@EActivity(R.layout.activity_detail)
public class CashOrderDetailActivity extends AppCompatActivity {

    @Extra
    int loanLog;
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    TextView type_tv, time_tv, num_tv, money_tv, deadline_tv, old_day_tv, new_day_tv, shenqingFee_tv, guanliFee_tv, lixiFee_tv, youhuiquan_tv, huankuan_tv, status_tv;
    private CashModel cashModel;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("借款详情");
        APIManager.getInstance().Member_LoanLog(this, String.valueOf(loanLog), new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                cashModel = (CashModel) model;
                type_tv.setText(cashModel.getType());
                time_tv.setText(cashModel.getAddTime());
                num_tv.setText(cashModel.getLoanNO());
                money_tv.setText(cashModel.getLoanId() + "元");
                deadline_tv.setText(cashModel.getTermId() + "天");
                old_day_tv.setText(cashModel.getPalyTime());
                new_day_tv.setText(cashModel.getRepayTime());
                shenqingFee_tv.setText(cashModel.getApplyfee() + "元");
                guanliFee_tv.setText(cashModel.getUserfee() + "元");
                lixiFee_tv.setText(cashModel.getInterest() + "元");
                youhuiquan_tv.setText(cashModel.getCoupID() + "元");
                int total = cashModel.getLoanId() + Integer.valueOf(cashModel.getApplyfee()).intValue() + Integer.valueOf(cashModel.getUserfee()).intValue() + Integer.valueOf(cashModel.getInterest()).intValue() - Integer.valueOf(cashModel.getCoupID()).intValue();
                huankuan_tv.setText(cashModel.getLoanId() + "元");
                switch (cashModel.getStatus()) {
                    case 0:
                        status_tv.setText("待审核");
                        break;
                    case 1:
                        status_tv.setText("正在确认银行卡");
                        break;
                    case 2:
                        status_tv.setText("身份认证中");
                        break;
                    case 3:
                        status_tv.setText("待签约");
                        break;
                    case 4:
                        status_tv.setText("正在放款中");
                        break;
                    case 5:
                        status_tv.setText("待还款");
                        break;
                    case 6:
                        status_tv.setText("已完成");
                        break;
                    case 7:
                        status_tv.setText("已取消");
                        break;
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    //取消借款
    void cancelOrder(int id) {
        APIManager.getInstance().cancelLoan(this, id, new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                EventBus.getDefault().post(new UserEvent.CashOrderEvent());
                if (result == 1) {
                    finish();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }
}
