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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.anybeen.mark.imageeditor.ImageCardActivity;
import com.anybeen.mark.imageeditor.ImageEditorActivity;
import com.anybeen.mark.imageeditor.utils.BitmapUtils;
import com.anybeen.mark.imageeditor.utils.ToastUtil;
import com.anybeen.mark.imageeditor.view.CornerImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final int ON_ALBUM_RESULT = 1001;
    private static final int ON_ALBUM_RESULT_CORNER_IMAGE_VIEW = 1002;
    private String albumPictureAbsPath;
    private CornerImageView civ;
    private CornerImageView iv_card_image;      // 打开卡片选择
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        // ToastUtil.makeText(mContext, "我的测试：" + android.os.Build.MODEL + "***" + android.os.Build.BRAND);

        findViewById(R.id.iv_open_album).setOnClickListener(this);
        civ = (CornerImageView) findViewById(R.id.civ);
        civ.setOnClickListener(this);

        iv_card_image = (CornerImageView) findViewById(R.id.iv_card_image);
        iv_card_image.setOnClickListener(this);



        // 自定义toast
        Toast toas = new Toast(mContext);
        toas.setView(LayoutInflater.from(mContext).inflate(R.layout.layout_image_editor_pop_font_select, null));
        toas.setDuration(Toast.LENGTH_LONG);
        toas.show();
        // 使用 facebook fresco
//        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.logo_image);
//        Uri logoUri = Uri.parse("https://raw.githubusercontent.com/liaohuqiu/fresco-docs-cn/docs/static/fresco-logo.png");
//        simpleDraweeView.setImageURI(logoUri);

        SimpleDraweeView aniView = (SimpleDraweeView) findViewById(R.id.sdv_test_iv);
        // Uri aniImageUri = Uri.parse("https://camo.githubusercontent.com/588a2ef2cdcfb6c71e88437df486226dd15605b3/687474703a2f2f737261696e2d6769746875622e71696e6975646e2e636f6d2f756c7472612d7074722f73746f72652d686f7573652d737472696e672d61727261792e676966");
        Uri aniImageUri = Uri.parse("http://car0.autoimg.cn/upload/spec/12215/20120403075232956264.jpg");
        // Uri aniImageUri = Uri.parse("http://img3.imgtn.bdimg.com/it/u=1050619607,704139382&fm=21&gp=0.jpg");

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(aniImageUri)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        aniView.setController(controller);
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
            case R.id.iv_card_image:
                intent = new Intent(this, ImageCardActivity.class);
                MainActivity.this.startActivity(intent);
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
