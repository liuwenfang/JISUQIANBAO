package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.BankModel;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.BaseModel;
import com.ahxbapp.jsqb.model.CommonEnity;
import com.ahxbapp.jsqb.model.ContactModel;
import com.ahxbapp.jsqb.model.ContactsInfo;
import com.ahxbapp.jsqb.utils.DeviceUtil;
import com.ahxbapp.jsqb.utils.GetContactsInfo;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.wheel.WheelView;
import com.ahxbapp.jsqb.wheel.adapters.ArrayWheelAdapter;
import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 联系人认证
 */
@EActivity(R.layout.activity_contact2)
public class ContactPersonActivity extends BaseActivity {
    /**
     * 联系人
     */
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, tv_cancel1, tv_ok1, mFamilyTV, mSocialTV;

    @ViewById
    RelativeLayout mFamilyRL, mSocialRL;
    @ViewById
    LinearLayout lin_area1;
    @ViewById
    WheelView wheel_bank;
    @ViewById
    TextView mPhoneET, shET;
    @ViewById
    TextView mContactET, qsET;
    @ViewById
    Button mSubmitBN;
    @ViewById
    View mView;

    private TextView textView;
    private List<ContactsInfo> contactsInfoList;

    private List<BankModel> familyModels = new ArrayList<>();
    private List<BankModel> socialModels = new ArrayList<>();

    private List<BankModel> models = new ArrayList<>();
    private ContactModel contacts;

    private int fID;
    private int sID;

    private int flag;

    private int mResult; //判断通讯录是否获取到

    @AfterViews
    void init() {
        EventBus.getDefault().register(this);
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("联系人");

        //联系人显示
        loadContact();
        //获取亲属关系
        loadFamily();
        //获取社会关系
        loadSocial();
        //获取手机通讯录
//        initViews();
    }

    private void initViews() {
        GetContactsInfo getContactsInfo = new GetContactsInfo(this, this);
        //获取通讯录好友列表ContactsInfoList
        contactsInfoList = getContactsInfo.getLocalContactsInfos();
        for (ContactsInfo contactsInfo : contactsInfoList) {
            String name = contactsInfo.getName();
            String phone = contactsInfo.getPhone();
            Log.e("tag", "name==>" + name + "phone==>" + phone);
        }
        Log.e("tag", "通讯录==>" + contactsInfoList.size());

        if (contactsInfoList == null) {
            return;
        }
        //对象转为json
        String directory = new Gson().toJson(contactsInfoList);

        showDialogLoading();
//        APIManager.getInstance().getPhoneDirectory(this, directory, new APIManager.APIManagerInterface.getContact_back() {
//            @Override
//            public void Success(Context context, int result, String message) {
//                hideProgressDialog();
//                if (result == 1) {
//                    Log.e("tag", "获取通讯录成功");
//                    mResult = 1;
//                } else {
//                    showMiddleToast(message);
//                }
//            }
//
//            @Override
//            public void Failure(Context context, JSONObject response) {
//                hideProgressDialog();
//                mResult = 0;
//            }
//        });
    }

