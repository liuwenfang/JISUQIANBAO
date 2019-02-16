package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.MessageModel;

import java.util.List;

/**
 * Created by xp on 16/8/30.
 */
public class MessageAdapter extends CommonAdapter<MessageModel> {

    ViewHolder.ViewHolderInterface.common_click checkClick;
    public MessageAdapter(Context context, List<MessageModel> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, MessageModel data) {
        LinearLayout llContent = holder.getView(R.id.llContent);
        if (data.getIsRead()==0){//未读
            llContent.setBackgroundResource(R.drawable.shape_white_5);
        }else {
            llContent.setBackgroundResource(R.drawable.shape_gray_5);
        }
        holder.setText(R.id.tvTitle,data.getTitle());
        holder.setText(R.id.tvTime, data.getAddTime());
        holder.setText(R.id.tvContent, data.getContent());
    }
}
