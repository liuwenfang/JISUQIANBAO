package com.ahxbapp.jsqb.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.base.MyJsonResponse;
import com.ahxbapp.common.base.MyJsonResponseNew;
import com.ahxbapp.common.model.PageInfo;
import com.ahxbapp.common.network.MyAsyncHttpClient;
import com.ahxbapp.common.util.EncryptUtils;
import com.ahxbapp.jsqb.model.AlipayModel;
import com.ahxbapp.jsqb.model.AuthenModel;
import com.ahxbapp.jsqb.model.BankModel;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.BaseFeeModel;
import com.ahxbapp.jsqb.model.BaseModel;
import com.ahxbapp.jsqb.model.CashModel;
import com.ahxbapp.jsqb.model.ComClassModel;
import com.ahxbapp.jsqb.model.ContactModel;
import com.ahxbapp.jsqb.model.CouponCashModel;
import com.ahxbapp.jsqb.model.FAQModel;
import com.ahxbapp.jsqb.model.JobMedel;
import com.ahxbapp.jsqb.model.MessageModel;
import com.ahxbapp.jsqb.model.MobileModel;
import com.ahxbapp.jsqb.model.OrderModel;
import com.ahxbapp.jsqb.model.OtherLogin;
import com.ahxbapp.jsqb.model.PersonDataModel;
import com.ahxbapp.jsqb.model.PersonModel;
import com.ahxbapp.jsqb.model.Province;
import com.ahxbapp.jsqb.model.TaoBaoModel;
import com.ahxbapp.jsqb.model.User;
import com.ahxbapp.jsqb.model.VersionModel;
import com.ahxbapp.jsqb.model.WxModel;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.ahxbapp.jsqb.utils.StringUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by gravel on 16/7/21.
 */
public class APIManager {
    //学信网认证
    private static final String IsLNet_url = Global.HOST + "home/IsLNet";
    //获取图片验证码
    private static final String getLNetInfo_url = Global.HOST + "home/GetLNetInfo";
    //第三方账号注册
    private static final String otherReg_url = Global.HOST + "Home/ThirdLogin";
    //绑定
    private static final String binding_url = Global.HOST + "home/IsBindMobile";
    //第三方登录
    private static final String otherLogin_url = Global.HOST + "Home/ISThirdLogin";
    private static final String verifyPhone3 = Global.HOST + "Home/SendCode3";
    //获取开户行
    public static final String bank_url = Global.HOST + "home/BankList";
    //获取学历
    public static final String education_url = Global.HOST + "home/EduList";
    //获取居住时长
    public static final String address_url = Global.HOST + "home/LiveList";
    //获取亲属关系
    public static final String family_url = Global.HOST + "home/RelaList";
    //获取社会关系
    public static final String social_url = Global.HOST + "home/SocList";
    //获取职位
    public static final String job_url = Global.HOST + "home/PostList";
    //获取月收入
    public static final String income_url = Global.HOST + "home/IncomeList";
    //获取子女
    public static final String child_url = Global.HOST + "home/CNumList";
    //获取开户省市
    public static final String city_url = Global.HOST + "home/XT_Area";
    //银行卡认证
    private static final String card_url = Global.HOST + "home/IsBank";
    //    private static final String card_url = Global.BASEURL + "Home/IsBank";
    //获取借款天数
    private static final String getLoanDays_url = Global.HOST + "home/TermList";
    //提交借款申请
    private static final String submitLoan_url = Global.HOST + "home/LoadSave";
    //根据本金、时间计算息费
    private static final String getInterest_url = Global.HOST + "home/Getfee";
    //获取基本费用
    private static final String getBaseFee_url = Global.HOST + "home/Basicfee";
    //获取借款金额
    private static final String getLoanMoeny_url = Global.HOST + "home/LoanList";
    //获取个人信息
    private static final String getPerData_url = Global.HOST + "Member/GetPerson";
    //获取常见问题
    private static final String getProblem_url = Global.HOST + "Member/Problem";
    //添加留言
    private static final String addLyMsg_Url = Global.HOST + "Member/lyMsgAdd";

    //获取客服电话机服务时间
    private static final String getSerMobile_url = Global.HOST + "Member/GetSerMobile";

    //获取认证状态
    private static final String getAuthenStatus_url = Global.HOST + "home/Authen";

    //修改手机号
    private static final String updatePhone_url = Global.HOST + "Home/UpPhone";
    //修改邮箱
    private static final String updateEmail_url = Global.HOST + "Home/UpEmail";
    //修改微信号
    private static final String updateWeixin_url = Global.HOST + "Home/UpWeiXin";
    //修改登录密码
    private static final String updatePwd_url = Global.HOST + "Home/UpPwd";
    //用户帮助和关于我们
    private static final String getNews_url = Global.HOST + "Member/GetAgree";
    //用户反馈
    private static final String addFeedback_url = Global.HOST + "Home/AddFeedback";
    //联系我们
    private static final String contactUS_url = Global.HOST + "api/contact";

    private static final String modifyPassword_url = Global.HOST + "Default/UpPwd";
    //登录
    private static final String Login_url = Global.HOST + "Home/login";
    //注册
    private static final String register_url = Global.HOST + "Home/reg";
    private static final String user_suggestBack_url = Global.HOST + "Home/AddFeedback";
    private static final String answer_add_url = Global.HOST + "api/AddPutMsg";
    private static final String user_update = Global.HOST + "Home/UserMody";
    //上传头像
    private static final String Uploadhead_url = Global.HOST + "Home/Uploadhead";
    private static final String upload_url = Global.HOST + "api/UploadPic";
    private static final String user_message = Global.HOST + "Home/MsgList";
    private static final String config_news_url = Global.HOST + "news/index";
    private static final String config_getShiYiTu_url = Global.HOST + "api/Getdiagram";

    //获取用户信息详情
    private static final String users_url = Global.HOST + "Home/users";
    private static final String answer_PutMsgList = Global.HOST + "api/PutMsgList";
    private static final String merchant_getList = Global.HOST + "api/GetMerchList";
    private static final String merchant_getDetail = Global.HOST + "api/MerchDetail";
    //找回密码的验证码
    private static final String verifyPhone2 = Global.HOST + "Home/SendCode2";
    //获取注册验证码
    private static final String verifyPhone = Global.HOST + "Home/SendCode";
    //找回密码验证
    private static final String resetQR = Global.HOST + "Home/ResetQR";
    //忘记密码
    private static final String resetPwd = Global.HOST + "Home/ResetPwd";
    //获取收货地址列表
    private static final String receiptAddress = Global.HOST + "Home/AddressList";
    //设为默认地址
    private static final String isDefault = Global.HOST + "api/SetIsDefult";
    //删除收货地址
    private static final String deleteAddr = Global.HOST + "api/DeleteAddr";
    //添加收货地址
    private static final String addAddr = Global.HOST + "Home/EditAddress";
    //根据ID获取收货地址
    private static final String getAddrByID = Global.HOST + "Home/GetByAddress";
    //获取手机运营商
    private static final String phoneConfig = Global.HOST + "Home/phoneConfig";
    //发送短信验证码
    private static final String url_sendLoginSmsCode = Global.HOST + "Home/sendLoginSmsCode";

