package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.anybeen.mark.yinjiimageeditorlibrary.R;

/**
 * Created by maidou on 2016/4/12.
 * TODO 未完成，圆心头像控件
 */
public class CircleImageViewDyzs extends View{
    private float mMeasureWidth, mMeasureHeight;
    private float mCircleRadius;    // 圆角半径
    private PointF mRingPointF;     // 圆环


    private Path mClipPath;         // 裁剪区域
    private Paint mPaint;           // 画笔对象
    private Bitmap mBitmap;         // 裁剪的 bitmap 对象

    public CircleImageViewDyzs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public CircleImageViewDyzs(Context context) {
        super(context);
        init();
    }
    public CircleImageViewDyzs(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        // init params
        mRingPointF = new PointF();
        mClipPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3.0f);
        mPaint.setColor(Color.MAGENTA);

        mCircleRadius = 5f; // 初始化时的值

        // load bitmap
        loadBitmap();
    }
    private void loadBitmap() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_icon_filter_sample);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = this.getMeasuredWidth();
        mMeasureHeight = this.getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCircleRadius = mMeasureHeight / 2;
        mRingPointF.set(mCircleRadius, mCircleRadius);
        mClipPath.addCircle(mRingPointF.x, mRingPointF.y, mCircleRadius, Path.Direction.CW);
        canvas.clipPath(mClipPath);

        canvas.save();              // 保存缩放前的画布
        canvas.scale(3f, 3f);       // 画笔按比例缩放
        canvas.drawBitmap(mBitmap, 0f, 0f, null);
        canvas.restore();           // 还原画布，否则会出现全部缩放的效果

        // 裁剪完成后再画出 ring
        canvas.drawCircle(mRingPointF.x, mRingPointF.y, mCircleRadius, mPaint);
    }
}
