package com.ahxbapp.jsqb.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ahxbapp.common.BlankViewDisplay;
import com.ahxbapp.common.network.RefreshBaseFragment;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.OrderAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.OrderModel;
import com.ahxbapp.jsqb.utils.MyToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_order)
public class OrderFragment extends RefreshBaseFragment {

    @ViewById
    ListView mylist;
    @ViewById
    View blankLayout;

    private int orderStatus;
    List<OrderModel> mData = new ArrayList<>();
    OrderAdapter myadapter;

    @AfterViews
    void initView(){
        //监听点击
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                showMiddleToast("点击了第"+position+"个cell");
                OrderModel model = mData.get(position);
//                OrderDetailActivity_.intent(getActivity()).orderNo(model.getOrderNO()).orderStatus(model.getOrderState()).start();
            }
        });
        myadapter = new OrderAdapter(getActivity().getApplicationContext(), mData, R.layout.item_order_list,new ViewHolder.ViewHolderInterface.common_click() {
            @Override
            public void click(Context context, int position, View vv) {
                //确认支付
                showMiddleToast("确认支付");
            }
        },new ViewHolder.ViewHolderInterface.common_click() {
            @Override
            public void click(final Context context, final int position, View vv) {
                //取消订单
//                showMiddleToast("取消订单");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.warning).setMessage(R.string.confirm_cancel_order).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDialogLoading();
                        OrderModel model = mData.get(position);
                        APIManager.getInstance().cancelOrder(getActivity(), model.getOrderNO(), new APIManager.APIManagerInterface.no_object() {
                            @Override
                            public void Success(Context context, int result, String message, String enmessage) {
                                hideProgressDialog();
                                if (result == 1){
                                    MyToast.showToast(context,message);
                                    //重新下载数据
                                    loadData();
                                }else {
                                    MyToast.showToast(context,message);
                                }
                            }

                            @Override
                            public void Failure(Context context, JSONObject response) {
                                hideProgressDialog();
                            }
                        });
                    }
                }).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        },new ViewHolder.ViewHolderInterface.common_click() {
            @Override
            public void click(Context context, int position, View vv) {
                //确认收货
//                showMiddleToast("确认收货");

            }
        },new ViewHolder.ViewHolderInterface.common_click() {
            @Override
            public void click(final Context context, final int position, View vv) {
                //删除订单
//                showMiddleToast("删除订单");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.warning).setMessage(R.string.confirm_delete_order).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDialogLoading();
                        OrderModel model = mData.get(position);
                        APIManager.getInstance().deleteOrder(context, model.getOrderNO(), new APIManager.APIManagerInterface.no_object() {
                            @Override
                            public void Success(Context context, int result, String message, String enmessage) {
                                hideProgressDialog();
                                if (result == 1) {
                                    MyToast.showToast(context, message);
                                    loadData();
                                } else {
                                    MyToast.showToast(context, message);
                                }
                            }

                            @Override
                            public void Failure(Context context, JSONObject response) {
                                hideProgressDialog();
                            }
                        });
                    }
                }).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
        mylist.setAdapter(myadapter);
        loadData();
    }

    void loadData(){
        showDialogLoading();
        APIManager.getInstance().getOrderList(getActivity().getApplicationContext(), 1, 10, orderStatus, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseDataListModel<OrderModel> mm = (BaseDataListModel<OrderModel>) model;
                if (mm.getResult() == 1) {
                    if (pageIndex == 1) {
                        mData.clear();
                    }
                    mData.addAll(mm.getDatalist());
                    //刷新listview
                    myadapter.notifyDataSetChanged();
                }
                BlankViewDisplay.setBlank(mData.size(),getActivity(),true,blankLayout,onClickRetry);
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                BlankViewDisplay.setBlank(mData.size(), getActivity(), false, blankLayout, onClickRetry);
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    //点击重新加载
    View.OnClickListener onClickRetry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadData();
        }
    };
}