    //更换手机号码
    private static final String url_sendTwoCode = Global.HOST + "Home/sendTwoCode";

    private static final String newLogin = Global.HOST + "lg/loginForm.html?random=0.09867052540098808";

    private static final String checkPhonePsd = Global.HOST + "Home/checkPhonePsd";
    //获取省市区
    private static final String getPro_city_area = Global.HOST + "Home/CityList";
    private static final String getDesign = Global.HOST + "api/Design";
    private static final String deleteDesign = Global.HOST + "api/DelDesign";
    //获取我的消息
    private static final String getMessage = Global.HOST + "Member/Msg";
    //获取订单列表
    private static final String getOrderList = Global.HOST + "api/orderlist";
    //取消订单
    private static final String cancelOrder = Global.HOST + "api/Quorder";
    //删除订单
    private static final String deleteOrder = Global.HOST + "api/delorder";
    //确认收货
    private static final String confirmReceive = Global.HOST + "api/ConfirmPro";
    //获取订单详情
    private static final String OrderDetail = Global.HOST + "api/orderdetail";
    //获取购物车列表
    private static final String getCartList = Global.HOST + "api/Shopping";
    //商品加减
    private static final String updateNum = Global.HOST + "api/UpShopNum";
    //获取配送方式
    private static final String getSendMethod = Global.HOST + "api/DistrStyleList";
    //获取优惠券
    private static final String getCoupon = Global.HOST + "api/CouponList";
    //下单
    private static final String generate_order = Global.HOST + "api/AddOrder";
    //修改个人信息
    private static final String update_info = Global.HOST + "Home/UserMody";
    //首页轮播图
    private static final String getBannerList = Global.HOST + "Home/Bannerlist";
    //通讯录
    private static final String phoneDirectory = Global.HOST + "Home/PhoneDirectory";

    private static final String diy_getComClassList = Global.HOST + "api/ComClass";
    private static final String diy_commodityList = Global.HOST + "api/commodityList";
    private static final String diy_getBackClass = Global.HOST + "Home/BackClass";
    private static final String diy_getBackList = Global.HOST + "Home/BackList";

    private String strPageIndex = "pageIndex", strPageSize = "pageSize";

    public static String exchargeUrl = Global.HOST + "home/exchange";
    public static String requestKeFuUrl = Global.HOST + "Home/Contact_Img";
//    public static String requestKeFuUrl = Global.HOST + "home/Customer";

    public static int IDENTIFIER_VALUE=8;

    /**
     * 银行卡还款
     */
    public static String requestBankPay = Global.HOST + "home/LLsub";

    private static APIManager manager = null;

    public static synchronized APIManager getInstance() {
        if (manager == null) {
            manager = new APIManager();
        }
        return manager;
    }

    private String bannerUrl = Global.HOST + "Member/GetHomePic";
//    private String bannerUrl = "http://ts.910ok.com/Member/GetHomePic";

    public void requestBanner(final Context context, final APIManagerInterface.aliPay baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                if (response != null) {
                    try {
                        baseBlock.Success(context, response.getString("data"), null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put("phoneType", 2);
        client.post(context, bannerUrl, params, jsonResponse);
    }

    /**
     * 通讯录
     */
    public void getPhoneDirectory(final Context context, String directory, String sms, String call, final APIManagerInterface.getContact_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("directory", directory); //姓名和手机号的json
        params.put("sms", sms);
        params.put("call", call);
        params.put("os", String.valueOf("1")); //Android传1
        client.post(context, phoneDirectory, params, parseJson);
    }

    /**
     * 获取最新版本
     */
    private static final String get_version = Global.HOST + "Home/GetMaxVersion";

    public void get_version(final Context context, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    VersionModel versionModel = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), VersionModel.class);
                    BaseDataListModel<VersionModel> model = new BaseDataListModel<>();
                    model.setMessage(response.getString("message"));
                    model.setResult(response.getInt("result"));
                    model.setModel(versionModel);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
//                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("Type", "1");
        params.put("Identifier", 20);
        client.post(context, get_version, params, parseJson);
    }

    /**
     * 身份证认证
     * 显示身份证照片
     */
    private static final String getIdentity_url = Global.HOST + "Member/GetIdentity";

    public void Member_GetIdentity(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, getIdentity_url, params, parseJson);
    }

    /**
     * 上传身份证照片
     */
//    private static final String uploadID_url = Global.HOST + "Home/upTest";
    private static final String uploadID_url = Global.HOST + "Home/UploadID";

    public void user_sfzPhoto(final Context context, File file, int flag, int loanlogID, int type, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };

