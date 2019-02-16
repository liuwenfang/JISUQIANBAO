package com.ahxbapp.jsqb.activity;

import android.widget.TextView;

import com.ahxbapp.common.ui.BackActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.utils.MyToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * 暂未使用
 */
@EActivity(R.layout.activity_user_edit)
@OptionsMenu(R.menu.set_password)
public class UserInfoUpdateActivity extends BackActivity {
    @Extra
    int type;
    @Extra
    String val;
    @Extra
    String title;

    @ViewById
    TextView value;

    @AfterViews
    final void initAccountSetting() {
        final String hintFormat = "请输入%s";
        value.setHint(String.format(hintFormat, title));
        value.setText(val);
        value.requestFocus();
    }

    @OptionsItem
    void submit() {
        String str = "";
        if (type == 0) {
            str = "昵称";
        }
        if (value.getText().toString().length() == 0) {
            MyToast.showToast(this, String.format("请输入%s", str));
            return;
        }
//        showProgressBar(true);
//        new APIManager().user_updateUserInfo(this, user, new APIManager.APIManagerInterface.baseBlock() {
//            @Override
//            public void Success(Context context, JSONObject response) {
//                showProgressBar(false);
//                user.setCurrentUser();
//                MyApp.userObj=user;
//                setResult(Activity.RESULT_OK);
//                finish();
//            }
//
//            @Override
//            public void Failure(Context context, JSONObject response) {
//                showProgressBar(false);
//            }
//        });
    }

}
