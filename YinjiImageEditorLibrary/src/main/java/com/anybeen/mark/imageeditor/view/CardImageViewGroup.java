package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.anybeen.mark.imageeditor.utils.ToastUtil;

import java.util.ArrayList;

/**
 * Created by maidou on 2016/4/22.
 *
 *
 * 获取四个图片的 rect, 移动完成后计算两张图片的 rect 重叠参数~
 * 重叠一定大小的时候就交换位置
 *
 *
 */
public class CardImageViewGroup extends FrameLayout{
    private static final String TAG = CardImageViewGroup.class.getSimpleName();
    private ImageView imageA, imageB, imageC, imageD;
    private ArrayList<ImageView> mImagesList;
    private ImageView mCurrentImage;

    // 定义四个模板的 Rect
    private Rect templateRectA, templateRectB, templateRectC, templateRectD;
    private ArrayList<Rect> mRectList;

    public CardImageViewGroup(Context context) {
        this(context, null);
    }

    public CardImageViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardImageViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    public void setStickerImages(ArrayList<ImageView> list) {
        this.mImagesList = list;
        invalidate();
    }

    public void setTemplateRect(ArrayList<Rect> rectList) {
        this.mRectList = rectList;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mImagesList = new ArrayList<>();
        if (mImagesList != null && mImagesList.size() > 0) {
            for(ImageView image : mImagesList) {
                addView(image);
            }
        }

        mRectList = new ArrayList<>();
        if (mRectList != null && mRectList.size() > 0) {
            templateRectA = mRectList.get(0);
            templateRectB = mRectList.get(1);
            templateRectC = mRectList.get(2);
            templateRectD = mRectList.get(3);
        }
    }

    private ImageView testStickerForCard;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        testStickerForCard = (ImageView) getChildAt(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        System.out.println("CardImageViewGroup left:" + left + "/top:" + top + "/right:" + right + "/bottom:" + bottom);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "dispatchTouchEvent ACTION_UP");
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onInterceptTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onInterceptTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onInterceptTouchEvent ACTION_UP");
                break;

            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Log.e(TAG, "requestDisallowInterceptTouchEvent ");
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    /**
     * 在 onTouch 中判断，当 up 的时候才获取事件并判断图片的 rect 与模板预设的 templateRect 重叠大小
     */
    private boolean youCanMoveThisImage = false;
    private int actDownX, actDownY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("act_down");
                actDownX = (int) event.getX(0);
                actDownY = (int) event.getY(0);
                if (isInTouch(event)) {
                    System.out.println("act_down_true");
                    youCanMoveThisImage = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("图片移动ing" + youCanMoveThisImage);
                if (youCanMoveThisImage) {
                    System.out.println("图片移动中");
                    int actMoveX = (int) event.getX(0);
                    int actMoveY = (int) event.getY(0);
                    int actOffsetX = actMoveX - actDownX;
                    int actOffsetY = actMoveY - actDownY;
                    int l, t, r, b;
                    l = mCurrImageRect.left + actOffsetX;
                    t = mCurrImageRect.top + actOffsetY;
                    r = mCurrImageRect.right + actOffsetX;
                    b = mCurrImageRect.bottom + actOffsetY;
                    mCurrentImage.layout(l, t, r, b);
                    actDownX = actMoveX;
                    actDownY = actMoveY;
                }
                break;
            case MotionEvent.ACTION_UP:
                youCanMoveThisImage = false;
                break;
            case MotionEvent.ACTION_CANCEL:
        }
        return super.onTouchEvent(event);
    }


    private Rect mCurrImageRect = new Rect();
    private boolean isInTouch(MotionEvent event) {
//        Rect tempRectA = ((ImageView) getChildAt(0)).getRectSquare();
//        tempRectA.union(templateRectA);
        mCurrentImage = (ImageView) getChildAt(0);
        mCurrImageRect.set(
                mCurrentImage.getLeft(),
                mCurrentImage.getTop(),
                mCurrentImage.getRight(),
                mCurrentImage.getBottom());
        float downX = event.getX();
        float downY = event.getY();
        boolean isInTouch = downX >= mCurrImageRect.left && downY >= mCurrImageRect.top &&
                downX <= mCurrImageRect.right && downY <= mCurrImageRect.bottom;
        return isInTouch;
    }


//    ImageView iv_test = new ImageView(mContext);
//    FrameLayout.LayoutParams llp = new FrameLayout.LayoutParams(
//            FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//
//    llp.leftMargin = CommonUtils.floatToInt(info.left * mScale + mLeaveLeft);
//    llp.topMargin = CommonUtils.floatToInt(info.top * mScale + mLeaveTop);
//    llp.rightMargin = CommonUtils.floatToInt(
//            rl_bitmap_layout.getWidth() - ((info.left + bitmap.getWidth()) * mScale + mLeaveLeft)
//            );
//    llp.bottomMargin = CommonUtils.floatToInt(
//            rl_bitmap_layout.getHeight() - ((info.top + bitmap.getHeight()) * mScale + mLeaveTop)
//            );
//    llp.width = CommonUtils.floatToInt(bitmap.getWidth());
//    llp.height = CommonUtils.floatToInt(bitmap.getHeight());
//    iv_test.setLayoutParams(llp);
//    iv_test.setImageBitmap(bitmap);
//    test_card_image_view.addView(iv_test);

}
