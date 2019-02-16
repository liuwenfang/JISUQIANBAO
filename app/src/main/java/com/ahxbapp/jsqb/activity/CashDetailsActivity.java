package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

/**
 * 借款详情   (暂未用到)
 */
@EActivity(R.layout.activity_order_details)
public class CashDetailsActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;


    @ViewById
    TextView one_text, two_text, three_text, four_text, five_text;
    @ViewById
    TextView mTitle1, mTitle2, mTitle3, mTitle4, mTitle5;
    @ViewById
    TextView mContent1, mContent2, mContent3, mContent4, mContent5;

    @Extra
    int loanID;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("借款详情");

        loadDetails();
    }

    //借款詳情
    void loadDetails() {
        showDialogLoading();
        APIManager.getInstance().detailsLoan(this, loanID, new APIManager.APIManagerInterface.loan_details() {

            @Override
            public void Success(Context context, int result, String message, int data) {
                hideProgressDialog();
                if (result == 1) {
                    if (data == 0) {
                        one_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle1.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent1.setTextColor(getResources().getColor(R.color.detail_cash));
                    } else if (data == 1) {
                        one_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle1.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent1.setTextColor(getResources().getColor(R.color.detail_cash));
                        two_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle2.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent2.setTextColor(getResources().getColor(R.color.detail_cash));
                    } else if (data == 2) {
                        one_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle1.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent1.setTextColor(getResources().getColor(R.color.detail_cash));
                        two_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle2.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent2.setTextColor(getResources().getColor(R.color.detail_cash));
                        three_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle3.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent3.setTextColor(getResources().getColor(R.color.detail_cash));
                    } else if (data == 3) {
                        one_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle1.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent1.setTextColor(getResources().getColor(R.color.detail_cash));
                        two_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle2.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent2.setTextColor(getResources().getColor(R.color.detail_cash));
                        three_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle3.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent3.setTextColor(getResources().getColor(R.color.detail_cash));
                        four_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle4.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent4.setTextColor(getResources().getColor(R.color.detail_cash));
                    } else if (data == 5) {
                        one_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle1.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent1.setTextColor(getResources().getColor(R.color.detail_cash));
                        two_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle2.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent2.setTextColor(getResources().getColor(R.color.detail_cash));
                        three_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle3.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent3.setTextColor(getResources().getColor(R.color.detail_cash));
                        four_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle4.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent4.setTextColor(getResources().getColor(R.color.detail_cash));
                        five_text.setBackgroundResource(R.drawable.text_blue);
                        mTitle5.setTextColor(getResources().getColor(R.color.nav_blue));
                        mContent5.setTextColor(getResources().getColor(R.color.detail_cash));
                    }
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }
}
