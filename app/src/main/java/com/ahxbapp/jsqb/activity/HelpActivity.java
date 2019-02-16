package com.ahxbapp.jsqb.activity;

import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * 支付宝认证帮助
 */
@EActivity(R.layout.activity_help)
public class HelpActivity extends BaseActivity {

    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    ImageButton mToolbarLeftIB;

    @AfterViews
    void inithelpView() {
        mToolbarTitleTV.setText("支付宝二维码操作流程");
        mToolbarLeftIB.setImageResource(R.mipmap.back);
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
