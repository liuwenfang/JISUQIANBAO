package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.enter.SimpleTextWatcher;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.BankModel;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.PersonDataModel;
import com.ahxbapp.jsqb.model.Province;
import com.ahxbapp.jsqb.utils.DeviceUtil;
import com.ahxbapp.jsqb.utils.MatchUtil;
import com.ahxbapp.jsqb.wheel.OnWheelChangedListener;
import com.ahxbapp.jsqb.wheel.WheelView;
import com.ahxbapp.jsqb.wheel.adapters.ArrayWheelAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_card)
public class CardActivity extends BaseActivity implements OnWheelChangedListener {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    Button mSubmitBN;

    @ViewById
    TextView tv_cancel, tv_cancel1, tv_ok, tv_ok1, mBankTV, provice_TV;
    @ViewById
    WheelView wheel_provice, wheel_bank, wheel_city;

    @ViewById
    RelativeLayout bank_RL, bank_RL1;
    @ViewById
    LinearLayout lin_area1, lin_area;

    @ViewById
    EditText mMobileET, mCardET,mNameET;

    @ViewById
    View mView1, mView;

    @Extra
    int LoanID;

    String IDNumber="";
    int bankID;

    private List<BankModel> bankModels = new ArrayList<>();
    private List<Province> provinceModels = new ArrayList<>();

