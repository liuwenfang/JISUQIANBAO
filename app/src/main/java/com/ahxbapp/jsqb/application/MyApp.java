package com.ahxbapp.jsqb.application;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.ahxbapp.jsqb.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import org.litepal.LitePalApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by cc191954 on 14-8-9.
 * 用来做一些初始化工作，比如设置 host，
 * 初始化图片库配置
 */
public class MyApp extends LitePalApplication {

    public static float sScale;
    public static int sWidthDp;
    public static int sWidthPix;
    public static int sHeightPix;

    public static int sEmojiNormal;
    public static int sEmojiMonkey;

    private static int sMainCreate = 0;

    public static boolean getMainActivityState() {
        return sMainCreate > 0;
    }

    public static void setMainActivityState(boolean create) {
        if (create) {
            ++sMainCreate;
        } else {
            --sMainCreate;
        }
        Log.d("", "showsss " + sMainCreate);

    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .diskCacheFileCount(300)
//                .imageDownloader(new MyImageDownloader(context))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs() // Remove for release app
                .diskCacheExtraOptions(sWidthPix / 3, sWidthPix / 3, null)
                .build();

        ImageLoader.getInstance().init(config);
    }

    private static String getProcessName(Context context) {
        ActivityManager actMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = actMgr.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : appList) {
            if (info.pid == android.os.Process.myPid()) {
                return info.processName;
            }
        }
        return "";
    }

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e("", "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e("", "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
//        AccountInfo.CustomHost customHost = AccountInfo.getCustomHost(this);
//        String host = customHost.getHost();
//        if (host.isEmpty()) {
//            host = Global.DEFAULT_HOST;
//        }
//        Global.HOST = host;
//        Global.HOST_API = Global.HOST + "/api";
//
//        try {
//            Global.sVoiceDir = FileUtil.getDestinationInExternalFilesDir(this, Environment.DIRECTORY_MUSIC, FileUtil.DOWNLOAD_FOLDER).getAbsolutePath();
//            Log.w("VoiceDir", Global.sVoiceDir);
//        } catch (Exception e) {
//            Global.errorLog(e);
//        }
//
//
//        MyAsyncHttpClient.init(this);
//
        initImageLoader(this);
//
//        loadBaiduMap();
//
        sScale = getResources().getDisplayMetrics().density;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
        sHeightPix = getResources().getDisplayMetrics().heightPixels;
        sWidthDp = (int) (sWidthPix / sScale);
//
//        sEmojiNormal = getResources().getDimensionPixelSize(R.dimen.emoji_normal);
//        sEmojiMonkey = getResources().getDimensionPixelSize(R.dimen.emoji_monkey);
//
//        sUserObject = AccountInfo.loadAccount(this);
//        sUnread = new Unread();
//
//        RedPointTip.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    // --- Google Analytics --------------------------------------------------------------------
//    private Tracker mTracker;
//
//    synchronized public Tracker getDefaultTracker() {
//        if (mTracker == null) {
//            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//            mTracker = analytics.newTracker(R.xml.global_tracker);
//        }
//        return mTracker;
//    }
}