        try {
            if (flag == 1) {
                //正面
                params.put("ZIDPic", file);
            } else if (flag == 2) {
                //反面
                params.put("FIDPic", file);
            } else if (flag == 3) {
                //自拍
                params.put("IDPic", file);
            }
        } catch (FileNotFoundException e) {
            block.Failure(context, null);
        }
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("LoanlogID", loanlogID); //借款id
        params.put("type", type); //
        client.post(context, uploadID_url, params, parseJson);
    }

    /**
     * 确认打款
     */
    private static final String url_payment = Global.HOST + "Member/payment";

    public void payment(final Context context, int loanID, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("ID", loanID);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_payment, params, parseJson);
    }

    /**
     * 签约显示
     */
    private static final String url_singed = Global.HOST + "Member/Sign";

    public void singedShow1(final Context context, int loanID, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("ID", loanID);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_singed, params, parseJson);
    }

    /**
     * 快速审核费收取功能
     */
    private static final String requestLoanLogKssh = Global.HOST + "Home/LoanLogKssh";

    public void requestLoanLogKssh(final Context context, int loanID, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    if (response != null) {
                        if (response.getInt("result") == 1) {
                            block.Success(context, response);
                        } else {
                            block.Failure(context, response);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("ID", loanID);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requestLoanLogKssh, params, parseJson);
    }

    /**
     * 快速签约费收取功能
     */
    private static final String requestLoanlogKSQY = Global.HOST + "Home/LoanlogKSQY";

    public void requestLoanlogKSQY(final Context context, int loanID, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);
                try {
                    if (response != null) {
                        if (response.getInt("result") == 1) {
                            block.Success(context, response);
                        } else {
                            block.Failure(context, response);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("ID", loanID);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requestLoanlogKSQY, params, jsonResponse);
    }

    /**
     * 贷超
     */
    private static final String requestLoanSuperInfo = Global.HOST + "Home/GetLoanSuperInfo";

    public void requestLoanSuperInfo(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);
                try {
                    if (response != null) {
                        if (response.getInt("result") == 1) {
                            block.Success(context, response);
                        } else {
                            block.Failure(context, response);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requestLoanSuperInfo, params, jsonResponse);
    }


    /**
     * 活动
     */
    private static final String requestActivityInfo = Global.HOST + "Home/HomeActivityInfo";

    public void requestActivityInfo(final Context context, final APIManagerInterface.baseBlock block) {
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);
                try {
                    if (response != null) {
                        if (response.getInt("result") == 1) {
                            block.Success(context, response);
                        } else {
                            block.Failure(context, response);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        client.post(context, requestActivityInfo, null, jsonResponse);
    }

    /**
     * 签约信息展示
     */
    private static final String requestParamValue = Global.HOST + "Home/GetParamValue";

    public void requestParamValue(final Context context, final APIManagerInterface.requestMobile baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
//                super.onMySuccess(response);
                try {
                    if (response != null) {
                        String result = response.getString("result");
                        if (result != null && !result.equals("")) {
                            baseBlock.Success(context, result);
                        } else {
                            baseBlock.Failure(context, "");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
//                super.onMyFailure(response);
                if (response != null) {
                    try {
                        if (response != null) {
                            String result = response.getString("result");
                            if (result != null && !result.equals("")) {
                                baseBlock.Success(context, result);
                            } else {
                                baseBlock.Failure(context, "");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put("ParamCode", 3001);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requestParamValue, params, jsonResponse);
    }

    /**
     * 显示银行卡信息
     */
    private static final String url_bankInfo = Global.HOST + "home/BankDisplay";

    public void bankInfo(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_bankInfo, params, parseJson);
    }

    /**
     * 查看借款详情
     */
    private static final String url_LoanDetail = Global.HOST + "Member/LoanDetail";

    public void LoanDetail1(final Context context, String loanlogID, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("LoanlogID", loanlogID);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_LoanDetail, params, parseJson);
    }

    /**
     * 判断当前用户是否有未完成的订单
     */
    private static final String url_isNoComplate = Global.HOST + "Member/NoComplate";

    public void Member_isNoComplate(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_isNoComplate, params, parseJson);
    }

    /**
     * 消息已读
     */
    private static final String url_YRead = Global.HOST + "Member/YRead";

    public void Member_YRead1(final Context context, String messageID, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };

        params.put("MsgID", messageID);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_YRead, params, parseJson);
    }

    /**
     * 单条借款详情
     */
    private static final String url_LoanLog = Global.HOST + "Member/LoanLog";

    public void Member_LoanLog(final Context context, String loanLogID, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {

                    CashModel model = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), CashModel.class);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("loanLogID", loanLogID);
        client.post(context, url_LoanLog, params, parseJson);
    }


    /**
     * 查看合同
     */
    private static final String url_contract = Global.HOST + "Member/Contract";

    public void Member_Contract(final Context context, String loanLogID, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("loanLogID", loanLogID);
        params.put("Identifier", IDENTIFIER_VALUE);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_contract, params, parseJson);
    }

    /**
     * 获取用户淘宝网信息
     */
    private static final String url_taobaoadd = Global.HOST + "Home/AddTBInfo";

    public void taobao_add(final Context context, List<TaoBaoModel> list, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };

        try {
            params.put("jsonString", URLEncoder.encode(com.alibaba.fastjson.JSONObject.toJSONString(list), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_taobaoadd, params, parseJson);
    }

//    /**
//     * 获取用户支付宝信息
//     */
//    private static final String url_zfbAdd = Global.HOST + "Home/GetContent";

//    public void alipay_add(final Context context, String htmlstr, String htmlstr1, String htmlstr2, final APIManagerInterface.baseBlock block) {
//        RequestParams params = new RequestParams();
//        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
//        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
//            @Override
//            public void onMySuccess(JSONObject response) {
//                block.Success(context, response);
//            }
//
//            @Override
//            public void onMyFailure(JSONObject response) {
//                super.onMyFailure(response);
//                block.Failure(context, response);
//            }
//        };
//        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
//        params.put("htmlstr", htmlstr);//花呗
//        params.put("htmlstr1", htmlstr1);//账单
//        params.put("htmlstr2", htmlstr2);//个人信息
//        client.post(context, url_zfbAdd, params, parseJson);
//    }
    /**
     * 获取用户支付宝信息
     */
    private static final String url_zfbAdd = Global.HOST + "Home/AddZFBInfo";

    public void alipay_add(final Context context, List<AlipayModel> list, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponseNew parseJson = new MyJsonResponseNew(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };

        try {
            params.put("jsonString", URLEncoder.encode(com.alibaba.fastjson.JSONObject.toJSONString(list), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url_zfbAdd, params, parseJson);
    }


    /**
     * 学信网认证
     */
    public void home_IsLNet(final Context context, String Acc, String Psd, String YZM, String lt, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("LNetAct", Acc);
        params.put("LNetPsd", Psd);
        params.put("yzm", YZM);
        params.put("lt", lt);
        client.post(context, IsLNet_url, params, parseJson);
    }

    /**
     * 获取图片验证码
     */
    public void home_getcodeImageUrl(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, getLNetInfo_url, params, parseJson);
    }

    /**
     * 绑定验证码
     */
    public void verifyPhone3(final Context context, String mobile, final APIManagerInterface.checkPhonePsd_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("Mobile", mobile);
        params.put("MobilePwd", encryption(mobile));
        client.post(context, verifyPhone3, params, parseJson);
    }

    private String encryption(String mobile) {
//        String[] split = NetworkUtils.getIPAddress(true).split("\\.");
//        //IP地址的最后一位
//        String ip = split[split.length - 1];
        String msg = mobile + "|" + "_sc901@Pwmd5";
        String encryption = EncryptUtils.MD5(msg);
        return encryption;
    }

    /**
     * 三方注册
     */
    public void login_otherReg(final Context context, OtherLogin login, final APIManagerInterface.login_otherLogin block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMyFailure(JSONObject response) {
                block.Failure(context, response);
            }

            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    String token = response.getString("token") != null ? response.getString("token") : "";
                    block.Success(context, response.getInt("result"), token, response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }
        };
        params.put("type", login.getType());
        params.put("openid", login.getOpenid());
        params.put("name", login.getName());
        params.put("sex", login.getSex());
        params.put("head", login.getHead());
        params.put("Mobile", login.getMobile());
        params.put("Code", login.getCode());
        params.put("Psd", login.getPassword());
        client.post(context, otherReg_url, params, parseJson);
    }

    /**
     * 绑定
     */
    public void login_binding(final Context context, OtherLogin login, final APIManagerInterface.login_otherLogin block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMyFailure(JSONObject response) {
                block.Failure(context, response);
            }

            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    String token = response.getString("token") != null ? response.getString("token") : "";
                    block.Success(context, response.getInt("result"), token, response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }
        };
        params.put("type", login.getType());
        params.put("openid", login.getOpenid());
        params.put("Mobile", login.getMobile());
        params.put("Code", login.getCode());
        client.post(context, binding_url, params, parseJson);
    }

    /**
     * 第三方登录
     */
    public void login_otherLogin(final Context context, OtherLogin login, final APIManagerInterface.login_otherLogin block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMyFailure(JSONObject response) {

                block.Failure(context, response);
            }

            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    String token = response.getString("token") != null ? response.getString("token") : "";
                    block.Success(context, response.getInt("result"), token, response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }
        };
        params.put("type", login.getType());
        params.put("openid", login.getOpenid());
        client.post(context, otherLogin_url, params, parseJson);
    }


    /**
     * 获取借款天数
     */
    public void home_getLoanDays(final Context context, final APIManagerInterface.common_FAQlist block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {

                    List<BankModel> list = com.alibaba.fastjson.JSONObject.parseArray(response.getString("dataList"), BankModel.class);

                    block.Success(context, list);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        client.post(context, getLoanDays_url, params, parseJson);
    }

    /**
     * 提交借款申请
     */
    public void home_submitLoan(final Context context, int loanId, int termId, int coupID, BaseFeeModel feeModel, float lixiFee, float backM, final APIManagerInterface.common_wordBack block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("LoanId", loanId);
        params.put("TermId", termId);
        params.put("CoupID", coupID);
        params.put("Applyfee", feeModel.getApplyfee());
        params.put("Userfee", feeModel.getUserfee());
        params.put("Interest", lixiFee);
        params.put("BackM", backM);
        client.post(context, submitLoan_url, params, parseJson);
    }

    /**
     * 根据本金、时间计算息费
     */
    public void home_getInterest(final Context context, int loanId, int termId, final APIManagerInterface.loanFeeBack block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    float interest = Float.parseFloat(response.getDouble("Interest") + "");
                    float applyfee = Float.parseFloat(response.getDouble("Applyfee") + "");
                    float userfee = Float.parseFloat(response.getDouble("Userfee") + "");
                    block.Success(context, interest, applyfee, userfee);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("LoanId", loanId);
        params.put("TermId", termId);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, getInterest_url, params, parseJson);
    }

    /**
     * 获取基本费用
     */
    public void home_getBaseFee(final Context context, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {

                    BaseFeeModel model = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), BaseFeeModel.class);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, getBaseFee_url, params, parseJson);
    }

    /**
     * 获取借款金额
     */
    public void home_getLoanMoney(final Context context, final APIManagerInterface.common_FAQlist block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {

                    List<BankModel> list = com.alibaba.fastjson.JSONObject.parseArray(response.getString("dataList"), BankModel.class);

                    block.Success(context, list);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, getLoanMoeny_url, params, parseJson);
    }

    /**
     * 获取个人信息
     */
    public void member_getPerData(final Context context, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {

                    PersonDataModel model = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), PersonDataModel.class);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, getPerData_url, params, parseJson);
    }

    /**
     * 获取常见问题
     */
    public void member_getProblem(final Context context, final APIManagerInterface.common_FAQlist block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<FAQModel> list = com.alibaba.fastjson.JSONObject.parseArray(response.getString("dataList"), FAQModel.class);

                    block.Success(context, list);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        client.post(context, getProblem_url, params, parseJson);
    }

    /**
     * 账户类型认证
     */
    public void Home_AccountCer(final Context context, int type, String firstString, String secondString, String cardNo, String LoanlogID, String PosID, String IncID, final APIManagerInterface.common_wordBack block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };

        String url = "";
        switch (type) {
            case 1:
                //学信网
                url = Global.HOST + "home/IsLNet";
                params.put("LNetAct", firstString);
                params.put("LNetPsd", secondString);
                break;
            case 2:
                //基本身份认证
                //外网服务器
                url = Global.HOST + "home/Sign_llp_new";
//                url = Global.HOST + "home/IsID";
                //内网服务器
//                url = Global.BASEURL + "Home/IsID";
                params.put("TrueName", firstString);
                params.put("Identity", secondString);
                params.put("CardNo", cardNo);
                params.put("flag_chnl", 0);
//                url = Global.HOST + "home/IsID";
//                //内网服务器
////                url = Global.BASEURL + "Home/IsID";
//                params.put("TrueName", firstString);
//                params.put("Identity", secondString);
//                params.put("LoanlogID", LoanlogID.length() > 0 ? LoanlogID : "0");
//                params.put("PosID", PosID.length() > 0 ? PosID : "0");
//                params.put("IncID", IncID.length() > 0 ? IncID : "0");
//                params.put("CardNo", cardNo);
                break;
            case 3:
                //支付宝
                url = Global.HOST + "home/IsPay";
                params.put("PayAct", firstString);
                params.put("PayPsd", secondString);
                break;
            case 4:
                //芝麻信用
                url = Global.HOST + "Member/zmrz";
                params.put("name", firstString);
                params.put("card_no", secondString);
                break;
            case 5:
                //淘宝
                url = Global.HOST + "home/IsTabao";
                params.put("TaoBao", firstString);
                params.put("TBPsd", secondString);
                break;
        }
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url, params, parseJson);
    }

    /**
     * 实名认证
     */
    public void Home_AuthenFrist(final Context context, String firstString, String secondString, String cardNo, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };

        String url = Global.HOST + "home/SMRZForApp";
        params.put("TrueName", firstString);
        params.put("Identity", secondString);
        params.put("CardNo", cardNo);
        params.put("flag_chnl", 0);
        params.put("Identifier", IDENTIFIER_VALUE);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url, params, parseJson);
    }

    private String requestIdStatus = Global.HOST + "home/getID2";

    /**
     * 获取实名认证的方式
     */
    public void requestIdStatus(final Context context, final APIManagerInterface.common_wordBack block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    if (response != null) { // result 1获取url参数，进入网页   0 签约
                        int result = response.getInt("result");
                        if (result == 1) {
                            String url = response.getString("url");
                            block.Success(context, result, url);
                        } else {
                            block.Success(context, result, "");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                try {
                    if (response != null && response.getInt("result") == 0) { // resuit 1获取url参数，进入网页   0 签约
                        block.Success(context, 0, "");
                    } else {
                        block.Failure(context, response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requestIdStatus, params, parseJson);
    }

    /**
     * 提示信息
     */
    public void requestPrompt(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        String url = Global.HOST + "home/selLLp";
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url, params, parseJson);
    }

    /**
     * 芝麻认证
     */
    public void requestZM(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        String url = Global.HOST + "home/zmOpen";
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url, params, parseJson);
    }

    private String bankUrl = Global.HOST + "home/Sign_llp_return_new";

    /**
     * 银行卡认证
     */
    public void requestBank(final Context context, String json, final APIManagerInterface.common_wordBack block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("json", json);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, bankUrl, params, parseJson);
    }

    private String checkBank = Global.HOST + "home/checkLLNo";

    public void requestCheckBank(final Context context, final APIManagerInterface.common_wordBack block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, checkBank, params, parseJson);
    }


    /**
     * 添加留言
     */
    public void member_addLyMsg(final Context context, String content, final APIManagerInterface.common_wordBack block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("Content", content);
        client.post(context, addLyMsg_Url, params, parseJson);
    }

    /**
     * 获取客服电话机服务时间
     */
    public void member_getSerMobile(final Context context, final APIManagerInterface.no_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getJSONObject("data").getString("Servtime"), response.getJSONObject("data").getString("Tel"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };

        client.post(context, getSerMobile_url, params, parseJson);
    }

    /**
     * 获取所有认证
     */
    public void home_getAuthenStatus(final Context context, final int chek, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    AuthenModel model = com.alibaba.fastjson.JSONObject.parseObject(response.getString("dataList"), AuthenModel.class);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        Log.d("TAG", " TOKEN  : " + PrefsUtil.getString(context, Global.TOKEN));
        params.put("chek", chek);
        client.post(context, getAuthenStatus_url, params, parseJson);
    }

    /**
     * 修改手机号
     */
    public void person_updatePhone(final Context context, String mobile, String code, final APIManagerInterface.no_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"), response.getString("enmessage"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("Mobile", mobile);
        params.put("Code", code);
        client.post(context, updatePhone_url, params, parseJson);
    }

    /**
     * 修改密码
     */
    public void person_updatePwd(final Context context, String Ypwd, String Pwd, final APIManagerInterface.no_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("Ypwd", Ypwd);
        params.put("Pwd", Pwd);
        client.post(context, updatePwd_url, params, parseJson);
    }

    /**
     * 用户帮助和关于我们
     */
    public void person_getNews(final Context context, String ID, final APIManagerInterface.config_getNews block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {

                    String content = response.getString("data");
                    block.Success(context, content);

                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("CategoriesID", ID);
        params.put("Identifier", IDENTIFIER_VALUE);
        client.post(context, getNews_url, params, parseJson);
    }

    /**
     * 用户反馈
     */
    public void person_addFeedback(final Context context, String Phone, String QQ, String Content, String Email, final APIManagerInterface.no_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {

                    block.Success(context, response.getInt("result"), response.getString("message"), response.getString("enmessage"));

                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("Phone", Phone);
        params.put("QQ", QQ);
        params.put("Email", Email);
        params.put("Content", Content);
        client.post(context, addFeedback_url, params, parseJson);
    }

    /**
     * 登录
     */
    public void login(final Context context, String mobile, String pass, final APIManagerInterface.login_otherLogin block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString(Global.TOKEN), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("Mobile", mobile);
        params.put("Psd", pass);
        params.put("Identifier", IDENTIFIER_VALUE);
        client.post(context, Login_url, params, parseJson);
    }

    /**
     * 获取用户信息详情
     */
    public void user_getUserInfo(final Context context, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    User user = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), User.class);
                    block.Success(context, user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, users_url, params, parseJson);
    }

    //获取注册验证码
    public void verifyPhone(final Context context, String mobile, final APIManagerInterface.checkPhonePsd_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("Mobile", mobile);
        params.put("Identifier", IDENTIFIER_VALUE);
        params.put("MobilePwd", encryption(mobile));
        client.post(context, verifyPhone, params, parseJson);
    }

    //注册
    public void register(final Context context, String phone, String code, String password, final APIManagerInterface.checkPhonePsd_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("Mobile", phone);
        params.put("Code", code);
        params.put("Psd", password);
        params.put("os", "1"); //android 传1
        params.put("Identifier", IDENTIFIER_VALUE);
        client.post(context, register_url, params, parseJson);
    }


    //找回密码的验证码
    public void verifyPhone1(final Context context, String mobile, final APIManagerInterface.checkPhonePsd_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("Mobile", mobile);
        params.put("MobilePwd", encryption(mobile));
        client.post(context, verifyPhone2, params, parseJson);
    }

    //找回密码验证
    public void resetQR(final Context context, String phone, String code, final APIManagerInterface.checkPhonePsd_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("Mobile", phone);
        params.put("Code", code);
        client.post(context, resetQR, params, parseJson);
    }

    //忘记密码
    public void resetPwd(final Context context, String phone, String pass, final APIManagerInterface.checkPhonePsd_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put("Mobile", phone);
        params.put("Psd", pass);
        client.post(context, resetPwd, params, parseJson);
    }


    //获取我的消息
    public void getMessage(final Context context, int PageIndex, int PageSize, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<MessageModel> models = com.alibaba.fastjson.JSONObject.parseArray(response.getString("data"), MessageModel.class);
                    BaseDataListModel<MessageModel> model = new BaseDataListModel<>();
                    model.setResult(response.getInt("result"));
                    model.setMessage(response.getString("message"));
                    model.setDatalist(models);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("PageIndex", PageIndex);
        params.put("PageSize", PageSize);
        client.post(context, getMessage, params, parseJson);
    }


    //获取订单列表
    public void getOrderList(final Context context, int pageIndex, int pageSize, int State, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<OrderModel> list = com.alibaba.fastjson.JSONObject.parseArray(response.getString("dataList"), OrderModel.class);
                    BaseDataListModel<OrderModel> model = new BaseDataListModel<>();
                    model.setResult(response.getInt("result"));
                    model.setMessage(response.getString("message"));
                    model.setDatalist(list);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        params.put("State", State);
        client.post(context, getOrderList, params, parseJson);
    }

    //取消订单
    public void cancelOrder(final Context context, String orderNo, final APIManagerInterface.no_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"), response.getString("enmessage"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("OrderNo", orderNo);
        client.post(context, cancelOrder, params, parseJson);
    }

    //删除订单
    public void deleteOrder(final Context context, String orderNo, final APIManagerInterface.no_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"), response.getString("enmessage"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("OrderNo", orderNo);
        client.post(context, deleteOrder, params, parseJson);
    }

    public void diy_getComClassList(final Context context, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<ComClassModel> list = com.alibaba.fastjson.JSONArray.parseArray(response.getString("dataList"), ComClassModel.class);
                    BaseDataListModel<ComClassModel> model = new BaseDataListModel<>();
                    model.setResult(response.getInt("result"));
                    model.setMessage(response.getString("message"));
                    model.setDatalist(list);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        client.post(context, diy_getComClassList, params, parseJson);
    }

    public void bank_list(final Context context, String url, final APIManagerInterface.common_object bank_black) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<BankModel> bankModels = com.alibaba.fastjson.JSONObject.parseArray(response.getString("dataList"), BankModel.class);
                    BaseDataListModel<BankModel> baseDataListModel = new BaseDataListModel<>();
                    baseDataListModel.setResult(response.getInt("result"));
                    baseDataListModel.setMessage(response.getString("message"));
                    baseDataListModel.setDatalist(bankModels);
                    bank_black.Success(context, baseDataListModel);
                } catch (JSONException e) {
                    bank_black.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                bank_black.Failure(context, response);
            }
        };
        params.put("", "");
        client.post(context, url, params, jsonResponse);
    }

    public void get_city(final Context context, final APIManagerInterface.common_object city_model) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<Province> provinceModels = com.alibaba.fastjson.JSONObject.parseArray(response.getString("dataList"), Province.class);
                    BaseDataListModel<Province> baseModel = new BaseDataListModel<>();
                    baseModel.setResult(response.getInt("result"));
                    baseModel.setMessage(response.getString("message"));
                    baseModel.setDatalist(provinceModels);
                    city_model.Success(context, baseModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                city_model.Failure(context, response);
            }
        };
        params.put("", "");
        client.post(context, city_url, params, jsonResponse);
    }

    //银行卡信息认证
    public void card_var(final Context context, int bankID, int bProID,
                         int bCityID, final String resMobile, String cardNo, String name, int loanlogID, String SFZNO, final APIManagerInterface.common_wordBack card_model) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    card_model.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    card_model.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                card_model.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("BankID", bankID);
        params.put("BProID", bProID);
        params.put("BCityID", bCityID);
        params.put("ResMobile", resMobile);
        params.put("CardNo", cardNo);
        params.put("TrueName", name);
        params.put("LoanlogID", loanlogID);
        params.put("SFZNo", SFZNO);
        client.post(context, card_url, params, jsonResponse);
    }

    //职位认证
    private static final String job_var_url = Global.HOST + "home/Position";

    public void job_var(final Context context, int posID, int incID, String company,
                        int proID, int cityID, String comAddr, String comMobile,
                        final APIManagerInterface.common_wordBack job_model) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    job_model.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    job_model.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                job_model.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("PosID", posID);
        params.put("IncID", incID);
        params.put("Company", company);
        params.put("ProID", proID);
        params.put("CityID", cityID);
        params.put("ComAddr", comAddr);
        params.put("ComMobile", comMobile);
        client.post(context, job_var_url, params, jsonResponse);
    }

    //联系人认证
    private static final String contact_url = Global.HOST + "home/Contact";

    public void contact_ver(final Context context, int relaID, String relaMobile, int socID,
                            String socMobile, String relaName, String socName, final APIManagerInterface.common_wordBack contact_result) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    contact_result.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    contact_result.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                contact_result.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("RelaID", relaID);
        params.put("RelaMobile", relaMobile);
        params.put("SocID", socID);
        params.put("SocMobile", socMobile);
        params.put("RelaName", relaName);
        params.put("SocName", socName);
        client.post(context, contact_url, params, jsonResponse);
    }

    //个人信心认证
    private static final String person_url = Global.HOST + "home/Perinformat";

    public void person_ver(final Context context, String qq, String email, int eduID,
                           int isMarry, int childNum, String addr, int liveID, final APIManagerInterface.common_wordBack person_result) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    person_result.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    person_result.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                person_result.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("QQ", qq);
        params.put("Email", email);
        params.put("EduID", eduID);
        params.put("IsMarry", isMarry);
        params.put("ChildNumID", childNum);
        params.put("Addr", addr);
        params.put("LiveID", liveID);
        client.post(context, person_url, params, jsonResponse);
    }

    //个人信息显示
    public static final String person_message_url = Global.HOST + "home/PerDispaly";

    public void personMessage(final Context context, String url, final APIManagerInterface.common_object person_model) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    PersonModel model = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), PersonModel.class);
                    BaseModel<PersonModel> personModel = new BaseModel<>();
                    personModel.setResult(response.getInt("result"));
                    personModel.setMessage(response.getString("message"));
                    personModel.setData(model);
                    person_model.Success(context, personModel);
                } catch (JSONException e) {
                    person_model.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                person_model.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url, params, jsonResponse);
    }

    //显示职位信息
    public static final String show_job_url = Global.HOST + "home/PosDispaly";

    public void show_job(final Context context, String url, final APIManagerInterface.common_object show_job) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    JobMedel model = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), JobMedel.class);
                    BaseModel<JobMedel> jobModel = new BaseModel<>();
                    jobModel.setResult(response.getInt("result"));
                    jobModel.setMessage(response.getString("message"));
                    jobModel.setData(model);
                    show_job.Success(context, jobModel);
                } catch (JSONException e) {
                    show_job.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                show_job.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, url, params, jsonResponse);
    }

    //联系人信息显示
    private static final String show_contact_url = Global.HOST + "home/ConDispaly";

    public void show_contact(final Context context, final APIManagerInterface.common_object contact_model) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    ContactModel model = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), ContactModel.class);
                    BaseModel<ContactModel> jobModel = new BaseModel<>();
                    jobModel.setResult(response.getInt("result"));
                    jobModel.setMessage(response.getString("message"));
                    jobModel.setData(model);
                    contact_model.Success(context, jobModel);
                } catch (JSONException e) {
                    contact_model.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                contact_model.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, show_contact_url, params, jsonResponse);
    }

    //获取手机号码和运营商
    private static final String operators_url = Global.HOST + "home/MobileL";

    public void operators(final Context context, final APIManagerInterface.common_object operators_model) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    MobileModel mobileModel = com.alibaba.fastjson.JSONObject.parseObject(response.getString("data"), MobileModel.class);
                    BaseModel<MobileModel> baseModel = new BaseModel<>();
                    baseModel.setResult(response.getInt("result"));
                    baseModel.setMessage(response.getString("message"));
                    baseModel.setData(mobileModel);
                    operators_model.Success(context, baseModel);
                } catch (JSONException e) {
                    operators_model.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                operators_model.Failure(context, response);
            }
        };

        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, operators_url, params, jsonResponse);
    }

    //手机验证发送短信验证码
    private static final String ver_phone = Global.HOST + "home/SendCode2";

    public void phone_ver(final Context context, String mobile, final APIManagerInterface.common_wordBack phone) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    phone.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    phone.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                phone.Failure(context, response);
            }
        };
        params.put("Mobile", mobile);
        params.put("Identifier", 7);
        client.post(context, ver_phone, params, jsonResponse);
    }

    //手机验证
    private static final String next_phone = Global.HOST + "home/IsMobiel";

    public void next_phone(final Context context, String serviPsd, String code, String mobile, final APIManagerInterface.common_wordBack next) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    next.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    next.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                next.Failure(context, response);
            }
        };
        params.put("ServiPsd", serviPsd);
        params.put("Code", code);
        params.put("Mobile", mobile);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, next_phone, params, jsonResponse);
    }

    //获取手机运营商
    public void phoneConfig(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, phoneConfig, params, parseJson);
    }

    public void sendTwoCode(final Context context, String userid, String ctm, String PhoneToken, String mobilePhone, String captchaCode, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("ctm", ctm);
        params.put("PhoneToken", PhoneToken);
        params.put("userid", userid);
        params.put("mobilePhone", mobilePhone);
        params.put("captchaCode", captchaCode);
        client.post(context, url_sendTwoCode, params, parseJson);
    }

    //手机认证输入验证码
    private static final String url_validateSmsCode = Global.HOST + "Home/writecode";

    //        private static final String url_validateSmsCode = Global.HOST + "Home/validateSmsCode";
    //手机认证输入验证码
    public void validateSmsCode(final Context context, String phonetoken, String randomCode, final APIManagerInterface.getContact_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("code", randomCode);
        params.put("phonetoken", phonetoken);
//        params.put("phoneToken", PhoneToken);
//        params.put("userid", userid);
//        params.put("ctm", ctm);
//        params.put("captchaCode", captchaCode);
        client.post(context, url_validateSmsCode, params, parseJson);
    }

    public void login_new(final Context context, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };

        params.put("userName", "18655877850");
        params.put("password", "111111");
        client.post(context, newLogin, params, parseJson);
    }

    //发送短信验证码
    public void sendLoginSmsCode(final Context context, String userid, String ctm, String PhoneToken, String mobilePhone, String captchaCode, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("ctm", ctm);
        params.put("PhoneToken", PhoneToken);
        params.put("userid", userid);
        params.put("mobilePhone", mobilePhone);
        params.put("captchaCode", captchaCode);
        client.post(context, url_sendLoginSmsCode, params, parseJson);
    }

    //采集
    private static final String url_CaiJi = Global.HOST + "Home/SCaiJi";

    //采集通话记录
    public void CaiJi(final Context context, String phoneToken, String mobilePhone, final APIManagerInterface.checkPhonePsd_back block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    block.Success(context, response.getInt("result"));
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, phoneToken);
        params.put("phoneToken", PrefsUtil.getString(context, Global.TOKEN));  //当前会员token
        params.put("mobilePhone", mobilePhone);
        client.post(context, url_CaiJi, params, parseJson);
    }
