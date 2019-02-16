package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.CashModel;

import org.json.JSONObject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Admin on 2016/10/21.
 * Page
 */
public class CashOrderAdapter extends CommonAdapter<CashModel> {
    String status;
    ViewHolder.ViewHolderInterface.common_click cancelClick, checkClick;

    public CashOrderAdapter(Context context, List<CashModel> datas, int layoutId, ViewHolder.ViewHolderInterface.common_click cancelClick,
                            ViewHolder.ViewHolderInterface.common_click checkClick) {
        super(context, datas, layoutId);
        this.cancelClick = cancelClick;
        this.checkClick = checkClick;
    }

    @Override
    public void convert(ViewHolder holder, CashModel orderModel) {
        holder.setText(R.id.type, orderModel.getType());
        holder.setText(R.id.time, orderModel.getAddTime());
        holder.setText(R.id.num, orderModel.getLoanNO());
        holder.setText(R.id.money, orderModel.getLoanId() + "");
        holder.setText(R.id.deadline, orderModel.getTermId() + "");
        holder.setText(R.id.old_day, orderModel.getPalyTime());
        holder.setText(R.id.new_day, orderModel.getRepayTime());
        RelativeLayout layout = holder.getView(R.id.lookRL);
        Button button = holder.getView(R.id.cancelBN);

        if (orderModel.getStatus() == 0) {
            status = "审核";
        } else if (orderModel.getStatus() == 1) {
            status = "正在确认银行卡";
            button.setVisibility(View.GONE);
        } else if (orderModel.getStatus() == 2) {
            status = "身份证确认中";
            button.setVisibility(View.GONE);
        } else if (orderModel.getStatus() == 3) {
            status = "待签约";
            button.setVisibility(View.GONE);
        } else if (orderModel.getStatus() == 4) {
            status = "正在放款中";
            button.setText("查看合同");
        } else if (orderModel.getStatus() == 5) {
            status = "待还款";
            button.setText("查看合同");
        } else if (orderModel.getStatus() == 6) {
            status = "已完成";
            button.setText("查看合同");
        } else if (orderModel.getStatus() == 7) {
            status = "已取消";
            layout.setVisibility(View.GONE);
        }

        holder.setText(R.id.statusTV, status);

        holder.clickButton(R.id.lookBN, checkClick);
        holder.clickButton(R.id.cancelBN, cancelClick);
    }

}
