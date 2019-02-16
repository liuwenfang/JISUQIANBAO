package com.ahxbapp.jsqb.fragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.Global;
import com.ahxbapp.common.ui.BaseFragment;
import com.ahxbapp.common.util.SingleToast;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.activity.ConsummateInfoActivity_;
import com.ahxbapp.jsqb.activity.CouponCashActivity_;
import com.ahxbapp.jsqb.activity.FAQActivity_;
import com.ahxbapp.jsqb.activity.FirstCerActivity_;
import com.ahxbapp.jsqb.activity.HelpCenterActivity_;
import com.ahxbapp.jsqb.activity.InvitationActivity_;
import com.ahxbapp.jsqb.activity.LoginActivity_;
import com.ahxbapp.jsqb.activity.MessageActivity_;
import com.ahxbapp.jsqb.activity.OrderListActivity_;
import com.ahxbapp.jsqb.activity.PersonDataActivity_;
import com.ahxbapp.jsqb.activity.SetWebActivity_;
import com.ahxbapp.jsqb.activity.SettingActivity_;
import com.ahxbapp.jsqb.activity.WebActivity_;
import com.ahxbapp.jsqb.activity.WordActivity_;
import com.ahxbapp.jsqb.adapter.PersonalAdapter;
import com.ahxbapp.jsqb.adapter.ZRecyclerViewAdapter;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.PersonalModel;
import com.ahxbapp.jsqb.model.User;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.ahxbapp.jsqb.utils.VersionUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Jayzhang on 16/10/17.
 */
@EFragment(R.layout.person)
public class PersonFragment extends BaseFragment {

    @ViewById
    RelativeLayout message_Rela, perData_Rela, MyBorrowings_Rela, law_Rela, FAQ_Rela, word_Rela;
    @ViewById
    ImageView setting_btn, img_head, shimingImage;
    @ViewById
    TextView login_text, banben_text, tvShiMing;

    @ViewById
    LinearLayout llKeFu, llCoupon, llQuest, llOpinion, llSet;

    private PersonalAdapter personalAdapter;
    private List<PersonalModel> personalModels;
    String code;

    private Context mContext;

//    @Click
//    void login_text() {
//        showButtomToast("点击测试");
//    }

