package com.anybeen.mark.imageeditor.global;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by maidou on 2016/4/18.
 */
public class YinjiImageEditorApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
