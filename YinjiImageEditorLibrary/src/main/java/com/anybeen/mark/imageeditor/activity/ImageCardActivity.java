package com.anybeen.mark.imageeditor.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.anybeen.mark.imageeditor.adapter.CardTemplateAdapter;
import com.anybeen.mark.imageeditor.entity.CardDataInfo;
import com.anybeen.mark.imageeditor.entity.CardInnerPicInfo;
import com.anybeen.mark.imageeditor.entity.CardInnerPicShapeInfo;
import com.anybeen.mark.imageeditor.entity.CardTemplateInfo;
import com.anybeen.mark.imageeditor.entity.StickerInfo;
import com.anybeen.mark.imageeditor.utils.BitmapUtils;
import com.anybeen.mark.imageeditor.utils.CommonUtils;
import com.anybeen.mark.imageeditor.utils.Const;
import com.anybeen.mark.imageeditor.utils.FileUtils;
import com.anybeen.mark.imageeditor.view.StickerForCard;
import com.anybeen.mark.imageeditor.view.StickerView;
import com.anybeen.mark.yinjiimageeditorlibrary.R;

import java.util.ArrayList;

/**
 * Created by maidou on 2016/4/18.
 */
public class ImageCardActivity extends Activity{
    private Context mContext;
    private ProgressDialog mTaskDialog;
    private RecyclerView mRvCardSample;
    private CardTemplateAdapter mSamplePicAdapter;
    private int mCurrPosition = 0;

    private ArrayList<CardTemplateInfo> cardTemplateInfoList = new ArrayList<>();
    private CardTemplateInfo mCurrTemplateInfo = new CardTemplateInfo();
    private ArrayList<String> templateFolderList = new ArrayList<>();
    private String[] picsUrl;       // 图片的 url

    // 保存的参数
    private CardDataInfo saveDataInfo = new CardDataInfo();

    private ArrayList<StickerForCard> mStickerForCard = new ArrayList<>();// 存储背后的图片

    private ArrayList<StickerView> mStickerViews = new ArrayList<>();   // 存储贴纸列表
    private StickerView mCurrentView;           // 当前处于编辑状态的贴纸


    private Bitmap originalBitmap;
    private Bitmap copyBitmap;
    // originalBitmap 与 imageView 的缩放比例，留白区域
    private float mScale = 0f, mLeaveLeft = 0f, mLeaveTop = 0f;

    // --------top save & back
    private ImageView bt_save;
    private ImageView iv_back;

    // --------content
    private FrameLayout rl_bitmap_layout;           // TODO bitmap 层，需要自定义
    private ImageView iv_template_image;            // 模板图层
    private FrameLayout fl_sticker_and_text_layout; // 文字和贴纸层

    private String mEditType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置键盘隐藏状态
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // 设置取代顶部状态栏
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_card_image);
        mContext = this;
