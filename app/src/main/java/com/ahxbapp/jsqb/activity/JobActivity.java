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
import com.ahxbapp.jsqb.model.JobMedel;
import com.ahxbapp.jsqb.model.Province;
import com.ahxbapp.jsqb.utils.DeviceUtil;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.wheel.OnWheelChangedListener;
import com.ahxbapp.jsqb.wheel.WheelView;
import com.ahxbapp.jsqb.wheel.adapters.ArrayWheelAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作认证
 */
@EActivity(R.layout.activity_job)
public class JobActivity extends BaseActivity implements OnWheelChangedListener {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV, mCityTV, tv_ok, tv_cancel, tv_ok1, tv_cancel1, mJobTV, mIncomeTV;
    @ViewById
    RelativeLayout mJobRL, mInComeRL, mCityRL;
    @ViewById
    LinearLayout lin_area, lin_area1, mUnitLL;

    @ViewById
    WheelView wheel_provice, wheel_city, wheel_bank;
    @ViewById
    Button mSubmitBN;
    @ViewById
    EditText mNameET, mAddressET, mPhoneET;
    @ViewById
    View mView, mView1;

    private TextView textView;

    private List<BankModel> jobModels = new ArrayList<>();  //职位
    private List<BankModel> incomeModels = new ArrayList<>();      //月收入
    private List<Province> provinceModels = new ArrayList<>(); //省市

    private List<BankModel> models = new ArrayList<>();

    private JobMedel jobMedel;

    private int pID;
    private int cID;

    private int jobID;
    private int incomeID;

    private int flag = 0;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("职位信息");

