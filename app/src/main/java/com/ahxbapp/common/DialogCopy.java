package com.ahxbapp.common;

import android.support.v7.app.AlertDialog;
import android.view.View;


/**
 * Created by gravel on 15/1/29.
 */
public class DialogCopy {

    private static View.OnLongClickListener onLongClickListener;

    public static View.OnLongClickListener getInstance() {
        if (onLongClickListener == null) {

        }

        return onLongClickListener;
    }
}
