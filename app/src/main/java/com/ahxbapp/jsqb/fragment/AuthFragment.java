package com.ahxbapp.jsqb.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseFragment;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.activity.ContactPersonActivity_;
import com.ahxbapp.jsqb.activity.FirstCerActivity_;
import com.ahxbapp.jsqb.activity.IDPhotoActivity_;
import com.ahxbapp.jsqb.activity.WebActivity_;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.dialog.LoadDialog;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.model.ContactsInfo;
import com.ahxbapp.jsqb.utils.GetContactsInfo1;
import com.ahxbapp.jsqb.utils.PermissionUtils;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.List;

import cn.fraudmetrix.octopus.aspirit.main.OctopusManager;
import cn.fraudmetrix.octopus.aspirit.main.OctopusTaskCallBack;

/**
 * Created by Jayzhang on 16/10/17.
 */
@EFragment(R.layout.fragment_auth)
public class AuthFragment extends BaseFragment implements View.OnClickListener {

    @ViewById
    TextView tvTitle, tvContent;
    @ViewById
    Button btAuthName, btAuthContacts, btAuthPhone, btAuthTB;


    private Context mContext;

    // 认证
    private AuthenModel authenModel;

    private List<ContactsInfo> contactsInfoList;

    @AfterViews
    void init() {
        //创建/打开 数据库
        mContext = getContext();
        btAuthName.setOnClickListener(this);
        btAuthContacts.setOnClickListener(this);
        btAuthPhone.setOnClickListener(this);
        btAuthTB.setOnClickListener(this);

        GetContactsInfo1 getContactsInfo = new GetContactsInfo1(mContext);
        //获取通讯录好友列表ContactsInfoList
        contactsInfoList = getContactsInfo.getLocalContactsInfos();
    }


    public void loadView() {
        // 身份证认证  实名认证
        if (authenModel.getIsID() == 1) {
            Drawable top = getResources().getDrawable(R.mipmap.img_auth_id_confirm);
            btAuthName.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            btAuthName.setText("已认证");
            btAuthName.setTextColor(getResources().getColor(R.color.black));
        }
        // 联系人认证
        if (authenModel.getIsContact() == 1) {
            Drawable top = getResources().getDrawable(R.mipmap.img_auth_lianxiren_confirm);
            btAuthContacts.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            btAuthContacts.setText("已认证");
            btAuthContacts.setTextColor(getResources().getColor(R.color.black));
        }
        // 手机号
        if (authenModel.getIsMobile() == 1) {
            Drawable top = getResources().getDrawable(R.mipmap.img_auth_phone_confirm);
            btAuthPhone.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            btAuthPhone.setText("已认证");
            btAuthPhone.setTextColor(getResources().getColor(R.color.black));
        }
        //  淘宝
        if (authenModel.getIsTabao() == 1) {
            Drawable top = getResources().getDrawable(R.mipmap.img_auth_tb_confirm);
            btAuthTB.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
            btAuthTB.setText("已认证");
            btAuthTB.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onClick(View v) {
        if (authenModel == null) {
            showMiddleToast("数据异常，请重新进入。");
            requestAuthenStatus();
            return;
        } else if (authenModel.getIsID2() == 0) {
            FirstCerActivity_.intent(mContext).tag(2).startForResult(100);
            return;
        }
        switch (v.getId()) {
            //  个人认证
            case R.id.btAuthName:
                if (authenModel.getIsID() == 1) {
                    return;
                }
                if (authenModel.getIsID() == 0)
                    IDPhotoActivity_.intent(mContext).type(0).startForResult(100);
                break;
            case R.id.btAuthContacts://联系人
                if (authenModel.getIsContact() == 1) {
                    return;
                }
                if (authenModel.getIsID() == 1) {
                    checkPermission();
                } else {
                    showButtomToast("请先认证上一步");
                }
                break;
            case R.id.btAuthPhone://手机号
                if (authenModel.getIsMobile() == 1) {
                    return;
                }
                if (authenModel.getIsContact() == 1) {
                    if (contactsInfoList == null) {
                        showButtomToast("请打开获取手机通讯录权限!");
                        return;
                    }
                    showLoad();
                    updateDirectory();
                } else {
                    showButtomToast("请先认证上一步");
                }
                break;
            case R.id.btAuthTB://淘宝
                if (authenModel.getIsTabao() == 1) {
                    return;
                }
                if (authenModel.getIsMobile() == 1) {
//                    IDPhotoActivity_.intent(mContext).type(1).startForResult(100);
                    OctopusManager.getInstance().getChannel((Activity) mContext, "005003", null, new OctopusTaskCallBack() {
                        @Override
                        public void onCallBack(int code, String taskId) {
                            String msg = "success:";
                            if (code == 0) {//code
                                //String taskId = data.getStringExtra(OctopusConstants.OCTOPUS_TASK_RESULT_TASKID);//
                                requestAuthenStatus();
                            } else {
                                showMiddleToast("认证失败，请重新认证");
                            }
                        }
                    });
                } else {
                    showButtomToast("请先认证上一步");
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestAuthenStatus();
    }

    /**
     * 获取认证状态
     */
    private void requestAuthenStatus() {
        showDialogLoading();
        APIManager.getInstance().home_getAuthenStatus(mContext, 0, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object object) {
                authenModel = (AuthenModel) object;
                hideProgressDialog();
                if (authenModel != null) {
                    loadView();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    /**
     * 判断权限
     */
    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.GET_ACCOUNTS};
        PermissionUtils.requestPermissions(mContext, 310, permissions, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                ContactPersonActivity_.intent(mContext).startForResult(100);
            }

            @Override
            public void onPermissionDenied() {
                showButtomToast("请打开获取手机通讯录权限!");
            }
        });
    }


    private void updateDirectory() {
        String directory = new Gson().toJson(contactsInfoList);
        APIManager.getInstance().getPhoneDirectory(mContext, directory, "", "", new APIManager.APIManagerInterface.getContact_back() {
            @Override
            public void Success(Context context, int result, String message) {
                requestMobile(0);
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                loadDialog.dismiss();
            }
        });
    }

    private LoadDialog loadDialog;

    private void showLoad() {
        if (loadDialog == null) {
            loadDialog = new LoadDialog(mContext);
        }
        loadDialog.startAnmation();
        loadDialog.show();
    }

    //获取手机号认证的网页链接  0 个人 1 担保人
    private void requestMobile(int type) {
        APIManager.getInstance().requestMobile(mContext, type, new APIManager.APIManagerInterface.requestMobile() {
            @Override
            public void Success(Context context, String url) {
                loadDialog.dismiss();
                WebActivity_.intent(mContext).url(url).title("手机号认证").startForResult(100);
            }

            @Override
            public void Failure(Context context, String msg) {
                loadDialog.dismiss();
            }
        });
    }
}
