package com.ahxbapp.jsqb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.ConsumeTypeAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/1.
 */
public class ConsumeTypeDialog extends Dialog {
    public ConsumeTypeDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_consume_type);
        Window window = this.getWindow();
//        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(lp);
        mContext = context;
        mListView = (ListView) findViewById(R.id.mListView);
        initView();
    }

    private List<String> mData;
    private ListView mListView;
    private ConsumeTypeAdapter adapter;
    private Context mContext;

    private void initView() {
        mData = new ArrayList<>();
        mData.add("个人日常消费");
        mData.add("装修");
        mData.add("旅游");
        mData.add("学习");
        adapter = new ConsumeTypeAdapter(mContext, mData);
        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selType != null) {
                    selType.getSelType(mData.get(position));
                    dismiss();
                }
            }
        });
    }

    private selType selType;

    public void setSelType(ConsumeTypeDialog.selType selType) {
        this.selType = selType;
    }

    //  获取选择的页面
    public interface selType {
        void getSelType(String selType);
    }
}
