package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.common.util.DensityUtil;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.dialog.KeFuDialog;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.CommonEnity;
import com.ahxbapp.jsqb.model.WxModel;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 借款 签约页面
 */
@EActivity(R.layout.activity_loan)
public class LoanActivity extends BaseActivity implements View.OnTouchListener, PullToRefreshScrollView.OnRefreshListener {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    TextView jiekuanren, jine, shichang, zongheFee, fangshi, riqi, zhengce, HT, tvOther;
    @ViewById
    CheckBox mCheckBox, cbOther;
    @ViewById
    Button btSign, btKefu, btFinish;
    @ViewById
    ImageView fee_image, ivLoan, ivSpeed;
    @ViewById
    ScrollView mScrollViewSign, mScrollViewKefu;//签约的界面，客服页面
    @ViewById
    RelativeLayout rlLoan;
    @ViewById
    ImageView ivLine1, ivLine2, ivImage1, ivImage2, ivImage3;
    @ViewById
    TextView tvText1, tvText2, tvText3;
    @ViewById
    LinearLayout llOther;
    @ViewById
    PullToRefreshScrollView mPullToRefreshScrollView;

    @Extra
    int loanId, flag;//借款编号   订单状态
    private List<WxModel> wxModels;
    private Context mContext;

    @Extra
    int isVip; //  0 不是VIP  1 是
    @Extra
    String speedUrl;
    private boolean isPaySpeedClick;
    private boolean isPaySpeed;
    private boolean isSignClick;//签约
    private RotateAnimation rotateAnimation;

    @AfterViews
    void init() {
        mContext = this;
        EventBus.getDefault().register(this);
        wxModels = new ArrayList<>();
        parentView = (RelativeLayout) ivSpeed.getParent();
        ivSpeed.setOnTouchListener(this);
        initUI();
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("借款");
        mPullToRefreshScrollView.setOnRefreshListener(this);
    }

    /**
     * 界面切换
     */
    private void initUI() {
        if (flag == 1) { //签约
            loadSingedData();
            mScrollViewSign.setVisibility(View.VISIBLE);
            mScrollViewKefu.setVisibility(View.GONE);
        } else if (flag == 2) {//客服
            mToolbarLeftIB.setVisibility(View.GONE);
            mScrollViewSign.setVisibility(View.GONE);
            mScrollViewKefu.setVisibility(View.VISIBLE);
            ivLine1.setBackgroundResource(R.mipmap.img_jiantou_orange);
            ivImage2.setBackgroundResource(R.mipmap.img_loan2_orange);
            tvText2.setTextColor(getResources().getColor(R.color.nav_blue));
            requestWxCode(false);
            requestLoanLogKssh(loanId);
        } else if (flag == 3) {//放款中
            btFinish.setClickable(false);
            mToolbarLeftIB.setVisibility(View.GONE);
            mScrollViewSign.setVisibility(View.GONE);
            mScrollViewKefu.setVisibility(View.GONE);
            rlLoan.setVisibility(View.VISIBLE);
            ivLine1.setBackgroundResource(R.mipmap.img_jiantou_orange);
            ivImage2.setBackgroundResource(R.mipmap.img_loan2_orange);
            tvText2.setTextColor(getResources().getColor(R.color.nav_blue));
            ivLine2.setBackgroundResource(R.mipmap.img_jiantou_orange);
            ivImage3.setBackgroundResource(R.mipmap.img_loan3_orange);
            tvText3.setTextColor(getResources().getColor(R.color.nav_blue));
        }
//        Glide.with(mContext).load(R.mipmap.img_gif_loan)
//                .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivLoan);
    }


    @Click
    void mToolbarLeftIB() {
        finish();
    }

    @Click
    void tvAgreement() {
        SetWebActivity_.intent(this).flag(11).loanLogID(loanId).start();
    }

    @Click
    void btFinish() {
        LoanMainActivity_.intent(mContext).start();
        finish();
    }

    @Click
    void btSign() {//签约
        if (!mCheckBox.isChecked()) {
            showMiddleToast("请先同意《极速钱包借款合同》");
            return;
        }
//        flag = 2;
//        initUI();
        if (isShow && !cbOther.isChecked()) {
            showMiddleToast(msg);
            return;
        }
        if (isShow) {
            WebActivity_.intent(this).url(payUrl).title("支付").start();
//            EventBus.getDefault().post(new CommonEnity<>("sign"));
//            finish();
            isSignClick = true;
            mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
            return;
        }
        showDialogLoading();
        requestSign();
    }

