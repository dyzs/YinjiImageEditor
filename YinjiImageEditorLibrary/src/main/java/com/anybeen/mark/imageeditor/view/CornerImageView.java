package com.anybeen.mark.imageeditor.view;

import android.content.Context;
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
 * 自定义圆角图片，使用 path + 贝塞尔曲线 + clip裁剪
 * 仅用于正方形图片~~
 *
 * TODO
 * 1、添加 xml 配置方式的半径
 * 2、半径的 get 和 set 方法，限制半径大小不超过当前控件宽度的一半
 *
 * @warning 可以再 xml 中配置 src 或者 bg，但是一定得是透明的，可以设置一个透明的有边框的 shape 资源文件
 *
 */
public class CornerImageView extends ImageView{
    private float mViewWidth, mViewHeight;
    private float mCircleRadius = 30f;          // 圆角半径

    private PointF LeftTopCirclePointF;         // 左上角圆心
    private PointF RightTopCirclePointF;        // 右上角圆心
    private PointF LeftBottomCirclePointF;      // 左下角圆心
    private PointF RightBottomCirclePointF;     // 右下角圆心


    private PointF A, B, C, D, E, F, G, H;

    private Path clipPath;
    private Path linePath;
    private RectF rectf;
    private Paint paint;

    private Bitmap mBitmap;         // 背景图片
    private float mBitmapScale = 0.0f;// bitmap 需要缩放的比例
    private int mPaintColor;        // 画笔颜色
    private int mPureColor = -1;    // 背景纯颜色

    /**
     *
     * @param context
     */
    public CornerImageView(Context context) {
        super(context);
        init();
    }
    public CornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CornerImageView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init(){
        // 获取图片资源文件
        // this.setBackgroundResource(R.mipmap.pic_icon_filter_sample);
        mPaintColor = Color.BLACK;
        rectf = new RectF();
        clipPath = new Path();
        linePath = new Path();

        // 初始化四个圆的圆心
        LeftTopCirclePointF = new PointF();
        RightTopCirclePointF = new PointF();
        LeftBottomCirclePointF = new PointF();
        RightBottomCirclePointF = new PointF();

        // this.setScaleType(ScaleType.FIT_XY);
//        loadBitmap();
    }

    private void loadBitmap() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_icon_filter_sample);
    }


    // canvas.drawBitmap(backgroundBitmap, Rect , Rect , paint);


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth() * 1.0f;
        mViewHeight = getMeasuredHeight() * 1.0f;
    }
    // Path.FillType.INVERSE_EVEN_ODD : 去除交集区域
    // INVERSE_WINDING                : 去除交集区域
    // Path.FillType.EVEN_ODD         : 获取交集区域
    // WINDING                        : 获取交集区域
    @Override
    protected void onDraw(Canvas canvas) {
//        System.out.println("view:" + mViewWidth + "/" + mViewHeight);
        // 四个圆心赋值
        LeftTopCirclePointF.set(mCircleRadius, mCircleRadius);
        RightTopCirclePointF.set(mViewWidth - mCircleRadius, mCircleRadius);
        LeftBottomCirclePointF.set(mCircleRadius, mViewHeight - mCircleRadius);
        RightBottomCirclePointF.set(mViewWidth - mCircleRadius, mViewHeight - mCircleRadius);

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
        // 两条直线绘制，用 path


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(mPaintColor);

        /**
        // 添加了一个圆形的裁剪区域
        clipPath.reset();
        clipPath.addCircle(RightTopCirclePointF.x, RightTopCirclePointF.y, mCircleRadius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        clipPath.reset();
        clipPath.setFillType(Path.FillType.WINDING);
        clipPath.addCircle(RightTopCirclePointF.x - 8f, RightTopCirclePointF.y, mCircleRadius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        */

        clipPath.reset();                       // 重置
        clipPath.moveTo(mFloatPoints[0], mFloatPoints[1]);     // 移动到 A 点开始，为起始点
        float controlPointX = 0f;               // A 点的操纵点 X
        float controlPointY = 0f;               // A 点的操纵点 Y
        // 绘制 A 点到 B 点的贝塞尔, 中心点为左上角
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[2], // 终点x
                mFloatPoints[3]  // 终点y
        );

        // 曲线到达B点，再lineTo到C
        clipPath.lineTo(mFloatPoints[4], mFloatPoints[5]);
        // 曲线达到 C 点，通过贝塞尔曲线，绘制到 D 点，中心点为右上角
        controlPointX = mViewWidth;
        controlPointY = 0f;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[6], // 终点x
                mFloatPoints[7]  // 终点y
        );
        // 曲线到达 D 点，再 lineTo 到 E
        clipPath.lineTo(mFloatPoints[8], mFloatPoints[9]);
        // 曲线达到 E 点，通过贝塞尔曲线，绘制到 F 点，中心点为右下角
        controlPointX = mViewWidth;
        controlPointY = mViewHeight;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[10],// 终点x
                mFloatPoints[11] // 终点y
        );
        // 曲线到达 F 点，再 lineTo 到 G
        clipPath.lineTo(mFloatPoints[12], mFloatPoints[13]);
        // 曲线达到 G 点，通过贝塞尔曲线，绘制到 H 点，中心点为左下角
        controlPointX = 0f;
        controlPointY = mViewHeight;
        clipPath.quadTo(
                controlPointX,   // 操纵点x
                controlPointY,   // 操纵点y
                mFloatPoints[14],// 终点x
                mFloatPoints[15] // 终点y
        );
        // 曲线到达 G 点，再 lineTo 到 A，完成一圈绘制
        clipPath.lineTo(mFloatPoints[0], mFloatPoints[1]);
        clipPath.close();
        clipPath.setFillType(Path.FillType.WINDING);
        canvas.clipPath(clipPath);
