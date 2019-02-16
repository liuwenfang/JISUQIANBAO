package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
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
 * 我的借款
 */
@EActivity(R.layout.cash_order_item)
public class CashOrdeActivity extends BaseActivity {

    @Extra
    CashModel cashModel;
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    RelativeLayout lookRL;
    @ViewById
    TextView type, time, num, money, deadline, old_day, new_day, statusTV;
    @ViewById
    Button cancelBN;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("详情");
        type.setText(cashModel.getType());
        time.setText(cashModel.getAddTime());
        num.setText(cashModel.getLoanNO());
        money.setText(cashModel.getLoanId() + "");
        deadline.setText(cashModel.getTermId() + "");
        old_day.setText(cashModel.getPalyTime());
        new_day.setText(cashModel.getRepayTime());
        type.setText(cashModel.getType());
        String status = null;
        if (cashModel.getStatus() == 0) {
            status = "审核";
        } else if (cashModel.getStatus() == 1) {
            status = "正在确认银行卡";
            cancelBN.setVisibility(View.GONE);
        } else if (cashModel.getStatus() == 2) {
            status = "身份证确认中";
            cancelBN.setVisibility(View.GONE);
        } else if (cashModel.getStatus() == 3) {
            status = "待签约";
            cancelBN.setVisibility(View.GONE);
        } else if (cashModel.getStatus() == 4) {
            status = "正在放款中";
            cancelBN.setText("查看合同");
        } else if (cashModel.getStatus() == 5) {
            status = "待还款";
            cancelBN.setText("查看合同");
        } else if (cashModel.getStatus() == 6) {
            status = "已完成";
            cancelBN.setText("查看合同");
        } else if (cashModel.getStatus() == 7) {
            status = "已取消";
            lookRL.setVisibility(View.GONE);
        }
        statusTV.setText(status);
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    @Click
    void cancelBN() {
        if (cashModel != null) {
            if (cashModel.getStatus() == 0) {
                cancelOrder(cashModel.getID());
            } else if (cashModel.getStatus() >= 4) {
                SetWebActivity_.intent(this).flag(11).loanLogID(cashModel.getID()).start();
            }
        }
    }


    @Click
    void lookBN() {
        CashOrderDetailActivity_.intent(this).loanLog(cashModel.getID()).start();
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
