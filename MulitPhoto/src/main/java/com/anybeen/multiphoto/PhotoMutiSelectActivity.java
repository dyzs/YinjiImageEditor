package com.anybeen.multiphoto;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;

import java.util.List;

public class PhotoMutiSelectActivity extends FragmentActivity implements ImageGridAdapter.OnImageItemClickListener,ImageDataSource.OnImagesLoadedListener,AdapterView.OnItemClickListener,View.OnClickListener,ImagePicker.OnImageSelectedListener{

    private GridView gv_photo_list;
    private ImagePicker imagePicker;

    private boolean isOrigin = false;  //是否选中原图
    private int screenWidth;     //屏幕的宽
    private int screenHeight;    //屏幕的高
    private View mFooterBar;     //底部栏
    private Button mBtnOk;       //确定按钮
    private Button mBtnDir;      //文件夹切换按钮
    private ImageFolderAdapter mImageFolderAdapter;    //图片文件夹的适配器
    private ListPopupWindow mFolderPopupWindow;  //ImageSet的PopupWindow
    private List<ImageFolder> mImageFolders;   //所有的图片文件夹
    private ImageGridAdapter mImageGridAdapter;  //图片九宫格展示的适配器

    //是否需要刷新相册
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_muti_select);
        initView();
        initListener();

        imagePicker = ImagePicker.getInstance();
        imagePicker.clear();
        imagePicker.addOnImageSelectedListener(this);
        DisplayMetrics dm = Utils.getScreenPix(this);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;





        mImageGridAdapter = new ImageGridAdapter(this, null);
        mImageFolderAdapter = new ImageFolderAdapter(this, null);

        onImageSelected(0, null, false);
        new ImageDataSource(this, null, this);
    }

    private void initListener() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        gv_photo_list.setOnItemClickListener(this);
        mBtnOk.setOnClickListener(this);
        mBtnDir.setOnClickListener(this);
    }

    private void initView() {
        gv_photo_list = (GridView)findViewById(R.id.gv_photo_list);
        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnDir = (Button) findViewById(R.id.btn_dir);
        mFooterBar = findViewById(R.id.footer_bar);
    }






    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            Intent intent = new Intent();
            intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
            setResult(ImagePicker.RESULT_CODE_ITEMS, intent);  //多选不允许裁剪裁剪，返回数据
            finish();
        } else if (id == R.id.btn_dir) {
            //点击文件夹按钮
            if (mFolderPopupWindow == null) createPopupFolderList(screenWidth, screenHeight);
            backgroundAlpha(0.3f);   //改变View的背景透明度
            mImageFolderAdapter.refreshData(mImageFolders);  //刷新数据
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            } else {
                mFolderPopupWindow.show();
                //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                int index = mImageFolderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                mFolderPopupWindow.getListView().setSelection(index);
            }
        }  else if (id == R.id.iv_back) {
            //点击返回按钮
            if (null!=mFolderPopupWindow&&mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }else{
                finish();
            }
        }
    }

    /** 创建弹出的ListView */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setBackgroundDrawable(null);
        mFolderPopupWindow.setAdapter(mImageFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);  //如果不设置，就是 AnchorView 的宽度
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(mFooterBar);  //ListPopupWindow总会相对于这个View
        mFolderPopupWindow.setModal(false);  //是否为模态，影响返回键的处理
        mFolderPopupWindow.setAnimationStyle(R.style.popupwindow_anim_style);
        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mImageFolderAdapter.setSelectIndex(position);
                imagePicker.setCurrentImageFolderPosition(position);
                mFolderPopupWindow.dismiss();
                ImageFolder imageFolder = (ImageFolder) adapterView.getAdapter().getItem(position);
                if (null != imageFolder) {
                    mImageGridAdapter.refreshData(imageFolder.images);
                    mBtnDir.setText(imageFolder.name);
                }
                gv_photo_list.smoothScrollToPosition(0);//滑动到顶部
            }
        });
    }

    /** 设置屏幕透明度  0.0透明  1.0不透明 */
    public void backgroundAlpha(float alpha) {
        gv_photo_list.setAlpha(alpha);
        mFooterBar.setAlpha(1.0f);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            if (null!=mFolderPopupWindow&&mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }else{
                finish();
            }
        }
        return false;
    }
    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
        if (imagePicker.getSelectImageCount() > 0) {
            mBtnOk.setText(getString(R.string.select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
            mBtnOk.setTextColor(Color.parseColor("#ffffff"));
            mBtnOk.setEnabled(true);
        } else {
            mBtnOk.setText(getString(R.string.complete));
            mBtnOk.setTextColor(Color.parseColor("#696969"));
            mBtnOk.setEnabled(false);
        }
        mImageGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        this.mImageFolders = imageFolders;
        imagePicker.setImageFolders(imageFolders);
        mImageGridAdapter.refreshData(imageFolders.get(0).images);
        mImageGridAdapter.setOnImageItemClickListener(this);
        gv_photo_list.setAdapter(mImageGridAdapter);
        mImageFolderAdapter.refreshData(imageFolders);
    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
//根据是否有相机按钮确定位置
        position = imagePicker.isShowCamera() ? position - 1 : position;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
            if (requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                //发送广播通知图片增加了
                ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
                ImageItem imageItem = new ImageItem();
                imageItem.path = imagePicker.getTakeImageFile().getAbsolutePath();
                imagePicker.clearSelectedImages();
                imagePicker.addSelectedImageItem(0, imageItem, true);
                Intent intent = new Intent();
                intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                finish();
            }
    }

    @Override
    protected void onDestroy() {
        imagePicker.removeOnImageSelectedListener(this);
        super.onDestroy();
    }
}
