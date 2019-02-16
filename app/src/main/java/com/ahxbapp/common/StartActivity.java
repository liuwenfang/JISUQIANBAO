package com.ahxbapp.common;

import android.content.Intent;

/**
 * Created by gravel on 14-10-29.
 */
public interface StartActivity {
    void startActivityForResult(Intent intent, int requestCode);
}
