package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.CashModel;

import java.util.List;

/**
 * Created by Admin on 2016/10/21.
 * Page
 */
public class MyCashOrderAdapter extends CommonAdapter<CashModel> {
    String status;
    ViewHolder.ViewHolderInterface.common_click checkClick;

    public MyCashOrderAdapter(Context context, List<CashModel> datas, int layoutId,
                              ViewHolder.ViewHolderInterface.common_click checkClick) {
        super(context, datas, layoutId);
        this.checkClick = checkClick;
    }

    @Override
    public void convert(ViewHolder holder, CashModel orderModel) {
        holder.setText(R.id.tvMoneyCO, orderModel.getLoanId() + "");
        holder.setText(R.id.tvTitleCO, orderModel.getType() + "");
        holder.setText(R.id.tvNumCO, orderModel.getLoanNO() + "");
        holder.setText(R.id.tvDayCO, orderModel.getTermId() + "");
        holder.setText(R.id.tvMoneyCO, orderModel.getLoanId() + "");

        if (orderModel.getStatus() == 0) {
            status = "待审核";
        } else if (orderModel.getStatus() == 1) {
            status = "待确认银行卡";
        } else if (orderModel.getStatus() == 2) {
            status = "待确认身份证";
        } else if (orderModel.getStatus() == 3) {
            status = "待签约";
        } else if (orderModel.getStatus() == 4) {
            status = "放款";
        } else if (orderModel.getStatus() == 5) {
            status = "待还款";
        } else if (orderModel.getStatus() == 6) {
            status = "已完成";
        } else if (orderModel.getStatus() == 7) {
            status = "已取消";
        } else if (orderModel.getStatus() == 8) {
            status = "审核失败";
        }

        holder.setText(R.id.tvStatusCO, status);
        if (orderModel.getStatus() <= 5) {
            holder.setTextColor(R.id.tvStatusCO, R.color.nav_blue);
        } else if (orderModel.getStatus() <= 7) {
            holder.setTextColor(R.id.tvStatusCO, R.color.btn_gray);
        } else if (orderModel.getStatus() == 8) {
            holder.setTextColor(R.id.tvStatusCO, R.color.text_red);
        }
        holder.clickButton(R.id.llContent, checkClick);
    }
}
