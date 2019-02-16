package com.ahxbapp.jsqb.activity;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.utils.PrefsUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 启动页
 */
@EActivity(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity {

    @AfterViews
    void init() {
        String token = PrefsUtil.getString(this, Global.TOKEN);
        if (token == null) {
            JPushInterface.setAlias(LaunchActivity.this, "0", new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                }
            });
            JPushInterface.resumePush(LaunchActivity.this);
        }
//        GuideActivity_.intent(LaunchActivity.this).start();
        LoanMainActivity_.intent(LaunchActivity.this).start();
        finish();
    }


}