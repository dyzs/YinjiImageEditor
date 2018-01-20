package com.dyzs.yinjiimageeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.anybeen.mark.imageeditor.activity.ImageCardActivity;
import com.anybeen.mark.imageeditor.ImageEditorActivity;
import com.anybeen.mark.imageeditor.activity.TestActivity;
import com.anybeen.mark.imageeditor.entity.StickerInfo;
import com.anybeen.mark.imageeditor.utils.BitmapUtils;
import com.anybeen.mark.imageeditor.utils.Const;
import com.anybeen.mark.imageeditor.utils.FileUtils;
import com.anybeen.mark.imageeditor.utils.ToastUtil;
import com.anybeen.mark.imageeditor.view.CornerImageView;
import com.anybeen.mark.imageeditor.view.SampleColorProgressBar;
import com.anybeen.mark.imageeditor.view.StickerForCard;
import com.anybeen.multiphoto.GlideImageLoader;
import com.anybeen.multiphoto.ImageItem;
import com.anybeen.multiphoto.ImagePicker;
import com.anybeen.multiphoto.PhotoMutiSelectActivity;
import com.dyzs.yinjiimageeditor.floating.FloatWindowService;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity implements View.OnClickListener{
    private static final int ON_ALBUM_RESULT = 1001;
    private static final int ON_ALBUM_RESULT_CORNER_IMAGE_VIEW = 1002;
    private static final int ON_ALBUM_RESULT_MULIT_PIC = 1003;
    private String albumPictureAbsPath;
    private CornerImageView civ;
    private CornerImageView iv_card_image;      // 打开卡片选择
    private Context mContext;



    private FrameLayout test_rect, test_rect2, test_rect3;

    private Button btn_test_choose_pic;

    private Button iv_card_image_reload;    // 贴纸重载

    private ArrayList<String> mediaList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mContext = this;

        Button startFloatWindow = (Button) findViewById(R.id.start_float_window);
        startFloatWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                startService(intent);
                finish();
            }
        });


        findViewById(R.id.iv_open_album).setOnClickListener(this);

        initMultiPick();

        civ = (CornerImageView) findViewById(R.id.civ);
        civ.setOnClickListener(this);

        iv_card_image = (CornerImageView) findViewById(R.id.iv_card_image);
        iv_card_image.setOnClickListener(this);

        btn_test_choose_pic = (Button) findViewById(R.id.btn_test_choose_pic);
        btn_test_choose_pic.setOnClickListener(this);

        iv_card_image_reload = (Button) findViewById(R.id.iv_card_image_reload);
        iv_card_image_reload.setOnClickListener(this);

        /**
        // 自定义toast
        Toast toas = new Toast(mContext);
        toas.setView(LayoutInflater.from(mContext).inflate(R.layout.layout_image_editor_pop_font_select, null));
        toas.setDuration(Toast.LENGTH_LONG);
        toas.show();
         */
        // 使用 facebook fresco
//        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.logo_image);
//        Uri logoUri = Uri.parse("https://raw.githubusercontent.com/liaohuqiu/fresco-docs-cn/docs/static/fresco-logo.png");
//        simpleDraweeView.setImageURI(logoUri);

        SimpleDraweeView aniView = (SimpleDraweeView) findViewById(R.id.sdv_test_iv);
//        // Uri aniImageUri = Uri.parse("https://camo.githubusercontent.com/588a2ef2cdcfb6c71e88437df486226dd15605b3/687474703a2f2f737261696e2d6769746875622e71696e6975646e2e636f6d2f756c7472612d7074722f73746f72652d686f7573652d737472696e672d61727261792e676966");
//        Uri aniImageUri = Uri.parse("http://car0.autoimg.cn/upload/spec/12215/20120403075232956264.jpg");
//        // Uri aniImageUri = Uri.parse("http://img3.imgtn.bdimg.com/it/u=1050619607,704139382&fm=21&gp=0.jpg");
//
//        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(aniImageUri)
//                .build();
//
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setImageRequest(request)
//                .setAutoPlayAnimations(true)
//                .build();
//        aniView.setController(controller);
//
        aniView.setOnClickListener(this);


