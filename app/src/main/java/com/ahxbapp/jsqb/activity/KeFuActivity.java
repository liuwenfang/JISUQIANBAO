package com.ahxbapp.jsqb.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.utils.ScannerUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 联系客服  客服二维码
 */
@EActivity(R.layout.activity_ke_fu)
public class KeFuActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("联系客服");
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    @Click
    void tvSavePic() {
        ScannerUtils.saveImageToGallery(this, BitmapFactory.decodeResource(getResources(), R.mipmap.img_qr), ScannerUtils.ScannerType.RECEIVER);
    }

    @Click
    void tvCopy() {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText("Cashloan6");
        Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();
    }
}
