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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.jsqb.R;


/**
 * Created by Administrator on 2016/12/1.
 */
public class AuthPromptDialog extends Dialog {
    public AuthPromptDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_prompt);
        Window window = this.getWindow();
//        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        mContext = context;
        tvContent = (TextView) findViewById(R.id.tvContent);
        rlCancel = (RelativeLayout) findViewById(R.id.rlCancel);
        initClick();
    }

    private Context mContext;
    public TextView tvContent;
    private RelativeLayout rlCancel;

    private void initClick() {
        rlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

//    public void setTvContent(String msg) {
//        if (msg != null) {
//            tvContent.setText(msg);
//        }
//    }
}
