package com.ahxbapp.jsqb.fragment;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseFragment;
import com.ahxbapp.common.util.DensityUtil;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.activity.ClassitifyActivity_;
import com.ahxbapp.jsqb.activity.LoanActivity_;
import com.ahxbapp.jsqb.activity.LoginActivity_;
import com.ahxbapp.jsqb.activity.PayActivity_;
import com.ahxbapp.jsqb.activity.StartBorrowingsActivity_;
import com.ahxbapp.jsqb.activity.WebActivity_;
import com.ahxbapp.jsqb.adapter.ClassificationAdapter;
import com.ahxbapp.jsqb.adapter.ZRecyclerViewAdapter;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.dialog.PromptDialog;
import com.ahxbapp.jsqb.dialog.SuperDialog;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.ClassificationModel;
import com.ahxbapp.jsqb.model.CommonEnity;
import com.ahxbapp.jsqb.model.MessageModel;
import com.ahxbapp.jsqb.model.MyBannerModel;
import com.ahxbapp.jsqb.utils.GlideImageLoader;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.switfpass.pay.handle.PayHandlerManager;
import com.switfpass.pay.utils.MD5;
import com.switfpass.pay.utils.SignUtils;
import com.xiaosu.DataSetAdapter;
import com.xiaosu.VerticalRollingTextView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * Created by Jayzhang on 16/10/17.
 * 首页 借款 还款
 */
@EFragment(R.layout.fragment_homepage)
public class HomepageFragment extends BaseFragment implements PullToRefreshScrollView.OnRefreshListener, View.OnTouchListener {


    @ViewById
    ImageView ivSpeed, ivBottom;//RightIB,

    @ViewById
    Banner mBanner;
    @ViewById
    VerticalRollingTextView rollingView;

    @ViewById
    TextView tvDetail, tvMoney, tvDayMoney;//详情  金额  还款期限
    @ViewById
    PullToRefreshScrollView mPullToRefreshScrollView;//刷新
    @ViewById
    Button btSubmit;//状态
    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    RelativeLayout rlCenter;

    boolean isJiekuan = true;

    int jkStatus = -1;
    int passStatus = -1;

    int jkID = 0;

    int isShenFen = 0;

    String limitDay = "1";

    private ClassificationAdapter classificationAdapter;
    private List<ClassificationModel> classificationModels;

