package com.ahxbapp.jsqb.updateapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


/**
 * Created by Teprinciple on 2016/11/15.
 */
public class UpdateAppUtil {

    /**
     * 获取apk的版本号 currentVersionCode
     * @param ctx
     * @return
     */
    public static int getAPPLocalVersion(Context ctx) {
        int currentVersionCode = 0;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersionCode;
    }

//    public static void getAPPServerVersion(Context context, final VersionCallBack callBack){
////        HttpUtil.getObject(Api.GETVERSION.mapClear().addBody(), VersionInfo.class, new HttpUtil.ObjectCallback() {
////            @Override
////            public void result(boolean b, @Nullable Object obj) {
////                if (b){
//////                        BaseApplication.getIntance().checkVersion();
////                        callBack.callBack((VersionInfo) obj);
////                }
////            }
////        });
//    }

    /**
     * 获取服务器的版本号
     * @param context  上下文
     * @param callBack 为了控制线程，需要获取服务器版本号成功的回掉
     */
    public static void getAPPServerVersion(Context context, final VersionCallBack callBack){
        //todo 自己的网络请求获取 服务器上apk的版本号（需要与后台协商好）
        callBack.callBack(2);
    }

    /**
     * 更新APP
     * @param context
     */
    public static void updateApp(final Context context){
        getAPPServerVersion(context, new VersionCallBack() {
            @Override
            public void callBack(final int versionCode) {
                if (versionCode > 0){
                    Log.i("this","版本信息：当前"+getAPPLocalVersion(context)+",服务器："+ versionCode);
                    if (versionCode > getAPPLocalVersion(context)){
                        //更新版本
//                        final NormalDialog dialog = new NormalDialog(context);
//                        dialog.isTitleShow(true)
//                                .title("版本过低无法使用")
//                                .bgColor(Color.parseColor("#ffffff"))
//                                .cornerRadius(5)
//                                .content("请更新到最新版本才能借款哦")
//                                .btnNum(1)
//                                .btnText("立即更新")
//                                .contentGravity(Gravity.CENTER)
//                                .contentTextColor(Color.parseColor("#33333d"))
//                                .dividerColor(Color.parseColor("#dcdce4"))
//                                .btnTextSize(15.5f, 15.5f)
//                                .btnTextColor(Color.parseColor("#ff2814"), Color.parseColor("#0077fe"))
//                                .widthScale(0.85f)
//                                .show();
//                        dialog.setCanceledOnTouchOutside(false);
//                        dialog.setOnBtnClickL(new OnBtnClickL() {
//                            @Override
//                            public void onBtnClick() {
//                                dialog.dismiss();
//                                //更新
//                                String apkPath = "http://senbada.com:8888/app-release.apk";
//                                DownloadAppUtils.downloadForAutoInstall(context, apkPath, "极速钱包", "更新极速钱包");
//                            }
//                        });
                    }
                }
            }
        });
    }

    public interface VersionCallBack{
        void callBack(int versionCode);
    }
}
