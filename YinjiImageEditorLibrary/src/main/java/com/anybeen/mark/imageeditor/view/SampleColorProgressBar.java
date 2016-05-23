package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.anybeen.mark.imageeditor.utils.CommonUtils;

/**
 * Created by maidou on 2016/4/30.
 * 简单的进度条控件，包括设置进度条颜色
 * 利用 rect 在 canvas 上画出线
 * 利用 path 在 canvas 上画出线
 */
public class SampleColorProgressBar extends View{
    private Paint mPaint;
    private int mPaintColor;
    private int mTotalProgress;
    private int mProgress;
    private float mWidth;
    private float mHeight;
    private float mDrawLeft, mDrawTop, mDrawRight, mDrawBottom;
    @Deprecated
    private Rect mProgressRect;     // 扩展性没有 path 好
    private Path mPath;

    private float mCurrentWidth, mCurrentHeight;

    public SampleColorProgressBar(Context context) {
        this(context, null);
    }

    public SampleColorProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SampleColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaintColor = Color.MAGENTA;
        mTotalProgress = 100;
        mProgress = 50;

        mDrawLeft = 0f;
        mDrawTop = 0f;
        mDrawRight = 0f;
        mDrawBottom = 0f;


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2f);
        mPaint.setColor(mPaintColor);

        mPath = new Path();



    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(mWidth, 0);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        myDraw(canvas);
        super.onDraw(canvas);
    }

    private void myDraw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2f);
        paint.setColor(Color.BLACK);

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(mCurrentWidth * mProgress, 0);
        path.lineTo(mCurrentWidth * mProgress, mHeight);
        path.lineTo(0, mHeight);
        path.close();
        canvas.drawPath(path, paint);

    }

    public synchronized void setProgress(int progress) {
        if (progress > 100) {
            progress = 100;
        }
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() * 1.0f;
        mHeight = getMeasuredHeight() * 1.0f;
        mCurrentWidth = mWidth / 100;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    public int getmPaintColor() {
        return mPaintColor;
    }

    public void setmPaintColor(int mPaintColor) {
        this.mPaintColor = mPaintColor;
    }



}
