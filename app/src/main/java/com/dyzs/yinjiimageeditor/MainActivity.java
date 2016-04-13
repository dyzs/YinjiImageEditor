package com.dyzs.yinjiimageeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.anybeen.mark.imageeditor.ImageEditorActivity;
import com.anybeen.mark.imageeditor.utils.BitmapUtils;
import com.anybeen.mark.imageeditor.view.CornerImageView;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final int ON_ALBUM_RESULT = 1001;
    private static final int ON_ALBUM_RESULT_CORNER_IMAGE_VIEW = 1002;
    private String albumPictureAbsPath;
    private CornerImageView civ;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        findViewById(R.id.iv_open_album).setOnClickListener(this);
        civ = (CornerImageView) findViewById(R.id.civ);
        civ.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_open_album:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                MainActivity.this.startActivityForResult(intent, MainActivity.ON_ALBUM_RESULT);
                break;
            case R.id.civ:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                MainActivity.this.startActivityForResult(intent, MainActivity.ON_ALBUM_RESULT_CORNER_IMAGE_VIEW);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null && requestCode == ON_ALBUM_RESULT && resultCode == ) {
//
//        }
        if (data == null) return;
        Uri selectedImage = data.getData();
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor cursor = MainActivity.this.getContentResolver().query(selectedImage, filePathColumns, null, null, null);
        if(null==cursor){
            albumPictureAbsPath = selectedImage.getPath();
        }else{
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
            albumPictureAbsPath = cursor.getString(columnIndex);
        }
        if(null!=cursor) {
            cursor.close();
        }

        if (requestCode == ON_ALBUM_RESULT) {
            // 跳转到图片处理
            Intent intent = new Intent(MainActivity.this, ImageEditorActivity.class);
            intent.putExtra(ImageEditorActivity.FILE_PATH, albumPictureAbsPath);
            intent.putExtra(ImageEditorActivity.IS_NEW, true);
            MainActivity.this.startActivity(intent);
        }
        else if (requestCode == ON_ALBUM_RESULT_CORNER_IMAGE_VIEW) {
            int width = civ.getWidth();
            int height = civ.getHeight();
            System.out.println("view width:" + width + "/ view height:" + height);
            Bitmap bitmap = BitmapUtils.getSampledBitmap(albumPictureAbsPath, width, height);
            civ.setPictureBitmap(bitmap);

//            civ.setPictureResources(R.mipmap.pic_icon_filter_sample);
        }
    }
}
