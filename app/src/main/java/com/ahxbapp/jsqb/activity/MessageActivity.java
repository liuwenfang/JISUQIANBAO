package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahxbapp.common.BlankViewDisplay;
import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.MessageAdapter;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.MessageModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的消息
 */
@EActivity(R.layout.activity_message)
public class MessageActivity extends BaseActivity implements PullToRefreshListView.OnRefreshListener2 {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    PullToRefreshListView mylist;
    @ViewById
    View blankLayout;

    List<MessageModel> mData = new ArrayList<>();
    MessageAdapter myadapter;

    int PageIndex, PageSize;

    @AfterViews
    void initView() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("我的消息");
        PageSize = 10;
        createView();
    }

    void createView() {
        myadapter = new MessageAdapter(this, mData, R.layout.adapter_message);
        mylist.setAdapter(myadapter);
        loadData();
        mylist.setMode(PullToRefreshBase.Mode.BOTH);
        mylist.setOnRefreshListener(this);
        mylist.setAdapter(myadapter);
        myadapter.notifyDataSetChanged();


        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("点击了", "点击了");
                Log.e("mData", mData + "");
                Log.e("position", position + "");
                final MessageModel messageModel = mData.get(position - 1);
                if (messageModel.getIsRead() == 0) {
                    APIManager.getInstance().Member_YRead1(MessageActivity.this, String.valueOf(messageModel.getID()), new APIManager.APIManagerInterface.baseBlock() {
                        @Override
                        public void Success(Context context, JSONObject response) {

                            messageModel.setIsRead(1);
                            myadapter.notifyDataSetChanged();

                        }

                        @Override
                        public void Failure(Context context, JSONObject response) {

                        }
                    });
                }
            }
        });
    }

    void loadData() {
        showBlackLoading();
        APIManager.getInstance().getMessage(this, pageIndex, pageSize, new APIManager.APIManagerInterface.common_object() {
            @Override
            public void Success(Context context, Object model) {
                hideProgressDialog();
                BaseDataListModel<MessageModel> mm = (BaseDataListModel<MessageModel>) model;
                if (mm.getResult() == 1) {
                    if (pageIndex == 1) {
                        mData.clear();
                    }
                    mData.addAll(mm.getDatalist());
                    myadapter.notifyDataSetChanged();
                    mylist.onRefreshComplete();
                }
                BlankViewDisplay.setBlank(mData.size(), MessageActivity.this, true, blankLayout, onClickRetry);

            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                mylist.onRefreshComplete();
                BlankViewDisplay.setBlank(mData.size(), MessageActivity.this, false, blankLayout, onClickRetry);
            }
        });
    }

    //点击重新加载
    View.OnClickListener onClickRetry = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadData();
        }
    };

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        pageIndex = 1;
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        pageIndex++;
        loadData();
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mylist.onRefreshComplete();
            }
        }, 1000);
    }
}
