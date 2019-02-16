package com.ahxbapp.jsqb.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseFragment;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.activity.AccountActivity_;
import com.ahxbapp.jsqb.activity.AlipayCerActivity_;
import com.ahxbapp.jsqb.activity.ContactPersonActivity_;
import com.ahxbapp.jsqb.activity.FirstCerActivity_;
import com.ahxbapp.jsqb.activity.IDPhotoActivity_;
import com.ahxbapp.jsqb.activity.LoginActivity_;
import com.ahxbapp.jsqb.activity.PhoneActivity_;
import com.ahxbapp.jsqb.activity.TaobaoCerActivity_;
import com.ahxbapp.jsqb.activity.ZMActivity_;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.dialog.PromptDialog;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.utils.GetContactsInfoF;
import com.ahxbapp.jsqb.utils.MyToast;
import com.ahxbapp.jsqb.utils.PrefsUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jayzhang on 16/10/17.
 * 认证
 */
@EFragment(R.layout.activity_certification)
public class CertificationFragment extends BaseFragment implements View.OnClickListener {

    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    Button btn_xuexin, btn_shenfenzheng, btn_zhifubao, btn_taobao, btn_yinhangka, btn_shouji, btn_zhimafeng, btn_lianxiren;
    @ViewById
    TextView tvAliPay, tvZMXY, tvContent, tvId, tvPhone, tvBank;
    boolean isLoan = false;
    private AuthenModel model1 = new AuthenModel();

    @AfterViews
    void init() {
        mToolbarTitleTV.setText("认证");
        btn_xuexin.setOnClickListener(this);
        btn_shenfenzheng.setOnClickListener(this);
        btn_zhifubao.setOnClickListener(this);
        btn_taobao.setOnClickListener(this);
        btn_lianxiren.setOnClickListener(this);
//        btn_yinhangka.setOnClickListener(this);
        btn_shouji.setOnClickListener(this);
        btn_zhimafeng.setOnClickListener(this);
        loadView();
    }

    public void loadView() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (token == null) {
            Drawable top1 = getResources().getDrawable(R.mipmap.list_authentication_1);
            btn_xuexin.setCompoundDrawablesWithIntrinsicBounds(null, top1, null, null);
            Drawable top2 = getResources().getDrawable(R.mipmap.list_authentication_2);
            btn_shenfenzheng.setCompoundDrawablesWithIntrinsicBounds(null, top2, null, null);
            Drawable top3 = getResources().getDrawable(R.mipmap.list_authentication_3);
            btn_zhifubao.setCompoundDrawablesWithIntrinsicBounds(null, top3, null, null);
            Drawable top4 = getResources().getDrawable(R.mipmap.list_authentication_4);
            btn_zhimafeng.setCompoundDrawablesWithIntrinsicBounds(null, top4, null, null);
            Drawable top5 = getResources().getDrawable(R.mipmap.list_authentication_5);
            btn_taobao.setCompoundDrawablesWithIntrinsicBounds(null, top5, null, null);
            Drawable top6 = getResources().getDrawable(R.mipmap.list_authentication_6);
            btn_yinhangka.setCompoundDrawablesWithIntrinsicBounds(null, top6, null, null);
            Drawable top7 = getResources().getDrawable(R.mipmap.list_authentication_7);
            btn_shouji.setCompoundDrawablesWithIntrinsicBounds(null, top7, null, null);
            Drawable top = getResources().getDrawable(R.mipmap.img_lianxiren);
            btn_lianxiren.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);

            tvAliPay.setText("未认证");
            tvZMXY.setText("未认证");
            tvContent.setText("未认证");
            tvId.setText("未认证");
            tvPhone.setText("未认证");
            tvBank.setText("未认证");

