package com.ahxbapp.jsqb.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ahxbapp.jsqb.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

public class DeviceUtil {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final int seconds_of_1minute = 60;
    private static final int seconds_of_30minutes = 30 * 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_1day = 24 * 60 * 60;
    private static final int seconds_of_15days = seconds_of_1day * 15;
    private static final int seconds_of_30days = seconds_of_1day * 30;
    private static final int seconds_of_6months = seconds_of_30days * 6;
    private static final int seconds_of_1year = seconds_of_30days * 12;

    /**
     * 获取设备号
     *
     * @param context
     * @return
     */
    public static String getSnNumber(Context context) {
        String imei;
        try {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = mTelephonyMgr.getDeviceId();
        } catch (Exception e) {
            return "";
        }
        return imei;
    }

    /**
     * 获取Android_ID
     *
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * 返回屏幕宽高 int[宽，高]
     *
     * @param activity
     */
    public static int[] getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        return new int[]{screenWidth, screenHeigh};
    }

    /**
     * 隐藏软键盘
     *
     * @param v
     */
    public static void hideSoftInput(View v, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     *
     * @param context
     */
    public static void showSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 自动获取焦点并弹出软键盘
     *
     * @param v
     * @param context
     */
    public static void autoFocusShowSoftInput(View v, final Context context) {
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                showSoftInput(context);
            }
        }, 830);
    }

    /**
     * 判断是否有网络连接
     *
     * @param
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getFriendlyDate(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setTimeZone(TimeZone.getDefault());
        long interval = new Date().getTime() - date.getTime();
        long sec = interval / 1000;
        String str = "";

        if (sec < 60) {
            str = "刚刚";
        } else if (sec < 60 * 60) {
            long min = sec / 60;
            str = min + "分钟前";
        } else if (sec < 24 * 60 * 60) {
            long hour = sec / (60 * 60);
            str = hour + "小时前";
        } else {
            long day = sec / (24 * 60 * 60);
            str = day + "天前";
        }

        return str;
    }

    /**
     * @return timtPoint距离现在经过的时间，分为
     * 刚刚，1-29分钟前，半小时前，1-23小时前，1-14天前，半个月前，1-5个月前，半年前，1-xxx年前
     */
    public static String getTimeElapse(long createTime) {
        long nowTime = new Date().getTime() / 1000;
        //createTime是发表文章的时间
        long oldTime = createTime / 1000;
        //elapsedTime是发表和现在的间隔时间
        long elapsedTime = nowTime - oldTime;
        if (elapsedTime < seconds_of_1minute) {
            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {
            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {
            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {
            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_15days) {
            return elapsedTime / seconds_of_1day + "天前";
        }
        if (elapsedTime < seconds_of_30days) {
            return "半个月前";
        }
        if (elapsedTime < seconds_of_6months) {
            return elapsedTime / seconds_of_30days + "月前";
        }
        if (elapsedTime < seconds_of_1year) {
            return "半年前";
        }
        if (elapsedTime >= seconds_of_1year) {
            return elapsedTime / seconds_of_1year + "年前";
        }
        return "";
    }


    public static Date parseDate(String dateStr, String formatStr) {
        if (dateStr == null) {
            return null;
        }

        SimpleDateFormat formate = new SimpleDateFormat(formatStr, Locale.CHINA);
        Date date = null;

        try {
            date = formate.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String formatDate(Date date, String formatStr) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat formate = new SimpleDateFormat(formatStr, Locale.CHINA);
        return formate.format(date);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 弹出两个按钮的对话框
     *
     * @param poActivity
     * @param poHandler
     * @param psText
     * @param piCloseMsg     点击Positive按钮的msg
     * @param psPositiveName 按钮名
     * @param psCancelName   按钮名
     */
    public static void dialog_twoBtn(Activity poActivity,
                                     final Handler poHandler, String psText, final int piCloseMsg,
                                     final String psPositiveName, final String psCancelName) {
        final AlertDialog loDialog = new AlertDialog.Builder(poActivity)
                .create();
        LayoutInflater loInflater = poActivity.getLayoutInflater();
        loDialog.setView(loInflater.inflate(R.layout.dialog_info_two_btn, null));
        loDialog.show();
        loDialog.getWindow().setContentView(R.layout.dialog_info_two_btn);
        loDialog.setCancelable(false);
        TextView loTextInfo = (TextView) loDialog
                .findViewById(R.id.dialog_info_text);
        loTextInfo.setText(psText);
        Button loBtnOk = (Button) loDialog
                .findViewById(R.id.dialog_info_btn_ok);
        Button loBtnCancel = (Button) loDialog
                .findViewById(R.id.dialog_info_btn_cancel);
        if (!TextUtils.isEmpty(psPositiveName))
            loBtnOk.setText(psPositiveName);
        if (!TextUtils.isEmpty(psCancelName))
            loBtnCancel.setText(psCancelName);
        loBtnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loDialog.dismiss();
                if (poHandler != null) {
                    Message loMsg = new Message();
                    loMsg.what = piCloseMsg;
                    poHandler.sendMessage(loMsg);
                }
            }
        });
        loBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loDialog.dismiss();
            }
        });
    }


    public static String getIp(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return getLocalIpAddress();
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return getIpForWife(context);
            }
        } else {
            Toast.makeText(context, "当前没有网络连接！请确认！", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    public static String getIpForWife(Context context) {

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

}
