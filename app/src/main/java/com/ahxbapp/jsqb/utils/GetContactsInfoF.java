package com.ahxbapp.jsqb.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;

import com.ahxbapp.jsqb.model.ContactsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 15/9/2.
 * 获取联系人信息
 */
public class GetContactsInfoF {

    List<ContactsInfo> localList;
    List<ContactsInfo> SIMList;
    Context context;
    ContactsInfo contactsInfo;
    ContactsInfo SIMContactsInfo;

    public GetContactsInfoF(Context context) {
        localList = new ArrayList<ContactsInfo>();
        SIMList = new ArrayList<ContactsInfo>();
        this.context = context;
    }

    // ----------------得到本地联系人信息-------------------------------------
    public List<ContactsInfo> getLocalContactsInfos() {

        ContentResolver cr = context.getContentResolver();
        String str[] = {Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER,
                Phone.PHOTO_ID};
        Cursor cur = null;
        try {
            cur = cr.query(
                    Phone.CONTENT_URI, str, null,
                    null, null);
            if (cur != null) {
                while (cur.moveToNext()) {
                    contactsInfo = new ContactsInfo();
                    contactsInfo.setPhone(cur.getString(cur.getColumnIndex(Phone.NUMBER)).replace(" ",""));// 得到手机号码
                    contactsInfo.setName(cur.getString(cur.getColumnIndex(Phone.DISPLAY_NAME)));
                    localList.add(contactsInfo);
                }
                cur.close();
            }
        } catch (SecurityException e) {
        }
        return localList;
    }

    public List<ContactsInfo> getSIMContactsInfos() {
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        System.out.println("--SIM--");
        ContentResolver cr = context.getContentResolver();
        final String SIM_URI_ADN = "content://icc/adn";// SIM卡

        Uri uri = Uri.parse(SIM_URI_ADN);
        Cursor cursor = cr.query(uri, null, null, null, null);
        while (cursor.moveToFirst()) {
            SIMContactsInfo = new ContactsInfo();
            SIMContactsInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
            SIMContactsInfo.setPhone(cursor.getString(cursor.getColumnIndex("number")));
//            SIMContactsInfo.setBitmap(BitmapFactory.decodeResource(context.getResources(),
//                    R.mipmap.ic_launcher));
            SIMList.add(SIMContactsInfo);
        }
        cursor.close();

        return SIMList;
    }

}
