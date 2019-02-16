package com.ahxbapp.jsqb.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.model.MessageModel;
import com.ahxbapp.jsqb.model.PersonalModel;

import java.util.List;

/**
 * Created by xp on 16/8/30.
 */
public class PersonalAdapter extends ZRecyclerViewAdapter<PersonalModel> {


    public PersonalAdapter(@NonNull RecyclerView mRecyclerView, List<PersonalModel> dataLists) {
        super(mRecyclerView, dataLists,R.layout.adapter_personal);
    }

    @Override
    protected void bindData(ZViewHolder holder, PersonalModel data, int position) {
        holder.setBackgroundRes(R.id.ivPersonal,data.getRes());
        holder.setText(R.id.tvPersonal,data.getType());
    }
}
