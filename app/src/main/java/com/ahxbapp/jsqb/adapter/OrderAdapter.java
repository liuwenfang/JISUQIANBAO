package com.ahxbapp.jsqb.adapter;

import android.content.Context;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.OrderModel;

import java.util.List;

/**
 * Created by xp on 16/8/31.
 */
public class OrderAdapter extends CommonAdapter<OrderModel> {
    ViewHolder.ViewHolderInterface.common_click block1,block2,block3,block4;
    public OrderAdapter(Context context, List<OrderModel> datas, int layoutId,ViewHolder.ViewHolderInterface.common_click block1,ViewHolder.ViewHolderInterface.common_click block2,ViewHolder.ViewHolderInterface.common_click block3,ViewHolder.ViewHolderInterface.common_click block4) {
        super(context, datas, layoutId);
        this.block1 = block1;
        this.block2 = block2;
        this.block3 = block3;
        this.block4 = block4;
    }

    @Override
    public void convert(ViewHolder holder, OrderModel orderModel) {
        holder.setText(R.id.tv_orderNo, orderModel.getOrderNO());
        String orderStatus = null;
        if (orderModel.getOrderState() == 0){
            orderStatus = context.getResources().getString(R.string.order_nopay);
            holder.setHide(R.id.tv_total_pay);
            holder.setHide(R.id.layout_receive);
            holder.setHide(R.id.layout_complete);
        }else if (orderModel.getOrderState() == 1){
            orderStatus = context.getResources().getString(R.string.order_producting);
            holder.setHide(R.id.tv_price);
            holder.setHide(R.id.tv_youhui);
            holder.setHide(R.id.tv_pay);
            holder.setHide(R.id.layout_nopay);
            holder.setHide(R.id.layout_receive);
            holder.setHide(R.id.layout_complete);
        }else if (orderModel.getOrderState() == 2){
            orderStatus = context.getResources().getString(R.string.order_noreceive);
            holder.setHide(R.id.tv_price);
            holder.setHide(R.id.tv_youhui);
            holder.setHide(R.id.tv_pay);
            holder.setHide(R.id.layout_nopay);
            holder.setHide(R.id.layout_complete);
        }else if (orderModel.getOrderState() == 3){
            orderStatus = context.getResources().getString(R.string.order_complete);
            holder.setHide(R.id.tv_price);
            holder.setHide(R.id.tv_youhui);
            holder.setHide(R.id.tv_pay);
            holder.setHide(R.id.layout_nopay);
            holder.setHide(R.id.layout_receive);
        }
        holder.setText(R.id.tv_order_status, orderStatus);
        OrderDetailAdapter myadapter = new OrderDetailAdapter(context,orderModel.getOrderDetail(),R.layout.item_order_detail_list);
        holder.setListView(R.id.orderDetail_list, myadapter);
        if (orderModel.getFreight() == 0){
            //免运费
            holder.setText(R.id.tv_price,context.getResources().getString(R.string.fuhao)+Float.toString(orderModel.getTotalPrice())+"("+context.getResources().getString(R.string.free_freight)+")");
        }else {
            holder.setText(R.id.tv_price,Float.toString(orderModel.getTotalPrice())+"("+context.getResources().getString(R.string.freight)+":￥"+orderModel.getFreight()+")");
        }
        holder.setText(R.id.tv_youhui,context.getResources().getString(R.string.youhui_empty)+Float.toString(orderModel.getCoupPrice()));
        holder.setText(R.id.tv_pay,context.getResources().getString(R.string.haixu_pay)+Float.toString(orderModel.getPrice()));
        holder.setText(R.id.tv_total_pay,context.getResources().getString(R.string.xu_pay)+Float.toString(orderModel.getPrice()));
        //按钮点击回调
        holder.clickButton(R.id.btn_pay, block1);
        holder.clickButton(R.id.btn_cancel,block2);
        holder.clickButton(R.id.btn_receive,block3);
        holder.clickButton(R.id.btn_delete,block4);
    }
}