    TextWatcher textWatcher = new SimpleTextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String cardNo = mCardET.getText().toString();
            Log.e("cardNo.length()----",cardNo.length()+"");
            if (cardNo.length()==6){
                panduanBank(cardNo);
            }
        }

    };

    void panduanBank(String sixString){
        Log.e("sixStr----",sixString);
        BankModel model = null;
        for (int i=0;i<bankModels.size();i++){
            model = bankModels.get(i);

            String bankNOStr = model.getBankNOQZ();
            if (bankNOStr!=null){
                if (bankNOStr.indexOf(sixString)!=-1){
                    mBankTV.setText(model.getName());
                    bankID = model.getID();
                    return;
                }
            }

        }

    }

    @AfterViews
    void init() {

        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText(getResources().getString(R.string.card_message));
//        mToolbarRightIB.setVisibility(View.GONE);
        wheel_bank.setWheelBackground(R.drawable.wheel_bg);
        loadBank();
        loadCity();
        getBankInfo();
        wheel_provice.addChangingListener(this);
        wheel_city.addChangingListener(this);

        mCardET.addTextChangedListener(textWatcher);

        APIManager.getInstance().member_getPerData(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                PersonDataModel model1 = (PersonDataModel)model;
                IDNumber = model1.getIdentity();
            }
            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    //获取银行卡信息
    void getBankInfo(){
        showDialogLoading();
        APIManager.getInstance().bankInfo(this, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    String bankName = response.getJSONObject("data").getString("BankName");
                    mBankTV.setText(!bankName.equals("null")?bankName:"");
                    String cityName = response.getJSONObject("data").getString("BProName")+response.getJSONObject("data").getString("BCityName");
                    provice_TV.setText(!cityName.equals("nullnull")?cityName:"");
                    String phone = response.getJSONObject("data").getString("ResMobile");
                    mMobileET.setText(!phone.equals("null")?phone:"");
                    String kahao = response.getJSONObject("data").getString("CardNo");
                    mCardET.setText(!kahao.equals("null")?kahao:"");
                    String zsName = response.getJSONObject("data").getString("TrueName");
                    mNameET.setText(!zsName.equals("null")?zsName:"");
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
    //获取开户行
    void loadBank() {
        APIManager.getInstance().bank_list(this, APIManager.bank_url, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                BaseDataListModel<BankModel> baseModel = (BaseDataListModel<BankModel>) model;
                if (baseModel.getResult() == 1) {
                    bankModels.clear();
                    bankModels.addAll(baseModel.getDatalist());
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

    //银行卡认证
    void loadCard() {
        String bank = mBankTV.getText().toString().trim();
        String provice = provice_TV.getText().toString().trim();
        String mobile = mMobileET.getText().toString().trim();
        String card = mCardET.getText().toString().trim();
        String name = mNameET.getText().toString().trim();
        if (TextUtils.isEmpty(bank)) {
            showButtomToast("请选择开户行！");
            return;
        }

        if (TextUtils.isEmpty(provice)) {
            showButtomToast("请选择开户省市！");
            return;
        }

        if (!MatchUtil.isPhone(mobile)) {
            showButtomToast("请输入正确的手机号码！");
            return;
        }

        if (TextUtils.isEmpty(card)) {
            showButtomToast("请输入银行卡号");
            return;
        }

        if (!MatchUtil.isBankCardChecked(card)) {
            showButtomToast("请输入正确的银行卡号");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            showButtomToast("请输入银行卡持有人的真实姓名");
            return;
        }

        //BankModel bankModel = bankModels.get(wheel_bank.getCurrentItem());
        Province province = provinceModels.get(wheel_provice.getCurrentItem());
        Province.AreaCity city = provinceModels.get(wheel_provice.getCurrentItem()).
                getAreaCity().get(wheel_city.getCurrentItem());
        //Log.e("bankModel.getID()", bankModel.getID() + "");
        Log.e("province.getID()", province.getID() + "");
        Log.e("city.getID()", city.getID() + "");
        Log.e("LoanID", LoanID+ "");
        showDialogLoading();

        APIManager.getInstance().card_var(this, bankID, province.getID(), city.getID(),
                mobile, card,name,LoanID>0?LoanID:0,IDNumber, new APIManager.APIManagerInterface.common_wordBack() {
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

    @Click({R.id.mSubmitBN, R.id.bank_RL, R.id.tv_ok1, R.id.tv_cancel1, R.id.tv_cancel,
            R.id.bank_RL1, R.id.tv_ok, R.id.mToolbarLeftIB})
    void cardClick(View view) {
        switch (view.getId()) {
            case R.id.mToolbarLeftIB:
                finish();
                break;

            case R.id.mSubmitBN:
//                loadCard();
                showButtomToast("已认证，请不要重复认证");
                break;

            case R.id.bank_RL:
                DeviceUtil.hideSoftInput(mCardET, this);
                mView1.setOnClickListener(null);
                mSubmitBN.setVisibility(View.GONE);
                if (bankModels != null && bankModels.size() > 0) {
                    String[] strs = new String[bankModels.size()];
                    int inde = 0;
                    for (BankModel m : bankModels) {
                        strs[inde] = m.getName();
                        inde++;
                    }
                    Log.e("Success", strs.length + "");
                    wheel_bank.setViewAdapter(new ArrayWheelAdapter<String>(CardActivity.this, strs));
                    lin_area1.setVisibility(View.VISIBLE);
                    lin_area.setVisibility(View.GONE);
                }
                break;

            case R.id.tv_ok1:
                mSubmitBN.setVisibility(View.VISIBLE);
                lin_area1.setVisibility(View.GONE);
                BankModel bankModel = bankModels.get(wheel_bank.getCurrentItem());
                mBankTV.setText(bankModel.getName());
                bankID = bankModel.getID();
                break;

            case R.id.tv_cancel:
                lin_area.setVisibility(View.GONE);
                mSubmitBN.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_cancel1:
                mSubmitBN.setVisibility(View.VISIBLE);
                lin_area1.setVisibility(View.GONE);
                break;

            case R.id.bank_RL1:
                DeviceUtil.hideSoftInput(mCardET, this);
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
                    wheel_provice.setViewAdapter(new ArrayWheelAdapter<String>(CardActivity.this, strs));
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
                    Province.AreaCity cityModel = provinceModels.get(wheel_provice.getCurrentItem()).getAreaCity().get(wheel_city.getCurrentItem());
                    if (cityModel != null) {
                        pro += cityModel.getName() + " ";
                    }
                }
                provice_TV.setText(pro);
                break;
            default:
                break;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wheel_provice) {
            updataProvice();
        }
    }

    private void updataProvice() {
        String[] strs = new String[provinceModels.get(wheel_provice.getCurrentItem()).getAreaCity().size()];
        int inde = 0;
        for (Province.AreaCity model : provinceModels.get(wheel_provice.getCurrentItem()).getAreaCity()) {
            strs[inde] = model.getName();
            inde++;
        }
        wheel_city.setViewAdapter(new ArrayWheelAdapter<String>(CardActivity.this, strs));
        wheel_city.setCurrentItem(0);
    }

}

