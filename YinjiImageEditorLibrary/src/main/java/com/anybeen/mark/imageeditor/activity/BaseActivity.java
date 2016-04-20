package com.anybeen.mark.imageeditor.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by maidou on 2016/4/19.
 */
public abstract class BaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();

        handleEvent();

        initData();
    }


    public abstract void initView();

    public abstract void handleEvent();

    public abstract void initData();


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
