package com.ahxbapp.jsqb.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(context).load(path).centerCrop().into(imageView);
//        Uri uri = Uri.parse((String) path);
//        imageView.setImageURI(uri);
    }


}