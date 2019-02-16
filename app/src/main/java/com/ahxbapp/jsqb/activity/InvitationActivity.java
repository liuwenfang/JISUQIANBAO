package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.ShareAdapter;
import com.ahxbapp.jsqb.adapter.ZRecyclerViewAdapter;
import com.ahxbapp.jsqb.model.InvitationModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请好友
 */
@EActivity(R.layout.activity_invitation)
public class InvitationActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    RecyclerView mRecyclerView;

    private Context mContext;
    private ShareAdapter shareAdapter;
    private List<InvitationModel> modelList;

    @AfterViews
    void init() {
        mContext = this;
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("邀请好友");

        modelList = new ArrayList<>();
        shareAdapter = new ShareAdapter(mRecyclerView, modelList);
        shareAdapter.setOnItemClickListener(new ZRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                showButtomToast("功能暂未开通,敬请期待!");
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mRecyclerView.setAdapter(shareAdapter);
        modelList.add(new InvitationModel("微信", R.mipmap.img_share_wx));
        modelList.add(new InvitationModel("QQ", R.mipmap.img_share_qq));
        modelList.add(new InvitationModel("QQ空间", R.mipmap.img_share_qzone));
        modelList.add(new InvitationModel("朋友圈", R.mipmap.img_share_pyq));
        shareAdapter.notifyDataSetChanged();
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

//    @Click
//    void mToolbarLeftIB() {
//        finish();
//    }

}
