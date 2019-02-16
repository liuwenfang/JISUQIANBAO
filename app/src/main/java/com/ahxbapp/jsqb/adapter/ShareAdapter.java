package com.ahxbapp.jsqb.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.model.InvitationModel;
import com.ahxbapp.jsqb.model.PersonalModel;

import java.util.List;

/**
 * Created by xp on 16/8/30.
 */
public class ShareAdapter extends ZRecyclerViewAdapter<InvitationModel> {


    public ShareAdapter(@NonNull RecyclerView mRecyclerView, List<InvitationModel> dataLists) {
        super(mRecyclerView, dataLists,R.layout.adapter_share);
    }

    @Override
    protected void bindData(ZViewHolder holder, InvitationModel data, int position) {
        holder.setBackgroundRes(R.id.ivImage,data.getRes());
        holder.setText(R.id.tvName,data.getName());
    }
}
