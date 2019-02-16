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
import android.widget.TextView;

import com.ahxbapp.jsqb.R;


/**
 * Created by Administrator on 2016/12/1.
 */
public class PromptDialog extends Dialog {
    public PromptDialog(Context context) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.prompt_dialog);
        Window window = this.getWindow();
//        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        mContext = context;
        tvCancle = (TextView) findViewById(R.id.tvCancleP);
        tvConfirm = (TextView) findViewById(R.id.tvConfirmP);
        tvPrompt = (TextView) findViewById(R.id.tvPrompt);
        tvTitle = (TextView) findViewById(R.id.tvTitleP);
        viewXian = findViewById(R.id.viewXian);
        initClick();
    }

    private Context mContext;
    public TextView tvCancle, tvConfirm, tvTitle, tvPrompt;
    public View viewXian;

    private void initClick() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                gotoAppDetailSettingIntent();
            }
        });
    }


    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private void gotoAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getContext().getPackageName());
        }
        mContext.startActivity(localIntent);
    }
}
