//package com.dyzs.yinjiimageeditor;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.Typeface;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.anybeen.mark.app.compoment.Storage;
//import com.anybeen.mark.common.file.FileInfo;
//import com.anybeen.mark.imageeditor.SoftKeyBoardListener;
//import com.anybeen.mark.imageeditor.activity.StickerManagePageActivity;
//import com.anybeen.mark.imageeditor.adapter.CardTemplateAdapter;
//import com.anybeen.mark.imageeditor.component.AnimComponent;
//import com.anybeen.mark.imageeditor.component.CardTemplatesWorker;
//import com.anybeen.mark.imageeditor.component.EditPanelState;
//import com.anybeen.mark.imageeditor.component.KeyboardState;
//import com.anybeen.mark.imageeditor.component.PopWindowComponent;
//import com.anybeen.mark.imageeditor.entity.CardInnerCarrotInfo;
//import com.anybeen.mark.imageeditor.entity.CardInnerPicShapeInfo;
//import com.anybeen.mark.imageeditor.entity.CardInnerStickerInfo;
//import com.anybeen.mark.imageeditor.entity.CardTemplateInfo;
//import com.anybeen.mark.imageeditor.utils.BitmapUtils;
//import com.anybeen.mark.imageeditor.utils.ColorUtil;
//import com.anybeen.mark.imageeditor.utils.CommonUtils;
//import com.anybeen.mark.imageeditor.utils.Const;
//import com.anybeen.mark.imageeditor.utils.DensityUtils;
//import com.anybeen.mark.imageeditor.utils.FileUtils;
//import com.anybeen.mark.imageeditor.utils.FontMatrixUtils;
//import com.anybeen.mark.imageeditor.utils.SharedPrefsConfig;
//import com.anybeen.mark.imageeditor.view.CarrotEditText;
//import com.anybeen.mark.imageeditor.view.CustomSeekBar;
//import com.anybeen.mark.imageeditor.view.MovableTextView2;
//import com.anybeen.mark.imageeditor.view.SelectableView;
//import com.anybeen.mark.imageeditor.view.StickerForCard;
//import com.anybeen.mark.imageeditor.view.StickerView;
//import com.anybeen.mark.model.entity.DataInfo;
//import com.anybeen.mark.model.manager.NoteManager;
//
//import java.util.ArrayList;
//
///**
// * Created by maidou on 2016/4/18.
// */
//public class ImageCardActivity extends Activity{
//    private Context mContext;
//    private ProgressDialog mTaskDialog;
//    private RecyclerView mRvCardSample;
//    private CardTemplateAdapter mSamplePicAdapter;
//    private int mCurrPosition = 0;
//
//    private ArrayList<CardTemplateInfo> cardTemplateInfoListHorizontal = new ArrayList<>();
//    private ArrayList<CardTemplateInfo> cardTemplateInfoListVertical = new ArrayList<>();
//
//    private CardTemplateInfo mCurrTemplateInfo = new CardTemplateInfo();
//    private ArrayList<String> templateFolderList = new ArrayList<>();
//    private String[] originalPicsUrl;
//    private String[] originalPicsCopyUrl;       // 图片的 url
//
//    // 保存的参数
//    private DataInfo mDataInfo;
//    // 表示模板的透明框的图片
//    private ArrayList<StickerForCard> mStickerForShape = new ArrayList<>();
//    // 表示当前模板默认的贴纸
//    private ArrayList<StickerView> mStickerDefault = new ArrayList<>();
//    // 表示当前新增的贴纸(也包含了模板默认的文字)
//    private ArrayList<StickerView> mStickerLists = new ArrayList<>();
//
//    // 表示当前模板默认的文字
//    private ArrayList<MovableTextView2> mMTVsDefault = new ArrayList<>();
//    // 表示新加的文字(也包含了模板默认的文字)
//    private ArrayList<MovableTextView2> mMtvLists = new ArrayList<>();
//
//    private Bitmap originalBitmap;
//    private Bitmap copyBitmap;
//    // originalBitmap 与 imageView 的缩放比例，留白区域
//    private float mScale = 0f, mLeaveLeft = 0f, mLeaveTop = 0f;
//
//    // --------top save & back
//    private RelativeLayout layout_top_toolbar;
//    private ImageView bt_save;
//    private ImageView iv_add_sentence;
//    private ImageView iv_add_sticker;
//    private ImageView iv_back;
//
//    // --------content
//    private FrameLayout fl_base_group_card_image;
//    private FrameLayout rl_bitmap_layout;           // TODO bitmap 层，需要自定义
//    private ImageView iv_template_image;            // 模板图层
//    private FrameLayout fl_sticker_and_text_layout; // 文字和贴纸层
//
//    private String mCardType;   // 对应于 config 中的 channel, 表示透明图片框的个数, 分辨加载图片的个数
//
//    private String mEditType;   // 对应的编辑类型
//
//    // edit panel params
//    private LinearLayout ll_base_edit_panel;
//    private boolean editPanelVisible = false;
//    private LinearLayout edit_panel;            // 文字编辑面板，使用 LayoutInflate
//    private ImageView ep_KeyboardOptions;       // 键盘切换显示与隐藏
//    private CarrotEditText ep_OperateText;      // 文本编辑框
//    private TextView ep_BtnComplete;            // 完成按钮
//    private SeekBar ep_FontSize;                // 字体大小
//    private CustomSeekBar ep_CsbFontColor;      // 字体颜色 seek bar
//    private ImageView ep_IvColorShow;           // 颜色展示
//    private RadioGroup ep_rgFontGroup;          // 字体选择
//    private int currentFontCheckedId = -1;
//    private int mEditPanelHeight = 0;
//    // edit panel params
//
//    public int keyboardHeight = 0;              // 记录键盘高度
//    private boolean openKeyboardOnLoading;
//    private boolean isFirstAddMtv;
//    private KeyboardState mCurKeyboardState;
//    private EditPanelState editPanelGenerateState = EditPanelState.STATE_OTHER;
//
//    // 动画控制器
//    private AnimComponent animInstance = null;
//    private int animDuration = 250;
//
//    // 滤镜控制器
//    private PopWindowComponent filterPopInstance = null;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 设置键盘隐藏状态
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        setContentView(com.anybeen.mark.yinjiimageeditorlibrary.R.layout.activity_card_image);
//        mContext = this;
//        animInstance = AnimComponent.getInstance(mContext);
//        filterPopInstance = PopWindowComponent.getInstance(mContext);
//        openKeyboardOnLoading = true;
//        isFirstAddMtv = true;
//        mTaskDialog = new ProgressDialog(ImageCardActivity.this);
//
//        SharedPreferences mSharedPrefs = getSharedPreferences(SharedPrefsConfig.SP_NAME, MODE_PRIVATE);
//        boolean flag = mSharedPrefs.getBoolean(SharedPrefsConfig.SP_IS_STICKER_WORKER_WORKED, false);
//        if (!flag) {
//            new CardTemplatesWorker(mContext);  // 复制模板和解压模板
//        }
//
//        mEditType = getIntent().getStringExtra(Const.C_IMAGE_EDIT_TYPE);
//        if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_NEW_ADD)) { // 新增
//            mDataInfo = new DataInfo();
//        } else if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_RE_EDIT)) {
//
//        }
//        // mDataInfo = (DataInfo) getIntent().getSerializableExtra("dataInfo");
//
//        // 新增操作, 复制需要加载的图片的 url
//        ArrayList<String> picsList = getIntent().getStringArrayListExtra(Const.PICS_LIST);
//        originalPicsUrl = new String[picsList.size()];
//        originalPicsUrl = picsList.toArray(originalPicsUrl);    // 生成源文件路径
//
//        ArrayList<String> tempList = FileUtils.copyFilesToNewFolderByUrl(picsList, Const.C_TEMP_PIC_FOLDER);
//        originalPicsCopyUrl = new String[tempList.size()];
//        originalPicsCopyUrl = tempList.toArray(originalPicsCopyUrl);      // 复制后的路径
//
//        initTemplatesData(originalPicsCopyUrl.length);
//
//        initView();
//
//        // 设置当前操作的 template
//        mCurrTemplateInfo = cardTemplateInfoListHorizontal.get(0);
//
//        // 默认选中
//        templatesItemCheck(mCurrTemplateInfo);
//
//        initKeyboard();
//
//        initReceiver();
//    }
//
//    private void initTemplatesData(int pics) {
//        // according img count to check diff folder 根据选中的图片张数选择不同的模板类型
//        if (pics == 0) {
//            mCardType = Const.CARD_TYPE_FOLDER[0];
//        } else {
//            mCardType = Const.CARD_TYPE_FOLDER[pics];
//        }
//        // step 从文件夹下读取模板的文件路径
//        String templatesAbsPath = Const.CARD_TEMPLATES + mCardType;
//        templateFolderList = FileUtils.readListFromCardTemplatesDir(templatesAbsPath);
//
//        CardTemplateInfo info;
//        CardTemplateInfo tempInfo;
//        for (int i = 0; i < templateFolderList.size() ; i++) {
//            info = new CardTemplateInfo();
//            info.fileName   = templateFolderList.get(i);
//            info.absPath    = templatesAbsPath + templateFolderList.get(i);
//            info.configUrl  = templatesAbsPath + templateFolderList.get(i) + "/" + Const.C_CONFIG;
//            // 解析 config.txt
//            tempInfo = FileUtils.parseTemplateConfig(templatesAbsPath + templateFolderList.get(i) + "/" + Const.C_CONFIG);
//            info.modelName      = tempInfo.modelName;
//            info.description    = tempInfo.description;
//            info.category       = tempInfo.category;
//            info.channel        = tempInfo.channel;
//            info.orientation    = tempInfo.orientation;
//            info.stickerCount   = tempInfo.stickerCount;
//            info.carrotCount    = tempInfo.carrotCount;
//            info.innerPicShapeInfoList = tempInfo.innerPicShapeInfoList;
//            info.innerStickerInfoList  = tempInfo.innerStickerInfoList;
//            info.innerCarrotInfoList   = tempInfo.innerCarrotInfoList;
//
//            info.sampleUrl = templatesAbsPath + templateFolderList.get(i) + "/" + tempInfo.sampleName;
//            info.templateUrl = templatesAbsPath + templateFolderList.get(i) + "/" + Const.C_IMG + tempInfo.templateName;
//
//            // 判断是横版的还是竖版的, 1 表示竖版 Horizontal
//            if (info.orientation.equals("1")) {
//                cardTemplateInfoListHorizontal.add(info);
//            }
//            else if (info.orientation.equals("0")) {
//                cardTemplateInfoListVertical.add(info);
//            }
//
//        }
//    }
//
//    private void initView() {
//        mRvCardSample = (RecyclerView) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.rv_template_list);
//        mRvCardSample.setHasFixedSize(true);
//        LinearLayoutManager lm = new LinearLayoutManager(mContext);
//        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRvCardSample.setLayoutManager(lm);
//        mSamplePicAdapter = new CardTemplateAdapter(this, cardTemplateInfoListHorizontal);
//        mSamplePicAdapter.setOnItemClickListener(new CardTemplateAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, CardTemplateInfo templateInfo) {
////                if (mCurrPosition == position) {
////                    return;
////                }        // 重复点击不生效？要不要生效
//                mCurrPosition = position;
//                mCurrTemplateInfo = templateInfo;
//                templatesItemCheck(mCurrTemplateInfo);
//            }
//        });
//        mRvCardSample.setAdapter(mSamplePicAdapter);
//
//
//        // base frame layout
//        fl_base_group_card_image = (FrameLayout) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.fl_base_group_card_image);
//        // init toolbar view
//        layout_top_toolbar = (RelativeLayout) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.layout_top_toolbar);
//        bt_save = (ImageView) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.bt_save);
//        iv_add_sentence = (ImageView) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.iv_add_sentence);
//        iv_add_sticker = (ImageView) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.iv_add_sticker);
//        iv_back = (ImageView) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.iv_back);
//        bt_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveAll();
//            }
//        });
//        iv_add_sentence.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editPanelGenerateState = EditPanelState.STATE_NEW_ADD_MTV;
//                CommonUtils.hitKeyboardOpenOrNot(mContext);
//                if (isFirstAddMtv) {
//                    // 表示第一次添加 mtv 文本
//                    isFirstAddMtv = false;
//                } else {
//                    // 设置弹出软键盘
//                    if (mMtvLists.size() > 0) {
//                        for (MovableTextView2 m : mMtvLists) {
//                            m.setSelected(false);
//                        }
//                    }
//                    addMovableTextView();
//                }
//            }
//        });
//        iv_add_sticker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {   // 打开贴纸页面
//                // 2016/5/19 新增贴纸管理页面
//                Intent intent = new Intent(ImageCardActivity.this, StickerManagePageActivity.class);
//                startActivity(intent);
////                startActivityForResult(intent, StickerManagePageActivity.REQUEST_CODE_FOR_IMAGE_CARD);
////                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_templates_more);
////                addStickerView(bitmap, mStickerLists);
//            }
//        });
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO: 2016/5/20 如果在新增的时候，不保存模板时做文件删除, 删掉无用的文件
//                FileUtils.deleteFileFromStringArr(originalPicsCopyUrl);
//                finish();
//            }
//        });
//
//        // init content view
//        rl_bitmap_layout = (FrameLayout) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.rl_bitmap_layout);
//        iv_template_image = (ImageView) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.iv_templates_image);
//        fl_sticker_and_text_layout = (FrameLayout) findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.fl_sticker_and_text_layout);
//
//
//        // edit_panel 的父容器
////        ll_base_edit_panel = (LinearLayout) findViewById(R.id.ll_base_edit_panel);
//        ll_base_edit_panel = (LinearLayout) LayoutInflater.from(mContext).inflate(com.anybeen.mark.yinjiimageeditorlibrary.R.layout.layout_image_editor_base_edit_panel, null);
//        ll_base_edit_panel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                operateComplete();
//            }
//        });
//
//        ll_base_edit_panel.setVisibility(View.INVISIBLE);
//
//        fl_base_group_card_image.addView(ll_base_edit_panel);
//    }
//
//    private void templatesItemCheck(CardTemplateInfo templateInfo) {
//        if (mLoadImageTask != null) {
//            mLoadImageTask.cancel(true);
//        }
//        mLoadImageTask = new LoadImageTask();
//        mLoadImageTask.execute(templateInfo.templateUrl);
//    }
//
//    // 解析 config.txt 文件, 获取框框参数
//    private void parseConfigParams(CardTemplateInfo templateInfo) {
//        rl_bitmap_layout.removeAllViews();
//        mStickerForShape.clear();
//        int widthAfterScale, heightAfterScale, leftMargin, topMargin, rightMargin, bottomMargin;
//        Bitmap bitmap = null;
//        for (int i = 0; i < templateInfo.innerPicShapeInfoList.size(); i++){
//            final CardInnerPicShapeInfo info = templateInfo.innerPicShapeInfoList.get(i);
//            widthAfterScale = CommonUtils.floatToInt(info.width / mScale);
//            heightAfterScale = CommonUtils.floatToInt(info.height / mScale);
//            leftMargin = CommonUtils.floatToInt(info.left / mScale + mLeaveLeft);
//            topMargin = CommonUtils.floatToInt(info.top / mScale + mLeaveTop);
//            rightMargin = CommonUtils.floatToInt(rl_bitmap_layout.getWidth() - ((info.left + info.width) / mScale + mLeaveLeft));
//            bottomMargin = CommonUtils.floatToInt(rl_bitmap_layout.getHeight() - ((info.top + info.height) / mScale + mLeaveTop));
//            // 开始加载透明框的图片
//            if (bitmap != null && !bitmap.isRecycled()) {
//                bitmap.isRecycled();
//            }
//            // 在解析的时候保存对应的 picUrl
//            templateInfo.innerPicShapeInfoList.get(i).picOriginalUrl = originalPicsUrl[i];
//            templateInfo.innerPicShapeInfoList.get(i).picComposeUrl = originalPicsCopyUrl[i];
//            // wait TODO -getRightSizeBitmap 这个方法未完整, 待修正
//            bitmap = BitmapUtils.getRightSizeBitmap(originalPicsCopyUrl[i],
//                    CommonUtils.floatToInt(info.width / mScale),
//                    CommonUtils.floatToInt(info.height / mScale)
//            );
//            final StickerForCard view = new StickerForCard(mContext);
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//            lp.leftMargin = leftMargin;
//            lp.topMargin = topMargin;
//            lp.rightMargin = rightMargin;
//            lp.bottomMargin = bottomMargin;
//            lp.width = widthAfterScale;
//            lp.height = heightAfterScale;
//            view.setLayoutParams(lp);
//            view.setBitmap(bitmap);
//            view.setGhostBitmap(bitmap);
//            view.setBackgroundColor(templateInfo.backgroundColor);
//            view.setOperationListener(new StickerForCard.OperationListener() {
//                @Override
//                public void onClick(StickerForCard v) {
//                    // draw rect border, show pop window
//                    if (filterPopInstance.getPopWindow().isShowing()) {
//                        filterPopInstance.setPopWindowDismiss();
//                        v.drawRectFrame(false);
//                    } else {
//                        filterPopInstance.setOperateSticker(v);
//                        v.drawRectFrame(true);
//                    }
//                }
//            });
//            rl_bitmap_layout.addView(view);
//            mStickerForShape.add(view);
//        }
//
//        // 遍历移除( 因为新增的贴纸是不可随着模板变化而被删除的 )
//        if (mStickerDefault != null && mStickerDefault.size() > 0) {
//            for (StickerView sv : mStickerDefault) {
//                if (fl_sticker_and_text_layout.indexOfChild(sv) != -1) {
//                    fl_sticker_and_text_layout.removeView(sv);
//                }
//            }
//            mStickerLists.removeAll(mStickerDefault);
//            mStickerDefault.clear();
//        }
//        for(int i = 0; i < templateInfo.innerStickerInfoList.size(); i ++) {
//            CardInnerStickerInfo info = templateInfo.innerStickerInfoList.get(i);
//            widthAfterScale = CommonUtils.floatToInt(info.defWidth / mScale);
//            heightAfterScale = CommonUtils.floatToInt(info.defHeight / mScale);
//            leftMargin = CommonUtils.floatToInt(info.defLeft / mScale + mLeaveLeft);
//            topMargin = CommonUtils.floatToInt(info.defTop / mScale + mLeaveTop);
//            String iconUrl = Const.CARD_TEMPLATES + mCardType + templateInfo.fileName + "/" + Const.C_IMG + info.defStickerName;
//            if (bitmap != null && !bitmap.isRecycled()) {
//                bitmap.isRecycled();
//            }
//            bitmap = BitmapUtils.getSampledBitmap(iconUrl, widthAfterScale, heightAfterScale);
//            StickerView stickerView = addStickerView(bitmap, mStickerDefault);
//
//            float angle = CommonUtils.floatToInt(info.defAngle);
//            float px = info.defWidth/mScale/2 + info.defLeft/mScale;
//            float py = info.defHeight/mScale/2 + info.defTop/mScale;
//            Matrix matrix = new Matrix();
//            matrix.setRotate(angle, px, py);
//            matrix.postTranslate(leftMargin, topMargin);
//            stickerView.setStickerMatrix(matrix);
//        }
//        mStickerLists.addAll(mStickerDefault);
//
//        // 开始加载默认文字, 遍历移除
//        if (mMTVsDefault != null && mMTVsDefault.size() > 0) {
//            for (MovableTextView2 mtv : mMTVsDefault) {
//                if (fl_sticker_and_text_layout.indexOfChild(mtv) != -1) {
//                    fl_sticker_and_text_layout.removeView(mtv);
//                }
//            }
//            mMtvLists.removeAll(mMTVsDefault);
//            mMTVsDefault.clear();
//        }
//        MovableTextView2 mtv;
//        for (int i = 0; i < templateInfo.innerCarrotInfoList.size(); i ++) {
//            CardInnerCarrotInfo info = templateInfo.innerCarrotInfoList.get(i);
//            leftMargin = CommonUtils.floatToInt(info.defLeft / mScale + mLeaveLeft);
//            topMargin  = CommonUtils.floatToInt(info.defTop / mScale + mLeaveTop);
//            String text     = info.defCarrot;
//            float textSize  = DensityUtils.sp2px(mContext, DensityUtils.px2sp(mContext, info.defTextSize * 1.0f / mScale));
//            int textColor   = ColorUtil.getColorFromString(info.defColor);
//            String fontName = info.defFont;
//            Typeface tf     = CommonUtils.getTypeface(fontName);
//            mtv = generateMtv(text, textSize, textColor, fontName, tf, fl_sticker_and_text_layout);
//            resetLayoutParams(mtv, true, topMargin, leftMargin);
//            mMTVsDefault.add(mtv);
//        }
//        // 把所有的 mtv 加入到 list 中
//        mMtvLists.addAll(mMTVsDefault);
//    }
//
//    // task line---------------------
//    private LoadImageTask mLoadImageTask;
//    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mTaskDialog.setCancelable(false);
//            mTaskDialog.setMessage("图片加载中...");
//            mTaskDialog.show();
//        }
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            int displayWidth = BitmapUtils.getScreenPixels(mContext).widthPixels;
//            int displayHeight = BitmapUtils.getScreenPixels(mContext).heightPixels;
//            if (originalBitmap != null && originalBitmap.isRecycled()) {
//                originalBitmap.recycle();
//                originalBitmap = null;
//            }
//            originalBitmap = BitmapUtils.getSampledBitmap(params[0], displayWidth, displayHeight);
//            return originalBitmap;
//        }
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            iv_template_image.setImageBitmap(result);
//            float[] scaleAndLeaveSpace = CommonUtils.calcScaleAndLeaveSize(result, iv_template_image);
//            mScale      = scaleAndLeaveSpace[0];     // 原图与ImageView的缩放比例
//            mLeaveLeft  = scaleAndLeaveSpace[1];     // 图片自动缩放时造成的留白区域
//            mLeaveTop   = scaleAndLeaveSpace[2];
//            parseConfigParams(mCurrTemplateInfo);
//            mTaskDialog.dismiss();
//        }
//    }// end inner class
//
//    private SaveTask mSaveTask;
//    private int width, height;
//    private final class SaveTask extends AsyncTask<String, Void, Bitmap> {
//        private Bitmap bitmapCopy;
//        private Matrix tempMatrix;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mTaskDialog.setCancelable(false);
//            mTaskDialog.setMessage("图片保存中...");
//            mTaskDialog.show();
//            if (bitmapCopy != null && !bitmapCopy.isRecycled()) {
//                bitmapCopy.isRecycled();
//                bitmapCopy = null;
//            }
//            bitmapCopy = mStickerForShape.get(0).getBitmap().copy(Bitmap.Config.RGB_565, true);
//            tempMatrix = mStickerForShape.get(0).saveMatrix();
//
//            width = originalBitmap.getWidth();
//            height = originalBitmap.getHeight();
//
//        }
//        @Override
//        protected Bitmap doInBackground(String... params) {
//
//            copyBitmap = originalBitmap.copy(Bitmap.Config.ARGB_4444, true);
//            Canvas canvas = new Canvas();
//            Bitmap nullBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//            canvas.setBitmap(nullBitmap);
//            canvas.drawColor(mCurrTemplateInfo.backgroundColor);
//
//            canvas.save();
//            float translateX = mCurrTemplateInfo.innerPicShapeInfoList.get(0).left * 1.0f;
//            float translateY = mCurrTemplateInfo.innerPicShapeInfoList.get(0).top * 1.0f;
//            // 放大位图!!!因为卡片模板在显示的时候被屏幕缩小了 mScale 倍（视觉上的缩小）, 而卡片还是原来的尺寸
//            // 所以 bitmap 要放大 mScale 倍数, 在保存的时候
//            Matrix scaleMatrix = new Matrix();
//            scaleMatrix.setScale(mScale, mScale);
//            Bitmap scaleBitmap = Bitmap.createBitmap(bitmapCopy, 0, 0,
//                    bitmapCopy.getWidth(), bitmapCopy.getHeight(), scaleMatrix, true);
//            // 放大完成, 计算偏移量
//            float[] tempFloats = new float[9];
//            tempMatrix.getValues(tempFloats);
//            canvas.drawBitmap(scaleBitmap, translateX + tempFloats[2] * mScale, translateY + tempFloats[5] * mScale, null);
//            canvas.restore();
//
//            canvas.drawBitmap(copyBitmap, new Matrix(), null);
//
//            canvas.save();
//            canvas.scale(mScale, mScale);
//            canvas.translate(-mLeaveLeft, -mLeaveTop);
//            // bitmap 按比例缩放才能保存
//            // 保存模板的默认贴纸
//            if (mStickerLists != null || mStickerLists.size() > 0) {
//                for(StickerView sv : mStickerLists) {
//                    canvas.drawBitmap(sv.getBitmap(), sv.saveMatrix(), null);
//                    // TODO: 2016/5/18 未解决的贴纸缩放造成的失真问题
////                    Bitmap bitmapCopy2 = sv.getBitmap().copy(Bitmap.Config.RGB_565, true);
////                    float[] svFloats = new float[9];
////                    sv.saveMatrix().getValues(svFloats);
////                    Matrix tempMatrix = new Matrix();
//////                    tempMatrix.setValues(svFloats);
////                    // 设置 matrix 的旋转角度, 用 SinCos 方法
////                    tempMatrix.setSinCos(svFloats[3], svFloats[4]);
////                    tempMatrix.postScale(mScale, mScale);
////                    Bitmap tempBitmap = Bitmap.createBitmap(bitmapCopy2, 0, 0,
////                            bitmapCopy2.getWidth(), bitmapCopy2.getHeight(), tempMatrix, true);
////
//////                    tempMatrix.getValues(svFloats);
////                    float left = svFloats[2] / mScale - mLeaveLeft;
////                    float top = svFloats[5] / mScale - mLeaveTop;
//////                    canvas.drawBitmap(tempBitmap, left, top, null);
//////                    tempMatrix.setSinCos(1 / svFloats[3], 1 / svFloats[4]);
////                    canvas.drawBitmap(tempBitmap, left, 0, null);
//                }
//            }
//            canvas.restore();
//            canvas.save();
//            canvas.translate(-mLeaveLeft, -mLeaveTop);
//            canvas.scale(mScale, mScale);
//            if (mMtvLists != null || mMtvLists.size() > 0) {
//                saveSentences(canvas, mMtvLists);
//            }
//            canvas.restore();
//            long createTime = System.currentTimeMillis();
//            String composeName = "composePicture" + createTime;
//            BitmapUtils.saveBitmap(nullBitmap, composeName);
//
//            // bitmap 回收
//            bitmapCopy.recycle();
//            scaleBitmap.recycle();
//
//            if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_NEW_ADD)) { // 新增
//                mDataInfo.dataCardInfo.cardName = composeName;
//                mDataInfo.dataCardInfo.cardComposePath = Const.SAVE_DIR + composeName + ".jpg";
//                mDataInfo.dataCardInfo.cardBackgroundColor = mCurrTemplateInfo.backgroundColor;
//
//                mDataInfo.dataCardInfo.cardOrientation  = mCurrTemplateInfo.orientation;
//                mDataInfo.dataCardInfo.cardChannel      = mCurrTemplateInfo.channel;
//                mDataInfo.dataCardInfo.cardFileDirName  = mCurrTemplateInfo.fileName;
//                mDataInfo.dataCardInfo.cardTemplateName = mCurrTemplateInfo.templateName;
//                mDataInfo.dataCardInfo.cardSampleName   = mCurrTemplateInfo.sampleName;
//
//                FileInfo info = new FileInfo(Const.SAVE_DIR + composeName + ".jpg");
//                mDataInfo.metaDataNewNoteInfo.filelist.add(info);
//                mDataInfo.metaDataNewNoteInfo.templateName = "TheWhite";
//                mDataInfo.fullContent = "<p><img src='file://" + Const.SAVE_DIR + composeName + ".jpg" + "'></p>";
//
//                NoteManager.addNote(mDataInfo);
//
//                // 跳转预览页 // TODO: 2016/5/24
//                Intent intent = new Intent(mContext, DiaryViewWithViewPagerActivity.class);
//                Storage.info = mDataInfo;
//                ArrayList<DataInfo> dataInfo = new ArrayList<>();
//                dataInfo.add(mDataInfo);
//                Storage.list=dataInfo;
//                intent.putExtra("position",0);
//                mContext.startActivity(intent);
//                finish();
//            } else if (mEditType.equals(Const.C_IMAGE_EDIT_TYPE_RE_EDIT)) {
//                NoteManager.editNote(mDataInfo);
//            }
//
//            System.out.println("mDataInfo保存完成");
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            mTaskDialog.dismiss();
//        }
//    }
//
//    // 保存的方法
//    private void saveAll() {
//        if (mSaveTask != null) {
//            mSaveTask.cancel(true);
//        }
//        mSaveTask = new SaveTask();
//        mSaveTask.execute("");
//    }
//
//    @Override
//    protected void onDestroy() {
//        try{
//            mTaskDialog.dismiss();
//        }catch (Exception e) {
//        }
//        if (filterPopInstance.getPopWindow().isShowing()) {
//            filterPopInstance.getPopWindow().dismiss();
//        }
//        super.onDestroy();
//        this.unregisterReceiver(mReceiver);
//    }
//
//
//    // 切换横版与竖版, 备用方法, 等加入横版竖版切换时用
//    private void templatesAdapterSwitchOrientation(String orientation) {
//        if (orientation.equals("1")) {
//            mSamplePicAdapter = new CardTemplateAdapter(this, cardTemplateInfoListHorizontal);
//            mSamplePicAdapter.setOnItemClickListener(new CardTemplateAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position, CardTemplateInfo templateInfo) {
//                    mCurrPosition = position;
//                    mCurrTemplateInfo = templateInfo;
//                    templatesItemCheck(mCurrTemplateInfo);
//                }
//            });
//            mRvCardSample.setAdapter(mSamplePicAdapter);
//        }
//        else if (orientation.equals("0")) {
//            mSamplePicAdapter = new CardTemplateAdapter(this, cardTemplateInfoListVertical);
//            mSamplePicAdapter.setOnItemClickListener(new CardTemplateAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position, CardTemplateInfo templateInfo) {
//                    mCurrPosition = position;
//                    mCurrTemplateInfo = templateInfo;
//                    templatesItemCheck(mCurrTemplateInfo);
//                }
//            });
//            mRvCardSample.setAdapter(mSamplePicAdapter);
//        }
//    }
//
//
//    private StickerView mCurrentView;           // 当前处于编辑状态的贴纸
//    private void setCurrentEdit(StickerView stickerView) {
//        if (mCurrentView != null) {
//            mCurrentView.setInEdit(false);
//        }
//        mCurrentView = stickerView;
//        stickerView.setInEdit(true);
//    }
//
//
//    /**
//     * @details 通过参数值返回一个 {@link MovableTextView2} 对象
//     * @param text          输入的文字
//     * @param textSize      输入的文字大小
//     * @param color         输入的文字颜色的 int 值
//     * @param typefaceName  输入的文字的字体名称
//     * @param typeface      输入的文字的字体
//     * @param parentView
//     * @return
//     */
//    private MovableTextView2 generateMtv(
//            String text,
//            float textSize,
//            int color,
//            String typefaceName,
//            Typeface typeface,
//            final FrameLayout parentView) {
//        final MovableTextView2 mtv = new MovableTextView2(mContext);
//        if (text != null) {
//            mtv.setText(text);
//        }
//        if (textSize != 0.0f) {
//            mtv.setTextSize(textSize);
//        }
//        if (color != 0){
//            mtv.setTextColor(color);
//        }
//        mtv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                resetLayoutParams(mtv, false, 0, 0);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        mtv.setOnCustomClickListener(new MTVClickListener(mtv));
//        mtv.setTypefaceName(typefaceName);
//        mtv.setTypeface(typeface);
//        parentView.addView(mtv);
//        return mtv;
//    }
//
//
//    private void resetLayoutParams(MovableTextView2 mtv, boolean isEditStateReload, int top, int left) {
//        mtv.measure(
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        );
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mtv.getLayoutParams();
//        lp.gravity = -1;
//        lp.height = mtv.getMeasuredHeight();
//        lp.width = mtv.getMeasuredWidth();
//        if (isEditStateReload) {
//            lp.leftMargin = left;
//            lp.topMargin = top;
//        } else {
//            lp.leftMargin = mtv.getLeft();
//            lp.topMargin = mtv.getTop();
//        }
//        FrameLayout frameLayout = (FrameLayout) mtv.getParent();
//        lp.rightMargin = frameLayout.getWidth() - mtv.getMeasuredWidth();
//        mtv.setLayoutParams(lp);
//    }
//
//
//    private void saveSentences(Canvas canvas, ArrayList<MovableTextView2> lists) {
//        if (lists == null || lists.size() <= 0) {return;}
//        Paint mPaint = new Paint();     // 初始化画笔
//        mPaint.setAntiAlias(true);      // 设置消除锯齿
//        float saveLeft, saveBottom;
//        for (MovableTextView2 mtv : lists) {
//            mPaint.setColor(mtv.getCurrentTextColor());
//            mPaint.setTypeface(mtv.getTypeface());
//            float textViewL = mtv.getLeft() * 1.0f;
//            float textViewT = mtv.getTop() * 1.0f;
//            float textViewB = mtv.getBottom() * 1.0f;
//            String content  = mtv.getText().toString();
//            int index = content.lastIndexOf("\n");
//            String[] strArr = content.split("\\n");
//            ArrayList<String> al = new ArrayList<>();
//            for (String str : strArr) {al.add(str);}
//            if (index != -1 && content.length() == index + 1) {al.add("");}
//            float textSize = mtv.getTextSize();
//            float spacing = (textViewB - textViewT - textSize * (al.size())) / (al.size() + 1);
//            mPaint.setTextSize(textSize);
//            for(int i = 0; i < strArr.length; i ++) {
//                mPaint.setTextSize(textSize);
//                float leftPadding = 1.0f;
//                saveLeft = textViewL + leftPadding;
//                float textCenterVerticalBaselineY = FontMatrixUtils.calcTextCenterVerticalBaselineY(mPaint);
//                saveBottom = textViewT + textCenterVerticalBaselineY + spacing * (i + 1) + textSize * i;
//                canvas.drawText(strArr[i], saveLeft, saveBottom, mPaint
//                );
//            }
//        }
//        lists.clear();
//    }
//
//
//
//    //添加表情
//    private StickerView addStickerView(Bitmap bitmap, final ArrayList<StickerView> lists) {
//        final StickerView stickerView = new StickerView(this);
//        stickerView.setBitmap(bitmap);
//        stickerView.setOperationListener(new StickerView.OperationListener() {
//            @Override
//            public void onDeleteClick() {
//                lists.remove(stickerView);
//                fl_sticker_and_text_layout.removeView(stickerView);
//            }
//
//            @Override
//            public void onEdit(StickerView stickerView) {
//                mCurrentView.setInEdit(false);
//                mCurrentView = stickerView;
//                mCurrentView.setInEdit(true);
//            }
//        });
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        fl_sticker_and_text_layout.addView(stickerView, lp);
//        setCurrentEdit(stickerView);
//        lists.add(stickerView);
//        return stickerView;
//    }
//
//
//    private class MTVClickListener implements MovableTextView2.OnCustomClickListener {
//        MovableTextView2 mMtv;
//        public MTVClickListener(MovableTextView2 mtv2) {
//            this.mMtv = mtv2;
//        }
//        @Override
//        public void onCustomClick() {
//            mMtv.setSelected(true);
//            if (editPanelGenerateState == EditPanelState.STATE_OTHER) {
//                editPanelGenerateState = EditPanelState.STATE_RELOAD_AND_FIRST_CLICK;
//                CommonUtils.hitKeyboardOpenOrNot(mContext);
//                return;
//            }
//            if (mCurKeyboardState == KeyboardState.STATE_HIDE) {
//                editPanelVisible = true;
//                animInstance.translateViewFromBottom(ll_base_edit_panel, animDuration, editPanelVisible, mEditPanelHeight);
//                ll_base_edit_panel.setVisibility(View.VISIBLE);
//                loadMtvDataIntoEditPanel(mMtv);
//                editingChangePosition(mMtv);
//            }
//        }
//    }
//
//    private void initKeyboard() {
//        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {
//                mCurKeyboardState = KeyboardState.STATE_OPEN;
//                if (height != 0) {
//                    keyboardHeight = height;
//                    // 重新载入并且第一次点击
//                    if (editPanelGenerateState == EditPanelState.STATE_RELOAD_AND_FIRST_CLICK) {
//                        generateEditPanel(keyboardHeight);
//                        SystemClock.sleep(100);
//                        initColorSeekBar();
//                        handleEditPanelEvent();
//                        for (MovableTextView2 mtv : mMtvLists) {
//                            if (mtv.isSelected()) {
//                                loadMtvDataIntoEditPanel(mtv);
//                                editingChangePosition(mtv);
//                            }
//                        }
//                        editPanelGenerateState = EditPanelState.STATE_PANEL_CREATED;
//                        openKeyboardOnLoading = false;
//                        isFirstAddMtv = false;
//                        animInstance.putViewOutOfEyes(ll_base_edit_panel, AnimComponent.MODE_BOTTOM, mEditPanelHeight);
//                    }
//                    if (openKeyboardOnLoading && editPanelGenerateState == EditPanelState.STATE_NEW_ADD_MTV) {
//                        generateEditPanel(keyboardHeight);
//                        SystemClock.sleep(100);
//                        initColorSeekBar();
//                        handleEditPanelEvent();
//                        addMovableTextView();
//                        editPanelGenerateState = EditPanelState.STATE_PANEL_CREATED;
//                        openKeyboardOnLoading = false;
//                        isFirstAddMtv = false;
//                        animInstance.putViewOutOfEyes(ll_base_edit_panel, AnimComponent.MODE_BOTTOM, mEditPanelHeight);
//                    }
//                    editPanelVisible = true;
//                    animInstance.translateViewFromBottom(ll_base_edit_panel, animDuration, editPanelVisible, mEditPanelHeight);
//                    ll_base_edit_panel.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void keyBoardHide(int height) {
//                mCurKeyboardState = KeyboardState.STATE_HIDE;
//            }
//        });
//    }
//
//    private void generateEditPanel(int keyboardHeight) {
//        edit_panel = (LinearLayout) LayoutInflater.from(mContext).inflate(com.anybeen.mark.yinjiimageeditorlibrary.R.layout.layout_image_editor_edit_panel, null);
//        ep_KeyboardOptions = (ImageView) edit_panel.findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.iv_edit_panel_key_board_options);
//        ep_OperateText = (CarrotEditText) edit_panel.findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.et_edit_panel_text);
//        ep_BtnComplete = (TextView) edit_panel.findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.iv_edit_panel_complete);
//        ep_FontSize = (SeekBar) edit_panel.findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.sb_edit_panel_font_size);
//        ep_FontSize.setPadding(0, 0, 0, 0);
//        ep_FontSize.setThumbOffset(0);
//        ep_IvColorShow = (ImageView) edit_panel.findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.iv_edit_panel_color_show);
//        ep_CsbFontColor = (CustomSeekBar) edit_panel.findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.csb_edit_panel_font_color);
//        ep_CsbFontColor.setPadding(0, 0, 0, 0);
//        ep_CsbFontColor.setThumbOffset(DensityUtils.dp2px(mContext, DensityUtils.px2dp(mContext, 5)));
//        ep_rgFontGroup = (RadioGroup) edit_panel.findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.rg_font_group);
//        ll_base_edit_panel.addView(edit_panel);
//        // 获取编辑 panel 的头高度
//        LinearLayout ll_edit_panel_head = (LinearLayout) edit_panel.findViewById(com.anybeen.mark.yinjiimageeditorlibrary.R.id.ll_edit_panel_head);
//        ll_edit_panel_head.measure(0, 0);
//        int headHeight = ll_edit_panel_head.getMeasuredHeight();
//
//        LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) edit_panel.getLayoutParams();
//        lps.height = headHeight + keyboardHeight;
//        lps.gravity = Gravity.BOTTOM;
//        edit_panel.setLayoutParams(lps);
//        ll_base_edit_panel.setVisibility(View.VISIBLE);
//        mEditPanelHeight = headHeight + keyboardHeight;
//    }
//
//    private void hideEditPanelAndCloseKeyboard() {
//        if (mCurKeyboardState == KeyboardState.STATE_OPEN) {
//            mCurKeyboardState = KeyboardState.STATE_HIDE;
//            CommonUtils.hitKeyboardOpenOrNot(mContext);
//        }
//        if (editPanelVisible == true) {
//            editPanelVisible = false;
//            animInstance.translateViewFromBottom(ll_base_edit_panel, animDuration, editPanelVisible, mEditPanelHeight);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    SystemClock.sleep(animDuration);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ll_base_edit_panel.setVisibility(View.INVISIBLE);
//                        }
//                    });
//                }
//            }).start();
//        }
//    }
//
//
//    /**
//     * 加载 mtv 数据到 editPanel 中
//     * @param currMtv
//     */
//    private void loadMtvDataIntoEditPanel(MovableTextView2 currMtv) {
//        if (ll_base_edit_panel.getVisibility() == View.INVISIBLE) {
//            return;
//        }
//        for (MovableTextView2 m : mMtvLists) {
//            if (m.equals(currMtv)) {
//                ep_OperateText.setText(m.getText());
//                ep_OperateText.setFocusable(true);
//                ep_OperateText.setFocusableInTouchMode(true);
//                ep_OperateText.requestFocus();
//                ep_OperateText.setSelection(m.getText().length());   // 设置光标的位置
//                ep_IvColorShow.setBackgroundColor(m.getCurrentTextColor());
//                ep_CsbFontColor.setProgress(CommonUtils.matchProgress(m.getCurrentTextColor(), mContext));
//                ep_FontSize.setProgress(DensityUtils.px2dp(mContext, m.getTextSize()));
//                // ep_rgFontGroup
//                String fontName = m.getTypefaceName();
//                for (int i = 0; i < ep_rgFontGroup.getChildCount(); i++) {
//                    if (ep_rgFontGroup.getChildAt(i).getTag().equals(fontName)) {
//                        ((SelectableView) (ep_rgFontGroup.getChildAt(i))).setChecked(true);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 编辑中如果需要改变位置，则改变
//     * @param mtv
//     */
//    private void editingChangePosition(MovableTextView2 mtv) {
//        // 记录 l,t,r,b 在 resetLayoutParams 的时候使用
//        mtv.leftBeforeChange = mtv.getLeft();
//        mtv.topBeforeChange = mtv.getTop();
//        mtv.bottomBeforeChange = mtv.getBottom();
//
//        // 如果高度被弹出的edit_panel挡住了，那么改变当前对象的高度
//        Rect frame = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
//        int toolBarHeight = layout_top_toolbar.getHeight();
//        int displayHeight = BitmapUtils.getScreenPixels(mContext).heightPixels;
//        int distanceViewToDisplayBottom =
//                displayHeight - statusBarHeight - toolBarHeight - mtv.bottomBeforeChange;
//        if (distanceViewToDisplayBottom < mEditPanelHeight) {
//            int layoutTop =  displayHeight - mEditPanelHeight - statusBarHeight - toolBarHeight
//                    - mtv.getHeight() - 20;
//            mtv.measure(
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//            );
//            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mtv.getLayoutParams();
//            lp.gravity = -1;
//            lp.height = mtv.getMeasuredHeight();
//            lp.width = mtv.getMeasuredWidth();
//            lp.leftMargin = mtv.leftBeforeChange;
//            lp.topMargin = layoutTop;
//            mtv.setLayoutParams(lp);
//            mtv.setIsChangePosition(true);
//        }
//    }
//
//
//
//    /**
//     * @details 处理文字编辑面板的事件
//     */
//    private void handleEditPanelEvent() {
//        ep_CsbFontColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int curColor = getResources().getColor(colorValues[CommonUtils.matchedColor(progress)]);
//                ep_IvColorShow.setBackgroundColor(curColor);
//                for (MovableTextView2 m : mMtvLists) {
//                    if (m.isSelected()) {
//                        m.setTextColor(curColor);
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//            }
//        });
//
//        ep_BtnComplete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                operateComplete();
//            }
//        });
//
//        ep_FontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (progress < 10) return;
//                for (MovableTextView2 mtv : mMtvLists) {
//                    if (mtv.isSelected()) {
//                        // progress is dp
//                        int px = DensityUtils.dp2px(mContext, progress);
//                        float sp = DensityUtils.px2sp(mContext, px);
//                        mtv.setTextSize(sp);
//                        resetLayoutParams(mtv, false, 0, 0);
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//        edit_panel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//
//        ep_OperateText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                for (MovableTextView2 mtv : mMtvLists) {
//                    if (mtv.isSelected()) {
//                        mtv.setText(s);
//                        if (s.length() == 0) {
//                            mtv.setText(" ");
//                        }
//                        if (s.toString().contains(CarrotEditText.CLEAR_TEXT)) {
//                            ep_OperateText.setText(ep_OperateText.getText().toString().replace(CarrotEditText.CLEAR_TEXT, ""));
//                            ep_OperateText.setSelection(ep_OperateText.length());
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        ep_rgFontGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                SelectableView selectableView = (SelectableView) group.findViewById(checkedId);
//                if (selectableView == null || selectableView.isDownloading() || !selectableView.isChecked()) {
//                    return;
//                }
//                Typeface typeface = selectableView.getFontType();
//                if (typeface != null) {
//                    changeTypeface(typeface, selectableView.getTag().toString());
//                    currentFontCheckedId = checkedId;
//                } else {
//                    showDownloadFontDialog("3M", selectableView, ep_rgFontGroup);
//                }
//            }
//        });
//    }
//
//    /**
//     * 初始化颜色 seekBar 控件的值
//     */
//    private int[] colorValues = Const.COLOR_VALUES;
//    private void initColorSeekBar() {
//        ep_CsbFontColor.initData(CommonUtils.getProgressItemList(colorValues));
//        ep_CsbFontColor.invalidate();
//    }
//
//    private void changeTypeface(Typeface typeface, String fontName) {
//        if (mMtvLists.size() <= 0) return;
//        for (MovableTextView2 mtv:mMtvLists) {
//            if (mtv.isSelected()) {
//                mtv.setTypefaceName(fontName);
//                mtv.setTypeface(typeface);
//                resetLayoutParams(mtv, false, 0, 0);
//            }
//        }
//    }
//
//    private void addMovableTextView() {
//        final MovableTextView2 newAddMtv = new MovableTextView2(mContext);
//        newAddMtv.setSelected(true);
//        newAddMtv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                resetLayoutParams(newAddMtv, false, 0, 0);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        newAddMtv.setOnCustomClickListener(new MTVClickListener(newAddMtv));
//        ep_OperateText.setText(newAddMtv.getText());
//        ep_OperateText.setFocusable(true);
//        ep_OperateText.setFocusableInTouchMode(true);
//        ep_OperateText.requestFocus();
//        ep_OperateText.setSelection(newAddMtv.getText().length());   // 设置光标的位置
//        ep_IvColorShow.setBackgroundColor(newAddMtv.getCurrentTextColor());
//        ep_CsbFontColor.setProgress(newAddMtv.getColorSeekBarProgress());
//        ep_FontSize.setProgress(DensityUtils.px2dp(mContext, newAddMtv.getTextSize()));
//        String fontName = newAddMtv.getTypefaceName();
//        for (int i = 0; i < ep_rgFontGroup.getChildCount(); i++) {
//            if (ep_rgFontGroup.getChildAt(i).getTag().equals(fontName)) {
//                ((SelectableView) (ep_rgFontGroup.getChildAt(i))).setChecked(true);
//            }
//        }
//        fl_sticker_and_text_layout.addView(newAddMtv);
//        mMtvLists.add(newAddMtv);
//        newAddMtv.measure(
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//        );
//        FrameLayout.LayoutParams llp = (FrameLayout.LayoutParams) newAddMtv.getLayoutParams();
//        int displayHeight = BitmapUtils.getScreenPixels(mContext).heightPixels;
//        Rect frame = new Rect();
//        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
//        int toolBarHeight = layout_top_toolbar.getHeight();
//        llp.topMargin = displayHeight - mEditPanelHeight -
//                statusBarHeight - toolBarHeight - 20 - newAddMtv.getMeasuredHeight();
//        llp.gravity = Gravity.CENTER_HORIZONTAL;
//        newAddMtv.setLayoutParams(llp);
//    }
//
//
//    private void operateComplete() {
//        MovableTextView2 delMtv = null;
//        int listSize = mMtvLists.size();
//        for(int i = 0; i < listSize; i++) {
//            if (mMtvLists.get(i).getText().length() == 0) {
//                delMtv = mMtvLists.get(i);
//            }
//            if (" ".equals(mMtvLists.get(i).getText().toString())) {
//                delMtv = mMtvLists.get(i);
//            }
//            if (CarrotEditText.CLEAR_TEXT.equals(mMtvLists.get(i).getText().toString())) {
//                delMtv = mMtvLists.get(i);
//            }
//        }
//        if (delMtv != null) {
//            mMtvLists.remove(delMtv);
//            if (mMTVsDefault != null && mMTVsDefault.size() > 0) {
//                if (mMTVsDefault.indexOf(delMtv) != -1) {
//                    mMTVsDefault.remove(delMtv);
//                }
//            }
//            fl_sticker_and_text_layout.removeView(delMtv);
//        }
//        for (MovableTextView2 m : mMtvLists) {
//            m.setSelected(false);
//        }
//        // 还原位置
//        movableTextViewPositionRevert();
//        hideEditPanelAndCloseKeyboard();
//    }
//
//    // 还原 position
//    private void movableTextViewPositionRevert() {
//        for (MovableTextView2 m : mMtvLists) {
//            if (m.getIsChangePosition()) {
//                m.measure(
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//                );
//                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) m.getLayoutParams();
//                lp.gravity = -1;
//                lp.height = m.getMeasuredHeight();
//                lp.width = m.getMeasuredWidth();
//                lp.leftMargin = m.leftBeforeChange;
//                lp.topMargin = m.topBeforeChange;
//                m.setLayoutParams(lp);
//                m.setIsChangePosition(false);
//            }
//        }
//    }
//
//
//    private void showDownloadFontDialog(String fontSize, final SelectableView selectableView, final RadioGroup rg_font_group) {
//        new MaterialDialog.Builder(ImageCardActivity.this)
//                .title(com.anybeen.mark.yinjiimageeditorlibrary.R.string.no_font_file_title)
//                .content(ImageCardActivity.this.getResources().getString(com.anybeen.mark.yinjiimageeditorlibrary.R.string.no_font_file_content))
//                .positiveText("下载")
//                .negativeText("算了")
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        super.onPositive(dialog);
//                        if (!CommonUtils.isNetAvailable(ImageCardActivity.this)) {
//                            Toast.makeText(ImageCardActivity.this, getResources().getString(com.anybeen.mark.yinjiimageeditorlibrary.R.string.net_unavailable), Toast.LENGTH_SHORT);
//                            return;
//                        }
//                        selectableView.downloadFont(new SelectableView.OnDownloadCompleteListener() {
//                            @Override
//                            public void onDownloadComplete() {
//                                Typeface typeface = selectableView.getFontType();
//                                if (typeface != null) {
//                                    selectableView.invalidate();
//                                    selectableView.initView();
//                                    selectableView.setChecked(true);
//                                    changeTypeface(typeface, selectableView.getTag().toString());
//                                    currentFontCheckedId = rg_font_group.getCheckedRadioButtonId();
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onNegative(MaterialDialog dialog) {
//                        super.onNegative(dialog);
//                        rg_font_group.check(currentFontCheckedId);
//                    }
//                })
//                .cancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        rg_font_group.check(currentFontCheckedId);
//                    }
//                })
//                .show();
//    }
//
//    private MyBroadcastReceiver mReceiver;
//    private class MyBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(StickerManagePageActivity.BROAD_CAST)) {
//                String stickerName = intent.getStringExtra("stickerName");
//                Bitmap receiveBitmap = BitmapFactory.decodeFile(Const.STICKERS_DIR + stickerName);
//                addStickerView(receiveBitmap, mStickerLists);
//            }
//        }
//    }
//    private void initReceiver() {
//        mReceiver = new MyBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(StickerManagePageActivity.BROAD_CAST);
//        this.registerReceiver(mReceiver, intentFilter);
//    }
//}