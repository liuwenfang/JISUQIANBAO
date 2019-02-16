package com.ahxbapp.jsqb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahxbapp.jsqb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gravel on 16/7/22.
 */
public class MerchantServiceAdapter extends BaseAdapter {

    public static final int MAX_TAG_COUNT = 10;

    List<String> mData;
    private Context context;
    private LayoutInflater mInflater;

    public MerchantServiceAdapter(Context context, ArrayList<String> mData) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    public void setmData(ArrayList<String> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_merchant_service, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(getItem(position));
        return convertView;
    }

    static class ViewHolder {
        TextView title;
    }
}