    //    private MoneyListAdapter moneyListAdapter;
    private LinearLayout parentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        // 调用下，防止收到上次登陆账号的通知
    }

    @AfterViews
    void initLoanView() {
//        requestBanner();
        loadLoanData();
        isFrist = true;
        parentView = (LinearLayout) ivSpeed.getParent();
        ivSpeed.setOnTouchListener(this);
        initRandom();
        initClassification();

        ViewGroup.LayoutParams layoutParams = ivBottom.getLayoutParams();
        layoutParams.height = DensityUtil.screenHeight(getContext()) - DensityUtil.dip2px(getContext(), (70 + 50 + 5 + 110 + 60 + 180));
        ivBottom.setLayoutParams(layoutParams);

        int height = rlCenter.getHeight();
        showButtomToast(height + "...");
    }


    private String speedUrl = "";
    private boolean isPaySpeed;
    private boolean isPaySpeedClick;
    private boolean isSignClick;//签约
    private PromptDialog wxDialog;

    private void showWxDialog(final String num) {
        if (wxDialog == null) {
            wxDialog = new PromptDialog(getContext());
            wxDialog.tvPrompt.setText("微信客服账号");
            wxDialog.tvConfirm.setText("复制");
            wxDialog.tvCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wxDialog.dismiss();
                }
            });
            wxDialog.tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wxDialog.dismiss();
                    ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(num);
                    Toast.makeText(getContext(), "复制成功", Toast.LENGTH_LONG).show();
                }
            });
        }
        wxDialog.tvTitle.setText(num);
        wxDialog.show();
    }

    private boolean isFrist;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFrist) {
            loadLoanData();
            mPullToRefreshScrollView.setOnRefreshListener(this);
            //注册handler，接受调wftsdk通知消息。
//            PayHandlerManager.registerHandler(PayHandlerManager.PAY_H5_RESULT, handler);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLoanData();
        mPullToRefreshScrollView.setOnRefreshListener(this);
    }

    //通知
    public void onEvent(UserEvent.changeStatus status) {
        loadLoanData();
    }

    //通知
    public void onEvent(CommonEnity enity) {
//        if (enity.getType().equals("sign")) {
//            isSignClick = true;
//            mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //获取加载哪个视图的数据
    public void loadLoanData() {
        ivSpeed.setVisibility(View.GONE);
        isPaySpeedClick = false;
        isSignClick = false;
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        btSubmit.setTextColor(getResources().getColor(R.color.white));
        if (token == null) {
            showJieKuanView();
            mPullToRefreshScrollView.onRefreshComplete();
        } else {
            //判断是否有未完成的订单
            showDialogLoading();
            jkID = 0;
            APIManager.getInstance().Member_isNoComplate(getActivity(), new APIManager.APIManagerInterface.baseBlock() {
                @Override
                public void Success(Context context, JSONObject response) {
                    hideProgressDialog();
                    mPullToRefreshScrollView.onRefreshComplete();
                    try {
                        int result = response.getInt("result");
                        if (result == 1) {
                            //0待审核 1待确认银行卡  2待确认身份证  3待签约  4放款 5待还款  6已完成  7已取消 8审核失败
                            passStatus = response.getInt("Status");
                            if (passStatus == 8) {
                                //未通过审核
                                hideProgressDialog();
                                limitDay = response.getString("LimitDay");
                                isHuanKuan = false;
                                message = "";
                                showJieKuanView();
                                requestLoanSuperUrl();
                                return;
                            }

                            if (passStatus == 9) {
                                //身份认证没通过
                                hideProgressDialog();
                                isHuanKuan = false;
                                message = "";
                                showJieKuanView();
                                return;
                            }
                            // 有订单
                            int loanID = response.getInt("LoanlogID");
                            if (loanID > 0) {
                                jkID = loanID;
                                getDeatilStatusWithLoanID(loanID);
                            } else if (loanID == 0) {
                                //判断是否已经身份认证
                                isShenFen = response.getInt("IsID2");
                                showJieKuanView();
                                requestActivity();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void Failure(Context context, JSONObject response) {
                    hideProgressDialog();
                    mPullToRefreshScrollView.onRefreshComplete();
                }
            });
        }
    }

//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 310) {
//                //查询快速通道
//                getDeatilStatusWithLoanID(jkID);
//            }
//        }
//    };

    private void requestLoanSuperUrl() {
        APIManager.getInstance().requestLoanSuperUrl(getActivity(), new APIManager.APIManagerInterface.requestMobile() {
            @Override
            public void Success(Context context, String url) {
                showSuperDialog(url);
            }

            @Override
            public void Failure(Context context, String msg) {
            }
        });
    }

    private SuperDialog superDialog;

    private void showSuperDialog(String url) {
        if (superDialog == null) {
            superDialog = new SuperDialog(getContext(), url);
        }
        superDialog.show();
    }

    /**
     * 活动
     */
    private void requestActivity() {
        APIManager.getInstance().requestActivityInfo(getActivity(), new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                try {
                    final String url = response.getString("url");
                    String imgUrl = response.getString("imgUrl");
                    int imgSize = response.getInt("imgSize");
                    ViewGroup.LayoutParams layoutParams = ivSpeed.getLayoutParams();
                    layoutParams.height = DensityUtil.dip2px(getContext(), imgSize);
                    layoutParams.width = DensityUtil.dip2px(getContext(), imgSize);
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
                    isPaySpeed = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }


    private String orderNO, backM, orderid;
    private int isVip; //  0 不是VIP  1 是

    //查看借款状态
    void getDeatilStatusWithLoanID(final int loanID) {
        showDialogLoading();
        APIManager.getInstance().LoanDetail1(getActivity(), String.valueOf(loanID), new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    int status = response.getJSONObject("data").getInt("Status");
                    isVip = response.getJSONObject("data").getInt("isVip");
                    backM = response.getJSONObject("data").getString("BackM");
                    if (status < 5) {
                        isHuanKuan = false;
                        message = "";
                        showStatusView(status, backM + "");
//                        if (isSignClick) { //点击签约
//                            if (status == 0) {//待审核
//                                EventBus.getDefault().post(new CommonEnity<>("signSuccess"));
//                            } else if (status == 3) {//待签约
//                                mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
//                            }
//                        }
                    }
                    if (status == 5) {
                        message = "当前借款：" + response.getJSONObject("data").getString("BackM") + "元";
                        isHuanKuan = true;
                        orderNO = response.getJSONObject("data").getString("LoanNO");
                        orderid = response.getJSONObject("data").getString("ID");
                        tvDetail.setText("待还款金额");
//                        tvDayMoney.setText(message);
//                        tvDayMoney.setVisibility(View.VISIBLE);
                        tvMoney.setText(backM);
                        btSubmit.setText("立即还款");
                        showHuanKuanView();
                    }
//                    if (isPaySpeedClick) {//点击快速审核
//                        if (isVip == 1) {//如果是VIP  快速审核页面关闭
//                            EventBus.getDefault().post(new CommonEnity<>("isVip"));
//                        } else {
//                            mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
//                        }
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
//                if (moneyListAdapter != null) {
//                    isHuanKuan = false;
//                    message = "";
//                    coupon = "优惠券：0张可用";
//                    moneyListAdapter.setHuanKuan(isHuanKuan, message, coupon);
//                } else {
//                    initData();
//                }
                hideProgressDialog();
            }
        });
    }

    //显示借款视图
    void showJieKuanView() {
        isJiekuan = true;
        isHuanKuan = false;
        tvDayMoney.setVisibility(View.GONE);
        tvMoney.setText("5000");
        tvDetail.setText("最高额度");
        btSubmit.setText("立即借款");
        message = "";
    }

    private int count;
    private boolean isHuanKuan;
    private String message = "";

    private PromptDialog litmitDialog;

    private void showLitmitDialog(String msg) {
        if (litmitDialog == null) {
            litmitDialog = new PromptDialog(getContext());
        }
        litmitDialog.tvCancle.setVisibility(View.GONE);
        litmitDialog.viewXian.setVisibility(View.GONE);
        litmitDialog.tvTitle.setText(msg);
        litmitDialog.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                litmitDialog.dismiss();
            }
        });
        litmitDialog.show();
    }

    /**
     * 获取状态  是否已实名认证
     */
    private void requestState() {
        showDialogLoading();
        APIManager.getInstance().home_getAuthenStatus(getContext(), 0, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                AuthenModel authenModel = (AuthenModel) model;
                if (authenModel.getIsID2() == 1) {
                    StartBorrowingsActivity_.intent(HomepageFragment.this).start();
                } else {
                    StartBorrowingsActivity_.intent(HomepageFragment.this).start();
//                    FirstCerActivity_.intent(HomepageFragment.this).tag(2).start();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    private AlertDialog huanKuanDialog;

    private void showHuanKuanDialog() {
        huanKuanDialog = new AlertDialog.Builder(getActivity()).create();
        huanKuanDialog.show();
        Window window = huanKuanDialog.getWindow();
        window.setContentView(R.layout.payway);
        final Button cancel = (Button) window.findViewById(R.id.cancel_Button);
        final Button ok = (Button) window.findViewById(R.id.OK_Button);
        final RelativeLayout bank_Rela = (RelativeLayout) window.findViewById(R.id.bank_Rela);
        final RelativeLayout zfb_Rela = (RelativeLayout) window.findViewById(R.id.zfb_Rela);
        final RelativeLayout wx_Rela = (RelativeLayout) window.findViewById(R.id.wx_Rela);
        final CheckBox zfb_Box = (CheckBox) window.findViewById(R.id.zfb_Box);
        final CheckBox wx_Box = (CheckBox) window.findViewById(R.id.wx_Box);
        final CheckBox bank_Box = (CheckBox) window.findViewById(R.id.bank_Box);

        zfb_Rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zfb_Box.setChecked(true);
                wx_Box.setChecked(false);
                bank_Box.setChecked(false);
            }
        });
        bank_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_Box.setChecked(true);
                wx_Box.setChecked(false);
                zfb_Box.setChecked(false);
            }
        });
        bank_Rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bank_Box.setChecked(true);
                wx_Box.setChecked(false);
                zfb_Box.setChecked(false);
            }
        });
        zfb_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zfb_Box.setChecked(true);
                wx_Box.setChecked(false);
                bank_Box.setChecked(false);
            }
        });
        wx_Rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wx_Box.setChecked(true);
                zfb_Box.setChecked(false);
                bank_Box.setChecked(false);
            }
        });
        wx_Box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wx_Box.setChecked(true);
                bank_Box.setChecked(false);
                zfb_Box.setChecked(false);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huanKuanDialog.dismiss();
            }
        });
        //还款
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huanKuanDialog.dismiss();
                long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("HHmmss");
                Date d1 = new Date(time);
                String t1 = format.format(d1);
                Log.e("msg", t1);