//        canvas.drawPath(clipPath, paint);


        // 如果设置了颜色，那么就取代背景图片
        if (mPureColor != -1) {
            // 设置颜色
            canvas.save();
            canvas.drawColor(mPureColor);
            canvas.restore();
        }
        else if (mBitmap != null) {
            // TODO 计算图片比例等数据，不能在 setBitmap 的时候处理计算

            // 最终结果必须在画 bitmap 之前确定曲线，然后再 clip
            canvas.save();                                  // 保存缩放前的画布
            // 利用留白区域，平移bitmap
            canvas.translate(bitmapLeaveWidth, bitmapLeaveHeight);
            canvas.drawBitmap(mBitmap, 0f, 0f, null);
            canvas.restore();                               // 还原画布，否则会出现全部缩放的效果
        }
        canvas.drawPath(clipPath, paint);
//        canvas.save();
//        int w = this.getWidth();
//        int h = this.getHeight();
//        rectf.set(0, 0, w, h);

        /**
         * 画出四个边上的圆
        canvas.drawCircle(LeftTopCirclePointF.x, LeftTopCirclePointF.y, mCircleRadius, paint);
        canvas.drawCircle(RightTopCirclePointF.x, RightTopCirclePointF.y, mCircleRadius, paint);
        canvas.drawCircle(LeftBottomCirclePointF.x, LeftBottomCirclePointF.y, mCircleRadius, paint);
        canvas.drawCircle(RightBottomCirclePointF.x, RightBottomCirclePointF.y, mCircleRadius, paint);
        */

        /**
        根据Path进行绘制，绘制五角星
        Path path2 = new Path();
        path2.moveTo(27, 360);
        path2.lineTo(54, 360);
        path2.lineTo(70, 392);
        path2.lineTo(40, 420);
        path2.lineTo(10, 392);
        path2.close();
        canvas.drawPath(path2, paint);
        */

        /**三角形
        Path path = new Path();
        path.moveTo(10, 330);
        path.lineTo(70, 330);
        path.lineTo(40, 270);
        path.close();
        canvas.drawPath(path, paint);
        */
        /**
         // A 移动到 B 的贝塞尔曲线，done
         Path BezierPath = new Path();
         BezierPath.moveTo(0, 100);
         // c,d为曲线的操作点，为两点的中点
         float centerPointX = 20;
         float centerPointY = 20;
         // 操纵点x，操纵点y，终点x，终点y
         BezierPath.quadTo(centerPointX, centerPointY, 150, 0);
         BezierPath.lineTo(150, 0);
         BezierPath.lineTo(200, 100);
         BezierPath.close();
         canvas.drawPath(BezierPath, paint);
         */
        /**
        // 重置 clipPath
        clipPath.reset();
        canvas.clipPath(clipPath);
        clipPath.addCircle(LeftTopCirclePointF.x, LeftTopCirclePointF.y, mCircleRadius, Path.Direction.CW);
        canvas.clipPath(clipPath, Region.Op.REPLACE);
        clipPath.reset();
        clipPath.setFillType(Path.FillType.WINDING);
        clipPath.addCircle(LeftBottomCirclePointF.x, LeftBottomCirclePointF.y, mCircleRadius, Path.Direction.CW);
        Paint paint=new Paint();
        canvas.scale(0.5f, 0.5f);
        canvas.clipRect(new Rect(0, 0, (int) mCircleRadius, (int) mCircleRadius));//裁剪区域实际大小为50*50
        canvas.drawColor(Color.RED);
        canvas.restore();
        canvas.drawRect(new Rect(0,0,100,100), paint);//矩形实际大小为50*50
        canvas.clipRegion(new Region(new Rect(300,300,400,400)));//裁剪区域实际大小为100*100
        canvas.drawColor(Color.BLACK);
        */
//        canvas.restore();
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
        this.mPureColor = color;
        invalidate();
    }

    /**
     * 添加 bitmap 对象，设置图片
     * @param bitmap
     */
    public void setPictureBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        calcBitmapScale();
    }

    /**
     * 只能设置小图片
     * @param bitmap
     */
    public void setSmallBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        invalidate();
    }

    /**
     * TODO 用绝对路径来设置bitmap
     * @param absPath
     */
    public void setPictureFilePath(String absPath) {

    }

    /**
     * 通过设置一个资源文件来设置背景图片
     * TODO bitmap 对象要处理优化加载
     * @param resId
     */
    public void setPictureResources(int resId) {
        mBitmap = BitmapFactory.decodeResource(getResources(), resId);
        calcBitmapScale();
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
