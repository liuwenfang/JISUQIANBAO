package com.ahxbapp.jsqb.adapter;

import android.content.Context;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.ReceiptAddrModel;

import java.util.List;

/**
 * Created by xp on 16/8/23.
 */
public class ReceiptAddrAdapter extends CommonAdapter<ReceiptAddrModel>{


    ViewHolder.ViewHolderInterface.common_click clickBlock_isDefault;
    ViewHolder.ViewHolderInterface.common_click clickBlock_edit;
    ViewHolder.ViewHolderInterface.common_click clickBlock_delete;

    public ReceiptAddrAdapter(Context context,List<ReceiptAddrModel> datas,int layoutId,final ViewHolder.ViewHolderInterface.common_click block1,final ViewHolder.ViewHolderInterface.common_click block2,final ViewHolder.ViewHolderInterface.common_click block3){
        super(context, datas, layoutId);
        clickBlock_isDefault=block1;
        clickBlock_edit=block2;
        clickBlock_delete=block3;

    }

    @Override
    public void convert(ViewHolder holder, ReceiptAddrModel receiptAddr) {
        holder.setText(R.id.tv_name,receiptAddr.getName());
        holder.setText(R.id.tv_phone,receiptAddr.getMobile());
        holder.setText(R.id.tv_address,receiptAddr.getAddress()+" "+receiptAddr.getZip());
        if (receiptAddr.getIsdefult() == 1){
            //默认地址
            holder.setButtonDrawable(R.id.btn_isDefault,R.mipmap.bg_adress_check,1);
            holder.setShow(R.id.tv_defaultAddr);
        }else {
            holder.setButtonDrawable(R.id.btn_isDefault,R.mipmap.bg_adress,1);
            holder.setHide(R.id.tv_defaultAddr);
        }
        holder.clickButton(R.id.btn_isDefault, clickBlock_isDefault);
        holder.clickButton(R.id.btn_edit, clickBlock_edit);
        holder.clickButton(R.id.btn_delete, clickBlock_delete);
    }
}
