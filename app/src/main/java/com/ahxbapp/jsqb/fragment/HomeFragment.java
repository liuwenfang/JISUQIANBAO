package com.ahxbapp.jsqb.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseFragment;
import com.ahxbapp.common.util.DensityUtil;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.activity.CashOrderDetailActivity_;
import com.ahxbapp.jsqb.activity.ClassitifyActivity_;
import com.ahxbapp.jsqb.activity.FirstCerActivity_;
import com.ahxbapp.jsqb.activity.LoanActivity_;
import com.ahxbapp.jsqb.activity.LoginActivity_;
import com.ahxbapp.jsqb.activity.MessageActivity_;
import com.ahxbapp.jsqb.activity.PayActivity_;
import com.ahxbapp.jsqb.activity.SetWebActivity_;
import com.ahxbapp.jsqb.activity.StartBorrowingsActivity_;
import com.ahxbapp.jsqb.activity.WebActivity_;
import com.ahxbapp.jsqb.activity.WordActivity_;
import com.ahxbapp.jsqb.adapter.ClassificationAdapter;
import com.ahxbapp.jsqb.adapter.ZRecyclerViewAdapter;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.customview.NoScrollWebView;
import com.ahxbapp.jsqb.dialog.KeFuDialog;
import com.ahxbapp.jsqb.dialog.PromptDialog;
import com.ahxbapp.jsqb.dialog.SuperDialog;
import com.ahxbapp.jsqb.event.UserEvent;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.ClassificationModel;
import com.ahxbapp.jsqb.model.CommonEnity;
import com.ahxbapp.jsqb.model.WxModel;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.ahxbapp.jsqb.utils.StringUtils;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.utils.L;
import com.switfpass.pay.utils.MD5;
import com.xiaosu.DataSetAdapter;
import com.xiaosu.VerticalRollingTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * Created by Jayzhang on 16/10/17.
 * 首页 借款 还款
 */