    @Click
    void btKefu() {//客服
        if (wxModels.size() == 0) {
            requestWxCode(true);
        } else {
            showKeFuDialog();
        }
    }

    private KeFuDialog keFuDialog;
    private void showKeFuDialog() {
        if (keFuDialog == null) {
            keFuDialog = new KeFuDialog(mContext, wxModels.get(0).getWxCode());
        }
        keFuDialog.show();
    }

    private boolean isShow;
    private String msg;
    private String agreementUrl = "";
    private String payUrl = "";


    //加载签约信息
    void loadSingedData() {
        showDialogLoading();
        APIManager.getInstance().singedShow1(this, loanId, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    jiekuanren.setText(response.getJSONObject("data").getString("UserId"));
                    jine.setText(response.getJSONObject("data").getInt("LoanId") + "元");
                    shichang.setText(response.getJSONObject("data").getInt("TermId") + "天");
                    zongheFee.setText(response.getJSONObject("data").getString("ZCost") + "元");
                    int zongfee = response.getJSONObject("data").getInt("ZCost") + response.getJSONObject("data").getInt("LoanId") - response.getJSONObject("data").getInt("CoupID");
                    fangshi.setText("一次性还款" + response.getJSONObject("data").getInt("LoanId") + "元");
//                    fangshi.setText("一次性还款"+zongfee+"元");
                    riqi.setText(response.getJSONObject("data").getString("RepayTime"));
                    zhengce.setText("7天容时期," + response.getJSONObject("data").getString("RongP") + "元每天" + "\n" + "7天后逾期," + response.getJSONObject("data").getString("OverdueP") + "元每天");
//                    guanliFee.setText(response.getJSONObject("data").getInt("Userfee") + "元");
//                    lixiFee.setText(response.getJSONObject("data").getInt("Interest") + "元");
//                    youhuiquan.setText("-" + response.getJSONObject("data").getInt("CoupID") + "元");
//                    int zong = response.getJSONObject("data").getInt("ZCost") - response.getJSONObject("data").getInt("CoupID");
//                    zongji.setText(zong + "元");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
        requestLoanlogKSQY();
    }

    /**
     * 快速签约费收取功能
     */
    private void requestLoanlogKSQY() {
        APIManager.getInstance().requestLoanlogKSQY(this, loanId, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                try {
                    payUrl = response.getString("url");
                    agreementUrl = response.getString("agreementUrl");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isShow = true;
                llOther.setVisibility(View.VISIBLE);
                requestParamValue();
            }

            @Override
            public void Failure(Context context, JSONObject response) {
            }
        });
    }

    /**
     * 签约信息展示
     */
    private void requestParamValue() {
        APIManager.getInstance().requestParamValue(this, new APIManager.APIManagerInterface.requestMobile() {
            @Override
            public void Success(Context context, String url) {//
                tvOther.setText(url + "");
                msg = url;
            }

            @Override
            public void Failure(Context context, String msg) {

            }
        });
    }

