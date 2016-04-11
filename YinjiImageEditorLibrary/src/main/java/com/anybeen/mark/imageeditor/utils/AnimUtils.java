package com.anybeen.mark.imageeditor.utils;

import android.content.Context;
import android.view.View;

import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by maidou on 2016/3/31.
 */
public class AnimUtils {
    public static void translationStickerX(View view, boolean state, Context mContext) {
        if (state) {
            ViewPropertyAnimator.animate(view).translationX(0f).setDuration(200).start();
        } else {
            ViewPropertyAnimator.animate(view).translationX(BitmapUtils.getScreenPixels(mContext).widthPixels).setDuration(200).start();
        }
    }

    public static void translationFilterX(View view, boolean state, Context mContext) {
        if (state) {
            ViewPropertyAnimator.animate(view).translationX(0f).setDuration(200).start();
        } else {
            ViewPropertyAnimator.animate(view).translationX(BitmapUtils.getScreenPixels(mContext).widthPixels).setDuration(200).start();
        }
    }

}