    @AfterViews
    void init() {
        //创建/打开 数据库
        mContext = getContext();
        SQLiteDatabase db = Connector.getDatabase();
        loadView();
        try {
            code = VersionUtils.getVersionName(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        banben_text.setText("当前版本" + code);
    }

    private void initAdapter() {
        personalAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                switch (position) {
                    case 0://借款记录
                        OrderListActivity_.intent(mContext).start();
                        break;
                    case 1://消息中心
                        MessageActivity_.intent(mContext).start();
                        break;
                    case 2://我的关注
//                        WebActivity_.intent(mContext).url(APIManager.requestKeFuUrl+"?ContactValue=1").title("联系客服").start();
                        SingleToast.showMiddleToast(mContext, "开发中...");
                        break;
                    case 3://客服
                        WebActivity_.intent(mContext).url(APIManager.requestKeFuUrl + "?ContactValue=1").title("联系客服").start();
                        break;
                    case 4://认证资料
                        if (user != null && user.getTrueName() != null && !user.getTrueName().equals("")) {
                            ConsummateInfoActivity_.intent(mContext).start();
                        } else {
                            FirstCerActivity_.intent(mContext).tag(2).start();
                        }
                        break;
                    case 5://问题帮助   常见问题
                        FAQActivity_.intent(mContext).start();
                        break;
                    case 6://问题反馈
                        WordActivity_.intent(mContext).start();
                        break;
                    case 7://分享好友
                        InvitationActivity_.intent(mContext).start();
                        break;
                    case 8://法律责任
                        SetWebActivity_.intent(mContext).flag(4).start();
                        break;
                    case 9://设置
                        SettingActivity_.intent(mContext).startForResult(3000);
                        break;
                }
            }
        });
        personalModels.add(new PersonalModel("借款记录", R.mipmap.img_personal_1));
        personalModels.add(new PersonalModel("消息中心", R.mipmap.img_personal_2));
        personalModels.add(new PersonalModel("我的关注", R.mipmap.img_personal_3));
        personalModels.add(new PersonalModel("客服", R.mipmap.img_personal_4));
        personalModels.add(new PersonalModel("认证资料", R.mipmap.img_personal_5));
        personalModels.add(new PersonalModel("问题帮助", R.mipmap.img_personal_6));
        personalModels.add(new PersonalModel("问题反馈", R.mipmap.img_personal_7));
        personalModels.add(new PersonalModel("分享好友", R.mipmap.img_personal_8));
        personalModels.add(new PersonalModel("法律责任", R.mipmap.img_personal_9));
        personalModels.add(new PersonalModel("设置", R.mipmap.img_personal_10));

//        personalModels.add(new PersonalModel("我的认证", R.mipmap.img_personal_1));
//        personalModels.add(new PersonalModel("征信查询", R.mipmap.img_personal2));
//        personalModels.add(new PersonalModel("人工客服", R.mipmap.img_personal3));
//        personalModels.add(new PersonalModel("邀请好友", R.mipmap.img_personal4));
//        personalModels.add(new PersonalModel("优惠券", R.mipmap.img_personal5));
//        personalModels.add(new PersonalModel("我的收藏", R.mipmap.img_personal6));
//        personalModels.add(new PersonalModel("常见问题", R.mipmap.img_personal7));
//        personalModels.add(new PersonalModel("投诉建议", R.mipmap.img_personal8));
        personalAdapter.notifyDataSetChanged();
    }

    public void loadView() {
        //加载视图
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            //头像
//            Bitmap bit = BitmapFactory.decodeResource(getResources(), R.mipmap.avatar);
//            img_head.setImageBitmap(bit);
            img_head.setBackgroundResource(R.mipmap.avatar);
            login_text.setText(R.string.per_nologin_text);
//            shimingImage.setVisibility(View.GONE);
        } else {
            img_head.setBackgroundResource(R.mipmap.img_user_head);
            //加载本地
            loadLocal();
            //下载网络数据  个人信息
            loadData();
            //优惠券信息
        }
    }

    private User user;

    /**
     * 网络请求个人信息
     */
    //下载数据
    void loadData() {
        APIManager.getInstance().user_getUserInfo(getActivity(), new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                //用户信息
                user = (User) model;
                //存储数据库
                if (user != null) {
                    User user1 = DataSupport.find(User.class, user.getUid());
                    if (user1 == null) {
                        user.save();
                        PrefsUtil.setString(context, "user_id", String.valueOf(user.getUid()));
                        JPushInterface.setAlias(getContext(), String.valueOf(user.getID()), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                            }
                        });
                        JPushInterface.resumePush(getContext());
                        Log.e("1", "添加2--------->> user" + user.getID());
                    } else {
                        user.update(user.getUid());
                        Log.e("1", "修改2-------->> user" + user.getID());
                    }
                }

                //加载页面数据

