package com.ahxbapp.jsqb.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.dialog.PromptDialog;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.utils.GetContactsInfoF;
import com.ahxbapp.jsqb.utils.PermissionUtils;
import com.ahxbapp.jsqb.utils.PrefsUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * 必备认证
 */
@EActivity(R.layout.necessary)
public class NecessaryCerActivity extends BaseActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    LinearLayout phoneCer, perInfoCer, zhifubaoCer, taobaoCer, zmxyCer, yhkCer, sfzCer;

    @ViewById
    Button cer_phone, cer_info, cer_zfb, cer_taobao, btn_queding, cer_zmxy, cer_yhk, cer_sfz;

    private AuthenModel model1 = new AuthenModel();

    private Context mContext;
    @AfterViews
    void init() {
        mContext = this;
        EventBus.getDefault().register(this);
        //导航
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("必备认证");
        loadData();
    }

    void loadData() {
        String token = PrefsUtil.getString(NecessaryCerActivity.this, Global.TOKEN);
        if (token != null) {
            showDialogLoading();
            APIManager.getInstance().home_getAuthenStatus(this, 0, new APIManager.APIManagerInterface.common_object() {
                @Override
                public void Success(Context context, Object model) {
                    hideProgressDialog();
                    model1 = (AuthenModel) model;
                    //&&model1.getIsZM()==1
                    if (model1.getIsMobile() == 1 && model1.getIsContact() == 1 && model1.getIsPay() == 1 && model1.getIsTabao() == 1 && model1.getIsZM() == 1 && model1.getIsID() == 1) {
                        btn_queding.setEnabled(true);
                        btn_queding.setBackground(getResources().getDrawable(R.drawable.btn_cut));
                    } else {
                        btn_queding.setEnabled(false);
                        btn_queding.setBackground(getResources().getDrawable(R.drawable.btn_cut_gray));
                    }
                    //淘宝认证
                    if (model1.getIsTabao() == 1) {
                        cer_taobao.setBackground(getResources().getDrawable(R.drawable.btn_cut_color20dp));
                        cer_taobao.setText("已认证");
                    }
                    //支付宝认证
                    if (model1.getIsPay() == 1) {
                        cer_zfb.setBackground(getResources().getDrawable(R.drawable.btn_cut_color20dp));
                        cer_zfb.setText("已认证");
                    }
                    //芝麻信用认证
                    if (model1.getIsZM() == 1) {
                        cer_zmxy.setBackground(getResources().getDrawable(R.drawable.btn_cut_color20dp));
                        cer_zmxy.setText("已认证");
                    }
                    //身份证认证
                    if (model1.getIsID() == 1) {
                        cer_sfz.setBackground(getResources().getDrawable(R.drawable.btn_cut_color20dp));
                        cer_sfz.setText("已认证");
                    }
                    //紧急联系人认证
                    if (model1.getIsContact() == 1) {
                        cer_info.setBackground(getResources().getDrawable(R.drawable.btn_cut_color20dp));
                        cer_info.setText("已认证");
                    }
                    //手机号码认证
                    if (model1.getIsMobile() == 1) {
                        cer_phone.setBackground(getResources().getDrawable(R.drawable.btn_cut_color20dp));
                        cer_phone.setText("已认证");
                    }
                    //银行卡认证
                    if (model1.getIsBank() == 1) {
                        cer_yhk.setBackground(getResources().getDrawable(R.drawable.btn_cut_color20dp));
                        cer_yhk.setText("已认证");
                    }
                }

                @Override
                public void Failure(Context context, JSONObject response) {
                    hideProgressDialog();
                }
            });
        }
    }

    @OnActivityResult(3000)
    void PersonalDetailsActivity() {
        loadData();
    }

    /**
     * 必备认证流程
     * 淘宝-支付宝-芝麻信用-紧急联系人-身份证拍照-手机号码验证
     */

    @Click
    void cer_taobao() {
        TaobaoCerActivity_.intent(this).startForResult(3000);
    }

    @Click
    void taobaoCer() {
        //淘宝认证
        TaobaoCerActivity_.intent(this).startForResult(3000);
    }

    @Click
    void cer_zfb() {
        //支付宝认证
//        if (model1.getIsTabao() == 1) {
//            AlipayCerActivity_.intent(this).startForResult(3000);
//        } else {
//            showButtomToast("请先认证上一步");
//        }
        if (model1 != null && model1.getIsID2() != 1) {
            FirstCerActivity_.intent(NecessaryCerActivity.this).tag(2).startForResult(3000);
            return;
        }
        AlipayCerActivity_.intent(this).startForResult(3000);
    }

    /**
     * 支付宝
     */
    @Click
    void zhifubaoCer() {
        if (model1 != null && model1.getIsID2() != 1) {
            FirstCerActivity_.intent(NecessaryCerActivity.this).tag(2).startForResult(100);
            return;
        }
        AlipayCerActivity_.intent(this).startForResult(3000);
    }

    /**
     * 芝麻信用
     */
    @Click
    void cer_zmxy() {
        if (model1.getIsPay() == 1) {
            if (model1.getIsZM() == 0) {
                ZMActivity_.intent(this).startForResult(3000);
            }
        } else {
            showButtomToast("请先认证上一步");
        }
//        if (model1.getIsPay() == 1) {
//            FirstCerActivity_.intent(this).tag(1).startForResult(3000);
//        } else {
//            showButtomToast("请先认证上一步");
//        }
    }

    @Click
    void zmxyCer() {
        //芝麻信用认证
        if (model1.getIsPay() == 1) {
            if (model1.getIsZM() == 0) {
                ZMActivity_.intent(this).startForResult(3000);
            }
        } else {
            showButtomToast("请先认证上一步");
        }
    }

    /**
     * 紧急联系人
     */
    @Click
    void cer_info() {
        PermissionUtils.requestPermissions(this, 310, new String[]{Manifest.permission.GET_ACCOUNTS}, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (model1.getIsZM() == 1) {
                    ContactPersonActivity_.intent(mContext).startForResult(3000);
                } else {
                    showButtomToast("请先认证上一步");
                }
            }

            @Override
            public void onPermissionDenied() {
                showButtomToast("读取手机通讯录权限未打开!");
            }
        });
    }

    /**
     * 紧急联系人
     */
    @Click
    void perInfoCer() {
        PermissionUtils.requestPermissions(this, 310, new String[]{Manifest.permission.GET_ACCOUNTS}, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                //紧急联系人认证
                if (model1.getIsZM() == 1) {
                    ContactPersonActivity_.intent(mContext).startForResult(3000);
                } else {
                    showButtomToast("请先认证上一步");
                }
            }

            @Override
            public void onPermissionDenied() {
                showButtomToast("读取手机通讯录权限未打开!");
            }
        });

    }

    /**
     * 身份证
     */
    @Click
    void cer_sfz() {
        if (model1.getIsContact() == 1) {
            IDPhotoActivity_.intent(this).startForResult(3000);
        } else {
            showButtomToast("请先认证上一步");
        }
    }

    /**
     * 身份证
     */
    @Click
    void sfzCer() {
        //身份证认证
//        IDPhotoActivity_.intent(this).startForResult(3000);
        if (model1.getIsContact() == 1) {
            IDPhotoActivity_.intent(this).startForResult(3000);
        } else {
            showButtomToast("请先认证上一步");
        }
    }

    /**
     * 手机号
     */
    @Click
    void cer_phone() {
        PermissionUtils.requestPermissions(this, 310, new String[]{Manifest.permission.READ_SMS,
                Manifest.permission.READ_CALL_LOG, Manifest.permission.GET_ACCOUNTS}, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (model1.getIsID() == 1) {
                    PhoneActivity_.intent(mContext).startForResult(3000);
                } else {
                    showButtomToast("请先认证上一步");
                }
            }

            @Override
            public void onPermissionDenied() {
                showButtomToast("权限未打开!");
            }
        });


    }

    /**
     * 手机号
     */
    @Click
    void phoneCer() {
        //手机号码认证
//        PhoneActivity_.intent(this).startForResult(3000);
        PermissionUtils.requestPermissions(this, 310, new String[]{Manifest.permission.READ_SMS,
                Manifest.permission.READ_CALL_LOG, Manifest.permission.GET_ACCOUNTS}, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (model1.getIsID() == 1) {
                    PhoneActivity_.intent(mContext).startForResult(3000);
                    PhoneActivity_.intent(mContext).startForResult(3000);
                } else {
                    showButtomToast("请先认证上一步");
                }
            }

            @Override
            public void onPermissionDenied() {
                showButtomToast("权限未打开!");
            }
        });


    }

