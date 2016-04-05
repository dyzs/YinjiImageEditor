package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by maidou on 2016/4/1.
 */
public class CornerImageView extends ImageView{
    private Path clipPath;
    private RectF rectf;
    private Paint paint;
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
        mColor = Color.BLACK;
        rectf = new RectF();
        clipPath = new Path();


    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        System.out.println("onDrawing...");
        int w = this.getWidth();
        int h = this.getHeight();
        rectf.set(0, 0, w, h);
//        paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(mColor);
//        canvas.drawCircle(
//                w / 2,
//                h / 2,
//                w / 2,
//                paint
//        );
//        canvas.restore();
        clipPath.addRoundRect(rectf, 15.0f, 15.0f, Path.Direction.CW);
        canvas.clipPath(clipPath);
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
