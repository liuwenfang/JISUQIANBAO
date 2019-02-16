package com.ahxbapp.common.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by chenchao on 15/9/25.
 * 基本的listActivity，没有分隔条，没有分页
 */
@EActivity
public abstract class BaseListActivity extends BackActivity {

    public abstract String getActionbarTitle();
    public abstract String getGetUrl();
    public abstract View getBindView(int position, View convertView, ViewGroup parent);

    private ArrayAdapter<Object> mAdapter;


    private static final String TAG_HTTP_BASE_LIST_ACTIVITY = "TAG_HTTP_BASE_LIST_ACTIVITY";

    @AfterViews
    protected final void BaseListActivity() {


    }


}
