package com.ahxbapp.jsqb.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.model.ClassificationModel;
import com.ahxbapp.jsqb.utils.DensityUtils;

import java.util.List;

/**
 * Created by urnotxx on 2018/9/4.
 */

public class ClassificationAdapter extends ZRecyclerViewAdapter<ClassificationModel> {
    public ClassificationAdapter(@NonNull RecyclerView mRecyclerView, List<ClassificationModel> dataLists) {
        super(mRecyclerView, dataLists, R.layout.adapter_classification);
    }

    @Override
    protected void bindData(ZViewHolder holder, ClassificationModel data, int position) {
        LinearLayout llContent = holder.getView(R.id.llContent);
        ViewGroup.LayoutParams layoutParams = llContent.getLayoutParams();
        layoutParams.width = (int) (DensityUtils.getWidthInPx(getContext()) / 4);
        llContent.setLayoutParams(layoutParams);
        ImageView ivRes = holder.getView(R.id.ivRes);
        ivRes.setBackgroundResource(data.getRes());
        holder.setText(R.id.tvTitle, data.getTitle());
//        holder.setText(R.id.tvDetail, data.getDetail());
    }
}