            tvAliPay.setTextColor(getResources().getColor(R.color.btn_gray));
            tvZMXY.setTextColor(getResources().getColor(R.color.btn_gray));
            tvContent.setTextColor(getResources().getColor(R.color.btn_gray));
            tvId.setTextColor(getResources().getColor(R.color.btn_gray));
            tvPhone.setTextColor(getResources().getColor(R.color.btn_gray));
            tvBank.setTextColor(getResources().getColor(R.color.btn_gray));
            return;
        }
        showDialogLoading();
        APIManager.getInstance().home_getAuthenStatus(getActivity(), 0, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                model1 = (AuthenModel) model;
                if (model1.getIsLNet() == 1) {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_1color);
                    btn_xuexin.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                } else {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_1);
                    btn_xuexin.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                }
//tvAliPay, , , tvId, tvPhone, tvBank
                if (model1.getIsID() == 1) {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_2color);
                    btn_shenfenzheng.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvId.setText("已认证");
                    tvId.setTextColor(getResources().getColor(R.color.nav_blue));
                } else {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_2);
                    btn_shenfenzheng.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvId.setText("未认证");
                    tvId.setTextColor(getResources().getColor(R.color.btn_gray));
                }
                if (model1.getIsPay() == 1) {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_3color);
                    btn_zhifubao.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvAliPay.setText("已认证");
                    tvAliPay.setTextColor(getResources().getColor(R.color.nav_blue));
                } else {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_3);
                    btn_zhifubao.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvAliPay.setText("未认证");
                    tvAliPay.setTextColor(getResources().getColor(R.color.btn_gray));
                }
                if (model1.getIsZM() == 1) {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_4color);
                    btn_zhimafeng.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvZMXY.setText("已认证");
                    tvZMXY.setTextColor(getResources().getColor(R.color.nav_blue));
                } else {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_4);
                    btn_zhimafeng.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvZMXY.setText("未认证");
                    tvZMXY.setTextColor(getResources().getColor(R.color.btn_gray));
                }
                if (model1.getIsContact() == 1) {
                    Drawable top = getResources().getDrawable(R.mipmap.img_lianxiren_selected);
                    btn_lianxiren.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvContent.setText("已认证");
                    tvContent.setTextColor(getResources().getColor(R.color.nav_blue));
                } else {
                    Drawable top = getResources().getDrawable(R.mipmap.img_lianxiren);
                    btn_lianxiren.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvContent.setText("未认证");
                    tvContent.setTextColor(getResources().getColor(R.color.btn_gray));
                }
                if (model1.getIsTabao() == 1) {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_5color);
                    btn_taobao.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                } else {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_5);
                    btn_taobao.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                }
                if (model1.getIsBank() == 1) {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_6color);
                    btn_yinhangka.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvBank.setText("已认证");
                    tvBank.setTextColor(getResources().getColor(R.color.nav_blue));
                } else {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_6);
                    btn_yinhangka.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvBank.setText("未认证");
                    tvBank.setTextColor(getResources().getColor(R.color.btn_gray));
                }
                if (model1.getIsMobile() == 1) {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_7color);
                    btn_shouji.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvPhone.setTextColor(getResources().getColor(R.color.nav_blue));
                    tvPhone.setText("已认证");
                } else {
                    Drawable top = getResources().getDrawable(R.mipmap.list_authentication_7);
                    btn_shouji.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                    tvPhone.setText("未认证");
                    tvPhone.setTextColor(getResources().getColor(R.color.btn_gray));
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });

        //判断是否有未完成的订单
        showDialogLoading();
        APIManager.getInstance().Member_isNoComplate(getActivity(), new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    int result = response.getInt("result");
                    if (result == 1) {
                        int loanID = response.getInt("LoanlogID");
                        if (loanID > 0) {
                            isLoan = true;
                        } else {
                            isLoan = false;
                        }
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

    @OnActivityResult(1000)
    void LoginActivity() {
        loadView();
    }

    //点击
    @Override
    public void onClick(View v) {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (token == null) {
            LoginActivity_.intent(this).startForResult(1000);
            return;
        }
        if (model1 != null && model1.getIsID2() != 1) {
            FirstCerActivity_.intent(CertificationFragment.this).tag(2).startForResult(100);
            return;
        }
        if (isLoan == true) {
            MyToast.showToast(getActivity(), "您有未完成的借款订单,暂时不能更改认证信息!");
            return;
        }
        switch (v.getId()) {
            case R.id.btn_xuexin: //学信网
                AccountActivity_.intent(this).flag(1).startForResult(100);
                break;
            case R.id.btn_taobao: //淘宝
                if (model1.getIsBank() == 1) {
                    TaobaoCerActivity_.intent(this).startForResult(100);
                } else {
                    showButtomToast("请先去借款界面认证基本信息");
                }
                break;
            case R.id.btn_zhifubao: //支付宝
                AlipayCerActivity_.intent(this).startForResult(100);
                break;
            case R.id.btn_zhimafeng: //芝麻信用
                if (model1.getIsPay() == 1) {
                    if (model1.getIsZM() == 0) {
                        ZMActivity_.intent(this).startForResult(100);
                    }
                } else {
                    showButtomToast("请先认证上一步");
                }
//                if (model1.getIsZM() != 1) {
//                    FirstCerActivity_.intent(this).tag(1).startForResult(100);
//                } else {
//                    ZMActivity_.intent(this).start();
//                }

//                if (model1.getIsPay() == 1) {
//                    FirstCerActivity_.intent(this).tag(1).startForResult(100);
//                } else {
//                    showButtomToast("请先认证上一步");
//                }
                break;
            case R.id.btn_lianxiren: //联系人
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    showButtomToast("读取手机通讯录权限未打开!");
                    return;
                }
                if (model1.getIsZM() == 1) {
                    ContactPersonActivity_.intent(this).startForResult(100);
                } else {
                    showButtomToast("请先认证上一步");
                }
                break;
            case R.id.btn_shenfenzheng: //身份证
                if (model1.getIsContact() == 1) {
                    IDPhotoActivity_.intent(this).startForResult(100);
                } else {
                    showButtomToast("请先认证上一步");
                }
                break;
            case R.id.btn_shouji: //手机号码
//                PhoneActivity_.intent(this).startForResult(100);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    showButtomToast("读取短信权限未打开!");
                    return;
                }
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    showButtomToast("读取手机通话记录权限未打开!");
                    return;
                }
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    showButtomToast("读取手机通讯录权限未打开!");
                    return;
                }
                if (model1.getIsID() == 1) {
                    PhoneActivity_.intent(this).startForResult(100);
//                    try {
//                        CertificationFragment.this.requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, 310);
//                    } catch (Exception e) {
//                        e.getMessage();
//                    }
                } else {
                    showButtomToast("请先认证上一步");
                }
                break;
//            case R.id.btn_yinhangka: //银行卡
//                if (model1.getIsMobile() == 1) {
//                    BankActivity_.intent(this).startForResult(100);
//                } else {
//                    showButtomToast("请先认证上一步");
//                }
//                break;
        }
    }

    @OnActivityResult(100)
    void AccountActivity() {
        loadView();
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
                GetContactsInfoF getContactsInfoF = new GetContactsInfoF(getContext());
                if (getContactsInfoF.getLocalContactsInfos() == null || getContactsInfoF.getLocalContactsInfos().size() == 0) {
                    if (promptDialog == null) {
                        promptDialog = new PromptDialog(getContext());
                        promptDialog.show();
                    } else {
                        promptDialog.show();
                    }
                } else {
                    PhoneActivity_.intent(this).startForResult(100);
                }
            } else {
                if (promptDialog == null) {
                    promptDialog = new PromptDialog(getContext());
                    promptDialog.show();
                } else {
                    promptDialog.show();
                }
            }
        }
    }

    private PromptDialog promptDialog;
}
