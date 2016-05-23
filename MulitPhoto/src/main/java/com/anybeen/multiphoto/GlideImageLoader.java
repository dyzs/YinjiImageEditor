package com.anybeen.multiphoto;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)    //配置上下文
                .load("file://" + path)         //设置图片路径
                .error(R.mipmap.ic_gf_default_photo)  //设置错误图片
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
