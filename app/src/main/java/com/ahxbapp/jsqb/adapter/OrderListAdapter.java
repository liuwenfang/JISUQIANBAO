package com.ahxbapp.jsqb.adapter;

import android.content.Context;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.OrderDetail;

import java.util.List;

/**
 * Created by xp on 16/9/2.
 */
public class OrderListAdapter extends CommonAdapter<OrderDetail.OrderDetailListModel> {
    public OrderListAdapter(Context context, List<OrderDetail.OrderDetailListModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, OrderDetail.OrderDetailListModel orderDetailListModel) {
        holder.setImageUrl(R.id.img_cover, orderDetailListModel.getThumbnail());
        holder.setText(R.id.tv_name, orderDetailListModel.getName());
        holder.setText(R.id.tv_num,Integer.toString(orderDetailListModel.getNum()));
        holder.setText(R.id.tv_size,orderDetailListModel.getSize());
        holder.setText(R.id.tv_price,"￥"+Float.toString(orderDetailListModel.getPrice()));
    }
}
