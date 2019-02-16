package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahxbapp.common.widget.CircleImageView;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.model.MoneyModel;
import com.ahxbapp.jsqb.utils.DensityUtils;

import java.util.List;

/**
 * Created by zzx on 2017/6/13 0013.
 */

public class MoneyListAdapter extends RecyclerView.Adapter<MoneyListAdapter.MoneyHolder> {
    private Context mContext;
    private List<MoneyModel> modelList;
    private int count = 0;
    private boolean isHuanKuan = false;
    private onItemClickListener onItemClickListener;
    private String message;
    private String coupon;

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public void setHuanKuan(boolean huanKuan, String message, String coupon) {
        isHuanKuan = huanKuan;
        this.message = message;
        this.coupon = coupon;
        notifyDataSetChanged();
    }

    public MoneyListAdapter(Context mContext, List<MoneyModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @Override
    public MoneyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_mainlist, parent, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new MoneyHolder(view);
    }

    @Override
    public void onBindViewHolder(MoneyHolder holder, int position) {
        final MoneyModel moneyModel = modelList.get(position);
        holder.tvMoney.setText("" + moneyModel.getMoney());
        if (position == 0) {
            holder.civLogo.setBackgroundResource(R.mipmap.ic_loading_logo);
            holder.tvCount.setText("已借款" + count + "次");
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.tvCoupon.setVisibility(View.VISIBLE);
            if (isHuanKuan) {
                holder.tvHuanKuan.setVisibility(View.VISIBLE);
                holder.tvHuanMoney.setVisibility(View.VISIBLE);
                holder.tvMoney.setVisibility(View.GONE);
                holder.tvDetial.setVisibility(View.GONE);
                holder.tvFuHao.setVisibility(View.GONE);
                holder.tvHuanKuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onHuanKuanItemClick(v);
                        }
                    }
                });
                holder.llContent.setBackgroundResource(R.mipmap.img_bg1_nomoney);
                holder.tvHuanMoney.setText(message);
            } else {
                holder.tvHuanKuan.setVisibility(View.GONE);
                holder.tvHuanMoney.setVisibility(View.GONE);
                holder.tvMoney.setVisibility(View.VISIBLE);
                holder.tvDetial.setVisibility(View.VISIBLE);
                holder.tvFuHao.setVisibility(View.VISIBLE);
                holder.llContent.setBackgroundResource(R.mipmap.img_bg1_money);
                holder.llContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onOneItemClick(v);
                        }
                    }
                });
            }
            holder.tvCoupon.setText(coupon);
        } else {
            holder.tvTitle.setText("");
            holder.tvTitle.setText("老用户专享");
            holder.tvCount.setVisibility(View.GONE);
            holder.tvCoupon.setVisibility(View.GONE);
            holder.tvHuanKuan.setVisibility(View.GONE);
            holder.tvHuanMoney.setVisibility(View.GONE);
            holder.tvMoney.setVisibility(View.VISIBLE);
            holder.tvDetial.setVisibility(View.VISIBLE);
            holder.tvFuHao.setVisibility(View.VISIBLE);
            holder.civLogo.setBackgroundResource(R.mipmap.img_lock);
//            holder.civLogo.setVisibility(View.GONE);
            holder.llContent.setBackgroundResource(R.mipmap.img_bg2_money);
//            if (moneyModel.getMoney() < 2000) {
//                holder.llContent.setBackgroundResource(R.mipmap.imgbg_1);
//            } else if (moneyModel.getMoney() <= 10000) {
//                holder.llContent.setBackgroundResource(R.mipmap.img_bg2_money);
//            } else {
//                holder.llContent.setBackgroundResource(R.mipmap.imgbg_3);
//            }
        }

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MoneyHolder extends RecyclerView.ViewHolder {
        TextView tvCount, tvMoney;
        TextView tvHuanKuan, tvDetial, tvFuHao, tvTitle, tvCoupon, tvHuanMoney;
        LinearLayout llContent, llTop;
        ImageView civLogo;

        public MoneyHolder(View itemView) {
            super(itemView);
            tvCount = (TextView) itemView.findViewById(R.id.tvCount);
            tvMoney = (TextView) itemView.findViewById(R.id.tvMoney);
            tvHuanKuan = (TextView) itemView.findViewById(R.id.tvHuanKuan);
            tvDetial = (TextView) itemView.findViewById(R.id.tvDetial);
            tvFuHao = (TextView) itemView.findViewById(R.id.tvFuHao);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvCoupon = (TextView) itemView.findViewById(R.id.tvCoupon);
            tvHuanMoney = (TextView) itemView.findViewById(R.id.tvHuanMoney);
            llContent = (LinearLayout) itemView.findViewById(R.id.llContent);
            llTop = (LinearLayout) itemView.findViewById(R.id.llTop);
            civLogo = (ImageView) itemView.findViewById(R.id.civLogo);

            ViewGroup.LayoutParams layoutParams = llContent.getLayoutParams();
            layoutParams.width = (int) (DensityUtils.getWidthInPx(mContext) - DensityUtils.dip2px(mContext, 20));
            layoutParams.height = layoutParams.width * 19 / 30;
            llContent.setLayoutParams(layoutParams);

            ViewGroup.LayoutParams lp1 = llTop.getLayoutParams();
            lp1.height = (int) (layoutParams.height * 0.21);
            llTop.setLayoutParams(lp1);

        }
    }

    public void setonItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onHuanKuanItemClick(View v);

        void onOneItemClick(View view);
    }
}
