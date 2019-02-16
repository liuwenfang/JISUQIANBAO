package com.ahxbapp.jsqb.adapter;

import android.content.Context;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.OrderModel.OrderDetailModel;

import java.util.List;

/**
 * Created by xp on 16/8/31.
 */
public class OrderDetailAdapter extends CommonAdapter<OrderDetailModel>{
    public OrderDetailAdapter(Context context, List<OrderDetailModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, OrderDetailModel orderDetailModel) {
        holder.setImageUrl(R.id.img_cover, orderDetailModel.getThumbnail());
        holder.setText(R.id.tv_name, orderDetailModel.getName());
        holder.setText(R.id.tv_num,Integer.toString(orderDetailModel.getNum()));
        holder.setText(R.id.tv_size,orderDetailModel.getSize());
        holder.setText(R.id.tv_price,"￥"+Float.toString(orderDetailModel.getPrice()));
    }
}
