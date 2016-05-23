package com.anybeen.mark.imageeditor.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.view.WindowManager;

import com.anybeen.mark.imageeditor.entity.FilterInfo;
import com.anybeen.mark.imageeditor.utils.BitmapUtils;
import com.anybeen.mark.imageeditor.utils.FileUtils;

/**
 * Created by maidou on 2016/4/14.
 * TODO 抽取
 *      分出传统编辑和卡片类型编辑
 *
 */
public abstract class ImageEditorBaseActivity extends Activity {
    private ProgressDialog mTaskDialog;
    private Bitmap originalBitmap;
    private Bitmap copyBitmap;

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

    /**
     * 加载图片
     */
    public final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private Activity activity;
        public LoadImageTask(Activity act) {
            this.activity = act;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTaskDialog = new ProgressDialog(activity);
            mTaskDialog.setCancelable(false);
            mTaskDialog.setMessage("图片加载中...");
            mTaskDialog.show();
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            int displayWidth = BitmapUtils.getScreenPixels(activity).widthPixels;
            int displayHeight = BitmapUtils.getScreenPixels(activity).heightPixels;
            if (originalBitmap != null && originalBitmap.isRecycled()) {
                originalBitmap.recycle();
                originalBitmap = null;
            }
            originalBitmap = BitmapUtils.getSampledBitmap(params[0], displayWidth, displayHeight);
            copyBitmap = originalBitmap.copy(Bitmap.Config.RGB_565, true);
            return copyBitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            setContentImage(result);
            mTaskDialog.dismiss();
        }
    }// end inner class


    /**
     * 设置主要显示的 Image 对象
     * @param result
     */
    public abstract void setContentImage(Bitmap result);
}