@EFragment(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements PullToRefreshScrollView.OnRefreshListener, View.OnTouchListener {

    //    @ViewById
//    Button statusBtn;
    @ViewById
    TextView LeftTV, TitleTV, tvMainTitle;
    @ViewById
    ImageView ivSpeed, ivBottom; // RightIB,

    @ViewById
    RelativeLayout status_Scroll, nav_rela;
    @ViewById
    PullToRefreshScrollView scroll_status, scroll_none;

//    @ViewById
//    TextView one_text, two_text, three_text;
    //, four_text, five_text
//    @ViewById
//    TextView mTitle1, mTitle2, mTitle3;
    //, mTitle4, mTitle5
//    @ViewById
//    TextView mContent1, mContent2, mContent3;

    //, mContent4, mContent5
    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    LinearLayout llRecyclerView;
//    @ViewById
//    View viewXian2;

    @ViewById
    VerticalRollingTextView rollingView;
    @ViewById
    TextView tvDetail, tvMoney, tvDayMoney;
    @ViewById
    Button btApply;

    /**
     * 签约
     */
    @ViewById
    CheckBox mCheckBox, cbOther;
    @ViewById
    TextView jiekuanren, jine, shichang, zongheFee, fangshi, riqi, zhengce, HT, tvOther;
    @ViewById
    ImageView ivLine1, ivLine2, ivImage1, ivImage2, ivImage3;
    @ViewById
    TextView tvText1, tvText2, tvText3, tvKeFuMsg, tvTips1, tvTips2, tvDay;// 客服页面提示的文字
    @ViewById
    LinearLayout llOther;
    @ViewById
    Button btSign, btKefu;
    @ViewById
    LinearLayout llLoanMenu, llLoan, llTopSign, llKeFu;//签约,签约Top，客服
    @ViewById
    NoScrollWebView webView;
    @ViewById
    RelativeLayout rlLoan;//放款中
    @ViewById
    RelativeLayout rlVip, rlNotVip;


    boolean isJiekuan = true;

    int jkStatus = 0;

    int jkID = 0;

    int isShenFen = 0;

    String limitDay = "1";

    int passStatus = 0;

    private List<WxModel> wxModels;
    private RelativeLayout parentView;

    private boolean isForeground;//界面是否在前台

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        // 调用下，防止收到上次登陆账号的通知
    }

    @AfterViews
    void initLoanView() {
        wxModels = new ArrayList<>();
        loadLoanData();
        isFrist = true;
        scroll_status.setOnRefreshListener(this);
        scroll_none.setOnRefreshListener(this);
        parentView = (RelativeLayout) ivSpeed.getParent();
        ivSpeed.setOnTouchListener(this);
//        ViewGroup.LayoutParams layoutParams = ivBottom.getLayoutParams();
//        layoutParams.height = DensityUtil.screenHeight(getContext()) - DensityUtil.dip2px(getContext(), (70 + 50 + 5 + 110 + 60 + 180));
//        ivBottom.setLayoutParams(layoutParams);
//        initRandom();
        initWeb();
        initClassification();
    }

    private String speedUrl = "";
    private boolean isPaySpeed;
    private boolean isPaySpeedClick;
    private boolean isSignClick;//签约

    private boolean isFrist;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isFrist) {

            loadLoanData();
            //注册handler，接受调wftsdk通知消息。
        }
    }

    //通知
    public void onEvent(UserEvent.changeStatus status) {
        loadLoanData();
    }

    //通知
    public void onEvent(CommonEnity enity) {
        if (enity.getType().equals("sign")) {
            isSignClick = true;
            mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isForeground = false;
    }

    //获取加载哪个视图的数据
    public void loadLoanData() {
        ivSpeed.setVisibility(View.GONE);
        isPaySpeedClick = false;
        isSignClick = false;
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (token == null) {
            showJieKuanView();
            scroll_status.onRefreshComplete();
            scroll_none.onRefreshComplete();
        } else {
            //判断是否有未完成的订单
            showDialogLoading();
            APIManager.getInstance().Member_isNoComplate(getActivity(), new APIManager.APIManagerInterface.baseBlock() {
                @Override
                public void Success(Context context, JSONObject response) {
                    hideProgressDialog();
                    scroll_status.onRefreshComplete();
                    scroll_none.onRefreshComplete();
                    try {
                        int result = response.getInt("result");
                        if (result == 1) {
                            //0待审核 1待确认银行卡  2待确认身份证  3待签约  4放款 5待还款  6已完成  7已取消 8审核失败
                            passStatus = response.getInt("Status");
                            if (passStatus == 8) {
                                //未通过审核
                                hideProgressDialog();
                                limitDay = response.getString("LimitDay");
                                message = "";
                                showJieKuanView();
                                requestLoanSuperUrl();
                                return;
                            }

                            if (passStatus == 9) {
                                //身份认证没通过
                                hideProgressDialog();
                                message = "";
                                showJieKuanView();
                                return;
                            }

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
                    scroll_status.onRefreshComplete();
                    scroll_none.onRefreshComplete();
                }
            });
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 310) {
                //查询快速通道
                getDeatilStatusWithLoanID(jkID);
            }
        }
    };

    private void requestLoanSuperUrl() {
        APIManager.getInstance().requestLoanSuperUrl(getActivity(), new APIManager.APIManagerInterface.requestMobile() {
            @Override
            public void Success(Context context, String url) {
//                showButtomToast(url);
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
                    //String repayTime = response.getJSONObject("data").getString("RepayTime");
                    if (status < 5) {
                        message = "";
                        showStatusView(status, loanID);
                        if (isSignClick) { //点击签约
                            if (isForeground) //如果返回该页面  则不会继续
                                return;
                            if (status == 0) {//待审核
                                EventBus.getDefault().post(new CommonEnity<>("signSuccess"));
                            } else if (status == 3) {//待签约
                                mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
                            }
                        }
                    }
                    if (status == 5) {
                        message = "当前借款：" + response.getJSONObject("data").getString("BackM") + "元";
                        orderNO = response.getJSONObject("data").getString("LoanNO");
                        orderid = response.getJSONObject("data").getString("ID");
                        backM = response.getJSONObject("data").getString("BackM");
                        String RepayTime = response.getJSONObject("data").getString("RepayTime");
                        tvDay.setText(RepayTime);
//                        tvDetail.setText("待还款金额");
                        tvMoney.setText("￥" + backM + "元");
                        tvTips2.setVisibility(View.INVISIBLE);
                        tvTips1.setText("本期待还");
                        btApply.setText("我要还款");
                        showHuanKuanView();
                    }
                    if (isPaySpeedClick) {//点击快速审核
                        if (isForeground) //如果返回该页面  则不会继续
                            return;
                        if (isVip == 1) {//如果是VIP  快速审核页面关闭
                            EventBus.getDefault().post(new CommonEnity<>("isVip"));
                        } else {
                            mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
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

    /**
     * 快速审核费收取功能  jiemianzhanshi
     */
    private void requestLoanLogKssh(int loanID) {
        APIManager.getInstance().requestLoanLogKssh(getContext(), loanID, new APIManager.APIManagerInterface.baseBlock() {
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

    //改变状态和按钮状态

    //显示借款视图
    void showJieKuanView() {
//        showDialogLoading();
        status_Scroll.setVisibility(View.GONE);
        tvDayMoney.setVisibility(View.GONE);
        scroll_none.setVisibility(View.VISIBLE);
        isJiekuan = true;
        LeftTV.setVisibility(View.GONE);
        nav_rela.setVisibility(View.GONE);
        TitleTV.setText("极速钱包");
        tvMainTitle.setText("极速钱包");
        tvMoney.setText("10000");

        tvDetail.setText("最高额度");
        tvTips1.setText("温馨提示");
        tvTips2.setVisibility(View.VISIBLE);
        btApply.setText("我要借款");
        tvDay.setText(getPastDate(7));
        message = "";
    }

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
                    StartBorrowingsActivity_.intent(HomeFragment.this).start();
                } else {
                    FirstCerActivity_.intent(HomeFragment.this).tag(2).start();
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
                PayActivity_.intent(HomeFragment.this).prepayId(prepayId).ordernumber(ordernumber).startForResult(1000);
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
    void showStatusView(int status, int loanID) {

        TitleTV.setText("借 款");
        tvMainTitle.setText("借 款");
//        haveMessage();
        jkStatus = status;
        scroll_none.setVisibility(View.GONE);
        status_Scroll.setVisibility(View.VISIBLE);
        nav_rela.setVisibility(View.VISIBLE);
//        RightIB.setVisibility(View.VISIBLE);
        if (status == 3) {
            //签约
//            one_text.setBackgroundResource(R.drawable.text_blue);
//            mTitle1.setTextColor(getResources().getColor(R.color.nav_blue));
//            mContent1.setTextColor(getResources().getColor(R.color.detail_cash));
//            statusBtn.setText("签约");
//            statusBtn.setBackground(getResources().getDrawable(R.drawable.btn_cut));
//            statusBtn.setClickable(true);

//            two_text.setBackgroundResource(R.drawable.text_grey);
//            mTitle2.setTextColor(getResources().getColor(R.color.font_grey2));
//            mContent2.setTextColor(getResources().getColor(R.color.font_grey2));
//            three_text.setBackgroundResource(R.drawable.text_grey);
//            mTitle3.setTextColor(getResources().getColor(R.color.font_grey2));
//            mContent3.setTextColor(getResources().getColor(R.color.font_grey2));
            initSign();
        } else {
//            statusBtn.setBackground(getResources().getDrawable(R.drawable.btn_cut_gray));
//            statusBtn.setClickable(false);
            if (status == 0) {
                //审核
//                mContent1.setText("签约成功，若审核通过，及时放款");
//                two_text.setBackgroundResource(R.drawable.text_blue);
//                mTitle2.setTextColor(getResources().getColor(R.color.nav_blue));
//                mContent2.setTextColor(getResources().getColor(R.color.detail_cash));
//                three_text.setBackgroundResource(R.drawable.text_grey);
//                mTitle3.setTextColor(getResources().getColor(R.color.font_grey2));
//                mContent3.setTextColor(getResources().getColor(R.color.font_grey2));
                if (isVip == 1) {
//                    statusBtn.setTextColor(getResources().getColor(R.color.text_red));
//                    statusBtn.setText("您已进入VIP快速审核中");
                } else {
//                    statusBtn.setText("正在审核中");
                }
                if (wxModels.size() == 0) {
                    requestWxCode(false);
//                    requestWxTxt();
                }
                initKeFu();
            } else {
                //放款
//                statusBtn.setText("正在放款中");
//                mContent2.setText("审核通过，款项正在路上");
//                three_text.setBackgroundResource(R.drawable.text_blue);
//                mTitle3.setTextColor(getResources().getColor(R.color.nav_blue));
//                mContent3.setTextColor(getResources().getColor(R.color.detail_cash));
                initLoan();
            }
        }
    }

    //加载签约信息
    void loadSingedData(int loanId) {
        showDialogLoading();
        APIManager.getInstance().singedShow1(getContext(), loanId, new APIManager.APIManagerInterface.baseBlock() {
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
                    riqi.setText(response.getJSONObject("data").getString("RepayTime"));
                    zhengce.setText("7天容时期," + response.getJSONObject("data").getString("RongP") + "元每天" + "\n" + "7天后逾期," + response.getJSONObject("data").getString("OverdueP") + "元每天");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
        requestLoanlogKSQY(loanId);
    }

    private boolean isShow;
    private String msg;
    private String agreementUrl = "";
    private String payUrl = "";

    /**
     * 快速签约费收取功能
     */
    private void requestLoanlogKSQY(int loanId) {
        APIManager.getInstance().requestLoanlogKSQY(getContext(), loanId, new APIManager.APIManagerInterface.baseBlock() {
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
        APIManager.getInstance().requestParamValue(getContext(), new APIManager.APIManagerInterface.requestMobile() {
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
     * 审核中的提示语
     */
    private void requestWxTxt() {
        APIManager.getInstance().requestWxTxt(getContext(), new APIManager.APIManagerInterface.requestMobile() {
            @Override
            public void Success(Context context, String msg) {
                if (!StringUtils.isEmpty(msg)) {
//                    mContent2.setText(msg);
                }
            }

            @Override
            public void Failure(Context context, String msg) {

            }
        });
    }

    @Click
    void tvOther() {
        WebActivity_.intent(this).url(agreementUrl).title("签约条款").start();
    }

    @Click
    void tvAgreement() {
        SetWebActivity_.intent(this).flag(11).loanLogID(jkID).start();
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
            keFuDialog = new KeFuDialog(getContext(), wxModels.get(0).getWxCode());
        }
        keFuDialog.show();
    }

    @Click
    void btSign() {//签约
        if (!mCheckBox.isChecked()) {
            showMiddleToast("请先同意《极速钱包借款合同》");
            return;
        }
        if (isShow && !cbOther.isChecked()) {
            showMiddleToast(msg);
            return;
        }
        if (isShow) {
            WebActivity_.intent(this).url(payUrl).title("支付").start();
            EventBus.getDefault().post(new CommonEnity<>("sign"));
            isSignClick = true;
            mHandler.sendEmptyMessageDelayed(310, 3 * 1000);
            return;
        }
        showDialogLoading();
        requestSign();
    }

    /**
     * 签约
     */
    private void initSign() {
        ivLine1.setBackgroundResource(R.mipmap.img_jiantou_gray);
        ivImage2.setBackgroundResource(R.mipmap.img_loan2_gray);
        tvText2.setTextColor(getResources().getColor(R.color.text_grey));
        ivLine2.setBackgroundResource(R.mipmap.img_jiantou_gray);
        ivImage3.setBackgroundResource(R.mipmap.img_loan3_gray);
        tvText3.setTextColor(getResources().getColor(R.color.text_grey));
        llLoan.setVisibility(View.VISIBLE);
        llKeFu.setVisibility(View.GONE);
        rlLoan.setVisibility(View.GONE);//放款
        loadSingedData(jkID);
    }

    /**
     * 客服
     */
    private void initKeFu() {
        llLoan.setVisibility(View.GONE);
        llKeFu.setVisibility(View.VISIBLE);
        rlLoan.setVisibility(View.GONE);//放款
        ivLine1.setBackgroundResource(R.mipmap.img_jiantou_orange);
        ivImage2.setBackgroundResource(R.mipmap.img_loan2_orange);
        tvText2.setTextColor(getResources().getColor(R.color.nav_blue));
        if (isVip != 1) { //VIP审核
            tvKeFuMsg.setText("您的资料已提交，请点击下方按钮\n联系审核员进行审核放款");
            tvKeFuMsg.setTextColor(getResources().getColor(R.color.detail_cash));
            rlNotVip.setVisibility(View.VISIBLE);
            rlVip.setVisibility(View.GONE);
        } else {//尊敬的极速钱包会员，您已进入VIP快速审核通道，我们会优先审核放款
            rlNotVip.setVisibility(View.GONE);
            rlVip.setVisibility(View.VISIBLE);
            tvKeFuMsg.setText("尊敬的极速钱包会员，您已进入VIP快速审核\n通道，我们会优先审核放款");
            tvKeFuMsg.setTextColor(getResources().getColor(R.color.color_loan_vip));
        }
        if (wxModels.size() == 0)
            requestWxCode(false);
        requestLoanLogKssh(jkID);
    }

    /**
     * 放款页面
     */
    private void initLoan() {
        llLoan.setVisibility(View.GONE);//签约
        llKeFu.setVisibility(View.GONE);//客服
        rlLoan.setVisibility(View.VISIBLE);//放款
        ivLine1.setBackgroundResource(R.mipmap.img_jiantou_orange);
        ivImage2.setBackgroundResource(R.mipmap.img_loan2_orange);
        tvText2.setTextColor(getResources().getColor(R.color.nav_blue));
        ivLine2.setBackgroundResource(R.mipmap.img_jiantou_orange);
        ivImage3.setBackgroundResource(R.mipmap.img_loan3_orange);
        tvText3.setTextColor(getResources().getColor(R.color.nav_blue));
    }

    /**
     * 签约
     */
    private void requestSign() {
        APIManager.getInstance().payment(getContext(), jkID, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    int result = response.getInt("result");
                    if (result == 1) {
                        initKeFu();
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
     * 签约下面的网页
     */
    protected String url = "http://sswl.910ok.com/LoanSup/";

    private void initWeb() {
        WebSettings webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //加载需要显示的网页
//        webView.loadUrl(UrlContents.AWARD, map);
        //设置Web视图
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.d("TAG", "URL------:" + url);
                // 判断方法1 ,注释掉方法2再测试
                if (url.contains("platformapi/startapp") || url.contains("platformapi/startApp")) {
//                    startAlipayActivity(url);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                L.d("TAG", "onPageStarted------:" + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String murl) {
                String title = view.getTitle();
//                L.d("TAG", "onPageFinished------:" + url + "============" + title);
                if (murl.equals(url)) {
//                    scroll_status.scrollTo(0,0);
                    llLoanMenu.setVisibility(View.VISIBLE);
                    llTopSign.setVisibility(View.VISIBLE);
                } else {//签约隐藏
                    llLoanMenu.setVisibility(View.GONE);
                    llTopSign.setVisibility(View.GONE);
                }
                super.onPageFinished(view, url);
            }
        });

        if (url != null) {
//            webView.loadUrl(url, MyAsyncHttpClient.getMapHeaders());
            webView.loadUrl(url);
        }
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });
    }


//    @Override
//    public boolean onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        } else {
//            return false;
//        }
//    }

    /**
     * 微信客服
     */
    private void requestWxCode(final boolean show) {
        wxModels.clear();
        APIManager.getInstance().requestHomeWxKefu(getContext(), new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                if (model != null) {
                    BaseDataListModel<WxModel> wxModelBaseDataListModel = (BaseDataListModel<WxModel>) model;
                    if (wxModelBaseDataListModel.getResult() == 1 && wxModelBaseDataListModel.getDatalist() != null
                            && wxModelBaseDataListModel.getDatalist().size() > 0) {
                        wxModels.addAll(wxModelBaseDataListModel.getDatalist());
                        if (show) {
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


    //显示还款视图
    void showHuanKuanView() {
        status_Scroll.setVisibility(View.GONE);
        scroll_none.setVisibility(View.VISIBLE);
        nav_rela.setVisibility(View.GONE);
        isJiekuan = false;
    }

    //是否有未读消息
    void haveMessage() {
        showBlackLoading();
        APIManager.getInstance().getMessage(getActivity(), pageIndex, pageSize, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
//                BaseDataListModel<MessageModel> mm = (BaseDataListModel<MessageModel>) model;
//                for (int i = 0; i < mm.getDatalist().size(); i++) {
//                    MessageModel messageModel = mm.getDatalist().get(i);
//                    if (messageModel.getIsRead() == 0) {
//                        RightIB.setBackground(getResources().getDrawable(R.mipmap.rednews));
//                        return;
//                    }
//                }
//                RightIB.setBackground(getResources().getDrawable(R.mipmap.news));
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }

    @Click
    void statusBtn() {
//        if (jkStatus==1){
//            //确认银行卡
//            CardActivity_.intent(this).LoanID(jkID).startForResult(100);
//        }
//        if (jkStatus==2){
//            //身份认证
//            IDPhotoActivity_.intent(this).loanlogID(jkID).startForResult(100);
//        }
        if (jkStatus == 3) {
            //签约
            LoanActivity_.intent(this).loanId(jkID).flag(1).startForResult(100);
        }
    }

    @OnActivityResult(100)
    void change() {
        loadLoanData();
    }

    @Click
    void LeftTV() {
        CashOrderDetailActivity_.intent(getActivity()).loanLog(jkID).start();
    }

    @Click
    void btApply() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (token == null) {//未登录
            LoginActivity_.intent(HomeFragment.this).startForResult(1000);
            return;
        }
        //0待审核 1待确认银行卡  2待确认身份证  3待签约  4放款 5待还款  6已完成  7已取消 8审核失败
        if (passStatus == 5) {
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
    }

    @Click
    void RightIB() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            MessageActivity_.intent(getActivity()).start();
        }
    }

    @Click
    void LeftIB() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            WordActivity_.intent(this).start();
        }
    }


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

    String TAG = "LoanFragment";

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadLoanData();
    }

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

    private ClassificationAdapter classificationAdapter;
    private List<ClassificationModel> classificationModels;

    private void initClassification() {
        classificationModels = new ArrayList<>();
        classificationAdapter = new ClassificationAdapter(mRecyclerView, classificationModels);
        classificationAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                WebActivity_.intent(getContext()).url(APIManager.requestKeFuUrl + "?ContactValue=0").title(classificationModels.get(position).getTitle()).start();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(classificationAdapter);
        classificationModels.add(new ClassificationModel("花呗兑现", "专门客服兑现", R.mipmap.img_home_1));
        classificationModels.add(new ClassificationModel("分期乐", "专门客服兑现", R.mipmap.img_home_2));
        classificationModels.add(new ClassificationModel("白条兑现", "专门客服兑现", R.mipmap.img_home_3));
        classificationModels.add(new ClassificationModel("更多兑现", "专门客服兑现", R.mipmap.img_home_4));
//        classificationModels.add(new ClassificationModel("急速贷款", "一小时到账", R.mipmap.img_main_res_1));
//        classificationModels.add(new ClassificationModel("公积金贷", "公积金专享", R.mipmap.img_main_res_2));
//        classificationModels.add(new ClassificationModel("信用卡贷", "信用卡专享", R.mipmap.img_main_res_3));
//        classificationModels.add(new ClassificationModel("大额专属", "纯信用无抵押", R.mipmap.img_main_res_4));
        classificationAdapter.notifyDataSetChanged();
    }

    @Click
    void llLetter() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (token == null) {//未登录
            LoginActivity_.intent(HomeFragment.this).startForResult(1000);
            return;
        }
        ClassitifyActivity_.intent(getContext())
                .bgRes(R.mipmap.img_letter_bg)
                .title("征信查询").start();
    }


    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
}
