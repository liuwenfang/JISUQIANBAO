package com.ahxbapp.common.base;

import com.ahxbapp.common.network.RefreshBaseFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by chenchao on 15/3/9.
 */
@EFragment

public abstract class CustomMoreFragment extends RefreshBaseFragment {

    protected abstract String getLink();

//    @OptionsItem
//    protected void action_copy() {
//        String link = getLink();
//        Global.copy(getActivity(), link);
//        showButtomToast("已复制链接 " + link);
//    }

    public void search(String text) {}

}
