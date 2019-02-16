package com.ahxbapp.jsqb.adapter;

import android.content.Context;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.CartModel;

import java.util.List;

/**
 * Created by xp on 16/9/5.
 */
public class CalculateAdapter extends CommonAdapter<CartModel> {
    public CalculateAdapter(Context context, List<CartModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, CartModel cartModel) {
        holder.setImageUrl(R.id.img_cover, cartModel.getThumbnail());
        holder.setText(R.id.tv_name, cartModel.getTitle());
        holder.setText(R.id.tv_num,Integer.toString(cartModel.getNum()));
        holder.setText(R.id.tv_size,cartModel.getSize());
        holder.setText(R.id.tv_price,"￥"+Float.toString(cartModel.getPrice()));
    }
}
