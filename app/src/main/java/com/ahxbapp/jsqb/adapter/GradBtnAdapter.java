package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.ahxbapp.jsqb.R;

/**
 * Created by xp on 16/9/9.
 */
public class GradBtnAdapter extends BaseAdapter {
    Context context;
    String [] mData;
    String size;
    public GradBtnAdapter(String [] mData,Context context,String size) {
        this.context = context;
        this.mData = mData;
        this.size = size;
    }

    @Override
    public int getCount() {
        return mData.length==0?0:mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData.length==0?0:mData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_btn,null);
            holder = new ViewHolder();
            holder.btn = (Button)convertView.findViewById(R.id.btn_1);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.btn.setText(mData[position]);
        if (size.equals(mData[position])){
            holder.btn.setBackgroundResource(R.drawable.size_corner_small_bg);
        }else {
            holder.btn.setBackgroundResource(R.drawable.size_corner_small_gray_bg);
        }
        return convertView;
    }

    class ViewHolder{
        Button btn;
    }
}
