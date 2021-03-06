package com.ahxbapp.common.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ahxbapp.common.FootUpdate;
import com.ahxbapp.common.Global;
import com.ahxbapp.common.ImageLoadTool;
import com.ahxbapp.common.StartActivity;
import com.ahxbapp.common.network.NetworkCallback;
import com.ahxbapp.common.network.NetworkImpl;
import com.ahxbapp.common.network.UmengFragment;
import com.ahxbapp.common.util.SingleToast;
import com.ahxbapp.jsqb.R;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cc191954 on 14-8-11.
 * 封装了图片下载
 * 封装了网络请求
 */
public class BaseFragment extends UmengFragment implements NetworkCallback, FootUpdate.LoadMore, StartActivity {


    public int pageIndex=1;
    public int pageSize=10;
    public boolean loading=true;

    protected NetworkImpl networkImpl;
    protected LayoutInflater mInflater;
    protected FootUpdate mFootUpdate = new FootUpdate();
    protected View.OnClickListener mOnClickUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private ImageLoadTool imageLoadTool = new ImageLoadTool();
    private ProgressDialog mProgressDialog;

    protected void showProgressBar(boolean show) {
        showProgressBar(show, "");
    }

    protected void setProgressBarProgress() {
        if (mProgressDialog == null) {
            return;
        }

        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgress(30);
    }

    protected void showProgressBar(boolean show, String message) {
        if (mProgressDialog == null) {
            return;
        }

        if (show) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.hide();
        }
    }

    public AppCompatActivity getActionBarActivity() {
        return (AppCompatActivity) getActivity();
    }

    protected void showProgressBar(int messageId) {
        String message = getString(messageId);
        showProgressBar(true, message);
    }

    protected boolean progressBarIsShowing() {
        return mProgressDialog.isShowing();
    }

    protected ImageLoadTool getImageLoad() {
        return imageLoadTool;
    }

    @Override
    public void loadMore() {

    }

    protected void initSetting() {
        networkImpl.initSetting();
    }

    public boolean isLoadingFirstPage(String tag) {
        return networkImpl.isLoadingFirstPage(tag);
    }

    public boolean isLoadingLastPage(String tag) {
        return networkImpl.isLoadingLastPage(tag);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mInflater = LayoutInflater.from(getActivity());
        networkImpl = new NetworkImpl(getActivity(), this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        mSingleToast = new SingleToast(getActivity());
    }

    @Override
    public void onDestroy() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initSetting();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void parseJson(int code, JSONObject respanse, String tag, int pos, Object data) throws JSONException {
    }

    protected void postNetwork(String url, RequestParams params, final String tag) {
        networkImpl.loadData(url, params, tag, -1, null, NetworkImpl.Request.Post);
    }

    protected void postNetwork(String url, RequestParams params, final String tag, int dataPos, Object data) {
        networkImpl.loadData(url, params, tag, dataPos, data, NetworkImpl.Request.Post);
    }

    @Override
    public void getNetwork(String url, final String tag) {
        networkImpl.loadData(url, null, tag, -1, null, NetworkImpl.Request.Get);
    }

    public void getNetwork(String url) {
        networkImpl.loadData(url, null, url, -1, null, NetworkImpl.Request.Get);
    }

    protected void getNetwork(String url, final String tag, int dataPos, Object data) {
        networkImpl.loadData(url, null, tag, dataPos, data, NetworkImpl.Request.Get);
    }

    public void putNetwork(String url, final String tag) {
        networkImpl.loadData(url, null, tag, -1, null, NetworkImpl.Request.Put);
    }

    protected void showDialog(String title, String msg, DialogInterface.OnClickListener clickOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", clickOk)
                .setNegativeButton("取消", null)
                .show();
    }

    protected void showDialog(String[] titles, DialogInterface.OnClickListener clickOk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(titles, clickOk).show();
    }

    public void putNetwork(String url, final String tag, int dataPos, Object data) {
        networkImpl.loadData(url, null, tag, dataPos, data, NetworkImpl.Request.Put);
    }

    public void putNetwork(String url, RequestParams params, final String tag, Object data) {
        networkImpl.loadData(url, params, tag, -1, data, NetworkImpl.Request.Put);
    }

    public void deleteNetwork(String url, final String tag) {
        networkImpl.loadData(url, null, tag, -1, null, NetworkImpl.Request.Delete);
    }

    public void deleteNetwork(String url, final String tag, int dataPos, Object data) {
        networkImpl.loadData(url, null, tag, dataPos, data, NetworkImpl.Request.Delete);
    }

    public void deleteNetwork(String url, final String tag, Object data) {
        networkImpl.loadData(url, null, tag, -1, data, NetworkImpl.Request.Delete);
    }

    protected void getNextPageNetwork(String url, final String tag) {
        networkImpl.getNextPageNetwork(url, tag);
    }

    public void showErrorMsg(int code, JSONObject json) {
        if (code == NetworkImpl.NETWORK_ERROR) {
            showButtomToast(R.string.connect_service_fail);
        } else {
            String msg = Global.getErrorMsg(json);
            if (!msg.isEmpty()) {
                showButtomToast(msg);
            }
        }
    }

    SingleToast mSingleToast;

    public void showButtomToast(String msg) {
        if (!isResumed() || mSingleToast == null) {
            return;
        }

        mSingleToast.showButtomToast(msg);
    }

    public void showMiddleToast(int id) {
        if (!isResumed() || mSingleToast == null) {
            return;
        }
        mSingleToast.showMiddleToast(id);
    }

    public void showMiddleToast(String msg) {
        if (!isResumed() || mSingleToast == null) {
            return;
        }
        mSingleToast.showMiddleToast(msg);
    }

    public void showButtomToast(int messageId) {
        if (!isResumed() || mSingleToast == null) {
            return;
        }

        mSingleToast.showButtomToast(messageId);
    }

    protected void iconfromNetwork(ImageView view, String url) {
        imageLoadTool.loadImage(view, Global.makeSmallUrl(view, url));
    }

    protected void iconfromNetwork(ImageView view, String url, DisplayImageOptions options) {
        imageLoadTool.loadImage(view, Global.makeSmallUrl(view, url), options);
    }

    protected void showDialogLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showDialogLoading();
        }
    }

    protected void showBlackLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showBlackLoading();
        }
    }

    protected void hideProgressDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideProgressDialog();
        }
    }

    protected void callPhone(String phone){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }


}
