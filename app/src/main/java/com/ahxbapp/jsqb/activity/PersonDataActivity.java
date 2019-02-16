package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.PersonDataModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * 个人资料
 */
@EActivity(R.layout.activity_person_data)
public class PersonDataActivity extends BaseActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    RelativeLayout per_shouru, per_lianxiren2, per_sehnfenzheng, per_yinhang_kahao;
    @ViewById
    TextView text_phone, text_zhiye, text_shouru, lianxiren1, text_lianxiren1, lianxiren2, text_lianxiren2, text_shenfenzheng, text_xingming, text_yinhang_ming, text_kahao;
    @ViewById
    TextView yinhangName_update, zhiye_update, lianxiren1_update, lianxiren2_update, shouru_update, yinhang_update, shenfenzheng_update, xingming_update;

    /**
     * 个人资料
     */
    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("个人资料");
        loadData();
    }

    void loadData() {
        APIManager.getInstance().member_getPerData(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                PersonDataModel model1 = (PersonDataModel) model;
                text_phone.setText(model1.getMobile());
                text_zhiye.setText(model1.getPosName());
                text_shouru.setText(model1.getIncName());
                if (model1.getRelaName() != null) {
                    lianxiren1.setText(model1.getRelaName());
                    text_lianxiren1.setText(model1.getRelaMobile());
                }
                if (model1.getSocName() != null) {
                    lianxiren2.setText(model1.getSocName());
                    text_lianxiren2.setText(model1.getSocMobile());
                }
                text_shenfenzheng.setText(model1.getIdentity());
                text_xingming.setText(model1.getTrueName());
                text_yinhang_ming.setText(model1.getBankName());
                text_kahao.setText(model1.getCardNo());
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    @OnActivityResult(100)
    void AccountActivity() {
        loadData();
    }

    @Click
    void shenfenzheng_update() {
        AccountActivity_.intent(this).flag(2).startForResult(100);
    }

    @Click
    void zhiye_update() {
        JobActivity_.intent(this).startForResult(100);
    }

    @Click
    void shouru_update() {
        JobActivity_.intent(this).startForResult(100);
    }

    @Click
    void lianxiren1_update() {
        ContactPersonActivity_.intent(this).startForResult(100);
    }

    @Click
    void lianxiren2_update() {
        ContactPersonActivity_.intent(this).startForResult(100);
    }

    @Click
    void xingming_update() {
        AccountActivity_.intent(this).flag(2).startForResult(100);
    }

    @Click
    void yinhangName_update() {
        CardActivity_.intent(this).startForResult(100);
    }

    @Click
    void yinhang_update() {
        CardActivity_.intent(this).startForResult(100);
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
