package com.anybeen.mark.imageeditor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by maidou on 2016/4/14.
 * TODO 抽取
 *      分出传统编辑和卡片类型编辑
 *
 */
public abstract class ImageEditorBaseActivity extends Activity {
    private ProgressDialog mTaskDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initView();

        initData();
    }


    public abstract void initView();

    public abstract void initData();

    public void initProgressDialog() {
        mTaskDialog = new ProgressDialog(this);
        mTaskDialog.setCancelable(false);
        mTaskDialog.setMessage("加载中...");
        mTaskDialog.show();
    }

    public void setDialogShow() {
        mTaskDialog.show();
    }

    public void setDialogDismiss() {
        mTaskDialog.dismiss();
    }

    public void setDialogText(String text) {
        mTaskDialog.setMessage(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            mTaskDialog.dismiss();
        }catch (Exception e) {
        }
    }
}
