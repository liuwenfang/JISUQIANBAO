package com.ahxbapp.jsqb.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.activity.WebActivity_;

/**
 * Created by Administrator on 2018/5/5.
 */

public class SuperDialog extends Dialog {
//    private String url;

    public SuperDialog(final Context context, final String url) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_super);
        Window window = this.getWindow();
//        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        LinearLayout llSuper = (LinearLayout) findViewById(R.id.llSuper);
        llSuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebActivity_.intent(context).url(url).title("贷超").start();
                dismiss();
            }
        });
    }
}
