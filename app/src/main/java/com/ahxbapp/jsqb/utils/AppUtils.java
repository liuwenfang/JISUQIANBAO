package com.ahxbapp.jsqb.utils;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class AppUtils {
    public final static int StartImages = 1;
    public final static int StartCamera = 2;
    public final static int StartCamera2 = 3;
    public final static int StartCutPicture = 4;
    /**
     * 缓存信息
     */
    public static SharedPreferences memoryDataInfo;

    /**
     * 用户的信息
     */
    public static Map<String, Object> userInfo;

    public static DisplayImageOptions image_display_options = new DisplayImageOptions.Builder()
            //.showImageOnLoading(R.drawable.android_base_image_default)
//            .showImageForEmptyUri(R.drawable.img_def)
//            .showImageOnFail(R.drawable.img_def)
            .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
            .bitmapConfig(Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

    /**
     * 判空，若为空，显示message
     *
     * @param message
     */
    public static Object checkNull(Object obj, String message, Context context) {
        if (obj instanceof EditText) {
            EditText et = (EditText) obj;
            if (et.getText() == null || et.getText().toString().length() == 0) {
                Vibrator vb = (Vibrator) context
                        .getSystemService(Service.VIBRATOR_SERVICE);
                vb.vibrate(300);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                et.requestFocus();
                return null;
            }
            return et.getText().toString();
        }

        if (obj instanceof TextView) {
            TextView et = (TextView) obj;
            if (et.getText() == null || et.getText().toString().length() == 0) {
                Vibrator vb = (Vibrator) context
                        .getSystemService(Service.VIBRATOR_SERVICE);
                vb.vibrate(300);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                return null;
            }
            return et.getText().toString();
        }

        if (obj == null) {
            Vibrator vb = (Vibrator) context
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vb.vibrate(300);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return null;
        }
        return obj;
    }

    /**
     * 获得编辑的框的值
     *
     * @param edite
     * @return
     */
    public static String getEditeValue(View edite) {
        if (edite instanceof EditText) {
            EditText et = (EditText) edite;
            if (et.getText() != null) {
                return et.getText().toString();
            }
        }

        if (edite instanceof TextView) {
            TextView et = (TextView) edite;
            if (et.getText() != null) {
                return et.getText().toString();
            }
        }
        return "";
    }

    public static void saveBitmapToPath(Bitmap bitmap, String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            fos = new FileOutputStream(path);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }
}
