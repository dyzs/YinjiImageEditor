package com.anybeen.mark.imageeditor.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;

import com.anybeen.mark.imageeditor.utils.CommonUtils;
import com.anybeen.mark.imageeditor.utils.Const;
import com.anybeen.mark.imageeditor.utils.ToastUtil;
import com.anybeen.mark.yinjiimageeditorlibrary.R;

/**
 * Created by maidou on 2016/3/22.
 * wait to do
 */
public class ColorfulSeekBar extends SeekBar{
    private int mViewWidth;
    private int mViewHeight;
    private int left, top, right, bottom;
    private Rect seekBarRect;
    private Paint mColorPaint;
    private int[] mColorValues = Const.COLOR_VALUES;
    private int mTotalColorCount = Const.COLOR_VALUES.length;

    private Rect thumbRect;
    private Bitmap thumbIcon;
    private int thumbWidth, thumbHeight;

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init() {
        left = getPaddingLeft();
        top = getPaddingTop();
        right = getWidth() - getPaddingRight();

        thumbRect = new Rect();
        thumbIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_seek_bar_thumb_image_editor);
        thumbWidth = thumbIcon.getWidth();
        thumbHeight = thumbIcon.getHeight();
        Drawable drawableThumb = getThumb();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = this.getMeasuredWidth();
        mViewHeight = this.getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    private boolean isThumbLimit(MotionEvent event, Rect rect) {
        int left = rect.left;
        int right = rect.right;
        int top = rect.top;
        int bottom = rect.bottom;
        return event.getX(0) >= left && event.getX(0) <= right && event.getY(0) >= top && event.getY(0) <= bottom;
    }
}
