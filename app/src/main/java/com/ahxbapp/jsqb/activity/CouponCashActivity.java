package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.BlankViewDisplay;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.CashCouponAdapter;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.CouponCashModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * 优惠券
 */
@EActivity(R.layout.activity_coupon_cash)
public class CouponCashActivity extends BaseActivity implements PullToRefreshListView.OnRefreshListener2 {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    PullToRefreshListView mPullListView;
    @ViewById
    View blankLayout;

    @Extra
    float price;

    private List<CouponCashModel> cashModels = new ArrayList<>();
    private CashCouponAdapter couponAdapter;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("优惠券");

        loadCoupon();

        couponAdapter = new CashCouponAdapter(this, cashModels, R.layout.coupon_item);
        mPullListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullListView.setOnRefreshListener(this);
        mPullListView.setAdapter(couponAdapter);
        couponAdapter.notifyDataSetChanged();

    }

    @ItemClick
    void mPullListView(int position) {
        if (price == -1) {
            return;
        }
        CouponCashModel couponCashModel = cashModels.get(position - 1);
        if (couponCashModel.getIsOver() != 1) {
            if (price >= couponCashModel.getFullmoney()) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("coupon", couponCashModel);
                Intent intent = new Intent();
                intent.putExtra("couponModel", bundle);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showButtomToast("此优惠券需满" + couponCashModel.getFullmoney() + "元才可以使用哦！");
            }
        } else {
            showButtomToast("此优惠券已过期了哦！");
        }
    }

    //加载优惠券
    void loadCoupon() {
        showDialogLoading();
        APIManager.getInstance().coupon(this, pageIndex, pageSize, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseDataListModel<CouponCashModel> couponModel = (BaseDataListModel<CouponCashModel>) model;
                if (couponModel.getResult() == 1) {
                    if (pageIndex == 1)
                        cashModels.clear();

//                    cashModels.addAll(couponModel.getDatalist());

//                    CouponCashModel cash = new CouponCashModel();
//                    cash.setIsOver(1);
//                    cash.setEndTime("2016年按时打算");
//                    cashModels.add(cash);

                    couponAdapter.notifyDataSetChanged();
                    mPullListView.onRefreshComplete();
                }

                BlankViewDisplay.setBlank(cashModels.size(), CouponCashActivity.this, true, blankLayout, onClickRetry);
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                mPullListView.onRefreshComplete();
                BlankViewDisplay.setBlank(cashModels.size(), CouponCashActivity.this, false, blankLayout, onClickRetry);
            }
        });
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        loadCoupon();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        loadCoupon();
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullListView.onRefreshComplete();
            }
        }, 1000);
    }

    //点击重新加载
    View.OnClickListener onClickRetry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadCoupon();
        }
    };
}
