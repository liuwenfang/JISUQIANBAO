package com.ahxbapp.common.widget;

import android.support.v4.widget.SwipeRefreshLayout;

import com.ahxbapp.common.ui.BackActivity;
import com.ahxbapp.jsqb.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by chenchao on 15/6/10.
 * 对 refresh 的操作放到基类里
 */
@EActivity
public abstract class RefreshBaseActivity extends BackActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String UPDATE_ALL = "999999999";
    public static final int UPDATE_ALL_INT = 999999999;

    public boolean loading=true;
    @ViewById
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected final boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    protected final void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @AfterViews
    protected final void initRefreshBaseActivity() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
    }

    protected final void disableRefreshing() {
        swipeRefreshLayout.setEnabled(false);
    }
}
