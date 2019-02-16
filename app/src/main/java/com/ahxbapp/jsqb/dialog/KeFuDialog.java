package com.ahxbapp.jsqb.dialog;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ahxbapp.jsqb.R;


/**
 * Created by Administrator on 2016/12/1.
 */
public class KeFuDialog extends Dialog {
    public KeFuDialog(Context context, String content) {
        super(context, R.style.Theme_Light_FullScreenDialogAct);
        setContentView(R.layout.dialog_kefu);
        Window window = this.getWindow();
//        window.setWindowAnimations(R.style.myDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        mContext = context;
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvConfirm = (TextView) findViewById(R.id.tvConfirm);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContent.setText(content);
        this.content = content;
        initClick();
    }

    private Context mContext;
    public TextView tvCancel, tvConfirm, tvContent;
    private String content;

    private void initClick() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(content);
                Toast.makeText(mContext, "复制成功", Toast.LENGTH_LONG).show();
            }
        });
    }


}