//    //采集通话记录
//    public void CaiJi(final Context context, String ctm, String phoneToken, String userid, final APIManagerInterface.checkPhonePsd_back block) {
//        RequestParams params = new RequestParams();
//        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
//        MyJsonResponse parseJson = new MyJsonResponse(context) {
//            @Override
//            public void onMySuccess(JSONObject response) {
//                try {
//                    block.Success(context, response.getInt("result"));
//                } catch (JSONException e) {
//                    block.Failure(context, response);
//                }
//            }
//
//            @Override
//            public void onMyFailure(JSONObject response) {
//                super.onMyFailure(response);
//                block.Failure(context, response);
//            }
//        };
//        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
//        params.put("phoneToken", phoneToken); //当前会员token
//        params.put("userid", userid); //
//        params.put("ctm", ctm); //时间戳
//        client.post(context, url_CaiJi, params, parseJson);
//    }

    //输入服务密码获取验证码
    private static final String loginToPhone = Global.HOST + "Home/getphonecallbill";
//    private static final String loginToPhone = Global.HOST + "Home/loginToPhone";

    public void checkPhonePsd1(final Context context, String password, String otherInfo, final APIManagerInterface.baseBlock block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                block.Success(context, response);
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("password", password);
        params.put("otherInfo", otherInfo);
//        params.put("phoneToken", phoneToken);
//        params.put("userid", userid);
//        params.put("ctm", ctm);
//        params.put("password", password);
//        params.put("websitePassword", websitePassword);
//        params.put("captchaCode", captchaCode);
//        params.put("smsCode", smsCode);
        client.post(context, loginToPhone, params, parseJson);
    }


    //我要借款
    private static final String loan_url = Global.HOST + "Member/MyLoan";

    public void loan(final Context context, int status, int pageIndex, int pageSize, final APIManagerInterface.common_object loan_model) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<CashModel> cashModel = com.alibaba.fastjson.JSONObject.parseArray(response.getString("dataList"), CashModel.class);
                    BaseDataListModel<CashModel> cashs = new BaseDataListModel<>();
                    cashs.setResult(response.getInt("result"));
                    cashs.setMessage(response.getString("message"));
                    cashs.setDatalist(cashModel);
                    loan_model.Success(context, cashs);
                } catch (JSONException e) {
                    loan_model.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                loan_model.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("Status", status);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        client.post(context, loan_url, params, jsonResponse);
    }

    //取消借款
    private static final String cancel_url = Global.HOST + "Member/Canceloan";

    public void cancelLoan(final Context context, int loanlogID, final APIManagerInterface.common_wordBack cancel) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    cancel.Success(context, response.getInt("result"), response.getString("message"));
                } catch (JSONException e) {
                    cancel.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                cancel.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("LoanlogID", loanlogID);
        client.post(context, cancel_url, params, jsonResponse);
    }

    //借款详情
    public static final String details_url = Global.HOST + "Member/LoanDetail";

    public void detailsLoan(final Context context, int loanlogID, final APIManagerInterface.loan_details details) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    details.Success(context, response.getInt("result"), response.getString("message"), response.getInt("data"));
                } catch (JSONException e) {
                    details.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                details.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("LoanlogID", loanlogID);
        client.post(context, details_url, params, jsonResponse);
    }

    //获取优惠券
    private static final String coupon_url = Global.HOST + "home/GetCoupon";

    public void coupon(final Context context, int pageIndex, int pageSize, final APIManagerInterface.common_object coupon_model) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<CouponCashModel> cashModels = com.alibaba.fastjson.JSONObject.parseArray(response.getString("dataList"), CouponCashModel.class);
                    BaseDataListModel<CouponCashModel> couponModel = new BaseDataListModel<>();
                    couponModel.setResult(response.getInt("result"));
                    couponModel.setMessage(response.getString("message"));
                    couponModel.setDatalist(cashModels);
                    coupon_model.Success(context, couponModel);
                } catch (JSONException e) {
                    coupon_model.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                coupon_model.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        client.post(context, coupon_url, params, jsonResponse);
    }

    private static final String couponCount_irl = Global.HOST + "Home/GetCouponCount";

    //获取优惠券总数接口
    public void getCouponCount(final Context context, final APIManagerInterface.resultListener resultListener) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    resultListener.Success(context, response.getInt("data") + "");
                } catch (JSONException e) {
                    super.onMyFailure(response);
                    resultListener.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                resultListener.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, couponCount_irl, params, jsonResponse);
    }

    private static final String loanCount_irl = Global.HOST + "Member/MyLoanCount";

    //获取借款完成总数
    public void getLoanCount(final Context context, final APIManagerInterface.resultListener resultListener) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    resultListener.Success(context, response.getInt("data") + "");
                } catch (JSONException e) {
                    super.onMyFailure(response);
                    resultListener.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                resultListener.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, loanCount_irl, params, jsonResponse);
    }

    private String aliPay = Global.HOST + "Home/zhifu";

    public void requestAliPay(String orderNO, final Context context, final APIManagerInterface.aliPay baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                if (response != null) {
                    try {
                        baseBlock.Success(context, response.getString("prepay_id"), response.getString("ordernumber"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message "));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("id", orderNO);
//        client.post(context, Global.AliPay + "?total_fee=1", params, jsonResponse);
        client.post(context, aliPay, params, jsonResponse);
    }

    private String bankPay = Global.HOST + "Home/BankCarLLP";

    public void requestBankPay(String orderNO, final Context context, final APIManagerInterface.aliPay baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                if (response != null) {
                    try {
                        int result = response.getInt("result");
                        if (result == 1) {
                            baseBlock.Success(context, response.getString("message"), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("id", orderNO);
        client.post(context, bankPay, params, jsonResponse);
    }

    private String bankHuanKuanState = Global.HOST + "home/getBankPay";

    public void requestBankState(final Context context, final APIManagerInterface.aliPay baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                if (response != null) {
                    try {
                        int result = response.getInt("result");
                        if (result == 1) {
                            baseBlock.Success(context, response.getString("url"), null);
                        } else {
                            baseBlock.Failure(context, "");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        baseBlock.Failure(context, "");
                    }
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, bankHuanKuanState, params, jsonResponse);
    }

    //    private String bankAuthor = Global.HOST + "Home/instalmentsign";
    private String bankAuthor = Global.HOST + "Home/Sign_llp";

    public void requestBankAuthor(final Context context, final APIManagerInterface.aliPay baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                if (response != null) {
                    try {
                        baseBlock.Success(context, response.getString("sortdic"), null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, bankAuthor, params, jsonResponse);
    }

    private String bankSign = Global.HOST + "Home/Sign_llp_return";

    //String no_agree, String oid_partner, String result_sign, String sign, String sign_type, String user_id,
    public void requestBankSign(String json, final Context context, final APIManagerInterface.aliPay baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                if (response != null) {
                    baseBlock.Success(context, null, null);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
//        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("json", json);
        client.post(context, bankSign, params, jsonResponse);
    }

    //判断担保人界面是否显示
    private String requestGuarantee = Global.HOST + "home/getGuarantee";

    public void requestGuarantee(final Context context, final APIManagerInterface.requestGuarantee baseBlock) {
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);
                try {
                    if (response != null) {
                        if (response.getInt("result") == 1) {
                            String guarantee = response.getString("guarantee");
                            if (!TextUtils.isEmpty(guarantee)) {
                                baseBlock.Success(context, true, guarantee);
                            } else {
                                baseBlock.Success(context, false, "");
                            }
                        } else {
                            baseBlock.Failure(context, response.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        RequestParams params = new RequestParams();
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requestGuarantee, params, jsonResponse);
    }

    private String requestMobile = Global.HOST + "home/getMobile";

    //请求手机号的网页
    public void requestMobile(final Context context, int type, final APIManagerInterface.requestMobile baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);
                try {
                    if (response != null) {
                        if (response.getInt("result") == 1) {
                            String url = response.getString("url");
                            if (!StringUtils.isEmpty(url)) {
                                baseBlock.Success(context, url);
                            } else {
                                baseBlock.Failure(context, response.getString("message"));
                            }
                        } else {
                            baseBlock.Failure(context, response.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        params.put("type", type);
        client.post(context, requestMobile, params, jsonResponse);
    }

    private String requestWxTxt = Global.HOST + "home/getWxTxt";

    public void requestWxTxt(final Context context, final APIManagerInterface.requestMobile baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);
                try {
                    if (response != null) {
                        if (response.getInt("result") == 1) {
                            String url = response.getString("msg");
                            if (!StringUtils.isEmpty(url)) {
                                baseBlock.Success(context, url);
                            } else {
                                baseBlock.Failure(context, response.getString("message"));
                            }
                        } else {
                            baseBlock.Failure(context, response.getString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requestWxTxt, params, jsonResponse);
    }

    private String requestLoanSuperUrl = Global.HOST + "home/getLoanSuperUrl2";

    /**
     * phoneType
     * 1 苹果 ，2 安卓
     */
    public void requestLoanSuperUrl(final Context context, final APIManagerInterface.requestMobile baseBlock) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse jsonResponse = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                super.onMySuccess(response);
                try {
                    if (response != null) {
                        if (response.getInt("result") == 1) {
                            String url = response.getString("url");
                            if (!StringUtils.isEmpty(url)) {
                                baseBlock.Success(context, url);
                            } else {
                                baseBlock.Failure(context, "");
                            }
                        } else {
                            baseBlock.Failure(context, "");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                if (response != null) {
                    try {
                        baseBlock.Failure(context, response.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        params.put("phoneType", 2);
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requestLoanSuperUrl, params, jsonResponse);
    }

    private String requesrWxCode = Global.HOST + "home/getWxCode";

    //获取订单列表
    public void requestHomeWxKefu(final Context context, final APIManagerInterface.common_object block) {
        RequestParams params = new RequestParams();
        AsyncHttpClient client = MyAsyncHttpClient.createClient(context);
        MyJsonResponse parseJson = new MyJsonResponse(context) {
            @Override
            public void onMySuccess(JSONObject response) {
                try {
                    List<WxModel> list = com.alibaba.fastjson.JSONObject.parseArray(response.getString("wxCode"), WxModel.class);
                    BaseDataListModel<WxModel> model = new BaseDataListModel<>();
                    model.setResult(response.getInt("result"));
                    model.setDatalist(list);
                    block.Success(context, model);
                } catch (JSONException e) {
                    block.Failure(context, response);
                }
            }

            @Override
            public void onMyFailure(JSONObject response) {
                super.onMyFailure(response);
                block.Failure(context, response);
            }
        };
        params.put(Global.TOKEN, PrefsUtil.getString(context, Global.TOKEN));
        client.post(context, requesrWxCode, params, parseJson);
    }

    public interface APIManagerInterface {
        public interface resultListener {
            public void Success(Context context, String data);

            public void Failure(Context context, JSONObject response);
        }

        public interface login_otherLogin {
            public void Success(Context context, int result, String token, String message);

            public void Failure(Context context, JSONObject response);
        }

        public interface login_back {
            public void Success(Context context, String token);

            public void Failure(Context context, JSONObject response);
        }


        public interface checkPhonePsd_back {
            public void Success(Context context, int result);

            public void Failure(Context context, JSONObject response);
        }

        public interface getContact_back {
            public void Success(Context context, int result, String message);

            public void Failure(Context context, JSONObject response);
        }

        public interface loan_details {
            public void Success(Context context, int result, String message, int data);

            public void Failure(Context context, JSONObject response);
        }

        public interface no_object {
            public void Success(Context context, int result, String message, String enmessage);

            public void Failure(Context context, JSONObject response);
        }

        public interface baseBlock {
            public void Success(Context context, JSONObject response);

            public void Failure(Context context, JSONObject response);
        }


        public interface config_getNews {
            public void Success(Context context, String result);

            public void Failure(Context context, JSONObject response);
        }

        public interface common_list<T> {
            public void Success(Context context, List<T> list, PageInfo info);

            public void Failure(Context context, JSONObject response);
        }

        public interface common_object<T> {
            public void Success(Context context, T model);

            public void Failure(Context context, JSONObject response);
        }

        public interface aliPay {
            public void Success(Context context, String prepay_id, String ordernumber);

            public void Failure(Context context, String msg);
        }

        public interface common_wordBack {
            public void Success(Context context, int result, String message);

            public void Failure(Context context, JSONObject response);
        }

        public interface common_FAQlist<T> {
            public void Success(Context context, List<T> list);

            public void Failure(Context context, JSONObject response);
        }

        public interface loanFeeBack {
            public void Success(Context context, float Interest, float Applyfee, float Userfee);

            public void Failure(Context context, JSONObject response);
        }

        public interface requestGuarantee {
            public void Success(Context context, boolean isShow, String guarantee);

            public void Failure(Context context, String msg);
        }

        public interface requestMobile {
            public void Success(Context context, String url);

            public void Failure(Context context, String msg);
        }
    }
}
