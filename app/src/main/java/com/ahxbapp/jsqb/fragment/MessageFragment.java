package com.ahxbapp.jsqb.fragment;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ahxbapp.common.BlankViewDisplay;
import com.ahxbapp.common.Global;
import com.ahxbapp.common.network.RefreshBaseFragment;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.MessageAdapter;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.model.BaseDataListModel;
import com.ahxbapp.jsqb.model.MessageModel;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_message)
public class MessageFragment extends RefreshBaseFragment implements PullToRefreshListView.OnRefreshListener2 {

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
        mToolbarTitleTV.setText("我的消息");
        String token = PrefsUtil.getString(getActivity(), Global.TOKEN);
        if (token == null) {
            return;
        }
        PageSize = 10;
        createView();
    }

    void createView() {
        myadapter = new MessageAdapter(getContext(), mData, R.layout.adapter_message);
        mylist.setAdapter(myadapter);
        mylist.setMode(PullToRefreshBase.Mode.BOTH);
        mylist.setOnRefreshListener(this);
        mylist.setAdapter(myadapter);
        loadData();
//        myadapter.notifyDataSetChanged();
        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("点击了", "点击了");
                Log.e("mData", mData + "");
                Log.e("position", position + "");
                final MessageModel messageModel = mData.get(position - 1);
                if (messageModel.getIsRead() == 0) {
                    APIManager.getInstance().Member_YRead1(getContext(), String.valueOf(messageModel.getID()), new APIManager.APIManagerInterface.baseBlock() {
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
        APIManager.getInstance().getMessage(getContext(), pageIndex, pageSize, new APIManager.APIManagerInterface.common_object() {
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
                BlankViewDisplay.setBlank(mData.size(), getContext(), true, blankLayout, onClickRetry);

            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
                mylist.onRefreshComplete();
                BlankViewDisplay.setBlank(mData.size(), getContext(), false, blankLayout, onClickRetry);
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

    @Override
    public void onRefresh() {

    }
}
