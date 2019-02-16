package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.MessageModel;

import java.util.List;

/**
 * Created by xp on 16/8/30.
 */
public class MessageRecyclerAdapter extends ZRecyclerViewAdapter<MessageModel> {


    public MessageRecyclerAdapter(@NonNull RecyclerView mRecyclerView, List<MessageModel> dataLists) {
        super(mRecyclerView, dataLists,R.layout.adapter_message);
    }

    @Override
    protected void bindData(ZViewHolder holder, MessageModel data, int position) {
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
