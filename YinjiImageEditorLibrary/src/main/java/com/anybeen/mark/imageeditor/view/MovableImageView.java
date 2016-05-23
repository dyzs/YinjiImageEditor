package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by maidou on 2016/5/4.
 */
public class MovableImageView extends ImageView{
    public MovableImageView(Context context) {
        this(context, null);
    }

    public MovableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        this.setClickable(true);
    }

    private boolean youCanMoveThisImage = false;
    private int actDownX, actDownY;
    private int translateX, translateY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                );
                actDownX = (int) event.getRawX();
                actDownY = (int) event.getRawY();
                System.out.println("按下了图片~~~");
                youCanMoveThisImage = true;
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("图片移动：" + youCanMoveThisImage);
                if (youCanMoveThisImage) {
                    int actMoveX = (int) event.getRawX();
                    int actMoveY = (int) event.getRawY();
                    int actOffsetX = actMoveX - actDownX;
                    int actOffsetY = actMoveY - actDownY;
                    int l = this.getLeft();
                    int t = this.getTop();
                    l += actOffsetX;
                    t += actOffsetY;
                    int r = l + this.getWidth();
                    int b = t + this.getHeight();
                    this.layout(l, t, r, b);
                    translateX += actOffsetX;
                    translateY += actOffsetY;
//                    this.setTranslationX(translateX);
//                    this.setTranslationY(translateY);
                    actDownX = actMoveX;
                    actDownY = actMoveY;
                    System.out.println("image left:" + this.getLeft() + "/" + this.getTop());
                }
                break;
            case MotionEvent.ACTION_UP:
                youCanMoveThisImage = false;
                break;
        }
        return super.onTouchEvent(event);
    }
}


// 使用~
//MovableImageView iv_test = new MovableImageView(mContext);
//
//FrameLayout.LayoutParams llp = new FrameLayout.LayoutParams(
//        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//
//llp.leftMargin = CommonUtils.floatToInt(info.left * mScale + mLeaveLeft);
//        llp.topMargin = CommonUtils.floatToInt(info.top * mScale + mLeaveTop);
//        llp.rightMargin = CommonUtils.floatToInt(
//        rl_bitmap_layout.getWidth() - ((info.left + bitmap.getWidth()) * mScale + mLeaveLeft)
//        );
//        llp.bottomMargin = CommonUtils.floatToInt(
//        rl_bitmap_layout.getHeight() - ((info.top + bitmap.getHeight()) * mScale + mLeaveTop)
//        );
//        llp.width = CommonUtils.floatToInt(bitmap.getWidth());
//        llp.height = CommonUtils.floatToInt(bitmap.getHeight());
//
//        iv_test.setLayoutParams(llp);
//        iv_test.setImageBitmap(bitmap);
//        test_card_image_view.addView(iv_test);
