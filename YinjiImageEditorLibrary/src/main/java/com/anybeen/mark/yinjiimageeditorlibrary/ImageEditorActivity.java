package com.anybeen.mark.yinjiimageeditorlibrary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anybeen.mark.yinjiimageeditorlibrary.entity.CarrotInfo;
import com.anybeen.mark.yinjiimageeditorlibrary.entity.ImageDataInfo;
import com.anybeen.mark.yinjiimageeditorlibrary.entity.MatrixInfo;
import com.anybeen.mark.yinjiimageeditorlibrary.entity.ProgressItem;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.BitmapUtils;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.ColorUtil;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.CommonUtils;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.DensityUtils;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.FileUtils;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.FontMatrixUtils;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.ToastUtil;
import com.anybeen.mark.yinjiimageeditorlibrary.view.CarrotEditText;
import com.anybeen.mark.yinjiimageeditorlibrary.view.CustomSeekBar;
import com.anybeen.mark.yinjiimageeditorlibrary.view.MovableTextView2;
import com.anybeen.mark.yinjiimageeditorlibrary.view.SelectableView;
import com.anybeen.mark.yinjiimageeditorlibrary.view.StickerView;
import com.xinlan.imageeditlibrary.editimage.PhotoProcessing;

import java.util.ArrayList;

public class ImageEditorActivity extends Activity {
    public static final String FILE_PATH = "albumPhotoAbsolutePath";
    public static final String IS_NEW = "isNewAddPhoto";
    private ImageDataInfo mCurrDataInfo;


    private Context mContext;
    // topToolbar
    private ImageView bt_save;
    // mainContent
    private ImageView iv_main_image;
    private RecyclerView mRecyclerView_filter_list;
    // bottomToolbar
    private RadioGroup main_radio;
    private RadioButton rb_word;
    private RadioButton rb_sticker;

    @Deprecated
    private Bitmap mainBitmap;
    private Bitmap copyBitmap;
    private Bitmap filterSampleIconBitmap;

    private FrameLayout fl_main_content;



    private static int mCurrImgId = R.mipmap.pic_bg_software_esting_ngineer;
    public int keyboardHeight = 0;              // 记录键盘高度


    @Deprecated //未使用到
    private StickerView mCurrentView;           // 当前处于编辑状态的贴纸

    private ArrayList<MovableTextView2> mMtvLists;  // 存储文字列表
    private ArrayList<StickerView> mStickerViews;   // 存储贴纸列表
    private float[] scaleAndLeaveSize;              // 计算三个缩放比例
    private FrameLayout fl_image_editor_base_layout;


    // edit panel params
    private LinearLayout ll_base_edit_panel;
    private LinearLayout edit_panel;            // 文字编辑面板，使用 LayoutInflate
    private ImageView ep_KeyboardOptions;       // 键盘切换显示与隐藏
    private CarrotEditText ep_OperateText;      // 文本编辑框
    private Button ep_BtnComplete;              // 完成按钮
    private SeekBar ep_FontSize;                // 字体大小
    private CustomSeekBar ep_CsbFontColor;      // 字体颜色 seek bar
    private ImageView ep_IvColorShow;           // 颜色展示
    private RadioGroup ep_rgFontGroup;          // 字体选择
    private int currentFontCheckedId = -1;

    // edit panel params


    private boolean openKeyboardOnLoading;
    private boolean createMtvOnLoading;
    private boolean isFirstAddMtv;
    private KeyboardState mCurKeyboardState;
    private enum KeyboardState {
        STATE_OPEN, STATE_HIDE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_image_editor);
        mContext = this;
        mCurrDataInfo = new ImageDataInfo();
        mMtvLists = new ArrayList<>();
        mStickerViews = new ArrayList<>();
        openKeyboardOnLoading = true;
        createMtvOnLoading = true;
        isFirstAddMtv = true;


        fl_image_editor_base_layout = (FrameLayout) findViewById(R.id.fl_image_editor_base_layout);
        initView();
        // 加载图片
        loadBitmap();

        handleListener();

        reloadStickerMore();

