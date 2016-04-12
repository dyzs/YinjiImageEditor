package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
 */
public class CornerImageView extends ImageView{
    private float measureWidth, measureHeight;
    private float mCircleRadius = 60f;          // 圆角半径

    private PointF LeftTopCirclePointF;         // 左上角圆心
    private PointF RightTopCirclePointF;        // 右上角圆心
    private PointF LeftBottomCirclePointF;      // 左下角圆心
    private PointF RightBottomCirclePointF;     // 右下角圆心


    private PointF A, B, C, D, E, F, G, H;

    private Path clipPath;
    private Path linePath;
    private RectF rectf;
    private Paint paint;


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
        mColor = Color.RED;
        rectf = new RectF();
        clipPath = new Path();
        linePath = new Path();

        // 初始化四个圆的圆心
        LeftTopCirclePointF = new PointF();
        RightTopCirclePointF = new PointF();
        LeftBottomCirclePointF = new PointF();
        RightBottomCirclePointF = new PointF();


        loadBitmap();
    }
    private Bitmap bitmap;
    private void loadBitmap() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic_icon_filter_sample);
    }


    // canvas.drawBitmap(backgroundBitmap, Rect , Rect , paint);


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWidth = getMeasuredWidth() * 1.0f;
        measureHeight = getMeasuredHeight() * 1.0f;
    }
    // Path.FillType.INVERSE_EVEN_ODD : 去除交集区域
    // INVERSE_WINDING                : 去除交集区域
    // Path.FillType.EVEN_ODD         : 获取交集区域
    // WINDING                        : 获取交集区域
    @Override
    protected void onDraw(Canvas canvas) {
        System.out.println("onDrawing...");
        // canvas.clipRect(new RectF(0f, measureWidth - mCircleRadius * 2, mCircleRadius * 2, measureHeight));
        // 四个圆心赋值
        LeftTopCirclePointF.set(mCircleRadius, mCircleRadius);
        RightTopCirclePointF.set(measureWidth - mCircleRadius, mCircleRadius);
        LeftBottomCirclePointF.set(mCircleRadius, measureHeight - mCircleRadius);
        RightBottomCirclePointF.set(measureWidth - mCircleRadius, measureHeight - mCircleRadius);

        float[] mFloatPoints = new float[16];
        // A
        mFloatPoints[0] = 0f;                           // A pointX
        mFloatPoints[1] = mCircleRadius;                // A pointY
        // B
        mFloatPoints[2] = mCircleRadius;
        mFloatPoints[3] = 0f;
        // C
        mFloatPoints[4] = measureWidth - mCircleRadius;
        mFloatPoints[5] = 0f;
        // D
        mFloatPoints[6] = measureWidth;
        mFloatPoints[7] = mCircleRadius;
        // E
        mFloatPoints[8] = measureWidth;
        mFloatPoints[9] = measureHeight - mCircleRadius;
        // F
        mFloatPoints[10] = measureWidth - mCircleRadius;
        mFloatPoints[11] = measureHeight;
        // G
        mFloatPoints[12] = mCircleRadius;
        mFloatPoints[13] = measureHeight;
        // H
        mFloatPoints[14] = 0f;
        mFloatPoints[15] = measureHeight - mCircleRadius;
        // 两条直线绘制，用 path


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        paint.setColor(mColor);

//        // 添加了一个圆形的裁剪区域
//        clipPath.reset();
//        clipPath.addCircle(RightTopCirclePointF.x, RightTopCirclePointF.y, mCircleRadius, Path.Direction.CW);
//        canvas.clipPath(clipPath);
//
//        clipPath.reset();
//        clipPath.setFillType(Path.FillType.WINDING);
//        clipPath.addCircle(RightTopCirclePointF.x - 8f, RightTopCirclePointF.y, mCircleRadius, Path.Direction.CW);
//        canvas.clipPath(clipPath);


        clipPath.reset();                       // 重置
        clipPath.moveTo(0f, mCircleRadius);     // 移动到 A 点开始，为起始点
        float controlPointX = 0f;               // A 点的操纵点 X
        float controlPointY = 0f;               // A 点的操纵点 Y
        // 绘制 A 点到 B 点的贝塞尔, 中心点为左上角
        clipPath.quadTo(
                controlPointX,  // 操纵点x
                controlPointY,  // 操纵点y
                mCircleRadius,  // 终点x
                0f              // 终点y
        );

        // 曲线到达B点，再lineTo到C
        clipPath.lineTo(measureWidth - mCircleRadius, 0f);
        // 曲线达到 C 点，通过贝塞尔曲线，绘制到 D 点，中心点为右上角
        controlPointX = measureWidth;
        controlPointY = 0f;
        clipPath.quadTo(
                controlPointX,  // 操纵点x
                controlPointY,  // 操纵点y
                measureWidth,  // 终点x
                mCircleRadius  // 终点y
        );
        // 曲线到达 D 点，再 lineTo 到 E
        clipPath.lineTo(measureWidth, measureHeight - mCircleRadius);
        // 曲线达到 E 点，通过贝塞尔曲线，绘制到 F 点，中心点为右下角
        controlPointX = measureWidth;
        controlPointY = measureHeight;
        clipPath.quadTo(
                controlPointX,  // 操纵点x
                controlPointY,  // 操纵点y
                measureWidth - mCircleRadius,  // 终点x
                measureHeight  // 终点y
        );
        // 曲线到达 F 点，再 lineTo 到 G
        clipPath.lineTo(mCircleRadius, measureHeight);
        // 曲线达到 G 点，通过贝塞尔曲线，绘制到 H 点，中心点为左下角
        controlPointX = 0f;
        controlPointY = measureHeight;
        clipPath.quadTo(
                controlPointX,  // 操纵点x
                controlPointY,  // 操纵点y
                0f,  // 终点x
                measureHeight - mCircleRadius  // 终点y
        );
        // 曲线到达 G 点，再 lineTo 到 A，完成一圈绘制
        clipPath.lineTo(0f, mCircleRadius);
        clipPath.close();
        clipPath.setFillType(Path.FillType.WINDING);
        canvas.clipPath(clipPath);
//        canvas.drawPath(clipPath, paint);

        // 最终结果必须在画 bitmap 之前确定曲线，然后再 clip
        canvas.save();              // 保存缩放前的画布
        canvas.scale(10f, 10f);     // 画笔按比例缩放
        canvas.drawBitmap(bitmap, 0f, 0f, null);
        canvas.restore();           // 还原画布，否则会出现全部缩放的效果


        canvas.save();
        int w = this.getWidth();
        int h = this.getHeight();
        rectf.set(0, 0, w, h);

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
        // 重置 clipPath
//        clipPath.reset();
//        canvas.clipPath(clipPath);
//        clipPath.addCircle(LeftTopCirclePointF.x, LeftTopCirclePointF.y, mCircleRadius, Path.Direction.CW);
//        canvas.clipPath(clipPath, Region.Op.REPLACE);
//        clipPath.reset();
//        clipPath.setFillType(Path.FillType.WINDING);
//        clipPath.addCircle(LeftBottomCirclePointF.x, LeftBottomCirclePointF.y, mCircleRadius, Path.Direction.CW);
//        Paint paint=new Paint();
//        canvas.scale(0.5f, 0.5f);
//        canvas.clipRect(new Rect(0, 0, (int) mCircleRadius, (int) mCircleRadius));//裁剪区域实际大小为50*50
//        canvas.drawColor(Color.RED);
//        canvas.restore();
//        canvas.drawRect(new Rect(0,0,100,100), paint);//矩形实际大小为50*50
//        canvas.clipRegion(new Region(new Rect(300,300,400,400)));//裁剪区域实际大小为100*100
//        canvas.drawColor(Color.BLACK);
        canvas.restore();
        super.onDraw(canvas);
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int color) {
        this.mColor = color;
        invalidate();
    }

    private int mColor;

}