//        long t1 = System.currentTimeMillis();
//        String sTemp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/YinJiEditor";
//        FileUtils.copyZipFile(mContext, "Window", sTemp);
//
//        // 继续做zip文件的解压缩
//        FileUtils.unZipFile();
//        long t2 = System.currentTimeMillis();
//        System.out.println("耗时450毫秒：" + (t2 - t1));

        mTaskDialog = new ProgressDialog(ImageCardActivity.this);
        mEditType = getIntent().getStringExtra(Const.C_IMAGE_EDIT_TYPE);
        if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_NEW_ADD)) {
            // 新增操作, 复制需要加载的图片的 url
            ArrayList<String> picsList = getIntent().getStringArrayListExtra(Const.PICS_LIST);
            ArrayList<String> tempList = FileUtils.copyFilesToNewFolderByUrl(picsList, Const.C_TEMP_PIC_FOLDER);
            picsUrl = new String[tempList.size()];
            picsUrl = tempList.toArray(picsUrl);
        }
        else if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_RE_EDIT)) {
            // 重新编辑操作, 需要判断加载的图片的 pic 个数选择不同的模板
            saveDataInfo = FileUtils.readFileToCardDataInfo();
            picsUrl = new String[saveDataInfo.innerPicInfoList.size()];
            for (int i = 0; i < saveDataInfo.innerPicInfoList.size(); i++) {
                picsUrl[i] = saveDataInfo.innerPicInfoList.get(i).picUrl;
            }
        }

        // 加载虚拟数据
        initTemplatesData(picsUrl);

        initView();

        if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_NEW_ADD)) {
            // 设置当前操作的 template
            mCurrTemplateInfo = cardTemplateInfoList.get(0);
        }
        else if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_RE_EDIT)) {
            mCurrTemplateInfo = cardTemplateInfoList.get(3);
        }
        templateSelected(mCurrTemplateInfo);
    }

    /**
     * 加载模板数据
     * @param imgUrl  需要加载在卡片模板底部的图片的 Url
     */
    private void initTemplatesData(String[] imgUrl) {
        // according img count to check diff folder 根据选中的图片张数选择不同的模板类型
        String cardType;
        if (imgUrl == null) {
            cardType = Const.CARD_TYPE_FOLDER[0];
        } else {
            cardType = Const.CARD_TYPE_FOLDER[imgUrl.length];
        }
        // step 从文件夹下读取模板的文件路径
        String templatesAbsPath = Const.CARD_TEMPLATES + cardType;
        templateFolderList = FileUtils.readListFromCardTemplatesDir(templatesAbsPath);

        // wait TODO 读取 params.txt 文件, 获取 json 数据配置
        CardTemplateInfo info;
        CardTemplateInfo tempInfo;
        for (int i = 0; i < templateFolderList.size() ; i++) {
            info = new CardTemplateInfo();
            info.fileName = templateFolderList.get(i);
            info.absPath = templatesAbsPath + templateFolderList.get(i);

            info.configUrl = templatesAbsPath + templateFolderList.get(i) + "/" + Const.C_CONFIG;

            // TODO 解析 config.txt
            tempInfo = FileUtils.parseTemplateConfig(templatesAbsPath + templateFolderList.get(i) + "/" + Const.C_CONFIG);
            info.modelName = tempInfo.modelName;
            info.description = tempInfo.description;
            info.category = tempInfo.category;
            info.shapeCount = tempInfo.shapeCount;
            info.orientation = Const.CARD_ORIENTATION[Integer.parseInt(tempInfo.orientation)];
            info.stickerCount = tempInfo.stickerCount;
            info.carrotCount = tempInfo.carrotCount;
            info.innerPicShapeInfoList = tempInfo.innerPicShapeInfoList;

            info.sampleUrl = templatesAbsPath + templateFolderList.get(i) + "/" + Const.C_SAMPLE + tempInfo.sampleName;
            info.templateUrl = templatesAbsPath + templateFolderList.get(i) + "/" + Const.C_TEMPLATE + tempInfo.templateName;
            cardTemplateInfoList.add(info);

            // TODO　loadSticker and loadText

        }
    }

    private void initView() {
        mRvCardSample = (RecyclerView) findViewById(R.id.rv_template_list);
        mRvCardSample.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvCardSample.setLayoutManager(lm);
        mSamplePicAdapter = new CardTemplateAdapter(this, cardTemplateInfoList);
        mSamplePicAdapter.setOnItemClickListener(new CardTemplateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CardTemplateInfo templateInfo) {
                if (mCurrPosition == position) {
                    return;
                }        // 重复点击不生效？要不要生效
                mCurrPosition = position;
                mCurrTemplateInfo = templateInfo;
                templatesItemCheck(mCurrTemplateInfo);
            }
        });
        mRvCardSample.setAdapter(mSamplePicAdapter);

        // init toolbar view
        bt_save = (ImageView) findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAll();
            }
        });
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 不保存模板是做文件删除, 删掉无用的文件
                FileUtils.deleteFileFromStringArr(picsUrl);
                finish();
            }
        });

        // init content view
        rl_bitmap_layout = (FrameLayout) findViewById(R.id.rl_bitmap_layout);
        iv_template_image = (ImageView) findViewById(R.id.iv_templates_image);
        fl_sticker_and_text_layout = (FrameLayout) findViewById(R.id.fl_sticker_and_text_layout);
    }

    /**
     * 测试方法
     * 在初始化的时候加载默认的模板，用来表示加载了第一个 adapter 的模板数据
     */
    private void templateSelected(CardTemplateInfo templateInfo) {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask();
        mLoadImageTask.execute(templateInfo.templateUrl);

    }

    private void templatesItemCheck(CardTemplateInfo templateInfo) {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask();
        mLoadImageTask.execute(templateInfo.templateUrl);
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    // 测试解析 config.txt 文件, 获取框框参数
    private void parseConfigParams(CardTemplateInfo templateInfo) {
        rl_bitmap_layout.removeAllViews();
        mStickerForCard.clear();
        int widthAfterScale, heightAfterScale, leftMargin, topMargin, rightMargin, bottomMargin;
        Bitmap bitmap = null;
        for (int i = 0; i < templateInfo.innerPicShapeInfoList.size(); i++){
            CardInnerPicShapeInfo info = templateInfo.innerPicShapeInfoList.get(i);
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.isRecycled();
            }
            widthAfterScale = CommonUtils.floatToInt(info.width / mScale);
            System.out.println("widthAfterScale:" + widthAfterScale);
            heightAfterScale = CommonUtils.floatToInt(info.height / mScale);
            leftMargin = CommonUtils.floatToInt(info.left / mScale + mLeaveLeft);
            topMargin = CommonUtils.floatToInt(info.top / mScale + mLeaveTop);
            rightMargin = CommonUtils.floatToInt(rl_bitmap_layout.getWidth() - ((info.left + info.width) / mScale + mLeaveLeft));
            bottomMargin = CommonUtils.floatToInt(rl_bitmap_layout.getHeight() - ((info.top + info.height) / mScale + mLeaveTop));

            // 在解析的时候保存对应的 picUrl
            templateInfo.innerPicShapeInfoList.get(i).picUrl = picsUrl[i];
            // TODO -getRightSizeBitmap 这个方法未完整, 待修正
            bitmap = BitmapUtils.getRightSizeBitmap(picsUrl[i],
                    CommonUtils.floatToInt(info.width / mScale),
                    CommonUtils.floatToInt(info.height / mScale)
            );

            final StickerForCard view = new StickerForCard(mContext);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            lp.leftMargin = leftMargin;
            lp.topMargin = topMargin;
            lp.rightMargin = rightMargin;
            lp.bottomMargin = bottomMargin;
            lp.width = widthAfterScale;
            lp.height = heightAfterScale;

            view.setLayoutParams(lp);
            view.setBitmap(bitmap);
//            view.setBackgroundColor(templateInfo.backgroundColor);
            rl_bitmap_layout.addView(view);
            mStickerForCard.add(view);
            Matrix matrix = new Matrix();
            // TODO 判断是重载还是新增, 设置 matrix
            if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_RE_EDIT)) {
                float[] floats = saveDataInfo.innerPicInfoList.get(0).floatArr;
                matrix.setValues(floats);
                view.setMatrix(matrix);
            } else {
//                matrix.postScale(mScale, mScale);
//                view.setMatrix(matrix);
            }
        }
    }


    // task line---------------------
    private LoadImageTask mLoadImageTask;
    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTaskDialog.setCancelable(false);
            mTaskDialog.setMessage("图片加载中...");
            mTaskDialog.show();
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            int displayWidth = BitmapUtils.getScreenPixels(mContext).widthPixels;
            int displayHeight = BitmapUtils.getScreenPixels(mContext).heightPixels;
            if (originalBitmap != null && originalBitmap.isRecycled()) {
                originalBitmap.recycle();
                originalBitmap = null;
            }
            originalBitmap = BitmapUtils.getSampledBitmap(params[0], displayWidth, displayHeight);
            return originalBitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            iv_template_image.setImageBitmap(result);

            float[] scaleAndLeaveSpace = CommonUtils.calcScaleAndLeaveSize(result, iv_template_image);
            mScale  = scaleAndLeaveSpace[0];      // 原图与ImageView的缩放比例
            mLeaveLeft = scaleAndLeaveSpace[1];   // 图片自动缩放时造成的留白区域
            mLeaveTop = scaleAndLeaveSpace[2];
            parseConfigParams(mCurrTemplateInfo);
            mTaskDialog.dismiss();
        }
    }// end inner class

    private SaveTask mSaveTask;
    private int width, height;
    private final class SaveTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTaskDialog.setCancelable(false);
            mTaskDialog.setMessage("图片保存中...");
            mTaskDialog.show();

            width = originalBitmap.getWidth();
            height = originalBitmap.getHeight();
            // 要加透明度参数
            copyBitmap = originalBitmap.copy(Bitmap.Config.ARGB_4444, true);
            Canvas canvas = new Canvas();
            Bitmap nullBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            canvas.setBitmap(nullBitmap);   // 填充 bitmap
            canvas.drawColor(Color.MAGENTA);// 绘制颜色
