package com.ahxbapp.jsqb.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ahxbapp.common.DialogUtil;
import com.ahxbapp.common.Global;
import com.ahxbapp.common.ImageLoadTool;
import com.ahxbapp.common.util.SingleToast;
import com.ahxbapp.jsqb.R;
import com.ahxbapp.jsqb.api.APIManager;
import com.ahxbapp.jsqb.utils.ImageUtils;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 身份证认证
 */
@EActivity(R.layout.activity_idphoto)
public class IDPhotoActivity extends TakePhotoActivity {

    @ViewById
    ImageButton mToolbarLeftIB;
    @ViewById
    TextView mToolbarTitleTV;
    @ViewById
    RelativeLayout zp_linear, zm_linear, fm_linear;
    @ViewById
    LinearLayout llPhotoHold;
    @ViewById
    Button submitBtn;
    @ViewById
    ImageView zmImage, fmImage, zpImage, zp_image;

    @ViewById
    RelativeLayout zpImage_rela;

    @Extra
    int loanlogID, type;// type 0   个人身份证  1   担保人

    List<File> selectFiles = new ArrayList<File>();

    @AfterViews
    void init() {
        mToolbarTitleTV.setText("身份证认证");
        mToolbarLeftIB.setImageResource(R.mipmap.back);

        if (type == 0) {
            selectFiles.add(null);
            selectFiles.add(null);
            selectFiles.add(null);
            mSingleToast = new SingleToast(this);
            llPhotoHold.setVisibility(View.VISIBLE);
            getIDPic();
        } else if (type == 1) {
            selectFiles.add(null);
            selectFiles.add(null);
            mSingleToast = new SingleToast(this);
            llPhotoHold.setVisibility(View.GONE);
        }

    }

    void getIDPic() {
        showBlackLoading();
        APIManager.getInstance().Member_GetIdentity(this, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                hideProgressDialog();
                try {
                    final String ZIDPic = response.getJSONObject("data").getString("ZIDPic") == "null" ? "" : "http://img.910ok.com/" + response.getJSONObject("data").getString("ZIDPic");
                    final String FIDPic = response.getJSONObject("data").getString("FIDPic") == "null" ? "" : "http://img.910ok.com/" + response.getJSONObject("data").getString("FIDPic");
                    final String ZPPic = response.getJSONObject("data").getString("IDPic") == "null" ? "" : "http://img.910ok.com/" + response.getJSONObject("data").getString("IDPic");
                    if (!"".equals(ZIDPic)) {
                        ImageLoader.getInstance().displayImage(ZIDPic, zmImage, ImageLoadTool.options);
                    }
                    if (!"".equals(FIDPic)) {
                        ImageLoader.getInstance().displayImage(FIDPic, fmImage, ImageLoadTool.options);
                    }
                    if (!"".equals(ZPPic)) {
                        ImageLoader.getInstance().displayImage(ZPPic, zpImage, ImageLoadTool.options);
                    }

                } catch (JSONException e) {
                }
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }


    void camera(int index) {
        selectIndex = index;
        takePhoto();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }

    @Click
    void zm_linear() {
        camera(1);
    }

    @Click
    void fm_linear() {
        camera(2);
    }

    @Click
    void zp_linear() {
        zpImage_rela.setVisibility(View.VISIBLE);
    }

    @Click
    void text_bg() {
        zpImage_rela.setVisibility(View.GONE);
        camera(3);
    }

    private String path;
    private int selectIndex = 0;

    private void takePhoto() {
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + +System.currentTimeMillis()
                + ".jpg";
        Uri imageUri = Uri.fromFile(new File(path));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            showMiddleToast("请打开相机权限");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 100);
            return;
        }
        getTakePhoto().onPickFromCapture(imageUri);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        if (result.getImage() != null) {
            path = result.getImage().getOriginalPath();
            ImageUtils.doCompressImage(path, path);
            File file = new File(path);
            selectFiles.set(selectIndex - 1, file);
            if (selectIndex == 1) {
                zmImage.setImageBitmap(BitmapFactory.decodeFile(path));
            } else if (selectIndex == 2) {
                fmImage.setImageBitmap(BitmapFactory.decodeFile(path));
            } else if (selectIndex == 3) {
                zpImage.setImageBitmap(convertBmp(BitmapFactory.decodeFile(path)));
            }
        }
    }

    public Bitmap convertBmp(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1); // 镜像水平翻转
        Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);

        return convertBmp;
    }

    @Click
    void zp_image() {
        zpImage_rela.setVisibility(View.GONE);
        camera(3);
    }

    @Click
    void mToolbarLeftIB() {
        finish();
    }

    @Click
    void submitBtn() {
        String[] tips = new String[]{"身份证正面", "身份证背面", "手持身份证"};
        boolean hasError = false;
        int i = 0;
        for (File file : selectFiles) {
            if (file == null) {
                hasError = true;
                break;
            }
            i++;
        }
        if (hasError) {
            showMiddleToast("请重新拍摄" + tips[i]);
            // MyToast.showToast(this,"请选择"+tips[i]);
            return;
        }

        showBlackLoading("上传图片中...");
        uploadImg(0);
    }

    //上传身份证图片
    private void uploadImg(final int index) {
        final APIManager manager = new APIManager();
        manager.user_sfzPhoto(this, selectFiles.get(index), index + 1, loanlogID, type, new APIManager.APIManagerInterface.baseBlock() {
            @Override
            public void Success(Context context, JSONObject response) {
                manager.user_getUserInfo(context, new APIManager.APIManagerInterface.common_object() {
                    @Override
                    public void Success(Context context, Object model) {
                        if (index != selectFiles.size() - 1) {
                            uploadImg(index + 1);
                        } else {
                            hideProgressDialog();
                            showMiddleToast("上传身份证图片成功!");
                            finish();
                        }
                    }

                    @Override
                    public void Failure(Context context, JSONObject response) {
                        hideProgressDialog();
                    }
                });
            }

            @Override
            public void Failure(Context context, JSONObject response) {
                hideProgressDialog();
            }
        });
    }


    /**
     * 载入动画
     */
    private DialogUtil.LoadingPopupWindow mDialogProgressPopWindow = null;

    public void showBlackLoading() {
        showBlackLoading("正在加载中...");
    }

    public void showBlackLoading(String title) {
        initDialogLoading(true);
        DialogUtil.showProgressDialog(this, mDialogProgressPopWindow, title);
    }

    public void initDialogLoading(boolean isBlack) {
        if (mDialogProgressPopWindow == null) {
            PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    hideProgressDialog();
                }
            };

            mDialogProgressPopWindow = DialogUtil.initProgressDialog(this, onDismissListener, isBlack);
        }
    }

    public void hideProgressDialog() {
        if (mDialogProgressPopWindow != null) {
            DialogUtil.hideDialog(mDialogProgressPopWindow);
        }
    }

    private SingleToast mSingleToast;

    public void showMiddleToast(String msg) {
        mSingleToast.showMiddleToast(msg);
    }

}