//                orderNO = orderNO + t1;
                if (wx_Box.isChecked() == true) {
                    //  PayWebActivity_.intent(getActivity()).flag(2).noOrder(orderNO).backM(backM).startForResult(100);
                    WebActivity_.intent(getContext()).url(Global.HOST + "Home/wx?token=" + PrefsUtil.getString(getContext(), Global.TOKEN)).start();
                } else if (zfb_Box.isChecked() == true) {
                    requestAliPay(orderid);
                    //威富通
                    //new GetPrepayIdTask().execute();
                    //51支付
//                    WebActivity_.intent(getContext()).url(Global.HOST + "Home/Code?token=" + PrefsUtil.getString(getContext(), Global.TOKEN)).start();
                    //     PayWebActivity_.intent(getActivity()).flag(1).noOrder(orderNO).backM(backM).startForResult(100);
                } else if (bank_Box.isChecked() == true) {
                    WebActivity_.intent(getContext()).url(APIManager.requestBankPay + "?id=" + orderid).start();
//                    requestBankState(orderid);
                }
            }
        });
    }


    private void requestAliPay(String orderNO) {
        showDialogLoading();
        APIManager.getInstance().requestAliPay(orderNO, getContext(), new APIManager.APIManagerInterface.aliPay() {

            @Override
            public void Success(Context context, String prepayId, String ordernumber) {
                hideProgressDialog();
                PayActivity_.intent(HomepageFragment.this).prepayId(prepayId).ordernumber(ordernumber).startForResult(1000);
            }

            @Override
            public void Failure(Context context, String response) {
                showButtomToast(response);
                hideProgressDialog();
            }
        });
    }

    /**
     * 判断银行卡还款方式
     */
    private void requestBankState(final String orderNO) {
        showDialogLoading();
        APIManager.getInstance().requestBankState(getContext(), new APIManager.APIManagerInterface.aliPay() {

            @Override
            public void Success(Context context, String url, String ordernumber) {
                hideProgressDialog();
                WebActivity_.intent(getContext()).url(url).title("银行卡还款").start();
            }

            @Override
            public void Failure(Context context, String response) {
                hideProgressDialog();
                requestBankPay(orderNO);
            }
        });
    }

    private void requestBankPay(String orderNO) {
        showDialogLoading();
        APIManager.getInstance().requestBankPay(orderNO, getContext(), new APIManager.APIManagerInterface.aliPay() {

            @Override
            public void Success(Context context, String prepayId, String ordernumber) {
                hideProgressDialog();
                JSONObject obj;
                try {
                    obj = new JSONObject(prepayId);
                    String ret_code = obj.getString("ret_code");
                    if (ret_code.equals("0000")) {
                        showButtomToast(obj.getString("ret_msg"));
                        loadLoanData();
                    } else {
                        showButtomToast(obj.getString("ret_msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, String response) {
                showButtomToast(response);
                hideProgressDialog();
            }
        });
    }

    @OnActivityResult(1000)
    void PayActivity() {
        loadLoanData();
    }

    //显示状态视图
    void showStatusView(int status, String money) {
//        haveMessage();
        jkStatus = status;
//        RightIB.setVisibility(View.VISIBLE);
        if (status == 3) {
            //签约
            btSubmit.setText("签约");
        } else {
            if (status == 0) {
                //审核
//                btSubmit.setText("签约成功，若审核通过，及时放款");
//                if (isVip == 1) {
//                    btSubmit.setTextColor(getResources().getColor(R.color.text_red));
//                    btSubmit.setText("您已进入VIP快速审核中");
//                } else {
//                    btSubmit.setText("正在审核中");
//                }
                LoanActivity_.intent(this).loanId(jkID).flag(2).startForResult(100);
                getActivity().finish();
            } else {
                LoanActivity_.intent(this).loanId(jkID).flag(3).startForResult(100);
                getActivity().finish();
                //放款
                btSubmit.setText("正在放款中");
                tvMoney.setText(money + "");
                tvDetail.setText("审核通过，款项正在路上");
            }
        }
    }


    //显示还款视图
    void showHuanKuanView() {
//        RightIB.setVisibility(View.VISIBLE);
//        haveMessage();
        isJiekuan = false;
    }

    //是否有未读消息
    void haveMessage() {
        showBlackLoading();
        APIManager.getInstance().getMessage(getActivity(), pageIndex, pageSize, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseDataListModel<MessageModel> mm = (BaseDataListModel<MessageModel>) model;
                for (int i = 0; i < mm.getDatalist().size(); i++) {
                    MessageModel messageModel = mm.getDatalist().get(i);
                    if (messageModel.getIsRead() == 0) {
//                        RightIB.setBackground(getResources().getDrawable(R.mipmap.rednews));
                        return;
                    }
                }
//                RightIB.setBackground(getResources().getDrawable(R.mipmap.news));
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    @Click
    void btSubmit() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (token == null) {//未登录
            LoginActivity_.intent(HomepageFragment.this).startForResult(1000);
            return;
        }
        //0待审核 1待确认银行卡  2待确认身份证  3待签约  4放款 5待还款  6已完成  7已取消 8审核失败
        if (passStatus == 0) {
            if (jkID == 0) {
                requestState();
            } else {
                //待审核
                LoanActivity_.intent(this).loanId(jkID).flag(2).startForResult(100);
            }
        } else if (passStatus == 3) {
            //签约
            LoanActivity_.intent(this).loanId(jkID)
                    .isVip(isVip).speedUrl(speedUrl).flag(1).startForResult(100);
        } else if (passStatus == 4) {
            //待放款
            LoanActivity_.intent(this).loanId(jkID).flag(3).startForResult(100);
        } else if (passStatus == 5) {
            if (huanKuanDialog == null) {
                showHuanKuanDialog();
            } else {
                huanKuanDialog.show();
            }
        } else if (passStatus != 8) {
            requestState();
        } else {
            if (Integer.parseInt(limitDay) == 0) {
                showLitmitDialog("您的申请借款审核未通过，详情请看系统消息！");
            } else {
                showLitmitDialog("您的申请借款审核未通过，请" + limitDay + "天后再次尝试！");
            }
        }

//        if (jkStatus == 3) {
//            //签约
//            LoanActivity_.intent(this).loanId(jkID).flag(1).startForResult(100);
//        } else if (jkStatus == 0) {
//
//        }

    }

//    @Click
//    void tvApply() {
//        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
//        if (token == null) {//未登录
//            LoginActivity_.intent(HomepageFragment.this).startForResult(1000);
//            return;
//        }
//        //0待审核 1待确认银行卡  2待确认身份证  3待签约  4放款 5待还款  6已完成  7已取消 8审核失败
//        if (passStatus == 5) {
//            if (huanKuanDialog == null) {
//                showHuanKuanDialog();
//            } else {
//                huanKuanDialog.show();
//            }
//        } else if (passStatus != 8) {
//            requestState();
//        } else {
//            if (Integer.parseInt(limitDay) == 0) {
//                showLitmitDialog("您的申请借款审核未通过，详情请看系统消息！");
//            } else {
//                showLitmitDialog("您的申请借款审核未通过，请" + limitDay + "天后再次尝试！");
//            }
//        }
//    }

//    @Click
//    void RightIB() {
//        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
//        if (TextUtils.isEmpty(token)) {
//            LoginActivity_.intent(this).startForResult(1000);
//        } else {
//            MessageActivity_.intent(getActivity()).start();
//        }
//    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genOutTradNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key += r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    String payOrderNo;


    public String createSign(String signKey, Map<String, String> params) {
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        buf.append("&key=").append(signKey);
        String preStr = buf.toString();
        String sign = "";
        // 获得签名验证结果
        try {
            sign = MD5.md5s(preStr).toUpperCase();
        } catch (Exception e) {
            sign = MD5.md5s(preStr).toUpperCase();
        }
        return sign;
    }


    String TAG = "LoanFragment";

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadLoanData();
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PayHandlerManager.PAY_H5_FAILED: //失败，原因如有（商户未开通[pay.weixin.wappay]支付类型）等
                    Log.i(TAG, "" + msg.obj);
                    break;
                case PayHandlerManager.PAY_H5_SUCCES: //成功
                    Log.i(TAG, "" + msg.obj);
                    break;

                default:
                    break;
            }
        }
    };


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
                    WebActivity_.intent(getContext()).url(speedUrl).title("").start();
                    if (isPaySpeed) {
                        isPaySpeedClick = true;
//                        mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
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

    private List<String> bannerUrls;

    /**
     *
     * */
    private void initBanner() {
        bannerUrls = new ArrayList<>();
        for (int i = 0; i < bannerModels.size(); i++) {
            bannerUrls.add(bannerModels.get(i).getPic_2xUrl());
//            bannerUrls.add("http://img.910ok.com/" + bannerModels.get(i).getUrl());
        }
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (bannerModels.get(position).getLinkUrl() != null) {
                    WebActivity_.intent(getContext()).url(bannerModels.get(position).getLinkUrl()).title("").start();
                }
            }
        });
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setBannerAnimation(Transformer.Default);
        mBanner.isAutoPlay(true);
        mBanner.setDelayTime(3000);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(bannerUrls);
        mBanner.start();
    }

    private List<MyBannerModel> bannerModels;

    private void requestBanner() {
        bannerModels = new ArrayList<>();
        APIManager.getInstance().requestBanner(getActivity(), new APIManager.APIManagerInterface.aliPay() {
            @Override
            public void Success(Context context, String banner, String ordernumber) {
                if (banner != null) {
                    List<MyBannerModel> models = JSONArray.parseArray(banner, MyBannerModel.class);
                    if (models != null && models.size() > 0) {
                        bannerModels.addAll(models);
                        initBanner();
                    }
                }
            }

            @Override
            public void Failure(Context context, String msg) {
                showButtomToast(msg);
            }
        });
    }

    private void initRandom() {
        List<String> msgList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 25; i++) {
            String msg = "";
            for (int j = 0; j < 4; j++) {
                int num = random.nextInt(9);
                msg = msg + num + "";
            }
            int money = ((int) (5 + Math.random() * 95)) * 100;
//            if (i % 2 == 0) {
//                money = 500;
//            } else {
//                money = 1000;
//            }
            msgList.add("尾号" + msg + "，成功借款" + money + "元");
        }
        rollingView.setDataSetAdapter(new DataSetAdapter<String>(msgList) {
            @Override
            protected String text(String s) {
                return s;
            }
        });
        // 开始滚动
        rollingView.run();
    }

    private void initClassification() {
        classificationModels = new ArrayList<>();
        classificationAdapter = new ClassificationAdapter(mRecyclerView, classificationModels);
        classificationAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ClassitifyActivity_.intent(getContext())
                        .bgRes(R.mipmap.img_kefu_bg)
                        .title(classificationModels.get(position).getTitle()).start();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        linearLayoutManager
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(classificationAdapter);
        classificationModels.add(new ClassificationModel("分期乐", "专门客服兑现", R.mipmap.img_home_1));
        classificationModels.add(new ClassificationModel("花呗兑现", "专门客服兑现", R.mipmap.img_home_2));
        classificationModels.add(new ClassificationModel("白条兑现", "专门客服兑现", R.mipmap.img_home_3));
        classificationModels.add(new ClassificationModel("任性付", "专门客服兑现", R.mipmap.img_home_4));
        classificationModels.add(new ClassificationModel("来分期", "专门客服兑现", R.mipmap.img_home_5));
        classificationAdapter.notifyDataSetChanged();
    }

    @Click
    void llLetter() {
        ClassitifyActivity_.intent(getContext())
                .bgRes(R.mipmap.img_letter_bg)
                .title("征信查询").start();
    }
}