//            BitmapUtils.saveBitmap(nullBitmap, "backgroundColor");

            canvas.save();
            float translateX = mCurrTemplateInfo.innerPicShapeInfoList.get(0).left * 1.0f;
            float translateY = mCurrTemplateInfo.innerPicShapeInfoList.get(0).top * 1.0f;

            System.out.println("copyBitmap:height:" + copyBitmap.getHeight());
            System.out.println("copyBitmap:tempWidth:" + copyBitmap.getWidth());

            Bitmap bitmapCopy = mStickerForCard.get(0).getBitmap().copy(Bitmap.Config.RGB_565, true);
            System.out.println("bitmapCopy:" + bitmapCopy.getWidth() + "/" + bitmapCopy.getHeight());

            // 放大位图!!!因为卡片模板在显示的时候被屏幕缩小了 mScale 倍（视觉上的缩小）, 而卡片还是原来的尺寸
            // 所以 bitmap 要放大 mScale 倍数, 在保存的时候
            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(mScale, mScale);
            Bitmap scaleBitmap = Bitmap.createBitmap(bitmapCopy, 0, 0,
                    bitmapCopy.getWidth(), bitmapCopy.getHeight(), scaleMatrix, true);
            // 放大完成, 计算偏移量
            Matrix tempMatrix = mStickerForCard.get(0).saveMatrix();
            float[] tempFloats = new float[9];
            tempMatrix.getValues(tempFloats);
            canvas.drawBitmap(scaleBitmap, translateX + tempFloats[2] * mScale, translateY + tempFloats[5] * mScale, null);
            canvas.restore();
            BitmapUtils.saveBitmap(scaleBitmap, "decodeBitmap");

            // bitmap 见好就收
            bitmapCopy.recycle();
            scaleBitmap.recycle();

            canvas.drawBitmap(copyBitmap, new Matrix(), null);
            BitmapUtils.saveBitmap(nullBitmap, "copyBitmap");
            // bitmap 保存完成
            // TODO 保存其它图片参数, 复制图片到指定目录下

            String timeMillis = System.currentTimeMillis() + "";
            saveDataInfo.saveId = timeMillis;
            saveDataInfo.saveCreateTime = timeMillis;
            saveDataInfo.saveCardName = "copyBitmap.jpg";
            saveDataInfo.saveCardGenerateUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/saves/copyBitmap.jpg";
            saveDataInfo.saveCardBackgroundColor = mCurrTemplateInfo.backgroundColor;

            saveDataInfo.saveFileName = mCurrTemplateInfo.fileName;
            saveDataInfo.saveTemplateName = mCurrTemplateInfo.templateName;
            saveDataInfo.saveSampleName = mCurrTemplateInfo.sampleName;

            CardInnerPicInfo picInfo = new CardInnerPicInfo();
            picInfo.picUrl = mCurrTemplateInfo.innerPicShapeInfoList.get(0).picUrl;
            picInfo.left = mCurrTemplateInfo.innerPicShapeInfoList.get(0).left;
            picInfo.top = mCurrTemplateInfo.innerPicShapeInfoList.get(0).top;
            picInfo.right = mCurrTemplateInfo.innerPicShapeInfoList.get(0).right;
            picInfo.bottom = mCurrTemplateInfo.innerPicShapeInfoList.get(0).bottom;
            picInfo.width = mCurrTemplateInfo.innerPicShapeInfoList.get(0).width;
            picInfo.height = mCurrTemplateInfo.innerPicShapeInfoList.get(0).height;
            float[] floats = new float[9];
            mStickerForCard.get(0).saveMatrix().getValues(floats);
            picInfo.floatArr = floats;
            saveDataInfo.innerPicInfoList.add(picInfo);

            // 将序列化 saveDataInfo 保存到本地
            FileUtils.saveSerializableCardDataInfo(saveDataInfo);
            System.out.println("本地序列化保存完成");

        }
        @Override
        protected Bitmap doInBackground(String... params) {
            return null;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            mTaskDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        try{
            mTaskDialog.dismiss();
        }catch (Exception e) {
        }
        super.onDestroy();
    }

    /**
     * 保存参数
     * 1、一张截图,
     * 2、保存对应的卡片模板图片的 url
     * 3、inner pic rect 参数和对应的图片(编号或者文件名), 做重载
     */
    private void saveAll() {
        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
        mSaveTask = new SaveTask();
        mSaveTask.execute("");
    }

}
//            Matrix matrix = mStickerForCard.get(0).saveMatrix();
//            float[] floats = new float[9];
//            matrix.getValues(floats);
//            for (float f : floats) {
//                System.out.println("ff:" + f);
//            }
//            float f3 = floats[2];// is wrong way, you must be get the scale params and put scale
//            float f6 = floats[5];
//            floats[2] = f3;
//            floats[5] = f6;