        reloadCarrot();
    }


    private void initView() {

        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        // edit_panel 的父容器
        ll_base_edit_panel = (LinearLayout) findViewById(R.id.ll_base_edit_panel);
        ll_base_edit_panel.setVisibility(View.INVISIBLE);

        // topBar
        bt_save = (ImageView) findViewById(R.id.bt_save);
        // mainContent
        mRecyclerView_filter_list = (RecyclerView) findViewById(R.id.rv_filter_list);
        mRecyclerView_filter_list.setHasFixedSize(true);
        int spacingInPixels = 10; //getResources().getDimensionPixelSize(R.dimen.space);
        mRecyclerView_filter_list.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView_filter_list.setLayoutManager(layoutManager);

        mRecyclerView_filter_list.setAdapter(new FilterAdapter(this));

        iv_main_image = (ImageView) findViewById(R.id.iv_main_image);
        // bottomToolbar
        main_radio = (RadioGroup) findViewById(R.id.main_radio);
        rb_word = (RadioButton) findViewById(R.id.rb_word);
        rb_sticker = (RadioButton) findViewById(R.id.rb_sticker);

    }
    private void loadBitmap() {
        boolean isNew = getIntent().getBooleanExtra(IS_NEW, true);
        String filePath = "";
        if (isNew) {//根据用户操作状态（新添加大图、编辑大图），分成两个Controller处理
            filePath = getIntent().getStringExtra(FILE_PATH);
//            addFirstMtv();
        } else {
//            mCurrDataInfo = (ImageDataInfo) getIntent().getSerializableExtra("DataInfo");
//            parseListData(mCurrDataInfo);
//            filePath = mCurrDataInfo.metaDataPictureInfo.oriPicturePath;
        }
        loadImage(filePath);
        System.out.println("=======" + filePath);
//        loadImage(filePath);
//        mainBitmap = loadImage();
//        mainBitmap = BitmapUtils.loadImage(this, mCurrImgId, imageWidth, imageHeight);
//        copyBitmap = mainBitmap.copy(Bitmap.Config.ARGB_8888, true);
//        iv_main_image.setImageBitmap(copyBitmap);
    }

    private void handleListener() {
        // 监听获取键盘高度, 只能监听到打开与关闭
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                System.out.println("keyboard open");
                mCurKeyboardState = KeyboardState.STATE_OPEN;
                if (height != 0) {
                    if (createMtvOnLoading && openKeyboardOnLoading) {
                        keyboardHeight = height;
                        SystemClock.sleep(300);
                        edit_panel = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_edit_panel, null);
                        ll_base_edit_panel.addView(edit_panel);

                        ep_KeyboardOptions = (ImageView) edit_panel.findViewById(R.id.iv_edit_panel_key_board_options);
                        ep_OperateText = (CarrotEditText) edit_panel.findViewById(R.id.et_edit_panel_text);
                        ep_BtnComplete = (Button) edit_panel.findViewById(R.id.iv_edit_panel_complete);
                        ep_FontSize = (SeekBar) edit_panel.findViewById(R.id.sb_edit_panel_font_size);
                        ep_IvColorShow = (ImageView) edit_panel.findViewById(R.id.iv_edit_panel_color_show);
                        ep_CsbFontColor = (CustomSeekBar) edit_panel.findViewById(R.id.csb_edit_panel_font_color);
                        ep_rgFontGroup = (RadioGroup) edit_panel.findViewById(R.id.rg_font_group);

                        // 获取编辑 panel 的头高度
                        LinearLayout ll_edit_panel_head = (LinearLayout) edit_panel.findViewById(R.id.ll_edit_panel_head);
                        ll_edit_panel_head.measure(0, 0);
                        int headHeight = ll_edit_panel_head.getMeasuredHeight();

                        LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) edit_panel.getLayoutParams();
                        lps.height = headHeight + keyboardHeight;
                        lps.gravity = Gravity.BOTTOM;
                        edit_panel.setLayoutParams(lps);
                        ll_base_edit_panel.setVisibility(View.VISIBLE);

                        addMovableTextView();
                        initDataToSeekBar();
                        handleEditPanelEvent();

                        createMtvOnLoading = false;
                        openKeyboardOnLoading = false;
                    } else {
                        ll_base_edit_panel.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void keyBoardHide(int height) {
                System.out.println("keyboard hide");
                mCurKeyboardState = KeyboardState.STATE_HIDE;
            }
        });


        bt_save.setOnClickListener(new SaveClickListener());
        // main_radio.setOnCheckedChangeListener(new CurrentRadioGroupOnCheckChangeListener());
        rb_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * wait TODO
                 * 加入一个 sharedPrefs 参数，保存到本地中，保存 edit_panel 的高度，下次打开图片编辑，
                 * 初始化 edit_panel 放在 onCreate 中
                 */
                CommonUtils.hitKeyboardOpenOrNot(mContext);
                if (isFirstAddMtv) {    // 表示第一次添加 mtv 文本
                    isFirstAddMtv = false;
                } else {
                    // 设置弹出软键盘
                    addMovableTextView();
                }
            }
        });

        rb_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStickerView();
            }
        });

        ll_base_edit_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateCarrotComplete();
            }
        });
    }



    private class SaveClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // 在图片全部逻辑加载完成后，计算图片加载后与屏幕的缩放比与留白区域
            calcScaleAndLeaveSize();
            Canvas canvas = new Canvas(copyBitmap);
            saveViews(canvas);
        }
    }

    private class MTVClickListener implements MovableTextView2.OnCustomClickListener {
        MovableTextView2 mMtv;
        public MTVClickListener(MovableTextView2 mtv2) {
            this.mMtv = mtv2;
        }
        @Override
        public void onCustomClick() {
            // details 因为当键盘隐藏的时候，点击才显示软键盘，而软键盘打开的时候，事件已经被它的父容器
            // 消费了，所以不可能触发到点击事件
            mMtv.setSelected(true);
            if (mCurKeyboardState == KeyboardState.STATE_HIDE) {
                ll_base_edit_panel.setVisibility(View.VISIBLE);
            }
            // 把 MovableTextView2 的数据载入到编辑面板中
            loadMtvDataIntoEditPanel(mMtv);
        }
    }

    /**
     * 加载 mtv 数据到 editPanel 中
     * @param currMtv
     */
    private void loadMtvDataIntoEditPanel(MovableTextView2 currMtv) {
        if (ll_base_edit_panel.getVisibility() == View.INVISIBLE) {
            return;
        }
        for (MovableTextView2 m : mMtvLists) {
            if (m.equals(currMtv)) {
                ep_OperateText.setText(m.getText());
                ep_OperateText.setSelection(m.getText().length());   // 设置光标的位置
                ep_IvColorShow.setBackgroundColor(m.getCurrentTextColor());
                ep_CsbFontColor.setProgress(m.getColorSeekBarProgress());
                // ep_CsbFontColor.setProgress();
                ep_FontSize.setProgress(DensityUtils.px2dp(mContext, m.getTextSize()));
                // ep_rgFontGroup
                String fontName = m.getTypefaceName();
                for (int i = 0; i < ep_rgFontGroup.getChildCount(); i++) {
                    if (ep_rgFontGroup.getChildAt(i).getTag().equals(fontName)) {
                        ((SelectableView) (ep_rgFontGroup.getChildAt(i))).setChecked(true);
                    }
                }
            }
        }
    }






    // ====================task line
    private LoadImageTask mLoadImageTask;
    public void loadImage(String filepath) {
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask();
        mLoadImageTask.execute(filepath);
    }
    private ProgressDialog loadDialog;
    private final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadDialog = new ProgressDialog(ImageEditorActivity.this);
            loadDialog.setCancelable(false);
            loadDialog.setMessage("图片加载中...");
            loadDialog.show();
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            int displayWidth = BitmapUtils.getScreenPixels(mContext).widthPixels;
            int displayHeight = BitmapUtils.getScreenPixels(mContext).heightPixels;
            copyBitmap = BitmapUtils.loadImageByPath(params[0], displayWidth, displayHeight).copy(Bitmap.Config.RGB_565, true);
            return copyBitmap;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            iv_main_image.setImageBitmap(result);
            loadDialog.dismiss();
        }
    }// end inner class

    /**
     * 添加一个文本控件
     */
    private void addMovableTextView() {
        final MovableTextView2 newAddMtv = new MovableTextView2(mContext);
        newAddMtv.setSelected(true);
        newAddMtv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetLayoutParams(newAddMtv, false, 0, 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newAddMtv.setOnCustomClickListener(new MTVClickListener(newAddMtv));
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) newAddMtv.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        newAddMtv.setLayoutParams(lp);

        ep_OperateText.setText(newAddMtv.getText());
        ep_OperateText.setSelection(newAddMtv.getText().length());   // 设置光标的位置
        ep_IvColorShow.setBackgroundColor(newAddMtv.getCurrentTextColor());
        ep_CsbFontColor.setProgress(newAddMtv.getColorSeekBarProgress());
        // ep_CsbFontColor.setProgress();
        ep_FontSize.setProgress(DensityUtils.px2dp(mContext, newAddMtv.getTextSize()));

        fl_main_content.addView(newAddMtv);
        mMtvLists.add(newAddMtv);
        // loadMtvDataIntoEditPanel(newAddMtv);
    }

    /**
     * @details 通过参数值返回一个 {@link MovableTextView2} 对象
     * @param text          输入的文字
     * @param textSize      输入的文字大小
     * @param rgb           输入的文字颜色的rgb
     * @param typefaceName  输入的文字的字体名称
     * @param typeface      输入的文字的字体
     * @param parentView
     * @return
     */
    private MovableTextView2 generateMtv(
            String text,
            float textSize,
            int[] rgb,
            String typefaceName,
            Typeface typeface,
            final FrameLayout parentView) {
        final MovableTextView2 mtv = new MovableTextView2(mContext);
        if (text != null) {
            mtv.setText(text);
        }
        if (textSize != 0.0f) {
            mtv.setTextSize(textSize);
        }
        if (rgb != null){
            mtv.setColorR(rgb[0]);
            mtv.setColorG(rgb[1]);
            mtv.setColorB(rgb[2]);
            mtv.setTextColor(ColorUtil.getColorByRGB(rgb));
        }
        mtv.setTypefaceName(typefaceName);
        mtv.setTypeface(typeface);
        parentView.addView(mtv);
        return mtv;
    }






    //添加表情
    private void addStickerView() {
        final StickerView stickerView = new StickerView(this);
        stickerView.setImageResource(R.mipmap.ic_cat);
        // maidou add
        // stickerView.setBitmapReloadMatrix(reloadMatrix);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mStickerViews.remove(stickerView);
                fl_main_content.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {
                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mStickerViews.indexOf(stickerView);
                if (position == mStickerViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = mStickerViews.remove(position);
                mStickerViews.add(mStickerViews.size(), stickerTemp);
            }
        });
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        fl_main_content.addView(stickerView, lp);
        mStickerViews.add(stickerView);
        setCurrentEdit(stickerView);
        mStickerViews.add(stickerView);
    }

    /**
     * 设置当前处于编辑模式的贴纸
     */
    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    /**
    // --- listener 底部的 RadioGroup, 切换监听
    private class RadioGroupOnCheckChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_word:
                    // Show Text Editor
                    addMovableTextView();
                    break;
                case R.id.rb_sticker:
                    // Show Sticker ImageList RecycleView
                    break;
                case R.id.rb_filter:
                    // Show Filter ImageList RecycleView
                    break;
                default:
                    System.out.println("出现未知错误：" + checkedId);
                    break;
            }
        }
    }
     */


    // inner class start 滤镜的处理方法，暂时不用
    private class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private Context context;
        public FilterAdapter(Context context) {
            this.context = context;
        }
        @Override
        public int getItemCount() {
            return PhotoProcessing.FILTERS.length;
        }

        @Override
        public int getItemViewType(int position) {
            return 1;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recycle_view_filter, null);
            FilterHolder holder = new FilterHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            FilterHolder h = (FilterHolder) holder;
            String name = PhotoProcessing.FILTERS[position];
            h.text.setText(name);
            if (filterSampleIconBitmap != null && !filterSampleIconBitmap.isRecycled()) {
                filterSampleIconBitmap = null;
            }
            filterSampleIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_icon_filter_sample);
            if (position == 0) {
                h.icon.setImageBitmap(filterSampleIconBitmap);
            }
            else {
                h.icon.setImageBitmap(PhotoProcessing.filterPhoto(filterSampleIconBitmap, position));
            }
            h.icon.setOnClickListener(new FilterClickListener(position));
        }
        public class FilterClickListener implements View.OnClickListener{
            private int clickPosition;
            public FilterClickListener(int position) {
                this.clickPosition = position;
            }
            @Override
            public void onClick(View v) {
                FilterTask filterTask = new FilterTask();
                filterTask.execute(clickPosition + "");
            }
        }
        public class FilterHolder extends RecyclerView.ViewHolder {
            public ImageView icon;
            public TextView text;
            public FilterHolder(View itemView) {
                super(itemView);
                this.icon = (ImageView) itemView.findViewById(R.id.icon);
                this.text = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
    // inner class end
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        public SpaceItemDecoration(int space) {
            this.space = space;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

    private final class FilterTask extends AsyncTask<String, Void, Bitmap> {
        private ProgressDialog loadDialog;
        public FilterTask() {
            super();
            loadDialog = new ProgressDialog(ImageEditorActivity.this, R.style.MyDialog);
            loadDialog.setCancelable(false);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadDialog.show();
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            int position = Integer.parseInt(params[0]);
            return PhotoProcessing.filterPhoto(
                    BitmapUtils.loadImage(ImageEditorActivity.this, mCurrImgId, fl_main_content),
                    position
            );
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            loadDialog.dismiss();
            iv_main_image.setImageBitmap(result);
            System.gc();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            loadDialog.dismiss();
        }
    }// end inner class

    /**
     * @details 计算得到原图图片相对于屏幕的缩放比例和最后缩放后的留白区域
     */
    private void calcScaleAndLeaveSize() {
        if (copyBitmap == null) {return;}
        scaleAndLeaveSize = new float[3];
        float svWidth = iv_main_image.getWidth() * 1.0f;
        float svHeight = iv_main_image.getHeight() * 1.0f;
        float copyBitWidth = copyBitmap.getWidth() * 1.0f;
        float copyBitHeight = copyBitmap.getHeight() * 1.0f;

        float scaleX = copyBitWidth / svWidth;
        float scaleY = copyBitHeight / svHeight;
        float scale = scaleX > scaleY ? scaleX:scaleY;
        float leaveW = 0.0f, leaveH = 0.0f;     // 留白区域
        if (scaleX > scaleY) {
            leaveH = (svHeight -  copyBitHeight / scale) / 2;
        } else {
            leaveW = (svWidth - copyBitWidth / scale) / 2;
        }
        scaleAndLeaveSize[0] = scale;       // 表示图片与屏幕的缩放比
        scaleAndLeaveSize[1] = leaveW;      // 表示图片的X轴留白区域
        scaleAndLeaveSize[2] = leaveH;      // 表示图片的Y轴留白区域
    }

    /**
     * @details 重新载入多个贴纸
     */
    private void reloadStickerMore() {
        ArrayList<MatrixInfo> matrixInfoLists = FileUtils.readFileToMatrixInfoLists();
        if (matrixInfoLists == null)return;
        for (MatrixInfo matrixInfo: matrixInfoLists) {
            float[] floats = matrixInfo.floatArr;
            final StickerView stickerView = new StickerView(this);
            stickerView.setImageResource(R.mipmap.ic_cat);
            stickerView.reloadBitmapAfterOnDraw(floats);
            stickerView.setOperationListener(new StickerView.OperationListener() {
                @Override
                public void onDeleteClick() {
                    mStickerViews.remove(stickerView);
                    fl_main_content.removeView(stickerView);
                }

                @Override
                public void onEdit(StickerView stickerView) {
                    mCurrentView.setInEdit(false);
                    mCurrentView = stickerView;
                    mCurrentView.setInEdit(true);
                }
                @Override
                public void onTop(StickerView stickerView) {
                    int position = mStickerViews.indexOf(stickerView);
                    if (position == mStickerViews.size() - 1) {
                        return;
                    }
                    StickerView stickerTemp = mStickerViews.remove(position);
                    mStickerViews.add(mStickerViews.size(), stickerTemp);
                }
            });
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            fl_main_content.addView(stickerView, lp);
            mStickerViews.add(stickerView);
            setCurrentEdit(stickerView);
        }
    }

    /**
     * @details 从本地文件中载入文本的序列化数据
     */
    private void reloadCarrot() {
        ArrayList<CarrotInfo> carrotInfoLists = FileUtils.readFileToCarrotInfoLists();
        if (carrotInfoLists == null)return;
        for (CarrotInfo carrotInfo: carrotInfoLists) {
            //wait TODO: 2016/3/24
//            final MovableTextView2 newAddMtv = new MovableTextView2(mContext);
//            fl_main_content.addView(newAddMtv);
//            mMtvLists.add(newAddMtv);
        }
    }

    /**
     * @details 保存贴纸和文本
     * @param canvas
     */
    private void saveViews(Canvas canvas) {
        float leaveH = 0f, leaveW = 0f, scale = 0f;
        scale  = scaleAndLeaveSize[0];   // 原图与ImageView的缩放比例
        leaveW = scaleAndLeaveSize[1];   // 图片自动缩放时造成的留白区域
        leaveH = scaleAndLeaveSize[2];
        canvas.scale(scale, scale);
        canvas.translate(-leaveW, -leaveH);
        // 保存文本
        saveBeautyWords(canvas, scale, leaveW, leaveH);


        // 保存贴纸
        saveSticker(canvas);

        // 最后生成图片
        String imagePath = FileUtils.saveBitmapToLocal(copyBitmap, mContext);
        iv_main_image.setImageBitmap(copyBitmap);
        ToastUtil.makeText(mContext, "保存图片成功~~~~~~~" + imagePath);
    }
    private void saveBeautyWords(Canvas canvas, float scale, float leaveW, float leaveH) {
        if (mMtvLists == null || mMtvLists.size() <= 0) {
            return;
        }
//        // 在保存之前先执行完成逻辑
//        if (mMtvLists != null && mMtvLists.size() > 0) {
        operateCarrotComplete();
//        }
        Paint mPaint = new Paint();     // 初始化画笔
        mPaint.setAntiAlias(true);      // 设置消除锯齿
        ArrayList<CarrotInfo> carrotInfoArrayList = new ArrayList<>();
        CarrotInfo carrotInfo;
        float saveLeft, saveBottom;
        for (MovableTextView2 mtv : mMtvLists) {
            carrotInfo = new CarrotInfo();
            mPaint.setColor(mtv.getCurrentTextColor());
            mPaint.setTypeface(mtv.getTypeface());
            mtv.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST));
            float viewHeight = mtv.getMeasuredHeight();
            float viewWidth = mtv.getMeasuredWidth();
            float textViewL = mtv.getLeft() * 1.0f;
            float textViewT = mtv.getTop() * 1.0f;
            float textViewR = mtv.getRight() * 1.0f;
            float textViewB = mtv.getBottom() * 1.0f;
//            float textViewH = mtv.getHeight() * 1.0f;
            float imgW = iv_main_image.getWidth() * 1.0f;
            float imgH = iv_main_image.getHeight() * 1.0f;
            float textSize = mtv.getTextSize();
            // textSize 就是文本在绘画时的高度，也是文本的大小
            mPaint.setTextSize(textSize);
            float textLength = mPaint.measureText(mtv.getText().toString());
            // 计算得到当前画笔绘制规则的 baseLine，用来准确计算
            float textCenterVerticalBaselineY = FontMatrixUtils.calcTextCenterVerticalBaselineY(mPaint);
            // 画笔实际绘画的 Y 坐标：baseLine + top + y轴上的间距
            saveBottom = textCenterVerticalBaselineY + textViewT + (textViewB - textViewT - textSize) / 2;
            // 得到绘制的第一个字符在 X 轴上与左边框的间距
            float leftPadding = (textViewR - textViewL - textLength) / 2;
            saveLeft = textViewL + leftPadding;
            canvas.drawText(
                    mtv.getText().toString(),
                    saveLeft, saveBottom,
                    mPaint
            );

            // 还得保存一个 position 相对于父控件的位置的比例
            carrotInfo.text = mtv.getText().toString();
            carrotInfo.textSize = mtv.getTextSize();
            carrotInfo.colorR = mtv.getColorR();
            carrotInfo.colorG = mtv.getColorG();
            carrotInfo.colorB = mtv.getColorB();
            carrotInfo.typeface = mtv.getTypefaceName();
            carrotInfo.pLeftScale = textViewL * 1.0f / imgW;
            carrotInfo.pTopScale = textViewT * 1.0f / imgH;
            carrotInfo.pLeft = (int) textViewL;
            carrotInfo.pTop = (int) textViewT;
            carrotInfoArrayList.add(carrotInfo);      // 保存了位置，颜色等属性参数

            // fl_main_content.removeView(mtv);
        }

        FileUtils.saveSerializableCarrotLists(carrotInfoArrayList);
        System.out.println("保存文本成功~~~~~~~");

//        mMtvLists.clear();
//        mMtvLists = null;
    }

    /**
     * 保存贴纸
     * @param canvas
     */
    private void saveSticker(Canvas canvas) {
        if (mStickerViews == null || mStickerViews.size() <= 0) {
            return;
        }
        ArrayList<MatrixInfo> matrixInfoArrayList = new ArrayList<>();
        MatrixInfo matrixInfo;
        for(StickerView sv:mStickerViews) {
            canvas.drawBitmap(sv.getBitmap(), sv.saveMatrix(), null);
            matrixInfo = new MatrixInfo();
            matrixInfo.floatArr = sv.saveMatrixFloatArray();
            matrixInfoArrayList.add(matrixInfo);
//            fl_main_content.removeView(sv);
        }
        FileUtils.saveSerializableMatrixLists(matrixInfoArrayList);
        System.out.println("保存贴纸成功~~~~~~~");
//        mStickerViews.clear();
//        mStickerViews = null;
    }

    /**
     * 初始化颜色 seekBar 控件的值
     */
    private int[] colorValues = {R.color.rainbow_red, R.color.rainbow_orange, R.color.rainbow_yellow,
            R.color.rainbow_green, R.color.rainbow_blue, R.color.rainbow_cyan,
            R.color.rainbow_purple, R.color.rainbow_white, R.color.rainbow_black};    // 红橙黄绿蓝靛紫黑白
    private void initDataToSeekBar() {
        ArrayList<ProgressItem> progressItemList;
        ProgressItem mProgressItem;
        float totalSpan = 9;
        float[] colorSpan = {1, 1, 1, 1, 1, 1, 1, 1, 1};
        progressItemList = new ArrayList<>();
        for (int i = 0; i < colorSpan.length; i++) {
            mProgressItem = new ProgressItem();
            mProgressItem.progressItemPercentage = ((colorSpan[i] / totalSpan) * 100);
            mProgressItem.color = colorValues[i];
            progressItemList.add(mProgressItem);
        }
        ep_CsbFontColor.initData(progressItemList);
        ep_CsbFontColor.invalidate();
    }

    /**
     * @details 处理文字编辑面板的事件
     */
    private void handleEditPanelEvent() {
        ep_CsbFontColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int curColor = getResources().getColor(colorValues[CommonUtils.matchedColor(progress)]);
                ep_IvColorShow.setBackgroundColor(curColor);
                for (MovableTextView2 m : mMtvLists) {
                    if (m.isSelected()) {
                        m.setTextColor(curColor);
                        m.setColorSeekBarProgress(progress);
                        int[] rgb = ColorUtil.getColorRGB(curColor);
                        m.setColorR(rgb[0]);
                        m.setColorG(rgb[1]);
                        m.setColorB(rgb[2]);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        ep_KeyboardOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateKeyboardState();
            }
        });

        ep_BtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateCarrotComplete();
            }
        });

        ep_FontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 10) return;
                for (MovableTextView2 mtv : mMtvLists) {
                    if (mtv.isSelected()) {
                        // progress is dp
                        int px = DensityUtils.dp2px(mContext, progress);
                        float sp = DensityUtils.px2sp(mContext, px);
                        mtv.setTextSize(sp);
                        // 重新设置参数
                        resetLayoutParams(mtv, false, 0, 0);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // 空实现点击方法，拦截面板点击事件，防止出现编辑面板低下的RadioGroup控件的焦点抢夺
        edit_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ep_OperateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (MovableTextView2 mtv : mMtvLists) {
                    if (mtv.isSelected()) {
                        mtv.setText(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ep_rgFontGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SelectableView selectableView = (SelectableView) group.findViewById(checkedId);
                if (selectableView == null || selectableView.isDownloading() || !selectableView.isChecked()) {
                    return;
                }
                Typeface typeface = selectableView.getFontType();
                if (typeface != null) {
                    changeTypeface(typeface, selectableView.getTag().toString());
                    currentFontCheckedId = checkedId;
                } else {
                    showDownloadFontDialog("3M", selectableView, ep_rgFontGroup);
                }
            }
        });
    }

    /**
     * 完成按钮的点击操作
     */
    private void operateCarrotComplete() {
        ToastUtil.makeText(mContext, "文本控件事件被拦截了");
        MovableTextView2 delMtv = null;
        int listSize = mMtvLists.size();
        for(int i = 0; i < listSize; i++) {
            if (mMtvLists.get(i).getText().length() == 0) {
                delMtv = mMtvLists.get(i);
            }
        }
        if (delMtv != null) {
            mMtvLists.remove(delMtv);
            fl_main_content.removeView(delMtv);
        }
        // 清理所有选中状态
        for (MovableTextView2 m : mMtvLists) {
            m.setSelected(false);
        }
        hideEditPanelAndCloseKeyboard();
    }

    @Deprecated
    private MovableTextView2 getOperateMtv() {
        for (MovableTextView2 mtv : mMtvLists) {
            if (mtv.isSelected()) {
                return mtv;
            }
        }
        return null;
    }


    /**
     * @details 切换键盘
     */
    private void operateKeyboardState() {
        if (mCurKeyboardState == KeyboardState.STATE_OPEN) {
            mCurKeyboardState = KeyboardState.STATE_HIDE;
        } else if (mCurKeyboardState == KeyboardState.STATE_HIDE) {
            mCurKeyboardState = KeyboardState.STATE_OPEN;
        }
        CommonUtils.hitKeyboardOpenOrNot(mContext);
    }

    /**
     * @details 同时关闭编辑panel和软键盘
     */
    private void hideEditPanelAndCloseKeyboard() {
        if (ll_base_edit_panel.getVisibility() == View.VISIBLE) {
            ll_base_edit_panel.setVisibility(View.INVISIBLE);
        }
        if (mCurKeyboardState == KeyboardState.STATE_OPEN) {
            mCurKeyboardState = KeyboardState.STATE_HIDE;
            CommonUtils.hitKeyboardOpenOrNot(mContext);
        }
    }

    /**
     * 重置参数
     * @param mtv
     * @param isEditStateReload
     * @param top
     * @param left
     */
    private void resetLayoutParams(MovableTextView2 mtv, boolean isEditStateReload, int top, int left) {
        mtv.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mtv.getLayoutParams();
        lp.gravity = -1;
        lp.height = mtv.getMeasuredHeight();
        lp.width = mtv.getMeasuredWidth();
        if (isEditStateReload) {
            lp.leftMargin = left;
            lp.topMargin = top;
        } else {
            lp.leftMargin = mtv.getLeft();
            lp.topMargin = mtv.getTop();
        }
        FrameLayout frameLayout = (FrameLayout) mtv.getParent();
        lp.rightMargin = frameLayout.getWidth() - mtv.getMeasuredWidth();
        mtv.setLayoutParams(lp);
    }



    //------------ 字体选择的所有操作方式
    /**
     * @param typeface 选中的字体
     */
    private void changeTypeface(Typeface typeface, String fontName) {
        if (mMtvLists.size() <= 0) return;
        for (MovableTextView2 mtv:mMtvLists) {
            if (mtv.isSelected()) {
                mtv.setTypefaceName(fontName);
                mtv.setTypeface(typeface);
                // 修改字体的时候也重新设置参数
                resetLayoutParams(mtv, false, 0, 0);
            }
        }
    }

    /**
     * @details CP from PhotoEditBaseController
     * @param fontSize
     * @param selectableView
     * @param rg_font_group
     */
    private void showDownloadFontDialog(String fontSize, final SelectableView selectableView, final RadioGroup rg_font_group) {
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
//                            ToastUtil.makeText(ImageEditorActivity.this, getResources().getString(R.string.net_unavailable));
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
                new ProgressDialog.Builder(ImageEditorActivity.this)
                .setTitle(R.string.no_font_file_title)
                .setMessage(ImageEditorActivity.this.getResources().getString(R.string.no_font_file_content))
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!CommonUtils.isNetAvailable(ImageEditorActivity.this)) {
                            ToastUtil.makeText(ImageEditorActivity.this, getResources().getString(R.string.net_unavailable));
                            return;
                        }
                        selectableView.downloadFont(new SelectableView.OnDownloadCompleteListener() {
                            @Override
                            public void onDownloadComplete() {
                                Typeface typeface = selectableView.getFontType();
                                if (typeface != null) {
                                    selectableView.invalidate();
                                    selectableView.initView();
                                    selectableView.setChecked(true);
                                    changeTypeface(typeface, selectableView.getTag().toString());
                                    currentFontCheckedId = rg_font_group.getCheckedRadioButtonId();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("算了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rg_font_group.check(currentFontCheckedId);
                    }
                })
                .show();
    }

    // 字体选择的所有操作方式---------done

}
