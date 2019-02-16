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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahxbapp.jsqb.R;


/**
 * Created by Administrator on 2016/12/1.
 */
public class LoadDialog extends Dialog {
    public LoadDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_phone);
        Window window = this.getWindow();
//        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        mContext = context;
        setCancelable(false);
        ivLoad = (ImageView) findViewById(R.id.ivLoad);
        initView();
    }

    private Context mContext;
    public ImageView ivLoad;
    private RotateAnimation animation;

    private void initView() {
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1500);
        animation.setRepeatCount(-1);
    }

    public void startAnmation() {
        animation.setStartTime(500L);
        ivLoad.startAnimation(animation);
    }

}
