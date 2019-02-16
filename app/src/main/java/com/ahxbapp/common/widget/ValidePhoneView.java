package com.ahxbapp.common.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.base.MyJsonResponse;
import com.ahxbapp.common.model.PhoneCountry;
import com.ahxbapp.common.network.MyAsyncHttpClient;
import com.ahxbapp.common.util.InputCheck;
import com.ahxbapp.common.util.OnTextChange;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.utils.MyToast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

/**
 * Created by chenchao on 16/1/4.
 */
public class ValidePhoneView extends TextView {

    private MyJsonResponse parseJson;

    OnTextChange editPhone;
    String inputPhone = "";

    PhoneCountry pickCountry = PhoneCountry.getChina();

    private String url = Global.HOST_API + "Home/SendCode";

    public ValidePhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPhoneMessage();
            }
        });
    }

    public void setEditPhone(OnTextChange edit) {
        editPhone = edit;
    }

    public void setPhoneCountry(PhoneCountry phoneCountry) {
        pickCountry = phoneCountry;
    }

    public void setPhoneString(String phone) {
        inputPhone = phone;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }

    private CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

        public void onTick(long millisUntilFinished) {
            ValidePhoneView.this.setText(String.format("%ds", millisUntilFinished / 1000));
            ValidePhoneView.this.setEnabled(false);
        }

        public void onFinish() {
            ValidePhoneView.this.setEnabled(true);
            ValidePhoneView.this.setText(R.string.send_code);
        }
    };

    public void onStop() {
        countDownTimer.cancel();
        countDownTimer.onFinish();
    }

    void sendPhoneMessage() {
        if (inputPhone.isEmpty() && editPhone == null) {
            Log.e("", "editPhone is null");
            return;
        }

        String phoneString;
        if (editPhone != null) {
            phoneString = editPhone.getText().toString();
        } else {
            phoneString = inputPhone;
        }

        if (!InputCheck.checkPhone(getContext(), phoneString)) return;

        RequestParams params = new RequestParams();

        AsyncHttpClient client = MyAsyncHttpClient.createClient(getContext());

        if (parseJson == null) {
            parseJson = new MyJsonResponse(getContext()) {
                @Override
                public void onMySuccess(JSONObject response) {
                    super.onMySuccess(response);
                    MyToast.showToast(getContext(), "验证码已发送");
                }

                @Override
                public void onMyFailure(JSONObject response) {
                    super.onMyFailure(response);
                    countDownTimer.cancel();
                    countDownTimer.onFinish();
                }
            };
        }

        params.put("Mobile", phoneString);
        client.post(getContext(), url, params, parseJson);

        countDownTimer.start();
    }
}
