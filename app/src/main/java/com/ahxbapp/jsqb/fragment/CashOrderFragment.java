package com.ahxbapp.jsqb.fragment;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.ahxbapp.common.BlankViewDisplay;
import com.ahxbapp.common.ui.BaseFragment;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.activity.CashOrdeActivity_;
import com.ahxbapp.jsqb.activity.CashOrderDetailActivity_;
import com.ahxbapp.jsqb.adapter.MyCashOrderAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.CashModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_cash_order)
public class CashOrderFragment extends BaseFragment implements PullToRefreshListView.OnRefreshListener2 {
    @ViewById
    PullToRefreshListView mPullListView;
    @ViewById
    View blankLayout;

    private int orderStatus;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    private List<CashModel> cashModels = new ArrayList<>();

    MyCashOrderAdapter myadapter;

    @AfterViews
    void init() {
        EventBus.getDefault().register(this);
        loadData();
        myadapter = new MyCashOrderAdapter(getActivity(), cashModels, R.layout.adapter_mycashorder, new ViewHolder.ViewHolderInterface.common_click() {
            @Override
            public void click(Context context, int position, View vv) {
                //TODO 查看借款详情
                CashOrdeActivity_.intent(getActivity()).cashModel(cashModels.get(position)).start();
//                CashDetailsActivity_.intent(getActivity()).extra("loanID", cashModels.get(position).getID()).start();
            }
        });
        mPullListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullListView.setOnRefreshListener(this);
        mPullListView.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();
    }

    //取消借款
    void cancelOrder(int id) {
        APIManager.getInstance().cancelLoan(getActivity(), id, new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                if (result == 1) {
                    loadData();
                    myadapter.notifyDataSetChanged();
                }
                showButtomToast(message);
                EventBus.getDefault().post(new UserEvent.CashOrderEvent());
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }
    //查看合同


    public void onEvent(UserEvent.CashOrderEvent event) {
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //加载订单
    public void loadData() {
        showDialogLoading();
        Log.e("loadData", orderStatus + "");
        APIManager.getInstance().loan(getActivity(), orderStatus, pageIndex, pageSize, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseDataListModel<CashModel> cashModel = (BaseDataListModel<CashModel>) model;
                if (cashModel.getResult() == 1) {
                    if (pageIndex == 1)
                        cashModels.clear();
                    cashModels.addAll(cashModel.getDatalist());
                    myadapter.notifyDataSetChanged();
                    BlankViewDisplay.setBlank(cashModels.size(), getActivity(), true, blankLayout, onClickRetry);
                }
                mPullListView.onRefreshComplete();
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                mPullListView.onRefreshComplete();
                BlankViewDisplay.setBlank(cashModels.size(), getActivity(), false, blankLayout, onClickRetry);
            }
        });
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        loadData();
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
            loadData();
        }
    };

}
