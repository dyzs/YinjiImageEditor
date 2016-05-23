package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * 表情贴纸
 */
public class StickerForCard extends ImageView {
    private Bitmap mBitmap;
    //置顶
    private Paint localPaint;
    private int mScreenWidth, mScreenHeight;
    private PointF mid = new PointF();
    private OperationListener operationListener;

    private Matrix matrix = new Matrix();
    /**
     * 是否在四条线内部
     */
    private boolean isInSide;

    private float lastX, lastY;
    /**
     * 是否在编辑模式
     */
    private boolean isInEdit = true;

    private final long stickerId;

    private DisplayMetrics dm;


    private Rect mBitmapRect = new Rect();
    public StickerForCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        stickerId = 0;
        init();
    }

    public StickerForCard(Context context) {
        super(context);
        stickerId = 0;
        init();
    }

    public StickerForCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        stickerId = 0;
        init();
    }

    private void init() {
        localPaint = new Paint();
        localPaint.setColor(Color.RED);
        localPaint.setAntiAlias(true);
        localPaint.setDither(true);
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(1.0f);

        localPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // 绘制长度1的实线,再绘制长度2的空白,再绘制长度4的实线,再绘制长度8的空白,依次重复,最后一位是起始位置的偏移量。
        localPaint.setPathEffect(new DashPathEffect(new float[]{8, 5, 8, 5}, 3));
        dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
    }
    // f1:scale, f5 sy,  f3 : translateX, f6 : translateY,
    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            float[] arrayOfFloat = new float[9];
            matrix.getValues(arrayOfFloat);
            float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
            float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
            float f5 = 0.0F * arrayOfFloat[0] + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f6 = 0.0F * arrayOfFloat[3] + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            float f7 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
            float f8 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
            mBitmapRect.set((int) f1, (int) f2, (int) f7, (int) f8);
            // System.out.println("rect:" + mBitmapRect.left + "/" + mBitmapRect.top);
            canvas.save();
            canvas.drawBitmap(mBitmap, matrix, null);

            if (false) {// isInEdit
                Path dottedLinePath = new Path();
                dottedLinePath.moveTo(f1, f2);
                dottedLinePath.lineTo(f3, f4);
                canvas.drawPath(dottedLinePath, localPaint);
                dottedLinePath.moveTo(f3, f4);
                dottedLinePath.lineTo(f7, f8);
                canvas.drawPath(dottedLinePath, localPaint);
                dottedLinePath.moveTo(f5, f6);
                dottedLinePath.lineTo(f7, f8);
                canvas.drawPath(dottedLinePath, localPaint);
                dottedLinePath.moveTo(f5, f6);
                dottedLinePath.lineTo(f1, f2);
                canvas.drawPath(dottedLinePath, localPaint);
            }
            canvas.restore();
        }
    }

    // maidou add-------------------------------------------------
    public Matrix saveMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        if (matrix != null) {
            this.matrix = matrix;
            invalidate();
        }
    }

    public void postMatrixScale(float sx, float sy) {
        matrix.postScale(sx, sy);
        invalidate();
    }

    public float[] saveMatrixFloatArray() {
        float[] floatArray = new float[9];
        matrix.getValues(floatArray);
        return floatArray;
    }

    // 重新载入时，把保存的浮点数组设置给矩阵再重绘
    public void reloadBitmapAfterOnDraw(float[] floatArray) {
        if (floatArray == null) return;
        matrix.setValues(floatArray);
        invalidate();
    }

    // 获取当前贴纸的 bitmap 对象
    public Bitmap getBitmap() {return mBitmap;}
    // maidou add end-----------------------------------------


    /**
     * @warning 原比例为除数2
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        invalidate();
    }

    private float clickX, clickY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean handled = true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInBitmap(event)) {
                    isInSide = true;
                    lastX = event.getX(0);
                    lastY = event.getY(0);
                    clickX = event.getX(0);
                    clickY = event.getY(0);
                } else {
                    handled = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isInSide) {
                    float x = event.getX(0);
                    float y = event.getY(0);
                    float dx = x - lastX;
                    float dy = y - lastY;
//                    centerPointF = new PointF();
//                    midDiagonalPoint(centerPointF);
//                    float centerPointX = centerPointF.x;
//                    float centerPointY = centerPointF.y;
//                    if (centerPointX < 0f) {
//                        dx = 1f;
//                    }
//                    if (centerPointY < 0f) {
//                        dy = 1f;
//                    }
//                    if (centerPointX > mWidth) {
//                        dx = -1f;
//                    }
//                    if (centerPointY > mHeight) {
//                        dy = -1f;
//                    }
                    matrix.postTranslate(dx, dy);
                    lastX = x;
                    lastY = y;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                System.out.println("init 左上右下：" + getLeft() + "/" + getTop() + "/" + getRight() + "/" + getBottom());

                // TODO 在 up 的时候设置图片超出左上右下后返回

                isInSide = false;
                float upX = event.getX(0);
                float upY = event.getY(0);
                System.out.println("upX & upY:" + upX + "/" + upY);
                // registered click event
                if (Math.abs(upX - clickX) <=10 && Math.abs(upY - clickY) <= 10) {
                    if (operationListener != null) {
                        operationListener.onClick(this);
//                        float[] arrayOfFloat = new float[9];
//                        matrix.getValues(arrayOfFloat);
//                        for (float f : arrayOfFloat) {
//                            System.out.println("f value:" + f);
//                        }
                    }
                }
                break;

        }
        if (handled && operationListener != null) {
            operationListener.onEdit(this);
        }
        return handled;
    }

    /**
     * 是否在四条线内部
     * 图片旋转后 可能存在菱形状态 不能用4个点的坐标范围去判断点击区域是否在图片内
     *
     * @return
     */
    private boolean isInBitmap(MotionEvent event) {
        float[] arrayOfFloat1 = new float[9];
        this.matrix.getValues(arrayOfFloat1);
        //左上角
        float f1 = 0.0F * arrayOfFloat1[0] + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f2 = 0.0F * arrayOfFloat1[3] + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //右上角
        float f3 = arrayOfFloat1[0] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[1] + arrayOfFloat1[2];
        float f4 = arrayOfFloat1[3] * this.mBitmap.getWidth() + 0.0F * arrayOfFloat1[4] + arrayOfFloat1[5];
        //左下角
        float f5 = 0.0F * arrayOfFloat1[0] + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f6 = 0.0F * arrayOfFloat1[3] + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];
        //右下角
        float f7 = arrayOfFloat1[0] * this.mBitmap.getWidth() + arrayOfFloat1[1] * this.mBitmap.getHeight() + arrayOfFloat1[2];
        float f8 = arrayOfFloat1[3] * this.mBitmap.getWidth() + arrayOfFloat1[4] * this.mBitmap.getHeight() + arrayOfFloat1[5];

        float[] arrayOfFloat2 = new float[4];
        float[] arrayOfFloat3 = new float[4];
        //确定X方向的范围
        arrayOfFloat2[0] = f1;//左上的x
        arrayOfFloat2[1] = f3;//右上的x
        arrayOfFloat2[2] = f7;//右下的x
        arrayOfFloat2[3] = f5;//左下的x
        //确定Y方向的范围
        arrayOfFloat3[0] = f2;//左上的y
        arrayOfFloat3[1] = f4;//右上的y
        arrayOfFloat3[2] = f8;//右下的y
        arrayOfFloat3[3] = f6;//左下的y
        return pointInRect(arrayOfFloat2, arrayOfFloat3, event.getX(0), event.getY(0));
    }

    /**
     * 判断点是否在一个矩形内部
     *
     * @param xRange
     * @param yRange
     * @param x
     * @param y
     * @return
     */
    private boolean pointInRect(float[] xRange, float[] yRange, float x, float y) {
        //四条边的长度
        double a1 = Math.hypot(xRange[0] - xRange[1], yRange[0] - yRange[1]);
        double a2 = Math.hypot(xRange[1] - xRange[2], yRange[1] - yRange[2]);
        double a3 = Math.hypot(xRange[3] - xRange[2], yRange[3] - yRange[2]);
        double a4 = Math.hypot(xRange[0] - xRange[3], yRange[0] - yRange[3]);
        //待检测点到四个点的距离
        double b1 = Math.hypot(x - xRange[0], y - yRange[0]);
        double b2 = Math.hypot(x - xRange[1], y - yRange[1]);
        double b3 = Math.hypot(x - xRange[2], y - yRange[2]);
        double b4 = Math.hypot(x - xRange[3], y - yRange[3]);

        double u1 = (a1 + b1 + b2) / 2;
        double u2 = (a2 + b2 + b3) / 2;
        double u3 = (a3 + b3 + b4) / 2;
        double u4 = (a4 + b4 + b1) / 2;

        //矩形的面积
        double s = a1 * a2;
        //海伦公式 计算4个三角形面积
        double ss = Math.sqrt(u1 * (u1 - a1) * (u1 - b1) * (u1 - b2))
                + Math.sqrt(u2 * (u2 - a2) * (u2 - b2) * (u2 - b3))
                + Math.sqrt(u3 * (u3 - a3) * (u3 - b3) * (u3 - b4))
                + Math.sqrt(u4 * (u4 - a4) * (u4 - b4) * (u4 - b1));
        return Math.abs(s - ss) < 0.5;


    }

    /**
     * 计算对角线交叉的位置
     *
     * @param paramPointF
     */
    private void midDiagonalPoint(PointF paramPointF) {
        float[] arrayOfFloat = new float[9];
        this.matrix.getValues(arrayOfFloat);
        float f1 = 0.0F * arrayOfFloat[0] + 0.0F * arrayOfFloat[1] + arrayOfFloat[2];
        float f2 = 0.0F * arrayOfFloat[3] + 0.0F * arrayOfFloat[4] + arrayOfFloat[5];
        float f3 = arrayOfFloat[0] * this.mBitmap.getWidth() + arrayOfFloat[1] * this.mBitmap.getHeight() + arrayOfFloat[2];
        float f4 = arrayOfFloat[3] * this.mBitmap.getWidth() + arrayOfFloat[4] * this.mBitmap.getHeight() + arrayOfFloat[5];
        float f5 = f1 + f3;
        float f6 = f2 + f4;
        paramPointF.set(f5 / 2.0F, f6 / 2.0F);
    }

    public interface OperationListener {
        void onEdit(StickerForCard StickerForCard);
        void onClick(StickerForCard StickerForCard);
//        void onClick(View v);
    }

    public void setOperationListener(OperationListener operationListener) {
        this.operationListener = operationListener;
    }

    public void setInEdit(boolean isInEdit) {
        this.isInEdit = isInEdit;
        invalidate();
    }



    // maidou add
    private int saveIndex;             // 保存贴纸的索引，reload使用
    private String saveNameCh;         // 保存中文名称
    private String saveNameEn;         // 保存英文名称或者拼音
    private String saveFileAbsPath;    // 表示可能存在贴纸从文件夹中读取
    private int saveResId;             // 资源 id  example:  R.id.pic_icon

    public int getSaveIndex() {
        return saveIndex;
    }

    public void setSaveIndex(int saveIndex) {
        this.saveIndex = saveIndex;
    }

    public String getSaveNameCh() {
        return saveNameCh;
    }

    public void setSaveNameCh(String saveNameCh) {
        this.saveNameCh = saveNameCh;
    }

    public String getSaveNameEn() {
        return saveNameEn;
    }

    public void setSaveNameEn(String saveNameEn) {
        this.saveNameEn = saveNameEn;
    }

    public String getSaveFileAbsPath() {
        return saveFileAbsPath;
    }

    public void setSaveFileAbsPath(String saveFileAbsPath) {
        this.saveFileAbsPath = saveFileAbsPath;
    }

    public int getSaveResId() {
        return saveResId;
    }

    public void setSaveResId(int saveResId) {
        this.saveResId = saveResId;
    }

    private PointF centerPointF;

    private int parentWidth = 0, parentHeight = 0;
    private float mWidth, mHeight;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        FrameLayout parent = (FrameLayout) this.getParent();
        parentWidth = parent.getWidth();
        parentHeight = parent.getHeight();
        mWidth = (float) getMeasuredWidth();
        mHeight = (float) getMeasuredHeight();
    }

    public Rect getRectSquare() {
        Rect rect = new Rect();
        rect.set(getLeft(), getTop(), getRight(), getBottom());
        return rect;
    }
}
