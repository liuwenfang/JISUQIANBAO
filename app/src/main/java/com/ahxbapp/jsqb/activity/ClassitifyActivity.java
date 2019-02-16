package com.ahxbapp.jsqb.activity;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * 分期乐，花呗，。。。
 * 征信查询
 */
@EActivity(R.layout.activity_classitify)
public class ClassitifyActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    ImageView ivBg;
    @Extra
    String title;
    @Extra
    int bgRes;

    @AfterViews
    void init() {
        mToolbarTitleTV.setText(title);
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        ivBg.setBackgroundResource(bgRes);
    }


    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
