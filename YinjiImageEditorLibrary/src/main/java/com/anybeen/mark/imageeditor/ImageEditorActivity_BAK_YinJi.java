//package com.anybeen.mark.imageeditor;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
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
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.anybeen.mark.imageeditor.adapter.FilterAdapter;
//import com.anybeen.mark.imageeditor.adapter.StickerAdapter;
//import com.anybeen.mark.imageeditor.entity.FilterInfo;
//import com.anybeen.mark.imageeditor.utils.BitmapUtils;
//import com.anybeen.mark.imageeditor.utils.CommonUtils;
//import com.anybeen.mark.imageeditor.utils.Const;
//import com.anybeen.mark.imageeditor.utils.DensityUtils;
//import com.anybeen.mark.imageeditor.utils.FontMatrixUtils;
//import com.anybeen.mark.imageeditor.view.CarrotEditText;
//import com.anybeen.mark.imageeditor.view.CustomSeekBar;
//import com.anybeen.mark.imageeditor.view.MovableTextView2;
//import com.anybeen.mark.imageeditor.view.SelectableView;
//import com.anybeen.mark.imageeditor.view.StickerView;
//import com.anybeen.mark.model.entity.DataInfo;
//import com.anybeen.mark.model.manager.PictureManager;
//import com.anybeen.mark.yinjiimageeditorlibrary.R;
//import com.nineoldandroids.view.ViewPropertyAnimator;
//import com.xinlan.imageeditlibrary.editimage.fliter.PhotoProcessing;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Deprecated // 已经过时的图片编辑 act, 使用 ImageCardActivity 作为新的图片编辑模块
//public class ImageEditorActivity_BAK_YinJi extends Activity {
//    public static final String FILE_PATH = "albumPhotoAbsolutePath";
//    public static final String IS_NEW = "isNewAddPhoto";
//    private boolean isNew = true;
//    String filePath = "";
//    private DataInfo mCurrDataInfo;
//
//    private boolean stickerListShowUp = false;
//    private boolean filterListShowUp = false;
//
//    private Context mContext;
//    // topToolbar
//    private RelativeLayout layout_top_toolbar;
//    private ImageView bt_save;
//    private ImageView iv_back;
//    // mainContent
//    private ImageView iv_main_image;
//    private RecyclerView mRvFilter;
//    private RecyclerView mRvSticker;
//    // bottomToolbar
//    private ImageButton rb_word, rb_sticker, rb_filter;
//
//    private Bitmap originalBitmap;              // 图片初始化获取的原图位图对象
//    private Bitmap copyBitmap;                  // 用来操作的 bitmap 对象
//
//    private FrameLayout fl_main_content;
//
//    public int keyboardHeight = 0;              // 记录键盘高度
//
//    @Deprecated //未使用到
//    private StickerView mCurrentView;           // 当前处于编辑状态的贴纸
//
//    private ArrayList<MovableTextView2> mMtvLists;  // 存储文字列表
//    private List<DataInfo.PictureSubElements> pictureSubElementsList;
//
//    private ArrayList<StickerView> mStickerViews;   // 存储贴纸列表，在 draw 的时候使用
//    private List<DataInfo.StickerInfo> pictureStickerInfoList;  // 保存的时候使用
//    private DataInfo.FilterInfo mFilterInfo;                    // 存储滤镜特效
//    private float[] scaleAndLeaveSize;              // 计算三个缩放比例
//
//    // edit panel params
//    private LinearLayout ll_base_edit_panel;
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
//
//    private boolean openKeyboardOnLoading;
//    private boolean isFirstAddMtv;
//    private KeyboardState mCurKeyboardState;
//    private enum KeyboardState {
//        STATE_OPEN, STATE_HIDE
//    }
//
//    private ProgressDialog mTaskDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        setContentView(R.layout.activity_image_editor);
//        mContext = this;
//        mCurrDataInfo = new DataInfo();
//        mMtvLists = new ArrayList<>();
//        pictureSubElementsList = new ArrayList<>();
//        mStickerViews = new ArrayList<>();
//        pictureStickerInfoList = new ArrayList<>();
//        mFilterInfo = new DataInfo.FilterInfo();
//        openKeyboardOnLoading = true;
//        isFirstAddMtv = true;
//
//        initView();
//        // 加载图片
//        loadBitmap();
//
//        handleListener();
//
//    }
//
//
//    private void initView() {
//        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
//        // edit_panel 的父容器
//        ll_base_edit_panel = (LinearLayout) findViewById(R.id.ll_base_edit_panel);
//        ll_base_edit_panel.setVisibility(View.INVISIBLE);
//
//        // topBar
//        layout_top_toolbar = (RelativeLayout) findViewById(R.id.layout_top_toolbar);
//        bt_save = (ImageView) findViewById(R.id.bt_save);
//        iv_back = (ImageView) findViewById(R.id.iv_back);
//        // mainContent
//
//        iv_main_image = (ImageView) findViewById(R.id.iv_main_image);
//        // bottomToolbar
//        rb_word = (ImageButton) findViewById(R.id.rb_word);
//        rb_sticker = (ImageButton) findViewById(R.id.rb_sticker);
//        rb_filter = (ImageButton) findViewById(R.id.rb_filter);
//
//        // recycle view 处理滤镜
//        mRvFilter = (RecyclerView) findViewById(R.id.rv_filter_list);
//        mRvFilter.setHasFixedSize(true);
//        LinearLayoutManager lmFilter = new LinearLayoutManager(mContext);
//        lmFilter.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRvFilter.setLayoutManager(lmFilter);
//        ArrayList<FilterInfo> list = null;
//        FilterAdapter fa = new FilterAdapter(this, list, null);
//        fa.setHandleFilterListener(new FilterAdapter.HandleFilterListener() {
//            @Override
//            public void handleFilter(int position) {
////                filterListShowUp = false;
////                viewTranslationX(mRvFilter, filterListShowUp);
//                // handle filter method();
//                FilterTask filterTask = new FilterTask();
//                filterTask.execute(position + "");
//            }
//        });
//        mRvFilter.setAdapter(fa);
//
//        // recycle view 处理贴纸
//        mRvSticker = (RecyclerView) findViewById(R.id.rv_sticker_list);
//        mRvSticker.setHasFixedSize(true);
//        LinearLayoutManager lmSticker = new LinearLayoutManager(mContext);
//        lmSticker.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRvSticker.setLayoutManager(lmSticker);
//        StickerAdapter sa = new StickerAdapter(this);
//        sa.setSil(new StickerAdapter.StickerIndexListener() {
//            @Override
//            public void onStickerIndex(int position) {
//                stickerListShowUp = false;
//                setViewRes(stickerListShowUp, rb_sticker);
//                viewTranslationX(mRvSticker, stickerListShowUp);
//                addStickerView(position);
//            }
//        });
//        mRvSticker.setAdapter(sa);
//
//        // 初始化时隐藏
//        mRvSticker.setTranslationX(BitmapUtils.getScreenPixels(mContext).widthPixels);
//        mRvFilter.setTranslationX(BitmapUtils.getScreenPixels(mContext).widthPixels);
//    }
//    private void loadBitmap() {
//        isNew = getIntent().getBooleanExtra(IS_NEW, true);
//        if (isNew) {//根据用户操作状态（新添加大图、编辑大图），分成两个Controller处理
//            filePath = getIntent().getStringExtra(FILE_PATH);
//        } else {
//            mCurrDataInfo = (DataInfo) getIntent().getSerializableExtra("DataInfo");
//            parseStickerData(mCurrDataInfo);
//            parseListData(mCurrDataInfo);
//            filePath = mCurrDataInfo.metaDataPictureInfo.oriPicturePath;
//        }
//        loadImage(filePath);
//    }
//
//    private void handleListener() {
//        // 监听获取键盘高度, 只能监听到打开与关闭，自定义枚举类，保存相对于的键盘状态
//        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
//            @Override
//            public void keyBoardShow(int height) {
//                mCurKeyboardState = KeyboardState.STATE_OPEN;
//                if (height != 0) {
//                    keyboardHeight = height;
//                    if (editPanelGenerateState == EditPanelState.STATE_RELOAD_AND_FIRST_CLICK) {      // 数据重载&&第一次生成键盘
//                        generateEditPanel(keyboardHeight);
//                        SystemClock.sleep(100);
//                        initColorSeekBar();
//                        handleEditPanelEvent();
//
//                        for (MovableTextView2 mtv : mMtvLists) {
//                            if (mtv.isSelected()) {
//                                loadMtvDataIntoEditPanel(mtv);
//                                editingChangePosition(mtv);
//                            }
//                        }
//                        editPanelGenerateState = EditPanelState.STATE_PANEL_CREATED;
//                        openKeyboardOnLoading = false;
//                        isFirstAddMtv = false;
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
//                    } else {
//                        ll_base_edit_panel.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void keyBoardHide(int height) {
//                mCurKeyboardState = KeyboardState.STATE_HIDE;
//            }
//        });
//
//
//        bt_save.setOnClickListener(new SaveClickListener());
//        iv_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        rb_word.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (stickerListShowUp) {
//                    stickerListShowUp = !stickerListShowUp;
//                    setViewRes(stickerListShowUp, rb_sticker);
//                    viewTranslationX(mRvSticker, stickerListShowUp);
//                    return;
//                }
//                if (filterListShowUp) {
//                    filterListShowUp = !filterListShowUp;
//                    setViewRes(filterListShowUp, rb_filter);
//                    viewTranslationX(mRvFilter, filterListShowUp);
//                    return;
//                }
//
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
//
//        rb_sticker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (filterListShowUp) {
//                    filterListShowUp = !filterListShowUp;
//                    setViewRes(filterListShowUp, rb_filter);
//                    viewTranslationX(mRvFilter, filterListShowUp);
//                    return;
//                }
//                stickerListShowUp = !stickerListShowUp;
//                setViewRes(stickerListShowUp, rb_sticker);
//                viewTranslationX(mRvSticker, stickerListShowUp);
//            }
//        });
//
//        rb_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (stickerListShowUp) {
//                    stickerListShowUp = !stickerListShowUp;
//                    setViewRes(stickerListShowUp, rb_sticker);
//                    viewTranslationX(mRvSticker, stickerListShowUp);
//                    return;
//                }
//                filterListShowUp = !filterListShowUp;
//                setViewRes(filterListShowUp, rb_filter);
//                viewTranslationX(mRvFilter, filterListShowUp);
//            }
//        });
//
//        ll_base_edit_panel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                operateComplete();
//            }
//        });
//
//        iv_main_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (stickerListShowUp) {
//                    stickerListShowUp = !stickerListShowUp;
//                    setViewRes(stickerListShowUp, rb_sticker);
//                    viewTranslationX(mRvSticker, stickerListShowUp);
//                }
//                if (filterListShowUp) {
//                    filterListShowUp = !filterListShowUp;
//                    setViewRes(filterListShowUp, rb_filter);
//                    viewTranslationX(mRvFilter, filterListShowUp);
//                }
//            }
//        });
//    }
//
//    /**
//     * 通过软键盘弹出初始化编辑界面
//     * @param keyboardHeight
//     */
//    private void generateEditPanel(int keyboardHeight) {
//        edit_panel = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_image_editor_edit_panel, null);
//
//        ep_KeyboardOptions = (ImageView) edit_panel.findViewById(R.id.iv_edit_panel_key_board_options);
//        ep_OperateText = (CarrotEditText) edit_panel.findViewById(R.id.et_edit_panel_text);
//        ep_BtnComplete = (TextView) edit_panel.findViewById(R.id.iv_edit_panel_complete);
//        ep_FontSize = (SeekBar) edit_panel.findViewById(R.id.sb_edit_panel_font_size);
//        ep_FontSize.setPadding(0, 0, 0, 0);
//        ep_FontSize.setThumbOffset(0);
//        ep_IvColorShow = (ImageView) edit_panel.findViewById(R.id.iv_edit_panel_color_show);
//
//        ep_CsbFontColor = (CustomSeekBar) edit_panel.findViewById(R.id.csb_edit_panel_font_color);
//        ep_CsbFontColor.setPadding(0, 0, 0, 0);
//        ep_CsbFontColor.setThumbOffset(DensityUtils.dp2px(mContext, DensityUtils.px2dp(mContext, 5)));
//
//        ep_rgFontGroup = (RadioGroup) edit_panel.findViewById(R.id.rg_font_group);
//
//        ll_base_edit_panel.addView(edit_panel);
//
//        // 获取编辑 panel 的头高度
//        LinearLayout ll_edit_panel_head = (LinearLayout) edit_panel.findViewById(R.id.ll_edit_panel_head);
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
//    private class SaveClickListener implements View.OnClickListener{
//        @Override
//        public void onClick(View v) {
//            // 在图片全部逻辑加载完成后，计算图片加载后与屏幕的缩放比与留白区域
//            calcScaleAndLeaveSize();
//            Canvas canvas = new Canvas(copyBitmap);
//            saveViews(canvas);
//        }
//    }
//    private EditPanelState editPanelGenerateState = EditPanelState.STATE_OTHER;
//    public enum EditPanelState {
//        STATE_RELOAD_AND_FIRST_CLICK, STATE_NEW_ADD_MTV, STATE_PANEL_CREATED, STATE_OTHER
//    }
//    private class MTVClickListener implements MovableTextView2.OnCustomClickListener {
//        MovableTextView2 mMtv;
//        public MTVClickListener(MovableTextView2 mtv2) {
//            this.mMtv = mtv2;
//        }
//        @Override
//        public void onCustomClick() {
//            if (stickerListShowUp) {
//                stickerListShowUp = !stickerListShowUp;
//                viewTranslationX(mRvSticker, stickerListShowUp);
//            }
//            if (filterListShowUp) {
//                filterListShowUp = !filterListShowUp;
//                viewTranslationX(mRvFilter, filterListShowUp);
//            }
//            mMtv.setSelected(true);
//            /**
//             * 解决一种情况，就是重新载入的时候，控件已经生产，点击事件有，但是此时键盘并没有弹出，edit panel
//             * 也还没有生产
//             */
//            if (editPanelGenerateState == EditPanelState.STATE_OTHER) {
//                editPanelGenerateState = EditPanelState.STATE_RELOAD_AND_FIRST_CLICK;
//                // 设置弹出软键盘，好让监听器得到当前弹出的消息
//                CommonUtils.hitKeyboardOpenOrNot(mContext);
//                return;
//            }
//            if (mCurKeyboardState == KeyboardState.STATE_HIDE) {
//                ll_base_edit_panel.setVisibility(View.VISIBLE);
//                loadMtvDataIntoEditPanel(mMtv);
//                editingChangePosition(mMtv);
//            }
//        }
//    }
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
//    // ====================task line
//    private LoadImageTask mLoadImageTask;
//    public void loadImage(String filepath) {
//        if (mLoadImageTask != null) {
//            mLoadImageTask.cancel(true);
//        }
//        mLoadImageTask = new LoadImageTask();
//        mLoadImageTask.execute(filepath);
//    }
//
//    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mTaskDialog = new ProgressDialog(ImageEditorActivity.this);
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
//            // 添加 try catch，有一条测试数据，没有原图~导致崩溃
//            try {
//                originalBitmap = BitmapUtils.getSampledBitmap(params[0], displayWidth, displayHeight);
//                copyBitmap = originalBitmap.copy(Bitmap.Config.RGB_565, true);
//                return copyBitmap;
//            } catch (NullPointerException npe) {
//                System.out.println("npe on loading image, please check data integrity");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mContext, "数据异常，请返回并同步数据！", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                copyBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565);
//                return copyBitmap;
//            }
//        }
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            iv_main_image.setImageBitmap(result);
//            mTaskDialog.dismiss();
//            mFilterInfo = mCurrDataInfo.metaDataPictureInfo.filterInfo;
//            if (mFilterInfo.filterIndex != 0) {
//                SystemClock.sleep(100);
//                // 生成滤镜特效
//                FilterTask filterTask = new FilterTask();
//                filterTask.execute(mFilterInfo.filterIndex + "");
//            }
//        }
//    }// end inner class
//
//    private SaveDiaryTask mSaveTask;
//    private final class SaveDiaryTask extends AsyncTask<Void, Void, Void> {
//        // 保存到本地目录中和数据库中
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // 关闭虚拟键盘
//            mTaskDialog = new ProgressDialog(ImageEditorActivity.this);
//            mTaskDialog.setCancelable(false);
//            mTaskDialog.setMessage("正在保存图片...");
//            mTaskDialog.show();
//        }
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            // 还是bitmap回收问题的老梗~
//            iv_main_image.setImageBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565));
//            // 在执行 add or edit 之前回收 bitmap 对象
//            bitmapCollections();
//            mTaskDialog.dismiss();
//            // 发送广播，通知首页刷新
//            Intent intent = new Intent(Const.INTENT_ACTION_REFRESH_DATA);
//            sendBroadcast(intent);
//            finish();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            if (isNew) {
//                // 新增时的保存方法
//                // 把原图相册中的图片复制到新的路径下，因为保存完后会被删掉
//                long time = System.currentTimeMillis();
//                String fileName = com.anybeen.mark.common.utils.Const.PHOTO_EDIT_COMPOSE_BITMAP + time;
//                String oriPicturePath = filePath; // BitmapUtils.copyImg(filePath);
//                String composePicPath = BitmapUtils.saveBitmap(copyBitmap, fileName);
//                mCurrDataInfo.createTime = time;  // 保存日记创建时间
//                mCurrDataInfo.dataCategory = "1003";
//                // 原始图片的路径
//                mCurrDataInfo.metaDataPictureInfo.oriPicturePath = oriPicturePath;
//                // comPicturePath: 合成的图片的保存地址
//                mCurrDataInfo.metaDataPictureInfo.comPicturePath = composePicPath;
//                // 保存listData
//                mCurrDataInfo.metaDataPictureInfo.listData = pictureSubElementsList;
//                mCurrDataInfo.metaDataPictureInfo.listStickers = pictureStickerInfoList;
//                mCurrDataInfo.metaDataPictureInfo.filterInfo = mFilterInfo;
//            } else {
//                String fileName = mCurrDataInfo.metaDataPictureInfo.comPicturePath;
//                fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf(".jpg"));
//                String composePicPath = BitmapUtils.saveBitmap(copyBitmap, fileName);
//                mCurrDataInfo.metaDataPictureInfo.comPicturePath = composePicPath;
//                mCurrDataInfo.metaDataPictureInfo.listData = pictureSubElementsList;
//                mCurrDataInfo.metaDataPictureInfo.listStickers = pictureStickerInfoList;
//                mCurrDataInfo.metaDataPictureInfo.filterInfo = mFilterInfo;
//            }
//            if (isNew) {
//                PictureManager.addPicture(mCurrDataInfo);
//            } else {
//                // 编辑的保存方法
//                PictureManager.editPicture(mCurrDataInfo);
//            }
//
//            return null;
//        }
//    }
//
//    // inner class start 滤镜的处理方法，暂时不用
//    private class FilterTask extends AsyncTask<String, Void, Bitmap> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mTaskDialog = new ProgressDialog(ImageEditorActivity.this);
//            mTaskDialog.setCancelable(false);
//            mTaskDialog.setMessage("图片处理中...");
//            mTaskDialog.show();
//        }
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            int position = Integer.parseInt(params[0]);
//            if (copyBitmap != null && !copyBitmap.isRecycled()) {
//                copyBitmap.isRecycled();
//                copyBitmap = null;
//            }
//            copyBitmap = PhotoProcessing.filterPhoto(
//                    originalBitmap.copy(Bitmap.Config.RGB_565, true),
//                    position
//            );
//            mFilterInfo.filterCount += 1;
//            mFilterInfo.filterIndex = position;
//            mFilterInfo.filterNameCh = PhotoProcessing.FILTERS[position];
//            mFilterInfo.filterNameEn = PhotoProcessing.FILTERS_NAME_EN[position];
//            return copyBitmap;
//        }
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            mTaskDialog.dismiss();
//            iv_main_image.setImageBitmap(result);
//        }
//    }
//    // filter class end............................
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
//        fl_main_content.addView(newAddMtv);
//        mMtvLists.add(newAddMtv);
//        // loadMtvDataIntoEditPanel(newAddMtv);
//        // 重置 position
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
//    //添加表情
//    private void addStickerView(int stickerIndex) {
//        if (mStickerViews.size() > 5) {return;}
//        final StickerView stickerView = new StickerView(this);
//        stickerView.setImageResource(Const.STICKERS_VALUES[stickerIndex]);
//        stickerView.setOperationListener(new StickerView.OperationListener() {
//            @Override
//            public void onDeleteClick() {
//                mStickerViews.remove(stickerView);
//                fl_main_content.removeView(stickerView);
//            }
//
//            @Override
//            public void onEdit(StickerView stickerView) {
//                mCurrentView.setInEdit(false);
//                mCurrentView = stickerView;
//                mCurrentView.setInEdit(true);
//                if (stickerListShowUp) {
//                    stickerListShowUp = !stickerListShowUp;
//                    viewTranslationX(mRvSticker, stickerListShowUp);
//                    return;
//                }
//                if (filterListShowUp) {
//                    filterListShowUp = !filterListShowUp;
//                    viewTranslationX(mRvFilter, filterListShowUp);
//                    return;
//                }
//            }
//
//        });
//        stickerView.setSaveIndex(stickerIndex);
//        stickerView.setSaveNameCh(Const.STICKERS_NAME[stickerIndex]);
//        stickerView.setSaveNameEn(Const.STICKERS_NAME_EN[stickerIndex]);
//        stickerView.setSaveFileAbsPath(Const.STICKER_FILE_ABS_PATH[stickerIndex]);
//        stickerView.setSaveResId(Const.STICKERS_VALUES[stickerIndex]);
//
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        fl_main_content.addView(stickerView, lp);
//        setCurrentEdit(stickerView);
//        mStickerViews.add(stickerView);
//    }
//
//    /**
//     * 设置当前处于编辑模式的贴纸
//     */
//    private void setCurrentEdit(StickerView stickerView) {
//        if (mCurrentView != null) {
//            mCurrentView.setInEdit(false);
//        }
//        mCurrentView = stickerView;
//        stickerView.setInEdit(true);
//    }
//
//    /**
//     * @details 计算得到原图图片相对于屏幕的缩放比例和最后缩放后的留白区域
//     */
//    private void calcScaleAndLeaveSize() {
//        if (copyBitmap == null) {return;}
//        scaleAndLeaveSize = CommonUtils.calcScaleAndLeaveSize(copyBitmap, iv_main_image);
//    }
//
//    /**
//     * @details 保存贴纸和文本
//     * @param canvas
//     */
//    private void saveViews(Canvas canvas) {
//        operateComplete();
//        // 隐藏 sticker recycle view
//        if (stickerListShowUp) {
//            stickerListShowUp = !stickerListShowUp;
//            setViewRes(stickerListShowUp, rb_sticker);
//            viewTranslationX(mRvSticker, stickerListShowUp);
//            return;
//        }
//        if (filterListShowUp) {
//            filterListShowUp = !filterListShowUp;
//            setViewRes(filterListShowUp, rb_filter);
//            viewTranslationX(mRvFilter, filterListShowUp);
//            return;
//        }
//
//        float leaveH = 0f, leaveW = 0f, scale = 0f;
//        scale  = scaleAndLeaveSize[0];   // 原图与ImageView的缩放比例
//        leaveW = scaleAndLeaveSize[1];   // 图片自动缩放时造成的留白区域
//        leaveH = scaleAndLeaveSize[2];
//        canvas.scale(scale, scale);
//        canvas.translate(-leaveW, -leaveH);
//        // 保存贴纸
//        saveSticker(canvas);
//
//        // 保存文本
//        saveBeautySentences(canvas, scale, leaveW, leaveH);
//
//        /**
//         * @warning 因为 vivo 的内存引用技术有问题，所以导致引用了两个bitmap，bitmap对象回收有异常
//         * Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
//         * 因为放弃了单个 bitmap 对象的操作，所以不必再设置1*1像素的bitmap
//         * 发生 imageView 在 draw copyBitmap 的时候而 saveTask 已经执行完成（安卓6.0 小米4）
//         * 总结为运行速度太快, 在 task 的子线程中添加 0.5 秒延迟
//         */
//        iv_main_image.setImageBitmap(copyBitmap);
//        if (mSaveTask != null) {
//            mSaveTask.cancel(true);
//        }
//        mSaveTask = new SaveDiaryTask();
//        mSaveTask.execute();
//    }
//    private void saveBeautySentences(Canvas canvas, float scale, float leaveW, float leaveH) {
//        if (mMtvLists == null || mMtvLists.size() <= 0) {return;}
//        Paint mPaint = new Paint();     // 初始化画笔
//        mPaint.setAntiAlias(true);      // 设置消除锯齿
//        DataInfo.PictureSubElements pse;
//        float saveLeft, saveBottom;
//        for (MovableTextView2 mtv : mMtvLists) {
//            pse = new DataInfo.PictureSubElements();
//            mPaint.setColor(mtv.getCurrentTextColor());
//            mPaint.setTypeface(mtv.getTypeface());
//            float textViewL = mtv.getLeft() * 1.0f;
//            float textViewT = mtv.getTop() * 1.0f;
//            float textViewR = mtv.getRight() * 1.0f;
//            float textViewB = mtv.getBottom() * 1.0f;
//            float imgW = iv_main_image.getWidth() * 1.0f;
//            float imgH = iv_main_image.getHeight() * 1.0f;
////            float textSize = mtv.getTextSize();
////            // textSize 就是文本在绘画时的高度，也是文本的大小
////            mPaint.setTextSize(textSize);
////            float textLength = mPaint.measureText(mtv.getText().toString());
////            // 计算得到当前画笔绘制规则的 baseLine，用来准确计算
////            float textCenterVerticalBaselineY = FontMatrixUtils.calcTextCenterVerticalBaselineY(mPaint);
////            // 画笔实际绘画的 Y 坐标：baseLine + top + y轴上的间距
////            saveBottom = textCenterVerticalBaselineY + textViewT + (textViewB - textViewT - textSize) / 2;
////            // 得到绘制的第一个字符在 X 轴上与左边框的间距
////            float leftPadding = (textViewR - textViewL - textLength) / 2;
////            saveLeft = textViewL + leftPadding;
////            canvas.drawText(
////                    mtv.getText().toString(),
////                    saveLeft, saveBottom,
////                    mPaint
////            );
//
//            String content = mtv.getText().toString();
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
//                // 画笔实际绘画的 Y 坐标：top + baseLine + 字体间距 + 字体高度
//                saveBottom = textViewT + textCenterVerticalBaselineY + spacing * (i + 1) + textSize * i;
//                canvas.drawText(strArr[i], saveLeft, saveBottom, mPaint
//                );
//            }
//
//
//            // 还得保存一个 position 相对于父控件的位置的比例
//            pse.text = mtv.getText().toString();
//            pse.textSize = mtv.getTextSize();
//            pse.typeface = mtv.getTypefaceName();
//            pse.pLeftScale = textViewL * 1.0f / imgW;
//            pse.pTopScale = textViewT * 1.0f / imgH;
//            pse.pLeft = (int) textViewL;
//            pse.pTop = (int) textViewT;
//            pse.color = mtv.getCurrentTextColor();
//            pictureSubElementsList.add(pse);        // 保存了位置，颜色等属性参数
//            fl_main_content.removeView(mtv);
//        }
//        mMtvLists.clear();
//        mMtvLists = null;
//    }
//
//    /**
//     * 保存贴纸
//     * @param canvas
//     */
//    private void saveSticker(Canvas canvas) {
//        if (mStickerViews == null || mStickerViews.size() <= 0) {
//            return;
//        }
//        ArrayList<DataInfo.StickerInfo> stickerInfoArrayList = new ArrayList<>();
//        DataInfo.StickerInfo stickerInfo;
//        for(StickerView sv:mStickerViews) {
//            canvas.drawBitmap(sv.getBitmap(), sv.saveMatrix(), null);
//            stickerInfo = new DataInfo.StickerInfo();
//            stickerInfo.floatArr = sv.saveMatrixFloatArray();
//            stickerInfo.index = sv.getSaveIndex();
//            stickerInfo.nameCh = sv.getSaveNameCh();
//            stickerInfo.nameEn = sv.getSaveNameEn();
//            stickerInfo.fileAbsPath = sv.getSaveFileAbsPath();
//            stickerInfo.resId = sv.getSaveResId();
//            stickerInfoArrayList.add(stickerInfo);
//
//            pictureStickerInfoList.add(stickerInfo);
//            fl_main_content.removeView(sv);
//        }
//        mStickerViews.clear();
//        mStickerViews = null;
//    }
//
//    private void parseStickerData(DataInfo dataInfo) {
//        mStickerViews.clear();
//        List<DataInfo.StickerInfo> listTemp = dataInfo.metaDataPictureInfo.listStickers;
//        if (listTemp.size() == 0 || listTemp == null) return;
//        for (int i = 0; i < listTemp.size(); i++) {
//            DataInfo.StickerInfo stickerInfo = listTemp.get(i);
//            float[] floats = stickerInfo.floatArr;
//            final StickerView stickerView = new StickerView(this);
//            stickerView.setImageResource(Const.STICKERS_VALUES[stickerInfo.index]);
//            stickerView.reloadBitmapAfterOnDraw(floats);
//            stickerView.setSaveIndex(stickerInfo.index);
//            stickerView.setSaveNameCh(stickerInfo.nameCh);
//            stickerView.setSaveNameEn(stickerInfo.nameEn);
//            stickerView.setSaveFileAbsPath(stickerInfo.fileAbsPath);
//            stickerView.setSaveResId(stickerInfo.resId);
//            stickerView.setOperationListener(new StickerView.OperationListener() {
//                @Override
//                public void onDeleteClick() {
//                    mStickerViews.remove(stickerView);
//                    fl_main_content.removeView(stickerView);
//                }
//                @Override
//                public void onEdit(StickerView stickerView) {
//                    mCurrentView.setInEdit(false);
//                    mCurrentView = stickerView;
//                    mCurrentView.setInEdit(true);
//                    if (stickerListShowUp) {
//                        stickerListShowUp = !stickerListShowUp;
//                        viewTranslationX(mRvSticker, stickerListShowUp);
//                        return;
//                    }
//                    if (filterListShowUp) {
//                        filterListShowUp = !filterListShowUp;
//                        viewTranslationX(mRvFilter, filterListShowUp);
//                        return;
//                    }
//                }
//            });
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//            fl_main_content.addView(stickerView, lp);
//            mStickerViews.add(stickerView);
//            setCurrentEdit(stickerView);
//        }
//    }
//
//    private void parseListData(DataInfo dataInfo) {
//        List<DataInfo.PictureSubElements> listData = dataInfo.metaDataPictureInfo.listData;
//        mMtvLists.clear();
//        if (!dataInfo.dataContent.equals("") && listData.size() == 0) {
//            String text = dataInfo.dataContent;
//            float textSize = DensityUtils.px2sp(mContext, dataInfo.metaDataPictureInfo.fontSize);
//            String fontName = dataInfo.metaDataPictureInfo.font;
//            MovableTextView2 mtv = generateMtv(
//                    text,
//                    textSize,
//                    0,
//                    fontName,
//                    CommonUtils.getTypeface(fontName),
//                    fl_main_content
//            );
//            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mtv.getLayoutParams();
//            lp.gravity = Gravity.CENTER;
//            mtv.setLayoutParams(lp);
//            mMtvLists.add(mtv);
//            return;
//        }
////        else if (dataInfo.dataContent.equals("") && listData.size() == 0) {
////            // 表示没有原来的数据，也没有保存任何新的数据
////            MovableTextView2 mtv = generateMtv(null, 0.0f, null, "default", CommonUtils.getTypeface("default"), fl_main_content);
////            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mtv.getLayoutParams();
////            lp.gravity = Gravity.CENTER;
////            mtv.setLayoutParams(lp);
////            mMtvLists.add(mtv);
////        }
//        for (DataInfo.PictureSubElements psElement: listData) {
//            int top = psElement.pTop;
//            int left = psElement.pLeft;
//            float textSize = DensityUtils.px2sp(mContext, psElement.textSize);
//            int color = psElement.color;
//            String text = psElement.text;
//            String fontName = psElement.typeface;
//            Typeface tf = CommonUtils.getTypeface(fontName);
//            MovableTextView2 mtv = generateMtv(
//                    text,
//                    textSize,
//                    color,
//                    fontName,
//                    tf,
//                    fl_main_content
//            );
//            // 添加颜色的进度
//            mtv.setColorSeekBarProgress(CommonUtils.matchProgress(psElement.color, mContext));//psElement.colorProgress);
//            resetLayoutParams(mtv, true, top, left);
//            mMtvLists.add(mtv);
//        }
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
//        ep_KeyboardOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                operateKeyboardState();
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
//                        // 重新设置参数
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
//            fl_main_content.removeView(delMtv);
//        }
//        for (MovableTextView2 m : mMtvLists) {
//            m.setSelected(false);
//        }
//        // 还原位置
//        movableTextViewPositionRevert();
//        hideEditPanelAndCloseKeyboard();
//    }
//
//
//    private void operateKeyboardState() {
//        if (mCurKeyboardState == KeyboardState.STATE_OPEN) {
//            mCurKeyboardState = KeyboardState.STATE_HIDE;
//        } else if (mCurKeyboardState == KeyboardState.STATE_HIDE) {
//            mCurKeyboardState = KeyboardState.STATE_OPEN;
//        }
//        CommonUtils.hitKeyboardOpenOrNot(mContext);
//    }
//
//    private void hideEditPanelAndCloseKeyboard() {
//        if (ll_base_edit_panel.getVisibility() == View.VISIBLE) {
//            ll_base_edit_panel.setVisibility(View.INVISIBLE);
//        }
//        if (mCurKeyboardState == KeyboardState.STATE_OPEN) {
//            mCurKeyboardState = KeyboardState.STATE_HIDE;
//            CommonUtils.hitKeyboardOpenOrNot(mContext);
//        }
//    }
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
//    private void viewTranslationX(View view, boolean state) {
//        if (state) {
//            ViewPropertyAnimator.animate(view).translationX(0f).setDuration(100).start();
//        } else {
//            ViewPropertyAnimator.animate(view).translationX(BitmapUtils.getScreenPixels(mContext).widthPixels).setDuration(100).start();
//        }
//    }
//
//    private void showDownloadFontDialog(String fontSize, final SelectableView selectableView, final RadioGroup rg_font_group) {
//        new MaterialDialog.Builder(ImageEditorActivity.this)
//                .title(R.string.no_font_file_title)
//                .content(ImageEditorActivity.this.getResources().getString(R.string.no_font_file_content))
//                .positiveText("下载")
//                .negativeText("算了")
//                .callback(new MaterialDialog.ButtonCallback() {
//                    @Override
//                    public void onPositive(MaterialDialog dialog) {
//                        super.onPositive(dialog);
//                        if (!CommonUtils.isNetAvailable(ImageEditorActivity.this)) {
//                            Toast.makeText(ImageEditorActivity.this, getResources().getString(R.string.net_unavailable), Toast.LENGTH_SHORT);
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
//    @Override
//    public void onDestroy() {
//        try{
//            mTaskDialog.dismiss();
//        }catch (Exception e) {
//        }
//        super.onDestroy();
//        mMtvLists = null;
//        mStickerViews = null;
//        mCurrDataInfo = null;
//        pictureSubElementsList = null;
//        pictureStickerInfoList = null;
//        if (mLoadImageTask != null) {
//            mLoadImageTask = null;
//        }
//        if (mSaveTask != null) {
//            mSaveTask = null;
//        }
//
//        System.gc();
//    }
//
//    private void bitmapCollections() {
//        if (copyBitmap != null && !copyBitmap.isRecycled()) {
//            copyBitmap.recycle();
//            copyBitmap = null;
//        }
//        if (originalBitmap != null && !originalBitmap.isRecycled()) {
//            originalBitmap.recycle();
//            originalBitmap = null;
//        }
//    }
//
//    private void setViewRes(boolean state, ImageButton view) {
//        int resId = 0;
//        if (rb_sticker.equals(view)) {
//            resId = state ? R.mipmap.btn_image_editor_tab_sticker_pressed : R.mipmap.btn_image_editor_tab_sticker_normal;
//        }
//        if (rb_filter.equals(view)) {
//            resId = state ? R.mipmap.btn_image_editor_tab_filter_pressed : R.mipmap.btn_image_editor_tab_filter_normal;
//        }
//        view.setImageResource(resId);
//    }
//}