    //联系人显示信息
    void loadContact() {
        showDialogLoading();
        APIManager.getInstance().show_contact(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseModel<ContactModel> contactModel = (BaseModel<ContactModel>) model;
                if (contactModel.getResult() == 1) {
                    contacts = contactModel.getData();
                    mFamilyTV.setText(contacts.getRelaName());
                    mContactET.setText(contacts.getRelaMobile());
                    mSocialTV.setText(contacts.getSocName());
                    mPhoneET.setText(contacts.getSocMobile());
                    qsET.setText(contacts.getRelaUserName());
                    shET.setText(contacts.getSocUserName());
                    fID = contacts.getRelaID();
                    sID = contacts.getSocID();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    //获取亲属关系
    void loadFamily() {
        APIManager.getInstance().bank_list(this, APIManager.family_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<BankModel> bankModel = (BaseDataListModel<BankModel>) model;
                if (bankModel.getResult() == 1) {
                    familyModels.clear();
                    familyModels.addAll(bankModel.getDatalist());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //获取社会关系
    void loadSocial() {
        APIManager.getInstance().bank_list(this, APIManager.social_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<BankModel> bankModel = (BaseDataListModel<BankModel>) model;
                if (bankModel.getResult() == 1) {
                    socialModels.clear();
                    socialModels.addAll(bankModel.getDatalist());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //    private final int REQUEST_FAMILY = 310;
//    private final int REQUEST_SOCIAL = 826;
    private int tag = 0;

    @Click({R.id.mToolbarLeftIB, R.id.mFamilyRL, R.id.mSocialRL, R.id.qsET, R.id.shET,
            R.id.tv_ok1, R.id.tv_cancel1, R.id.mSubmitBN})
    void jobClick(View view) {
        switch (view.getId()) {
            case R.id.mToolbarLeftIB:
                finish();
                break;

            //TODO 获取亲属关系
            case R.id.mFamilyRL:
                DeviceUtil.hideSoftInput(mContactET, this);
                flag = 0;
                mSubmitBN.setVisibility(View.GONE);
                mView.setOnClickListener(null);
                if (familyModels != null && familyModels.size() > 0) {
                    String[] strs = new String[familyModels.size()];
                    int inde = 0;
                    for (BankModel m : familyModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }
//                    Log.e("Success", strs.length + "");
                    textView = mFamilyTV;
                    models = familyModels;
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(ContactPersonActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.qsET:
                tag = 1;
                MailListActivity_.intent(this).start();
                break;
            case R.id.shET:
                tag = 2;
                MailListActivity_.intent(this).start();
                break;

            //TODO 获取社会关系
            case R.id.mSocialRL:
                DeviceUtil.hideSoftInput(mContactET, this);
                flag = 1;
                mView.setOnClickListener(null);
                mSubmitBN.setVisibility(View.GONE);
                if (socialModels != null && socialModels.size() > 0) {
                    String[] strs = new String[socialModels.size()];
                    int inde = 0;
                    for (BankModel m : socialModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }
//                    Log.e("Success", strs.length + "");
                    textView = mSocialTV;
                    models = socialModels;
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(ContactPersonActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tv_ok1:
                lin_area1.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                BankModel bankModel = models.get(wheel_bank.getCurrentItem());
                if (flag == 0) {
                    fID = bankModel.getID();
                } else if (flag == 1) {
                    sID = bankModel.getID();
                }

                textView.setText(bankModel.getName());
                break;

            case R.id.tv_cancel1:
                mSubmitBN.setVisibility(View.VISIBLE);
                lin_area1.setVisibility(View.GONE);
                break;

            //TODO 提交
            case R.id.mSubmitBN:
                submitPerson();
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(CommonEnity enity) {
        if (enity.getType().equals("contactsInfo")) {
            contactsInfo = (ContactsInfo) enity.getData();
            if (tag == 1) {
                qsET.setText(contactsInfo.getName());
                mContactET.setText(contactsInfo.getPhone());
            } else if (tag == 2) {
                shET.setText(contactsInfo.getName());
                mPhoneET.setText(contactsInfo.getPhone());
            }
        }
    }


    private ContactsInfo contactsInfo;

    /**
     * 联系人认证
     */
    void submitPerson() {
        String family = mFamilyTV.getText().toString().trim();
        String fPhone = mContactET.getText().toString().trim();
        String social = mSocialTV.getText().toString().trim();
        String sPhone = mPhoneET.getText().toString().trim();
        String qsName = qsET.getText().toString().trim();
        String shName = shET.getText().toString().trim();

//        if (mResult != 1) {
//            showButtomToast("获取不到您的通讯录，请去设置-应用管理-极速钱包-权限管理-获取通讯录修改为允许！");
//            return;
//        }

        if (TextUtils.isEmpty(family)) {
            showButtomToast("请选择亲属关系！");
            return;
        }

        if (TextUtils.isEmpty(fPhone)) {
            showButtomToast("请选择亲属的手机号码！");
            return;
        }
        if (TextUtils.isEmpty(qsName)) {
            showButtomToast("请选择亲属的姓名！");
            return;
        }
        if (TextUtils.isEmpty(shName)) {
            showButtomToast("请选择联系人的姓名！");
            return;
        }
        if (!MatchUtil.isPhone(fPhone)) {
            showButtomToast("请选择正确的手机号码！");
            return;
        }

        if (TextUtils.isEmpty(social)) {
            showButtomToast("请选择社会关系！");
            return;
        }

        if (TextUtils.isEmpty(sPhone)) {
            showButtomToast("请选择手机号码！");
            return;
        }

        if (!MatchUtil.isPhone(sPhone)) {
            showButtomToast("请选择正确的手机号码！");
            return;
        }

//        BankModel familyModel = familyModels.get(wheel_bank.getCurrentItem());
//        BankModel socialModel = socialModels.get(wheel_bank.getCurrentItem());

        APIManager.getInstance().contact_ver(this, fID, fPhone, sID, sPhone, qsName, shName,
                new APIManager.APIManagerInterface.common_wordBack() {
                    @Override
                    public void Success(Context context, int result, String message) {
                        if (result == 1) {
                            setResult(RESULT_OK);
                            finish();
                        }
                        showButtomToast(message);
                    }

                    @Override
                    public void Failure(Context context, JSONObject response) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
