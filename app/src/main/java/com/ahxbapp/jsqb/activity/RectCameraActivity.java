package com.ahxbapp.jsqb.activity;

import android.app.Activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.utils.PrefsUtil;
import com.ahxbapp.jsqb.view.CameraTopRectView;
import com.ahxbapp.jsqb.view.MyCamPara;

/**
 * 拍照  暂未用到
 */
public class RectCameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView mySurfaceView = null;
    private SurfaceHolder mySurfaceHolder = null;
    private CameraTopRectView topView = null; //自定义顶层view

    private Camera myCamera = null;

    private Camera.Parameters myParameters;

    private ImageView conversionImage;
    private TextView cancelTxt;
    private ImageView takeImage;
    private TextView saveTxt;
    private TextView rephotographTxt;

    private ImageView ivPhoto;

    private boolean isPreviewing = false;
    private Bitmap bm;
    private static final String IMG_PATH = "SHZQ";
    private File file;
    private Uri uri;

    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window myWindow = this.getWindow();
        myWindow.setFlags(flag, flag);

        setContentView(R.layout.activity_rect_camera);
        mySurfaceView = (SurfaceView) findViewById(R.id.sv_camera);
        mySurfaceView.setZOrderOnTop(false);
        mySurfaceHolder = mySurfaceView.getHolder();
        mySurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        topView = (CameraTopRectView) findViewById(R.id.top_view);
        cancelTxt = (TextView) findViewById(R.id.txt_cancel);
        conversionImage = (ImageView) findViewById(R.id.image_conversion);
        takeImage = (ImageView) findViewById(R.id.image_take);

        takeImage.setClickable(false);
        conversionImage.setClickable(false);
        cancelTxt.setClickable(false);
        takeImage.setOnClickListener(this);
        conversionImage.setOnClickListener(this);
        cancelTxt.setOnClickListener(this);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        ivPhoto.setVisibility(View.GONE);
        topView.draw(new Canvas());
        String zp = PrefsUtil.getString(RectCameraActivity.this, "zp");
        if (zp.equals("3")) {
            topView.setVisibility(View.GONE);
        }
        saveTxt = (TextView) findViewById(R.id.txt_save);
        rephotographTxt = (TextView) findViewById(R.id.txt_rephotograph);
        saveTxt.setOnClickListener(this);
        rephotographTxt.setOnClickListener(this);
        initCamera();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (myCamera == null) {

            initCamera();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (myCamera != null) {
            isPreviewing = false;
            conversionImage.setClickable(false);
            takeImage.setClickable(false);
            cancelTxt.setClickable(false);
            myCamera.release(); // release the camera for other applications
            myCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (myCamera != null) {
            myCamera.release();
            myCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try {
            if (myCamera == null) {
                return;
            }
            myCamera.setPreviewDisplay(mySurfaceHolder);
            myCamera.startPreview();
            isPreviewing = true;
            conversionImage.setClickable(true);
            takeImage.setClickable(true);
            cancelTxt.setClickable(true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }
    }

    ShutterCallback myShutterCallback = new ShutterCallback() {

        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };
    PictureCallback myRawCallback = new PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub

        }
    };
    PictureCallback myjpegCalback = new PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
                isPreviewing = false;

                saveTxt.setVisibility(View.VISIBLE);
                rephotographTxt.setVisibility(View.VISIBLE);
                conversionImage.setVisibility(View.GONE);
                takeImage.setVisibility(View.GONE);
                cancelTxt.setVisibility(View.GONE);

                Bitmap sizeBitmap = Bitmap.createScaledBitmap(bitmap,
                        topView.getViewWidth(), topView.getViewHeight() - 50, true);
                String zp = PrefsUtil.getString(RectCameraActivity.this, "zp");
                if (zp.equals("3")) {
                    bm = sizeBitmap;
                } else {
                    bm = Bitmap.createBitmap(sizeBitmap, topView.getRectLeft() - 50,
                            topView.getRectTop(),
                            topView.getRectRight() - topView.getRectLeft() - 50,
                            topView.getRectBottom() - topView.getRectTop());// 截取
//                    bm = rotateBitmap(bm, 90);
                    ivPhoto.setImageBitmap(bm);
                    ivPhoto.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        return newBM;
    }

    private static Camera.Size pictureSize;
    private static Camera.Size previewSize;
    private Display display;

    // 初始化摄像头
    public void initCamera() {
        if (myCamera == null) {
            myCamera = getCameraInstance();
        }

        if (myCamera != null) {
            myParameters = myCamera.getParameters();
            WindowManager windowManager = getWindowManager();
            display = windowManager.getDefaultDisplay();
//            int screenWidth =  display.getWidth();
//            int screenHeight = display.getHeight();

            myParameters.setPictureFormat(PixelFormat.JPEG);
            String zp = PrefsUtil.getString(RectCameraActivity.this, "zp");
            if (zp.equals("3")) {
                myParameters.set("rotation", 90);
            }
            if (getCameraFocusable() != null) {
                myParameters.setFocusMode(getCameraFocusable());
            }

            pictureSize = MyCamPara.getInstance().getPictureSize(myParameters.getSupportedPictureSizes(), 800);
            //预览大小
            previewSize = MyCamPara.getInstance().getPreviewSize(myParameters.getSupportedPreviewSizes(), display.getHeight());
            if (previewSize != null)
                myParameters.setPreviewSize(previewSize.width, previewSize.height);
            if (pictureSize != null)
                myParameters.setPictureSize(pictureSize.width, pictureSize.height);

            myCamera.setDisplayOrientation(90);
            myCamera.setParameters(myParameters);
        } else {
            Toast.makeText(this, "相机错误！", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.image_conversion:
//切换前后摄像头
                int cameraCount = 0;
                CameraInfo cameraInfo = new CameraInfo();
                cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

                for (int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                    if (cameraPosition == 1) {
                        //现在是后置，变更为前置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                            myCamera.stopPreview();//停掉原来摄像头的预览
                            myCamera.release();//释放资源
                            myCamera = null;//取消原来摄像头


                            myCamera = Camera.open(i);//打开当前选中的摄像头
                            if (myCamera != null) {
                                myParameters = myCamera.getParameters();
//                                WindowManager windowManager = getWindowManager();
//                                Display display = windowManager.getDefaultDisplay();
//                                int screenWidth =  display.getWidth();
//                                int screenHeight = display.getHeight();
//                                myParameters.setPictureSize(screenWidth, screenHeight);
                                myParameters.setPictureFormat(PixelFormat.JPEG);
                                myParameters.set("rotation", 270);
                                if (getCameraFocusable() != null) {
                                    myParameters.setFocusMode(getCameraFocusable());
                                }
                                pictureSize = MyCamPara.getInstance().getPictureSize(myParameters.getSupportedPictureSizes(), 800);
                                //预览大小
                                previewSize = MyCamPara.getInstance().getPreviewSize(myParameters.getSupportedPreviewSizes(), display.getHeight());
                                if (previewSize != null)
                                    myParameters.setPreviewSize(previewSize.width, previewSize.height);
                                if (pictureSize != null)
                                    myParameters.setPictureSize(pictureSize.width, pictureSize.height);

                                myCamera.setDisplayOrientation(90);
                                myCamera.setParameters(myParameters);
                            } else {
                                Toast.makeText(this, "相机错误！", Toast.LENGTH_SHORT).show();
                            }
                            try {
                                myCamera.setPreviewDisplay(mySurfaceHolder);//通过surfaceview显示取景画面
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            myCamera.startPreview();//开始预览
                            cameraPosition = 0;
                            break;
                        }
                    } else {
                        //现在是前置， 变更为后置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                            myCamera.stopPreview();//停掉原来摄像头的预览
                            myCamera.release();//释放资源
                            myCamera = null;//取消原来摄像头
                            myCamera = Camera.open(i);//打开当前选中的摄像头

                            if (myCamera != null) {
                                myParameters = myCamera.getParameters();
                                WindowManager windowManager = getWindowManager();
                                Display display = windowManager.getDefaultDisplay();
                                int screenWidth = display.getWidth();
                                int screenHeight = display.getHeight();
//                                myParameters.setPictureSize(screenWidth, screenHeight);
                                myParameters.setPictureFormat(PixelFormat.JPEG);
                                //myParameters.set("rotation", 90);
                                if (getCameraFocusable() != null) {
                                    myParameters.setFocusMode(getCameraFocusable());
                                }


                                pictureSize = MyCamPara.getInstance().getPictureSize(myParameters.getSupportedPictureSizes(), 800);
                                //预览大小
                                previewSize = MyCamPara.getInstance().getPreviewSize(myParameters.getSupportedPreviewSizes(), display.getHeight());
                                if (previewSize != null)
                                    myParameters.setPreviewSize(previewSize.width, previewSize.height);
                                if (pictureSize != null)
                                    myParameters.setPictureSize(pictureSize.width, pictureSize.height);

                                myCamera.setDisplayOrientation(90);
                                myCamera.setParameters(myParameters);
                            } else {
                                Toast.makeText(this, "相机错误！", Toast.LENGTH_SHORT).show();
                            }
                            try {
                                myCamera.setPreviewDisplay(mySurfaceHolder);//通过surfaceview显示取景画面
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            myCamera.startPreview();//开始预览
                            cameraPosition = 1;
                            break;
                        }
                    }

                }
                break;
            case R.id.image_take:
                // if(CommonUtils.isFastDoubleClick())return;
                if (isPreviewing) {
                    // 拍照
                    myCamera.takePicture(myShutterCallback, myRawCallback,
                            myjpegCalback);

                } else {
//                    // 保存图片
//                    File file = getOutputMediaFile();
//                    this.file = file;
//                    this.uri = Uri.fromFile(file);
//                    if (file != null && bm != null) {
//                        FileOutputStream fout;
//                        try {
//                            fout = new FileOutputStream(file);
//                            BufferedOutputStream bos = new BufferedOutputStream(
//                                    fout);
//                            // Bitmap mBitmap = Bitmap.createScaledBitmap(bm,
//                            // topView.getViewWidth(), topView.getViewHeight(),
//                            // false);
//                            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                            // bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                            bos.flush();
//                            bos.close();
//                        } catch (FileNotFoundException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        //返回数据
//                        Intent intent = new Intent();
//                        intent.setData(uri);
//                        intent.putExtra("sb",file.toString());
//                        Bundle bundle = new Bundle();
//                        intent.putExtras(bundle);
//                        setResult(RESULT_OK, intent);
//                    }
//                    finish();
                }
                break;
            case R.id.txt_cancel:
                // if(CommonUtils.isFastDoubleClick())return;
                if (isPreviewing) {
                    finish();
                    // 退出相机
                } else {
//                    if(myCamera == null){
//                        initCamera();
//                    }
//                    // 重新拍照
//                    try {
//                        myCamera.setPreviewDisplay(mySurfaceHolder);
//                        myCamera.startPreview();
//                        isPreviewing = true;
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
                }
                break;
            case R.id.txt_rephotograph:
                if (myCamera == null) {
                    initCamera();
                }
                // 重新拍照
                ivPhoto.setVisibility(View.GONE);
                try {
                    myCamera.setPreviewDisplay(mySurfaceHolder);
                    myCamera.startPreview();
                    isPreviewing = true;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                saveTxt.setVisibility(View.GONE);
                rephotographTxt.setVisibility(View.GONE);
                conversionImage.setVisibility(View.VISIBLE);
                takeImage.setVisibility(View.VISIBLE);
                cancelTxt.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_save:
                // 保存图片
                File file = getOutputMediaFile();
                this.file = file;
                this.uri = Uri.fromFile(file);
                if (file != null && bm != null) {
                    FileOutputStream fout;
                    try {
                        fout = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(
                                fout);
                        // Bitmap mBitmap = Bitmap.createScaledBitmap(bm,
                        // topView.getViewWidth(), topView.getViewHeight(),
                        // false);
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        // bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //返回数据
                    Intent intent = new Intent();
                    intent.setData(uri);
                    intent.putExtra("sb", file.toString());
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
            default:
                break;
        }
    }

    private String getCameraFocusable() {
        List<String> focusModes = myParameters.getSupportedFocusModes();
        if (focusModes
                .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            return Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            return Camera.Parameters.FOCUS_MODE_AUTO;
        }
        return null;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            if (getCameraId() >= 0) {
                c = Camera.open(getCameraId()); // attempt to get a Camera
                // instance
            } else {
                return null;
            }
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e("getCameraInstance", e.toString());
        }
        return c; // returns null if camera is unavailable
    }

    private int getCameraId() {
        if (!checkCameraHardware(this)) {
            return -1;
        }
        int cNum = Camera.getNumberOfCameras();
        int defaultCameraId = -1;
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < cNum; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
        return defaultCameraId;
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        File mediaStorageDir = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMG_PATH);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
        } else {
            Toast.makeText(this, "没有sd卡", Toast.LENGTH_SHORT).show();
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }
}
