package com.ahxbapp.jsqb.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.dialog.PromptDialog;
import com.ahxbapp.jsqb.model.CommonEnity;
import com.ahxbapp.jsqb.model.User;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.ahxbapp.jsqb.utils.VersionUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;

/**
 * 设置
 */
@EActivity(R.layout.setting)
public class SettingActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, size_text, tvVersion;
    @ViewById
    RelativeLayout upDataPwd_Rela, clear_Rela;
    @ViewById
    Button exit_btn;

    @AfterViews
    void init() {

        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("设置");
        String token = PrefsUtil.getString(this, Global.TOKEN);
        if (token != null) {
            exit_btn.setBackground(getResources().getDrawable(R.drawable.round_corner_btn));
            exit_btn.setEnabled(true);
        } else {
            exit_btn.setBackground(getResources().getDrawable(R.drawable.round_cornerbtn_gray));
            exit_btn.setEnabled(false);
        }
        String code = "";
        try {
            code = VersionUtils.getVersionName(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvVersion.setText("v" + code);
        //IOHelper.totalFileSize()

    }

    /**
     * 修改密码
     */
    @Click
    void upDataPwd_Rela() {
        String token = PrefsUtil.getString(this, Global.TOKEN);
        if (token == null) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            SecurityCodeActivity_.intent(this).start();
        }
    }

    /**
     * 清除缓存    暂未实现
     */
    @Click
    void clear_Rela() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要清除缓存?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //清理缓存
                showButtomToast("清理缓存成功");
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    /**
     * 退出
     */
    @Click
    void exit_btn() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
            promptDialog.tvTitle.setText("确定退出吗？");
            promptDialog.tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    promptDialog.dismiss();
                    PrefsUtil.remove(SettingActivity.this, Global.TOKEN);
                    //删除用户信息
                    DataSupport.deleteAll(User.class);
//                    setResult(RESULT_OK);
                    JPushInterface.setAlias(SettingActivity.this, "0", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                        }
                    });
                    JPushInterface.resumePush(SettingActivity.this);
                    EventBus.getDefault().post(new CommonEnity<>("logOut"));
                    LoanMainActivity_.intent(SettingActivity.this).start();
                    finish();
                }
            });
        }
        promptDialog.show();
    }

    private PromptDialog promptDialog;

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
