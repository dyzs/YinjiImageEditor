package com.anybeen.mark.yinjiimageeditorlibrary.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.Toast;


import com.anybeen.mark.yinjiimageeditorlibrary.R;
import com.anybeen.mark.yinjiimageeditorlibrary.component.ZipExtractorTask;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.CommonUtils;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.Const;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.LogUtil;

import java.io.File;


/**
 * Created by wjk on 2015/6/12.
 * 可显示选中/非选中两种状态的View 用于字幕位置/字体类型选择器
 */
public class SelectableView extends RadioButton{


    /**下载进度条画笔*/
    private  Paint mRingPaint;
    private  Paint mRingPaintBg;
    private Context context;
    private int mCurrNormalBg;
    private int mRingRadius;
    private RectF oval;
    private ProgressReceiver receiver;
    private OnDownloadCompleteListener downloadCompleteListener;
    private boolean isReceiverRegisted;

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if(checked){
            setBackgroundResource(R.drawable.shape_font_btn_checked_bg);
        }else {
            setBackgroundResource(mCurrNormalBg);
        }
    }

    public SelectableView(Context context) {
        super(context, null);
    }

    public SelectableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //初始化显示样式：可用、不可用
        boolean shouldInitPaint = initView();
        if(shouldInitPaint){
            mRingPaint = new Paint();
            mRingPaint.setAntiAlias(true);
            mRingPaint.setColor(context.getResources().getColor(R.color.common_red));
            mRingPaint.setStyle(Paint.Style.STROKE);

            mRingPaintBg = new Paint();
            mRingPaintBg.setAntiAlias(true);
            mRingPaintBg.setColor(context.getResources().getColor(R.color.grey_bg));
            mRingPaintBg.setStyle(Paint.Style.STROKE);

            oval = new RectF();
        }

       /* 向配置文件中写入配置
        Properties prop = new Properties();
        prop.put("prop1", "abc");
        prop.put("prop2", "1");
        prop.put("prop3", "3.14");
        File file = new File(Const.FONT_PATH+ File.separator+"font.properties");
        if(!file.exists()) {
            CommUtils.copyFiles2Assets(context, "fonts/font.properties", Const.FONT_PATH);
        }
        CommUtils.saveConfig(file, prop);*/
    }



    public boolean initView() {
        boolean shouldInitPaint = false;
        if(getFontType()==null){
            setBackgroundResource(R.drawable.shape_font_btn_disable_bg);
            setSelected(false);
            mCurrNormalBg = R.drawable.shape_font_btn_disable_bg;
            shouldInitPaint = true;
        }else{
            setBackgroundResource(R.drawable.shape_font_btn_enable_bg);
            setSelected(true);
            mCurrNormalBg = R.drawable.shape_font_btn_enable_bg;
        }
        return shouldInitPaint;
    }

    /**
     * 获取当前对应字体文件大小
     * @return String  xxMB
     */
    public String getFontSize(){
        String fontSize = null;
        String fontName = (String) getTag();
        File file = new File(Const.FONT_PATH+ File.separator+"font.properties");
        if(file.exists()) {
            fontSize = CommonUtils.getFontConfigProperties(file, context).getProperty(fontName);
        }else{
            CommonUtils.copyFiles2Assets(context,"fonts/font.properties",Const.FONT_PATH);
            getFontSize();
        }
        if(fontSize==null||fontSize.isEmpty()){
            fontSize="3M";
        }
        return fontSize;
    }

    /**
     * 获取当前位置上的字体对象,如果本地存在就返回，否则返回Null
     * @return 字体对象
     */
    public Typeface getFontType(){
        String fontName =  (String) getTag();
        if(fontName.equals("default")){
            return Typeface.create(Typeface.DEFAULT,Typeface.NORMAL);
        }
        File file = new File(Const.FONT_PATH+ File.separator+fontName);
        if(file.exists()) {
            Typeface typeface= null;
            try {
                typeface = Typeface.createFromFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return typeface;
        }else{
            return null;
        }
    }


    /**
     * 下载字体到本地
     */
    public void downloadFont(OnDownloadCompleteListener downloadCompleteListener) {
        this.downloadCompleteListener = downloadCompleteListener;
        Intent intent = new Intent();
        String fontName = ((String) getTag()).replace("ttf","zip");
        intent.putExtra("fontName",fontName);
        intent.setAction(Const.INTENT_ACTION_FONT_DOWNLOAD);
        String name = context.getPackageName();
        intent.setPackage(context.getPackageName());
        context.startService(intent);
        registerReceiver();
    }

    private void registerReceiver() {
        //注册广播
        receiver = new ProgressReceiver();
        IntentFilter filter = new IntentFilter("com.anybeen.mark.app.download_progress");
        context.registerReceiver(receiver, filter);
        isReceiverRegisted = true;
    }
    private void unRegisterReceiver() {
        //注销广播
        if(receiver!=null&&isReceiverRegisted) {
            context.unregisterReceiver(receiver);
            isReceiverRegisted = false;
        }
    }

    private boolean isDownloading = false;
    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOAD_FAILED:
                    isDownloading = false;
                    Toast.makeText(context, "下载失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOAD_OK:
                    mProgress = -1;
                    unRegisterReceiver();
                    isDownloading = false;
                    doZipExtractorWork(downloadCompleteListener);
                    break;
                case DOWNLOADING:
                    invalidate();
                    isDownloading = true;
                    break;
            }
        }
    };

    public void doZipExtractorWork(OnDownloadCompleteListener downloadCompleteListener){
        String fontName = ((String) getTag()).replace("ttf","zip");
        String pathIn = Const.FONT_PATH + File.separator + fontName;
        String pathOut = Const.FONT_PATH + File.separator;
        ZipExtractorTask task = new ZipExtractorTask(pathIn, pathOut, context, true,downloadCompleteListener);
        task.execute();
    }

    public boolean isDownloading(){
        return isDownloading;
    }

    private static final int DOWNLOAD_OK = 221;
    private static final int DOWNLOAD_FAILED = 222;
    private static final int DOWNLOADING =223 ;
    private int ringWidth = 10;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRingRadius = (getMeasuredWidth()/2)*3/5;
        ringWidth = (getMeasuredWidth()/2)/7;
    }
    private int mProgress = -1;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mRingPaint!=null&&mProgress>=0){
            int mXCenter = getWidth() / 2;
            int mYCenter = getHeight() / 2;
            oval.left = mXCenter - mRingRadius-ringWidth;
            oval.top = mYCenter - mRingRadius-ringWidth;
            oval.right = mXCenter + mRingRadius+ringWidth;
            oval.bottom = mYCenter + mRingRadius+ringWidth;
            mRingPaint.setStrokeWidth(ringWidth);
            mRingPaintBg.setStrokeWidth(ringWidth);
            canvas.drawArc(oval, -90, 360, false, mRingPaintBg);
            canvas.drawArc(oval, -90, ((float)mProgress / 100) * 360, false, mRingPaint);
        }
    }

    public class ProgressReceiver extends BroadcastReceiver{
        public ProgressReceiver(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            if (handler != null) {
                if (intent.getStringExtra("tag").equals((String) getTag())) {
                    //更新进度
                    int progress = intent.getIntExtra("progress", -3);
                    if (progress == -2) { //下载出错
                        handler.sendEmptyMessageDelayed(DOWNLOAD_FAILED, 100);
                    } else if (progress == 100) {
                        handler.sendEmptyMessageDelayed(DOWNLOAD_OK, 100);
                    } else if(progress>0){
                        mProgress = progress;
                        handler.sendEmptyMessageDelayed(DOWNLOADING, 5);
                        LogUtil.e("wjk", "当前下载进度：" + mProgress);
                    }
                } else {
                    mProgress = 0;
                    handler.sendEmptyMessageDelayed(DOWNLOADING, 5);
                }
            }
        }
    }

    public interface OnDownloadCompleteListener{
         void onDownloadComplete();
    }
}
