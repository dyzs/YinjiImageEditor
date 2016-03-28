//package com.anybeen.mark.yinjiimageeditorlibrary.engine;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//
//import com.anybeen.mark.yinjiimageeditorlibrary.ImageEditorActivity;
//import com.anybeen.mark.yinjiimageeditorlibrary.utils.BitmapUtils;
//
///**
// * Created by maidou on 2016/3/28.
// */
//public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//    private ProgressDialog dialog;
//    private ImageEditorActivity mAct;
//    private Bitmap copyBitmap;
//    public LoadImageTask(ImageEditorActivity activity) {
//        this.mAct = activity;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        dialog = new ProgressDialog(mAct);
//        dialog.setCancelable(false);
//        dialog.setMessage("图片加载中...");
//        dialog.show();
//    }
//    @Override
//    protected Bitmap doInBackground(String... params) {
//        int displayWidth = BitmapUtils.getScreenPixels(mAct).widthPixels;
//        int displayHeight = BitmapUtils.getScreenPixels(mAct).heightPixels;
//        copyBitmap = BitmapUtils.loadImageByPath(params[0], displayWidth, displayHeight).copy(Bitmap.Config.RGB_565, true);
//        return copyBitmap;
//    }
//    @Override
//    protected void onPostExecute(Bitmap result) {
//        super.onPostExecute(result);
//        iv_main_image.setImageBitmap(result);
//        dialog.dismiss();
//    }
//}
