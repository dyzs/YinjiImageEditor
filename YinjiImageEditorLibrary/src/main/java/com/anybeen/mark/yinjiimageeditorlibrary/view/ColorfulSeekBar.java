package com.dyzs.yinjiimageeditorlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by maidou on 2016/3/22.
 */
public class ColorfulSeekBar extends View{
    private int mViewWidth;
    private int mViewHeight;
    private int left, top, right, bottom;
    private Rect seekBarRect;
    private Paint mColorsPaint;
    private int[] ColorValues = new int[]{0xFF0000, 0xFF7D00, 0xFFFF00, 0x00FF00, 0x0000FF,
            0x00FFFF, 0xFF00FF, 0x000000, 0xFFFFFF,};

    private Context mContext;

    public ColorfulSeekBar(Context context) {
        this(context, null);
    }

    public ColorfulSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfulSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        left = getPaddingLeft();
        top = getPaddingTop();
        right = getWidth() - getPaddingRight();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = widthMeasureSpec;
        mViewHeight = heightMeasureSpec;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {




        return super.onTouchEvent(event);
    }
}
