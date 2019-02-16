package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.customview.NoScrollGridView;
import com.ahxbapp.jsqb.model.CartModel;
import com.ahxbapp.jsqb.utils.DeviceUtil;
import com.ahxbapp.jsqb.utils.MyToast;

import java.util.List;

/**
 * Created by xp on 16/9/2.
 */
public class CartAdapter extends CommonAdapter<CartModel> {
    public boolean isChecked;
    ViewHolder tmpVh;
    ViewHolder.ViewHolderInterface.common_click block_add,block_reduce,block_choose,block_help;
    public CartAdapter(Context context, List<CartModel> datas, int layoutId,ViewHolder.ViewHolderInterface.common_click block1,ViewHolder.ViewHolderInterface.common_click block2,ViewHolder.ViewHolderInterface.common_click block3,ViewHolder.ViewHolderInterface.common_click block4) {
        super(context, datas, layoutId);
        block_add = block1;
        block_reduce = block2;
        block_choose = block3;
        block_help = block4;
    }

    @Override
    public void convert(ViewHolder holder, CartModel cartModel) {
        tmpVh = holder;
        holder.setImageUrl(R.id.img_cover, cartModel.getThumbnail());
        holder.setText(R.id.tv_name, cartModel.getTitle());
        holder.setText(R.id.tv_price,"￥"+ Float.toString(cartModel.getPrice()));
        holder.setText(R.id.tv_num, Integer.toString(cartModel.getNum()));
        //添加商品
        holder.clickButton(R.id.btn_add,block_add);
        //减少商品
        holder.clickButton(R.id.btn_reduce,block_reduce);
        holder.clickButton(R.id.btn_choose,block_choose);
        holder.clickButton(R.id.img_help,block_help);
        //选择
        if (isChecked){
            tmpVh.setCheckBoxState(R.id.btn_choose,true);
        }else {
            tmpVh.setCheckBoxState(R.id.btn_choose,false);
        }

        String str = cartModel.getComSize();
        String[] strs = str.split(",");
//        String [] strs = new String[]{"M","M","M","M","M","M","M","M"};
        GradBtnAdapter myadapter = new GradBtnAdapter(strs,context,cartModel.getSize());
        NoScrollGridView gridView=holder.getView(R.id.gv_size);
        ViewGroup.LayoutParams params =gridView.getLayoutParams();
        params.width= DeviceUtil.dip2px(context,37)*str.length();
        gridView.setLayoutParams(params);
        gridView.setNumColumns(str.length());
        holder.setGridView(R.id.gv_size, myadapter);

        //点击item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyToast.showToast(context,position+"");
            }
        });

        myadapter.notifyDataSetChanged();
    }
}
