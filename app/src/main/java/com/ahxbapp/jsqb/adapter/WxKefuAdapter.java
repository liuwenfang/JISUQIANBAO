package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.model.MoneyModel;
import com.ahxbapp.jsqb.model.WxModel;
import com.ahxbapp.jsqb.utils.DensityUtils;

import java.util.List;

/**
 * Created by zzx on 2017/6/13 0013.
 */

public class WxKefuAdapter extends RecyclerView.Adapter<WxKefuAdapter.WxHolder> {
    private Context mContext;
    private List<WxModel> modelList;


    public WxKefuAdapter(Context mContext, List<WxModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @Override
    public WxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_wx, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new WxHolder(view);
    }

    @Override
    public void onBindViewHolder(WxHolder holder, final int position) {
        final WxModel moneyModel = modelList.get(position);
        holder.tvWxKefu.setText("微信客服" + (position + 1));
        holder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onOneItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class WxHolder extends RecyclerView.ViewHolder {
        TextView tvWxKefu;
        LinearLayout llContent;

        public WxHolder(View itemView) {
            super(itemView);
            tvWxKefu = (TextView) itemView.findViewById(R.id.tvWxKefu);
            llContent = (LinearLayout) itemView.findViewById(R.id.llContent);
//            ViewGroup.LayoutParams layoutParams = llContent.getLayoutParams();
//            layoutParams.width = (int) (DensityUtils.getWidthInPx(mContext) - DensityUtils.dip2px(mContext, 20));
//            layoutParams.height = layoutParams.width * 19 / 30;
//            llContent.setLayoutParams(layoutParams);

        }
    }

    private onItemClickListener onItemClickListener;

    public void setonItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onOneItemClick(int position);
    }
}