        //职位信息显示
        loadShowJob();
        //获取省市
        loadCity();
        //获取职位
        loadJob();
        //获取收入
        loadInCome();
        wheel_provice.addChangingListener(this);

    }

    //职位信息显示
    void loadShowJob() {
        showDialogLoading();
        APIManager.getInstance().show_job(this, APIManager.show_job_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseModel<JobMedel> jobMedels = (BaseModel<JobMedel>) model;
                if (jobMedels.getResult() == 1) {
                    jobMedel = jobMedels.getData();

                    mJobTV.setText(jobMedel.getPosName());
                    mIncomeTV.setText(jobMedel.getIncName());
                    mNameET.setText(jobMedel.getCompany());
                    mCityTV.setText(jobMedel.getProName() == null ? "" : jobMedel.getProName() + jobMedel.getCityName() == null ? "" : jobMedel.getCityName());
                    mAddressET.setText(jobMedel.getComAddr());
                    mPhoneET.setText(jobMedel.getComMobile());

                    jobID = jobMedel.getPosID();
                    incomeID = jobMedel.getIncID();
                    pID = jobMedel.getProID();
                    cID = jobMedel.getCityID();

                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }


    //获取职位
    void loadJob() {
        APIManager.getInstance().bank_list(this, APIManager.job_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<BankModel> bankModel = (BaseDataListModel<BankModel>) model;
                if (bankModel.getResult() == 1) {
                    jobModels.clear();
                    jobModels.addAll(bankModel.getDatalist());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //获取月收入
    void loadInCome() {
        APIManager.getInstance().bank_list(this, APIManager.income_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<BankModel> bankModel = (BaseDataListModel<BankModel>) model;
                if (bankModel.getResult() == 1) {
                    incomeModels.clear();
                    incomeModels.addAll(bankModel.getDatalist());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //获取开户省市
    void loadCity() {
        APIManager.getInstance().get_city(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<Province> models = (BaseDataListModel<Province>) model;
                if (models.getResult() == 1) {
                    provinceModels.clear();
                    provinceModels.addAll(models.getDatalist());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    @Click({R.id.mToolbarLeftIB, R.id.mInComeRL, R.id.mJobRL, R.id.mCityRL, R.id.tv_ok, R.id.tv_cancel, R.id.tv_ok1,
            R.id.tv_cancel1, R.id.mUnitLL, R.id.mNameET, R.id.mAddressET, R.id.mPhoneET, R.id.mSubmitBN})
    void jobClick(View view) {
        switch (view.getId()) {
            case R.id.mToolbarLeftIB:
                finish();
                break;

            //TODO 职位信息
            case R.id.mJobRL:
                flag = 0;
                wheel_bank.setCurrentItem(0);
                DeviceUtil.hideSoftInput(mAddressET, this);
                DeviceUtil.hideSoftInput(mNameET, this);
                DeviceUtil.hideSoftInput(mPhoneET, this);
                mSubmitBN.setVisibility(View.GONE);
                mView1.setOnClickListener(null);
                if (jobModels != null && jobModels.size() > 0) {
                    String[] strs = new String[jobModels.size()];
                    int inde = 0;
                    for (BankModel m : jobModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }

                    Log.e("Success", strs.length + "");
                    textView = mJobTV;
                    models = jobModels;
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(JobActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                    lin_area.setVisibility(View.GONE);
                }
                break;

            //TODO 月收入
            case R.id.mInComeRL:
                flag = 1;
                wheel_bank.setCurrentItem(0);
                DeviceUtil.hideSoftInput(mAddressET, this);
                DeviceUtil.hideSoftInput(mNameET, this);
                DeviceUtil.hideSoftInput(mPhoneET, this);
                mSubmitBN.setVisibility(View.GONE);
                mView1.setOnClickListener(null);
                if (incomeModels != null && incomeModels.size() > 0) {
                    String[] strs = new String[incomeModels.size()];
                    int inde = 0;
                    for (BankModel m : incomeModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }
                    Log.e("Success", strs.length + "");
                    textView = mIncomeTV;
                    models = incomeModels;
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(JobActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                    lin_area.setVisibility(View.GONE);
                }
                break;

            //TODO 省市
            case R.id.mCityRL:
                DeviceUtil.hideSoftInput(mAddressET, this);
                DeviceUtil.hideSoftInput(mNameET, this);
                DeviceUtil.hideSoftInput(mPhoneET, this);
                mSubmitBN.setVisibility(View.GONE);
                mView.setOnClickListener(null);
                if (provinceModels != null && provinceModels.size() > 0) {
                    String[] strs = new String[provinceModels.size()];
                    int inde = 0;
                    for (Province m : provinceModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }
                    Log.e("Success", strs.length + "");
                    Log.e("tv_ok", provinceModels.get(wheel_provice.getCurrentItem()).getAreaCity().size() + "");
                    wheel_provice.setViewAdapter(new ArrayWheelAdapter<String>(JobActivity.this, strs));
                    updataProvice();
                    lin_area.setVisibility(View.VISIBLE);
                    lin_area1.setVisibility(View.GONE);
                }
                break;

            case R.id.tv_ok:
                lin_area.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                Province provinceModel = provinceModels.get(wheel_provice.getCurrentItem());
                String pro = "";
                if (provinceModel != null) {
                    pro += provinceModel.getName() + " ";
                    pID = provinceModel.getID();
                    Province.AreaCity cityModel = provinceModels.get(wheel_provice.getCurrentItem()).getAreaCity().get(wheel_city.getCurrentItem());
                    if (cityModel != null) {
                        pro += cityModel.getName() + " ";
                        cID = cityModel.getID();
                    }
                }
                mCityTV.setText(pro == null ? "" : pro);
                break;

            case R.id.tv_cancel:
                lin_area.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_ok1:
                lin_area1.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                BankModel bankModel = models.get(wheel_bank.getCurrentItem());
                if (flag == 0) {
                    jobID = models.get(wheel_bank.getCurrentItem()).getID();
                } else if (flag == 1) {
                    incomeID = models.get(wheel_bank.getCurrentItem()).getID();
                }
                String name = "";
                if (bankModel != null) {
                    name += bankModel.getName() + " ";
                    textView.setText(name);
                }
                break;

            case R.id.tv_cancel1:
                lin_area1.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                break;

            case R.id.mUnitLL:
            case R.id.mNameET:
            case R.id.mAddressET:
            case R.id.mPhoneET:
                lin_area.setVisibility(View.GONE);
                lin_area1.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                break;

            case R.id.mSubmitBN:
                submitJob();
                break;
            default:
                break;
        }
    }

    void submitJob() {
        String job = mJobTV.getText().toString().trim();
        String income = mIncomeTV.getText().toString().trim();
        String name = mNameET.getText().toString().trim();
        String provice = mCityTV.getText().toString().trim();
        String addr = mAddressET.getText().toString().trim();
        String phone = mPhoneET.getText().toString().trim();

        if (TextUtils.isEmpty(job)) {
            showButtomToast("请选择职业！");
            return;
        }
        if (TextUtils.isEmpty(income)) {
            showButtomToast("请选择收入！");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            showButtomToast("请输入单位名称！");
            return;
        }
        if (TextUtils.isEmpty(provice)) {
            showButtomToast("请选择所属省市！");
            return;
        }
        if (TextUtils.isEmpty(addr)) {
            showButtomToast("请输入单位地址！");
            return;
        }

        if (!MatchUtil.isPhone(phone)) {
            if (!MatchUtil.isTelephone(phone)) {
                showButtomToast("请输入正确的单位电话！");
                return;
            }
        }
//        if (TextUtils.isEmpty(phone) ){
//            showButtomToast("请输入单位电话号码！");
//            return;
//        }
        Log.e("JobActivity", "--" + jobID + "" + "--" + incomeID + "--" + pID + "--" + cID);
        showDialogLoading();
        APIManager.getInstance().job_var(this, jobID, incomeID, name, pID, cID, addr, phone,
                new APIManager.APIManagerInterface.common_wordBack() {
                    @Override
                    public void Success(Context context, int result, String message) {
                        hideProgressDialog();
                        if (result == 1) {
                            setResult(RESULT_OK);
                            finish();
                        }
                        showButtomToast(message);
                    }

                    @Override
                    public void Failure(Context context, JSONObject response) {
                        hideProgressDialog();
                    }
                });
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wheel_provice) {
            updataProvice();
        }
        if (wheel == wheel_bank) {
            incomeID = incomeModels.get(wheel_bank.getCurrentItem()).getID();
        }
    }

    private void updataProvice() {
        String[] strs = new String[provinceModels.get(wheel_provice.getCurrentItem()).getAreaCity().size()];
        int inde = 0;
        for (Province.AreaCity model : provinceModels.get(wheel_provice.getCurrentItem()).getAreaCity()) {
            strs[inde] = model.getName();
            inde++;
        }
        wheel_city.setViewAdapter(new ArrayWheelAdapter<String>(JobActivity.this, strs));
        wheel_city.setCurrentItem(0);
    }
}
