package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ahxbapp.jsqb.model.PersonModel;
import com.ahxbapp.jsqb.utils.DeviceUtil;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.wheel.WheelView;
import com.ahxbapp.jsqb.wheel.adapters.ArrayWheelAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人详细信息  (暂未用到)
 */
@EActivity(R.layout.activity_personal_details)
public class PersonalDetailsActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, tv_cancel1, tv_ok1, mEducationTV, mTimeTV, mChildTV, mMarryTV, mJobChooseTV, mContactChooseTV;
    @ViewById
    RelativeLayout mJobRL, mContactRL, mTimeRL, mChildRL, mMarriageRL;
    @ViewById
    WheelView wheel_bank;
    @ViewById
    LinearLayout lin_area1;
    @ViewById
    Button mSubmitBN;
    @ViewById
    EditText mQQET, mEmailET, mAddressET;
    @ViewById
    View mView1;


    private TextView textView;
    private List<BankModel> bankModels = new ArrayList<>();  //学历
    private List<BankModel> childModels = new ArrayList<>();  //子女
    private List<BankModel> timeModels = new ArrayList<>();  //居住时长
    private List<BankModel> marryModels = new ArrayList<>();  //婚姻
    private List<BankModel> model = new ArrayList<>();

    private PersonModel personModels; //个人信息

    private int eduID;
    private int marID;
    private int chID;
    private int tID;

    private int flag;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("个人信息");


        //个人信息显示
        loadPerson();
        //获取学历
        loadEducation();
        //获取子女
        loadChild();
        //获取居住时长
        loadAddressTime();
        //获取婚姻
        loadMarriage();

    }

    //个人信息显示
    void loadPerson() {
        showDialogLoading();
        APIManager.getInstance().personMessage(this, APIManager.person_message_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseModel<PersonModel> personModel = (BaseModel<PersonModel>) model;
                if (personModel.getResult() == 1) {
                    personModels = personModel.getData();
                    mQQET.setText(personModels.getQQ());
                    mEmailET.setText(personModels.getEmail());
                    mEducationTV.setText(personModels.getEduName());
                    if (personModels.getIsMarry() == 0) {
                        mMarryTV.setText("未婚");
                    } else if (personModels.getIsMarry() == 1) {
                        mMarryTV.setText("已婚");
                    } else {
                        mMarryTV.setText("离异");
                    }
                    mChildTV.setText(personModels.getChildNum());
                    mTimeTV.setText(personModels.getLiveName());
                    mAddressET.setText(personModels.getAddr());

                    if (personModels.getIsPost() == 1) {
                        mJobChooseTV.setText("");
                        mJobChooseTV.setBackgroundResource(R.mipmap.btn_selected);
                    } else {
                        mJobChooseTV.setText("请选择");
                    }

                    if (personModels.getIsContact() == 1) {
                        mContactChooseTV.setText("");
                        mContactChooseTV.setBackgroundResource(R.mipmap.btn_selected);
                    } else {
                        mContactChooseTV.setText("请选择");
                    }

                    eduID = personModels.getEduID();
                    marID = personModels.getIsMarry();
                    chID = personModels.getChildNumID();
                    tID = personModels.getLiveID();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    //获取学历
    void loadEducation() {
        APIManager.getInstance().bank_list(this, APIManager.education_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<BankModel> bankModel = (BaseDataListModel<BankModel>) model;
                if (bankModel.getResult() == 1) {
                    bankModels.clear();
                    bankModels.addAll(bankModel.getDatalist());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //获取婚姻
    void loadMarriage() {
        marryModels.clear();
        String[] str = new String[]{"未婚", "已婚", "离异"};
        for (int i = 0; i < str.length; i++) {
            BankModel model = new BankModel();
            model.setID(i);
            model.setName(str[i]);
            marryModels.add(model);
        }
    }

    //获取子女
    void loadChild() {
        APIManager.getInstance().bank_list(this, APIManager.child_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<BankModel> bankModel = (BaseDataListModel<BankModel>) model;
                if (bankModel.getResult() == 1) {
                    childModels.clear();
                    childModels.addAll(bankModel.getDatalist());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //获取居住时长
    void loadAddressTime() {
        APIManager.getInstance().bank_list(this, APIManager.address_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<BankModel> bankModel = (BaseDataListModel<BankModel>) model;
                if (bankModel.getResult() == 1) {
                    timeModels.clear();
                    timeModels.addAll(bankModel.getDatalist());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {


            }
        });
    }

    @Click({R.id.mToolbarLeftIB, R.id.mEducationRL, R.id.mMarriageRL, R.id.mJobRL, R.id.mContactRL, R.id.tv_ok1,
            R.id.tv_cancel1, R.id.mTimeRL, R.id.mChildRL, R.id.mSubmitBN})
    void personalClick(View view) {
        switch (view.getId()) {
            //TODO 返回上一页面
            case R.id.mToolbarLeftIB:
                finish();
                break;

            //TODO 学历
            case R.id.mEducationRL:
                flag = 0;
                mSubmitBN.setVisibility(View.GONE);
                mView1.setOnClickListener(null);
                DeviceUtil.hideSoftInput(mQQET, this);
                if (bankModels != null && bankModels.size() > 0) {
                    String[] strs = new String[bankModels.size()];
                    int inde = 0;
                    for (BankModel m : bankModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }
                    Log.e("Success", strs.length + "");
                    textView = mEducationTV;
                    model = bankModels;
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(PersonalDetailsActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                }
                break;

            //TODO 居住时长
            case R.id.mTimeRL:
                flag = 3;
                mSubmitBN.setVisibility(View.GONE);
                mView1.setOnClickListener(null);
                DeviceUtil.hideSoftInput(mQQET, this);
                if (timeModels != null && timeModels.size() > 0) {
                    String[] strs = new String[timeModels.size()];
                    int inde = 0;
                    for (BankModel m : timeModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }
                    Log.e("Success", strs.length + "");
                    textView = mTimeTV;
                    model = timeModels;
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(PersonalDetailsActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                }
                break;

            //TODO 获取子女
            case R.id.mChildRL:
                flag = 2;
                mSubmitBN.setVisibility(View.GONE);
                mView1.setOnClickListener(null);
                DeviceUtil.hideSoftInput(mQQET, this);
                if (childModels != null && childModels.size() > 0) {
                    String[] strs = new String[childModels.size()];
                    int inde = 0;
                    for (BankModel m : childModels) {
                        strs[inde] = m.getNum() + "";
                        inde++;
                    }
                    Log.e("Success", strs.length + "");
                    textView = mChildTV;
                    model = childModels;
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(PersonalDetailsActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.tv_ok1:
                lin_area1.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                BankModel bankModel = model.get(wheel_bank.getCurrentItem());

                if (flag == 0) {
                    eduID = bankModel.getID();
                    bankModel.setName(bankModel.getName());
                } else if (flag == 1) {
                    marID = bankModel.getID();
                    bankModel.setName(bankModel.getName());
                } else if (flag == 2) {
                    chID = bankModel.getID();
                    bankModel.setName(bankModel.getName());
                } else if (flag == 3) {
                    tID = bankModel.getID();
                    bankModel.setName(bankModel.getName());
                }

                if (bankModel.getName() == null) {
                    textView.setText(bankModel.getNum() + "");
                } else {
                    textView.setText(bankModel.getName());
                }

                break;

            case R.id.tv_cancel1:
                lin_area1.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                break;

            //TODO 婚姻
            case R.id.mMarriageRL:
                flag = 1;
                mSubmitBN.setVisibility(View.GONE);
                mView1.setOnClickListener(null);
                DeviceUtil.hideSoftInput(mQQET, this);
                if (marryModels != null && marryModels.size() > 0) {
                    String[] strs = new String[marryModels.size()];
                    int inde = 0;
                    for (BankModel m : marryModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }
                    Log.e("Success", strs.length + "");
                    textView = mMarryTV;
                    model = marryModels;
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(PersonalDetailsActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                }
                break;

            //TODO 职位信息
            case R.id.mJobRL:
                JobActivity_.intent(this).startForResult(1000);
                break;

            //TODO 紧急联系人
            case R.id.mContactRL:
                ContactPersonActivity_.intent(this).startForResult(2000);
                break;

            //TODO 提交
            case R.id.mSubmitBN:
                loadSubmit();

                break;
            default:
                break;
        }
    }

    @OnActivityResult(1000)
    void jobResult(int requestCode) {
        if (requestCode == RESULT_OK) {
            mJobChooseTV.setText("");
            mJobChooseTV.setBackgroundResource(R.mipmap.btn_selected);
        }
    }

    @OnActivityResult(2000)
    void contactResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            mContactChooseTV.setText("");
            mContactChooseTV.setBackgroundResource(R.mipmap.btn_selected);
        }
    }

    //提交
    void loadSubmit() {
        String qq = mQQET.getText().toString().trim();
        String email = mEmailET.getText().toString().trim();
        String education = mEducationTV.getText().toString().trim();
        String marry = mMarryTV.getText().toString().trim();
        String child = mChildTV.getText().toString().trim();
        String addr = mAddressET.getText().toString().trim();
        String time = mTimeTV.getText().toString().trim();

        if (TextUtils.isEmpty(qq)) {
            showButtomToast("请输入QQ号码！");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            showButtomToast("请输入电子邮箱！");
            return;
        }

        if (!MatchUtil.isEmail(email)) {
            showButtomToast("请输入正确的电子邮箱！");
            return;
        }

        if (TextUtils.isEmpty(education)) {
            showButtomToast("请选择学历！");
            return;
        }

        if (TextUtils.isEmpty(marry)) {
            showButtomToast("请选择婚姻！");
            return;
        }

        if (TextUtils.isEmpty(child)) {
            showButtomToast("请选择子女个数！");
            return;
        }

        if (TextUtils.isEmpty(addr)) {
            showButtomToast("请输入常驻地址！");
            return;
        }

        if (TextUtils.isEmpty(time)) {
            showButtomToast("请选择居住时长！");
            return;
        }

        if (mJobChooseTV.getText().length() != 0) {
            showButtomToast("请选择联系人");
            return;
        }

        if (mContactChooseTV.getText().length() != 0) {
            showButtomToast("请选择职位信息");
            return;
        }

        APIManager.getInstance().person_ver(this, qq, email, eduID, marID, chID, addr, tID,
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
}