//    /**
//     * 银行卡
//     */
//    @Click
//    void cer_yhk() {
//        if (model1.getIsMobile() == 1) {
//            BankActivity_.intent(this).startForResult(3000);
//        } else {
//            showButtomToast("请先认证上一步");
//        }
//    }

    /**
     * 银行卡
     */
    @Click
    void yhkCer() {
        //银行卡认证
//        CardActivity_.intent(this).startForResult(3000);
//        if (model1.getIsMobile() == 1) {
//            BankActivity_.intent(this).startForResult(3000);
//        } else {
//            showButtomToast("请先认证上一步");
//        }
    }

    @Click
    void btn_queding() {
        //我要借款
        StartBorrowingsActivity_.intent(this).start();
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    public void onEvent(UserEvent.changeStatus status) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 只需要调用这一句，剩下的 AndPermission 自动完成。
        if (requestCode == 310) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //permission was granted, yay! Do the contacts-related task you need to do.
                //这里进行授权被允许的处理

                GetContactsInfoF getContactsInfoF = new GetContactsInfoF(this);
                if (getContactsInfoF.getLocalContactsInfos() == null || getContactsInfoF.getLocalContactsInfos().size() == 0) {
                    if (promptDialog == null) {
                        promptDialog = new PromptDialog(this);
                        promptDialog.show();
                    } else {
                        promptDialog.show();
                    }
                } else {
                    PhoneActivity_.intent(this).startForResult(3000);
                }
            } else {
                if (promptDialog == null) {
                    promptDialog = new PromptDialog(NecessaryCerActivity.this);
                    promptDialog.show();
                } else {
                    promptDialog.show();
                }
            }
        }
    }

    private PromptDialog promptDialog;
}
