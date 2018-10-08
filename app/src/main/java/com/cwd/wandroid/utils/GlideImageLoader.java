package com.cwd.wandroid.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by thepl on 2017/11/9.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, final ImageView imageView) {
        imageView.setAlpha(0f);
        imageView.animate().alpha(1.0f).setDuration(500).start();
        Glide.with(context).load(path).into(imageView);
    }
}