//        test_rect = (FrameLayout) findViewById(R.id.test_rect);
//        test_rect2 = (FrameLayout) findViewById(R.id.test_rect2);
//        test_rect3 = (FrameLayout) findViewById(R.id.test_rect3);
//        reloadSticker();
        // 复制文件到 filesDir 下
        copyResToFilesDir();


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
            case R.id.sdv_test_iv:
                intent = new Intent(this, TestActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.btn_test_choose_pic:
                intent = new Intent(this, PhotoMutiSelectActivity.class);
                MainActivity.this.startActivityForResult(intent, ImagePicker.RESULT_CODE_ITEMS);
                break;
            case R.id.iv_card_image_reload:
                intent = new Intent(this, ImageCardActivity.class);
                intent.putExtra(Const.C_IMAGE_EDIT_TYPE, Const.C_IMAGE_EDIT_TYPE_RE_EDIT);
                MainActivity.this.startActivity(intent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null){//选取系统图片
//            if (requestCode == MediaAction.PICK_PICTURE.requestCode()) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if(mediaList!=null){
                    mediaList.clear();
                    Collections.reverse(images);
                    for (ImageItem imageItem : images) {
                        String path = imageItem.path;
                        mediaList.add(path);
                    }
                    // addMediaList(mediaList);
                    Intent intent = new Intent(this, ImageCardActivity.class);
                    intent.putStringArrayListExtra(Const.PICS_LIST, mediaList);
                    intent.putExtra(Const.C_IMAGE_EDIT_TYPE, Const.C_IMAGE_EDIT_TYPE_NEW_ADD);
                    MainActivity.this.startActivity(intent);
                }
//            }
            return;
        }



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



//图片多选的result参数
//        if (resultCode == Activity.RESULT_OK ) {
//            if (requestCode == MediaAction.CAPTURE_PICTURE.requestCode()) {//拍照返回
//                //添加图片
//                if(mediaList!=null&&mediaList.size()>0)
//                    addMediaList(mediaList);
//            }
//            return true;
//        }
    }




    /**
     * 初始化多选
     */
    private void initMultiPick() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
    }


    /**
     * @details 重新载入多个贴纸
     */
    private void reloadSticker() {
        ArrayList<StickerInfo> stickerInfoLists = FileUtils.readFileToStickerInfoLists();
        if (stickerInfoLists == null)return;
        StickerInfo stickerInfo = stickerInfoLists.get(0);
        float[] floats = stickerInfo.floatArr;
        final StickerForCard stickerView = new StickerForCard(this);
        stickerView.setImageResource(Const.STICKERS_VALUES[stickerInfo.index]);
        stickerView.reloadBitmapAfterOnDraw(floats);
        stickerView.setSaveIndex(stickerInfo.index);
        stickerView.setSaveNameCh(stickerInfo.nameCh);
        stickerView.setSaveNameEn(stickerInfo.nameEn);
        stickerView.setSaveFileAbsPath(stickerInfo.fileAbsPath);
        stickerView.setSaveResId(stickerInfo.resId);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        test_rect.addView(stickerView, lp);



        StickerInfo stickerInfo2 = stickerInfoLists.get(1);
        float[] floats2 = stickerInfo2.floatArr;
        final StickerForCard stickerView2 = new StickerForCard(this);
        stickerView2.setImageResource(Const.STICKERS_VALUES[stickerInfo2.index]);
        stickerView2.reloadBitmapAfterOnDraw(floats2);
        stickerView2.setSaveIndex(stickerInfo2.index);
        stickerView2.setSaveNameCh(stickerInfo2.nameCh);
        stickerView2.setSaveNameEn(stickerInfo2.nameEn);
        stickerView2.setSaveFileAbsPath(stickerInfo2.fileAbsPath);
        stickerView2.setSaveResId(stickerInfo2.resId);
        stickerView2.setOperationListener(new StickerForCard.OperationListener() {
            @Override
            public void onEdit(StickerForCard stickerView) {

            }

            @Override
            public void onClick(StickerForCard StickerForCard) {

            }
        });
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        test_rect3.addView(stickerView2, lp2);
    }



    private void copyResToFilesDir() {



    }
}