    /**
     * 签约
     */
    private void requestSign() {
        APIManager.getInstance().payment(this, loanId, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    int result = response.getInt("result");
                    if (result == 1) {
                        flag = 2;
                        initUI();
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

    /**
     * 微信客服
     */
    private void requestWxCode(final boolean isShow) {
        APIManager.getInstance().requestHomeWxKefu(this, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                if (model != null) {
                    BaseDataListModel<WxModel> wxModelBaseDataListModel = (BaseDataListModel<WxModel>) model;
                    if (wxModelBaseDataListModel.getResult() == 1 && wxModelBaseDataListModel.getDatalist() != null
                            && wxModelBaseDataListModel.getDatalist().size() > 0) {
                        wxModels.addAll(wxModelBaseDataListModel.getDatalist());
                        if (isShow) {
                            showKeFuDialog();
                        }
                    } else {
                        showMiddleToast("暂无客服");
                    }
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    /**
     * 快速审核费收取功能  jiemianzhanshi
     */
    private void requestLoanLogKssh(int loanID) {
        APIManager.getInstance().requestLoanLogKssh(mContext, loanID, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                try {
                    final String url = response.getString("url");
                    String imgUrl = response.getString("imgUrl");
                    int imgSize = response.getInt("imgSize");
                    ViewGroup.LayoutParams layoutParams = ivSpeed.getLayoutParams();
                    layoutParams.height = DensityUtil.dip2px(mContext, imgSize);
                    layoutParams.width = DensityUtil.dip2px(mContext, imgSize);
                    ivSpeed.setLayoutParams(layoutParams);
                    Glide.with(context).load(imgUrl).into(ivSpeed);
                    ivSpeed.setVisibility(View.VISIBLE);
//                    ivSpeed.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            WebActivity_.intent(getContext()).url(url).title("").start();
//                        }
//                    });
                    speedUrl = url;
                    isPaySpeed = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }


    private RelativeLayout parentView;
    private int lastX;
    private int lastY;
    private int downX;
    private int downY;

    private int maxRight;
    private int maxBottom;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //得到事件的坐标
        int eventX = (int) event.getRawX();
        int eventY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //得到父视图的right/bottom
                if (maxRight == 0) {//保证只赋一次值
                    maxRight = parentView.getRight();
                    maxBottom = parentView.getBottom();
                }
                //第一次记录lastX/lastY
                lastX = downX = eventX;
                lastY = downY = eventY;
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(eventX - downX) < 10 && Math.abs(eventY - downY) < 10) {
                    WebActivity_.intent(mContext).url(speedUrl).title("").start();
                    if (isPaySpeed) {
                        isPaySpeedClick = true;
                        mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //计算事件的偏移
                int dx = eventX - lastX;
                int dy = eventY - lastY;
                //根据事件的偏移来移动imageView
                int left = ivSpeed.getLeft() + dx;
                int top = ivSpeed.getTop() + dy;
                int right = ivSpeed.getRight() + dx;
                int bottom = ivSpeed.getBottom() + dy;
                //限制left >=0
                if (left < 0) {
                    right += -left;
                    left = 0;
                }
                //限制top
                if (top < 0) {
                    bottom += -top;
                    top = 0;
                }
                //限制right <=maxRight
                if (right > maxRight) {
                    left -= right - maxRight;
                    right = maxRight;
                }
                //限制bottom <=maxBottom
                if (bottom > maxBottom) {
                    top -= bottom - maxBottom;
                    bottom = maxBottom;
                }
                ivSpeed.layout(left, top, right, bottom);
                //再次记录lastX/lastY
                lastX = eventX;
                lastY = eventY;
                break;
            default:
                break;
        }
        return true;//所有的motionEvent都交给imageView处理
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 310) {
                //查询快速通道
                getDeatilStatusWithLoanID(loanId);
            }
        }
    };

    //查看借款状态
    void getDeatilStatusWithLoanID(final int loanID) {
        showDialogLoading();
        APIManager.getInstance().LoanDetail1(mContext, String.valueOf(loanID), new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    int status = response.getJSONObject("data").getInt("Status");
                    isVip = response.getJSONObject("data").getInt("isVip");
                    //String repayTime = response.getJSONObject("data").getString("RepayTime");
                    if (status < 5) {
                        if (isSignClick) { //点击签约
                            if (status == 0) {//待审核
                                flag = 2;
                                initUI();
//                                EventBus.getDefault().post(new CommonEnity<>("signSuccess"));
                            } else if (status == 3) {//待签约
                                mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
                            } else if (status == 4) {
                                flag = 3;
                                initUI();
                            }
                        }
                    } else if (status == 5) {
                        btFinish.setText("返回首页");
                        btFinish.setBackgroundResource(R.drawable.btn_cut);
                        btFinish.setClickable(true);
                    }
                    if (isPaySpeedClick) {//点击快速审核
                        if (isVip == 1) {//如果是VIP  快速审核页面关闭
                            EventBus.getDefault().post(new CommonEnity<>("isVip"));
                        } else {
                            mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                mPullToRefreshScrollView.onRefreshComplete();
            }
        });
    }

    //通知
    public void onEvent(CommonEnity enity) {
        if (enity.getType().equals("sign")) {
            isSignClick = true;
            mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        getDeatilStatusWithLoanID(loanId);
    }

    @Override
    public void onBackPressed() {
//        return;
//        super.onBackPressed();
    }
}
