package com.ahxbapp.jsqb.application;

import android.app.Application;
import android.os.Handler;

import com.ahxbapp.jsqb.utils.ActivityManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
//import com.umeng.common.message.UmengMessageDeviceConfig;
//import com.umeng.message.IUmengRegisterCallback;
//import com.umeng.message.MsgConstant;
//import com.umeng.message.PushAgent;
//import com.umeng.message.UmengNotificationClickHandler;
//import com.umeng.message.UmengRegistrar;
//import com.umeng.message.entity.UMessage;

import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxq on 2015/11/19.
 */
public class MyApplication extends Application {

    public static MyApplication application;
    // 用于在Activity间传递对象
    public Map<String, Object> intentMap;
    // 记录activity是否在运行状态
    private boolean active;

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    //private PushAgent mPushAgent;
    private Handler handler = new Handler();

    private ActivityManager activityManager = null;
    public ActivityManager getActivityManager() {
        return activityManager;
    }
    public void setActivityManager(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intentMap = new HashMap<String, Object>();
        application = this;
        //初始化自定义Activity管理器
        activityManager = ActivityManager.getScreenManager();
     /*   File temp = new File("/sdcard/ylb/temp/");//自已项目 文件夹
        if (!temp.exists()) {
            temp.mkdirs();
        }*/
        x.Ext.init(this);
        x.Ext.setDebug(false); // 是否输出debug日志
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "imageloader/YLBCache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                        //   .writeDebugLogs() // Remove for releaseapp
                .build();//开始构建
//        MyImageLoader.getInstance().init(config);

/**
 * 该Handler是在BroadcastReceiver中被调用，故
 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
 * */


    }

    /**
     * 将需要传递的对象放到HashMap中
     *
     * @param id
     * @param obj
     */
    public void addIntentObj(String id, Object obj) {
        intentMap.put(id, obj);
    }

    /**
     * 取出对象并将对象在Map中移除
     *
     * @param id
     * @return
     */
    public Object getIntentObj(String id) {
        return intentMap.get(id);
    }

    /**
     * 取出对象并将对象在Map中移除
     *
     * @param id
     * @return
     */
    public Object getIntentRemoveObj(String id) {
        return intentMap.remove(id);
    }

    public static MyApplication getInstance() {
        return application;
    }


}