//                shimingImage.setVisibility(View.VISIBLE);
                if (user.getTrueName() != null && !user.getTrueName().equals("")) {
                    tvShiMing.setText("已实名");
//                    Bitmap bit = BitmapFactory.decodeResource(getResources(), R.mipmap.yishim);
//                    shimingImage.setImageBitmap(bit);
                    login_text.setText(user.getTrueName());
                } else {
                    tvShiMing.setText("未实名");
//                    Bitmap bit = BitmapFactory.decodeResource(getResources(), R.mipmap.weism);
//                    shimingImage.setImageBitmap(bit);
                    login_text.setText(user.getNickName());
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {

            }
        });
    }

    /**
     * 本地个人信息
     */
    void loadLocal() {
        User localUser = DataSupport.findFirst(User.class);
        if (localUser != null) {
            //加载页面数据
            shimingImage.setVisibility(View.VISIBLE);
            if (localUser.getTrueName() != null && !user.getTrueName().equals("")) {
                tvShiMing.setText("已实名");
//                Bitmap bit = BitmapFactory.decodeResource(getResources(), R.mipmap.yishim);
//                shimingImage.setImageBitmap(bit);
                login_text.setText(localUser.getTrueName());
            } else {
//                Bitmap bit = BitmapFactory.decodeResource(getResources(), R.mipmap.weism);
//                shimingImage.setImageBitmap(bit);
                tvShiMing.setText("未实名");
                login_text.setText(localUser.getNickName());
            }
        }
    }

    @OnActivityResult(1000)
    void LoginActivity() {
        loadView();
    }

    @OnActivityResult(2000)
    void UserInfoActivity() {
        loadView();
    }

    @OnActivityResult(3000)
    void SettingActivity() {
        loadView();
    }

    /**
     * 个人信息（昵称与头像）
     */
    @Click
    void login_Rela() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            if (user != null && user.getTrueName() != null && !user.getTrueName().equals("")) {
//                PersonDataActivity_.intent(this).startForResult(2000);
            } else {
                FirstCerActivity_.intent(this).start();
            }
        }
    }

    /**
     * 系统消息
     */
    @Click
    void message_Rela() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            MessageActivity_.intent(this).start();
        }
    }

    /**
     * 个人资料
     */
    @Click
    void perData_Rela() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            User localUser = DataSupport.findFirst(User.class);
            if (localUser.getTrueName() != null && !user.getTrueName().equals("")) {
                PersonDataActivity_.intent(this).start();
            } else {
                FirstCerActivity_.intent(this).tag(2).start();
            }
        }
    }

    /**
     * 完善资料
     */
    @Click
    void rlPrefectData() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            if (user != null && user.getTrueName() != null && !user.getTrueName().equals("")) {
                ConsummateInfoActivity_.intent(this).start();
            } else {
                FirstCerActivity_.intent(this).tag(2).start();
            }
        }
    }

    /**
     * 我的借款
     */
    @Click
    void MyBorrowings_Rela() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            OrderListActivity_.intent(this).start();
        }
    }

    /**
     * 法律责任
     */
    @Click
    void law_Rela() {
        SetWebActivity_.intent(this).flag(4).start();
    }

    /**
     * 常见问题 帮助中心...
     */
    @Click
    void FAQ_Rela() {
//        FAQActivity_.intent(this).start();
        HelpCenterActivity_.intent(this).start();
    }

    /**
     * 留言
     */
    @Click
    void word_Rela() {
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (TextUtils.isEmpty(token)) {
            LoginActivity_.intent(this).startForResult(1000);
        } else {
            WordActivity_.intent(this).start();
        }
    }

    /**
     * 设置
     */
    @Click
    void setting_btn() {
        SettingActivity_.intent(this).startForResult(3000);
    }

    /**
     * 设置
     */
    @Click
    void rlSet() {
        SettingActivity_.intent(this).startForResult(3000);
    }

    @Click
    void llKeFu() {

    }

    /**
     * 优惠券
     */
    @Click
    void llCoupon() {
        CouponCashActivity_.intent(this).price(-1).startForResult(1000);
    }

    /**
     * 常见问题
     */
    @Click
    void llQuest() {
        FAQActivity_.intent(mContext).start();
    }

    /**
     * 意见反馈
     */
    @Click
    void llOpinion() {
        WordActivity_.intent(mContext).start();
    }

    /**
     * 设置
     */
    @Click
    void llSet() {
        SettingActivity_.intent(this).startForResult(3000);
    }

    /**
     * 已完成
     */
    @Click
    void llCashComplete() {
        OrderListActivity_.intent(this).flag(3).start();
    }

    /**
     * 待还款
     */
    @Click
    void llCashWaitRepayment() {
        OrderListActivity_.intent(this).flag(2).start();
    }

    /**
     * 待审核
     */
    @Click
    void llCashWaitAuditing() {
        OrderListActivity_.intent(this).flag(1).start();
    }
}
