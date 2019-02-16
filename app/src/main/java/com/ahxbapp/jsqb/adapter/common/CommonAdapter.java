package com.ahxbapp.jsqb.adapter.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用的adapter
 *
 * @param <T> 传入的实体类
 * @author 陈建
 * @Time 2015-05-31
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int mlayoutId;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.context = context;
        this.mDatas = datas;
        this.mlayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public List<T> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {

        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {

        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(convertView, context, parent, mlayoutId, position);
        convert(holder, getItem(position));
        return holder.getmConverView();
    }

//    @Override
//    public boolean isEnabled(int position) {
//        if (getItemViewType(position) == position) {
//            return false;
//        } else {
//            return true;
//        }
////        return super.isEnabled(position);
//    }

    public abstract void convert(ViewHolder holder, T t);
}
