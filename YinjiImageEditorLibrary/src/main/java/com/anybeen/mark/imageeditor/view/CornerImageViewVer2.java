package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.anybeen.mark.yinjiimageeditorlibrary.R;

/**
 * Created by maidou on 2016/4/1.
 * 可移动的ImageView
 */
public class CornerImageViewVer2 extends ImageView{
    private float mViewWidth, mViewHeight;
    private float mCircleRadius = 30f;          // 圆角半径

    private Path clipPath;
    private Paint paint;

    private Bitmap mBitmap;             // 背景图片
    private float mBitmapScale = 0.0f;  // bitmap 需要缩放的比例
    private int mPaintColor;            // 画笔颜色
    private int mPureColorBg = -1;      // 背景纯颜色

    /**
     *
     * @param context
     */
    public CornerImageViewVer2(Context context) {
        this(context, null);
    }
    public CornerImageViewVer2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CornerImageViewVer2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mBitmapScale = 0.0f;

        // 需要声明的 xml 属性
        mCircleRadius = 30f;
        mPureColorBg = -1;
        mPaintColor = Color.BLACK;

        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CornerImageView, 0, 0);
        mCircleRadius = ta.getDimension(R.styleable.CornerImageView_circleRadius, mCircleRadius);
        mPureColorBg = ta.getColor(R.styleable.CornerImageView_pureColor, mPureColorBg);
        mPaintColor = ta.getColor(R.styleable.CornerImageView_paintColor, mPaintColor);

        clipPath = new Path();


        // 两条直线绘制，用 path
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(mPaintColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth() * 1.0f;
        mViewHeight = getMeasuredHeight() * 1.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] mFloatPoints = new float[16];
        // A
        mFloatPoints[0] = 0f;                           // A pointX
        mFloatPoints[1] = mCircleRadius;                // A pointY
        // B
        mFloatPoints[2] = mCircleRadius;
        mFloatPoints[3] = 0f;
        // C
        mFloatPoints[4] = mViewWidth - mCircleRadius;
        mFloatPoints[5] = 0f;
        // D
        mFloatPoints[6] = mViewWidth;
        mFloatPoints[7] = mCircleRadius;
        // E
        mFloatPoints[8] = mViewWidth;
        mFloatPoints[9] = mViewHeight - mCircleRadius;
        // F
        mFloatPoints[10] = mViewWidth - mCircleRadius;
        mFloatPoints[11] = mViewHeight;
        // G
        mFloatPoints[12] = mCircleRadius;
        mFloatPoints[13] = mViewHeight;
        // H
        mFloatPoints[14] = 0f;
        mFloatPoints[15] = mViewHeight - mCircleRadius;

        clipPath.reset();
        clipPath.moveTo(mFloatPoints[0], mFloatPoints[1]);
        float controlPointX = 0f;
        float controlPointY = 0f;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[2], // 终点x
                mFloatPoints[3]  // 终点y
        );
        clipPath.lineTo(mFloatPoints[4], mFloatPoints[5]);
        controlPointX = mViewWidth;
        controlPointY = 0f;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[6], // 终点x
                mFloatPoints[7]  // 终点y
        );
        clipPath.lineTo(mFloatPoints[8], mFloatPoints[9]);
        controlPointX = mViewWidth;
        controlPointY = mViewHeight;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[10],// 终点x
                mFloatPoints[11] // 终点y
        );
        clipPath.lineTo(mFloatPoints[12], mFloatPoints[13]);
        controlPointX = 0f;
        controlPointY = mViewHeight;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[14],// 终点x
                mFloatPoints[15] // 终点y
        );
        clipPath.lineTo(mFloatPoints[0], mFloatPoints[1]);
        clipPath.close();
        clipPath.setFillType(Path.FillType.WINDING);
        canvas.clipPath(clipPath);
        // 如果设置了颜色，那么就取代背景图片
        if (mPureColorBg != -1) {
            // 设置颜色
            canvas.save();
            canvas.drawColor(mPureColorBg);
            canvas.restore();
        }
        else if (mBitmap != null) {
            canvas.save();                                  // 保存缩放前的画布
            // 利用留白区域，平移bitmap
            canvas.translate(bitmapLeaveWidth, bitmapLeaveHeight);
            canvas.drawBitmap(mBitmap, 0f, 0f, null);
            canvas.restore();                               // 还原画布，否则会出现全部缩放的效果
        }
        canvas.save();
        canvas.drawPath(clipPath, paint);
        canvas.restore();
        super.onDraw(canvas);
    }

    public int getPaintColor() {
        return mPaintColor;
    }

    public void setPaintColor(int color) {
        this.mPaintColor = color;
        invalidate();
    }

    public void setCornerRadius(float radius) {
        if (radius > Math.min(mViewWidth, mViewHeight) / 2) {
            this.mCircleRadius = mViewWidth / 3;
        }
        this.mCircleRadius  = radius;
    }

    public void setPureColor(int color) {
        this.mPureColorBg = color;
        invalidate();
    }

    /**
     * 添加 bitmap 对象，设置图片，计算图片缩放比例，计算图片偏移量
     * @param bitmap
     */
    public void setPictureBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        calcBitmapScale();
    }

    /**
     * 设置小图片
     * @param bitmap
     */
    public void setSmallBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        invalidate();
    }

    // 留白区域
    private float bitmapLeaveWidth = 0.0f, bitmapLeaveHeight = 0.0f;
    // 要加锁，计算完这个才 invalidate
    private void calcBitmapScale() {
        if (mBitmap != null) {
            float[] floats = calcScale(mBitmap);
            mBitmapScale = floats[0];
            bitmapLeaveWidth = floats[1];
            bitmapLeaveHeight = floats[2];
            mBitmap = bitmapScaleSelf(mBitmap);
        } else {
            mBitmapScale = 1.0f;
            bitmapLeaveWidth = 0.0f;
            bitmapLeaveHeight = 0.0f;
        }
        invalidate();
    }

    public float[] calcScale(Bitmap bitmap) {
//        float vWidth = this.getWidth() * 1.0f;
//        float vHeight = this.getHeight() * 1.0f;
        float vWidth = mViewWidth * 1.0f;
        float vHeight = mViewHeight * 1.0f;
        float bitWidth = bitmap.getWidth() * 1.0f;
        float bitHeight = bitmap.getHeight() * 1.0f;

        float scaleX = bitWidth / vWidth;
        float scaleY = bitHeight / vHeight;
        float scale = scaleX > scaleY ? scaleX:scaleY;
        float leaveW = 0.0f, leaveH = 0.0f;     // 留白区域
        if (scaleX > scaleY) {
            leaveH = (vHeight -  bitHeight / scale) / 2;
        } else {
            leaveW = (vWidth - bitWidth / scale) / 2;
        }
        return new float[]{scale, leaveW, leaveH};
    }

    public Bitmap bitmapScaleSelf(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(1 / mBitmapScale, 1 / mBitmapScale); //长和宽放大缩小的比例
        Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return temp;
    }

}
