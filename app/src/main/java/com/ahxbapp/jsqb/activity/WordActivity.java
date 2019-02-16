package com.ahxbapp.jsqb.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.utils.MyToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * 留言  反馈
 */
@EActivity(R.layout.word)
public class WordActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    Button word_btn, phone_btn;
    @ViewById
    TextView time_text;
    @ViewById
    EditText word_text;

    @AfterViews
    void init() {

        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("留言");
        APIManager.getInstance().member_getSerMobile(this, new APIManager.APIManagerInterface.no_object() {
            @Override
            public void Success(Context context, int result, String message, String enmessage) {
                phone_btn.setText(enmessage);
                String time = "服务时间:" + message;
                time_text.setText(time);
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    @Click
    void word_btn() {
        if (word_text.getText().toString().length() <= 0) {
            showMiddleToast("请输入留言内容");
            return;
        }
        showDialogLoading();
        APIManager.getInstance().member_addLyMsg(this, word_text.getText().toString(), new APIManager.APIManagerInterface.common_wordBack() {
            @Override
            public void Success(Context context, int result, String message) {
                hideProgressDialog();
                MyToast.showToast(context, message);
                if (result == 1) {
                    word_text.setText("");
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });

    }

    @Click
    void phone_btn() {
        if (phone_btn.getText().length() > 0) {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("tel:" + phone_btn.getText()).setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phone_btn.getText());
                    intent.setData(data);
                    startActivity(intent);
                }
            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        } else {
            showMiddleToast("获取客服电话失败，请稍后重试");
        }
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
