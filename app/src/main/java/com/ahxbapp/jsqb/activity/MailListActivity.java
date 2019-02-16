package com.ahxbapp.jsqb.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ahxbapp.common.ui.BaseActivity;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.adapter.MailAdapter;
import com.ahxbapp.jsqb.model.CommonEnity;
import com.ahxbapp.jsqb.model.ContactsInfo;
import com.ahxbapp.jsqb.utils.GetContactsInfoF;
import com.ahxbapp.jsqb.view.SearchView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 联系人列表
 */
@EActivity(R.layout.activity_mail_list)
public class MailListActivity extends BaseActivity {
    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    SearchView edSearch;
    @ViewById
    ListView listView;

    @AfterViews
    void init() {
        mToolbarLeftIB.setImageResource(R.mipmap.back);
        mToolbarTitleTV.setText("通讯录");
        GetContactsInfoF getContactsInfo = new GetContactsInfoF(this);
        allContactsInfo = new ArrayList<>();
        //获取通讯录好友列表ContactsInfoList
        contactsInfoList = getContactsInfo.getLocalContactsInfos();
        allContactsInfo.addAll(contactsInfoList);
        if (contactsInfoList != null) {
            mailAdapter = new MailAdapter(this, contactsInfoList);
            listView.setAdapter(mailAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EventBus.getDefault().post(new CommonEnity<>("contactsInfo", contactsInfoList.get(position)));
                    finish();
                }
            });
            mailAdapter.notifyDataSetChanged();
        }
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (allContactsInfo == null || allContactsInfo.size() == 0) {
                    return;
                }
                contactsInfoList.clear();
                if (s.toString() != null && !s.toString().equals("")) {
                    requestInfo(s.toString());
                } else {
                    contactsInfoList.addAll(allContactsInfo);
                    mailAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void requestInfo(String msg) {
        for (int i = 0; i < allContactsInfo.size(); i++) {
            if (allContactsInfo.get(i).getName() != null && allContactsInfo.get(i).getName().contains(msg)) {
                contactsInfoList.add(allContactsInfo.get(i));
            }
        }
        mailAdapter.notifyDataSetChanged();
    }

    private MailAdapter mailAdapter;
    private List<ContactsInfo> contactsInfoList, allContactsInfo;

    @Click
    void mToolbarLeftIB() {
        finish();
    }


}
