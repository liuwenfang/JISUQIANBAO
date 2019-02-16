package com.ahxbapp.jsqb.adapter;

import android.content.Context;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.common.CommonAdapter;
import com.ahxbapp.jsqb.adapter.common.ViewHolder;
import com.ahxbapp.jsqb.model.SendMethod;

import java.util.List;

/**
 * Created by xp on 16/9/6.
 */
public class SendAdapter extends CommonAdapter<SendMethod> {
    public SendAdapter(Context context, List<SendMethod> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, SendMethod sendMethod) {
        holder.setText(R.id.send_id,sendMethod.getName());
    }
}
