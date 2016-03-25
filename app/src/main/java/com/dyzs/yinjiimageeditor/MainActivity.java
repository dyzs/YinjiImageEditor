package com.dyzs.yinjiimageeditor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.anybeen.mark.yinjiimageeditorlibrary.ImageEditorActivity;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final int ON_ALBUM_RESULT = 1001;
    private String albumPictureAbsPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.iv_open_album).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
        MainActivity.this.startActivityForResult(intent, MainActivity.ON_ALBUM_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null && requestCode == ON_ALBUM_RESULT && resultCode == ) {
//
//        }
//        if (data == null) return;
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
        Intent intent = new Intent(MainActivity.this, ImageEditorActivity.class);
        intent.putExtra(ImageEditorActivity.FILE_PATH, albumPictureAbsPath);
        intent.putExtra(ImageEditorActivity.IS_NEW, true);
        MainActivity.this.startActivity(intent);
    }
}
