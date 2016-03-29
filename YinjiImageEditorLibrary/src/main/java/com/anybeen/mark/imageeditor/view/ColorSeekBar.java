package com.anybeen.mark.imageeditor.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.anybeen.mark.yinjiimageeditorlibrary.R;

//0xFF000000,0xFF9900FF, 0xFF0000FF, 0xFF00FF00,
//		0xFF00FFFF, 0xFFFF0000,0xFFFF00FF,0xFFFF6600, 0xFFFFFF00, 0xFFFFFFFF,0xFF000000

public class ColorSeekBar extends View {
	private int mBackgroundColor = 0xffffffff;
	private int[] mColors = new int[] {0xFF000000, 0xFFF44336, 0xFF9C27B0, 0xFF3F51B5,
			0xFF009688, 0xFFFFEB3B, 0xFFFF9800, 0xFF795548, 0xFF607D8B, 0xFF607D8B};
	private int c0,c1, mAlpha, mRed, mGreen, mBlue;
	private float x,y;
    private OnColorChangeListener mOnColorChangeLister;
	private Context mContext;
	private boolean mIsShowAlphaBar = false;
	private Bitmap mTransparentBitmap;
	private boolean mMovingColorBar;
	private boolean mMovingAlphaBar;
	private Rect mColorRect;
	private int mThumbHeight = 20;
	private int mBarHeight = 2;
	private LinearGradient mColorGradient;
	private Paint mColorRectPaint;
	private int realLeft;
	private int realRight;
	private int realTop;
	private int realBottom;
	private int mBarWidth;
	private int mMaxValue;
	private Rect mAlphaRect;
	private int mColorBarValue;
	private int mAlphaBarValue;
	private float mThumbRadius;
	private int mBarMargin = 5;
	private int mPaddingSize;
	private int mViewWidth;
	private int mViewHeight;
	public ColorSeekBar(Context context) {
		super(context);
		init(context, null, 0, 0);
	}

	public ColorSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0, 0);
	}

	public ColorSeekBar(Context context, AttributeSet attrs, int defStyleAttr){
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr, 0);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ColorSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs, defStyleAttr, defStyleRes);
	}

	protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		applyStyle(context, attrs, defStyleAttr, defStyleRes);
	}

	public void applyStyle(int resId){
		applyStyle(getContext(), null, 0, resId);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewWidth = widthMeasureSpec;
		mViewHeight = heightMeasureSpec;

		int speMode = MeasureSpec.getMode(heightMeasureSpec);
		if(speMode == MeasureSpec.AT_MOST){
			if(mIsShowAlphaBar){
				setMeasuredDimension(mViewWidth,mThumbHeight * 2 +  mBarHeight * 2  + mBarMargin);
			}else{
				setMeasuredDimension(mViewWidth,mThumbHeight + mBarHeight);
			}
		}
	}



	protected void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
		mContext = context;
		//get attributes
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorSeekBar, defStyleAttr, defStyleRes);
		int colorsId = a.getResourceId(R.styleable.ColorSeekBar_colors, 0);
		mMaxValue = a.getInteger(R.styleable.ColorSeekBar_maxValue,100);
		mColorBarValue = a.getInteger(R.styleable.ColorSeekBar_colorBarValue,0);
		mAlphaBarValue = a.getInteger(R.styleable.ColorSeekBar_alphaBarValue,0);
		mIsShowAlphaBar = a.getBoolean(R.styleable.ColorSeekBar_showAlphaBar, false);
		mBackgroundColor = a.getColor(R.styleable.ColorSeekBar_bgColor, Color.TRANSPARENT);
		mBarHeight = (int)a.getDimension(R.styleable.ColorSeekBar_barHeight,(float)dp2px(4));
		mThumbHeight = (int)a.getDimension(R.styleable.ColorSeekBar_thumbHeight,(float)dp2px(30));
		mBarMargin = (int)a.getDimension(R.styleable.ColorSeekBar_barMargin,(float)dp2px(0));
		a.recycle();

		if(colorsId != 0) mColors = getColorsById(colorsId);

		setBackgroundColor(mBackgroundColor);

		init();

		//init color
		pickColor(mColorBarValue);
		setAlphaValue();

	}

	/**
	 *
	 * @param id
	 * @return
     */
	private int[] getColorsById(int id){
		if(isInEditMode()){
			String[] s=mContext.getResources().getStringArray(id);
			int[] colors = new int[s.length];
			for (int j = 0; j < s.length; j++){
				colors[j] = Color.parseColor(s[j]);
			}
			return colors;
		}else{
			TypedArray typedArray=mContext.getResources().obtainTypedArray(id);
			int[] colors = new int[typedArray.length()];
			for (int j = 0; j < typedArray.length(); j++){
				colors[j] = typedArray.getColor(j,Color.BLACK);
			}
			typedArray.recycle();
			return colors;
		}
	}

	private void init() {
		//init l r t b
		realLeft = getPaddingLeft() + mPaddingSize;
		realRight = getWidth() - getPaddingRight() - mPaddingSize;
		realTop = getPaddingTop() + mPaddingSize;
		realBottom = getHeight() - getPaddingBottom() - mPaddingSize;

		//init size
		mThumbRadius = mThumbHeight / 2;
		mPaddingSize = (int)mThumbRadius;
		mBarWidth = realRight - realLeft;

		//init rect
		mColorRect = new Rect(realLeft ,realTop,realRight,realTop + mBarHeight);

		// 创建LinearGradient并设置渐变颜色数组
		// 第一个,第二个参数表示渐变起点 可以设置起点终点在对角等任意位置
		// 第三个,第四个参数表示渐变终点
		// 第五个参数表示渐变颜色
		// 第六个参数可以为空,表示坐标,值为0-1 new float[] {0.25f, 0.5f, 0.75f, 1 }
		// 如果这是 null 空的，颜色均匀分布，沿梯度线。
		// 第七个表示平铺方式
		// CLAMP重复最后一个颜色至最后
		// MIRROR重复着色的图像水平或垂直方向已镜像方式填充会有翻转效果
		// REPEAT重复着色的图像水平或垂直方向
		//init paint
		mColorGradient = new LinearGradient(0, 0, mColorRect.width(), 0, mColors, null, Shader.TileMode.MIRROR);
		mColorRectPaint = new Paint();
		mColorRectPaint.setShader(mColorGradient);
		mColorRectPaint.setAntiAlias(true);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTransparentBitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_4444);
		mTransparentBitmap.eraseColor(Color.TRANSPARENT);
	}



	public ColorSeekBar(Context context, float x1, float x2) {
		super(context);

	}
	@Override
	protected void onDraw(Canvas canvas) {
		init();
		float colorPosition = (float) mColorBarValue / mMaxValue * mBarWidth;

		Paint colorPaint = new Paint();
		colorPaint.setAntiAlias(true);
		colorPaint.setColor(pickColor(colorPosition));
		int[] toAlpha=new int[]{Color.argb(255, mRed, mGreen, mBlue),Color.argb(0, mRed, mGreen, mBlue)};
		//clear
		canvas.drawBitmap(mTransparentBitmap,0,0,null);

		//draw color bar
		canvas.drawRect(mColorRect, mColorRectPaint);

		//draw color bar thumb
		float thumbX = colorPosition + realLeft;
		float thumbY = mColorRect.top + mColorRect.height() / 2;
		canvas.drawCircle(thumbX,thumbY , mBarHeight / 2 + 5, colorPaint);

		//draw color bar thumb radial gradient shader
		RadialGradient thumbShader  = new RadialGradient(thumbX,  thumbY,  mThumbRadius, toAlpha, null, Shader.TileMode.MIRROR);
		Paint thumbGradientPaint = new Paint();
		thumbGradientPaint.setAntiAlias(true);
		thumbGradientPaint.setShader(thumbShader);
		canvas.drawCircle(thumbX, thumbY, mThumbHeight / 2, thumbGradientPaint);

		if(mIsShowAlphaBar){

			//init rect
			int top = (int)(mThumbHeight+ mThumbRadius + mBarHeight + mBarMargin);
			mAlphaRect = new Rect(realLeft,top,realRight,top + mBarHeight);

			//draw alpha bar
			Paint alphaBarPaint = new Paint();
			alphaBarPaint.setAntiAlias(true);
			LinearGradient alphaBarShader = new  LinearGradient(0, 0, mAlphaRect.width(), 0, toAlpha, null, Shader.TileMode.MIRROR);
			alphaBarPaint.setShader(alphaBarShader);
			canvas.drawRect(mAlphaRect,alphaBarPaint);

			//draw alpha bar thumb
			float alphaPosition = (float) mAlphaBarValue / 255 * mBarWidth;
			float alphaThumbX = alphaPosition + realLeft;
			float alphaThumbY = mAlphaRect.top + mAlphaRect.height() / 2;
			canvas.drawCircle(alphaThumbX, alphaThumbY, mBarHeight / 2 + 5, colorPaint);

			//draw alpha bar thumb radial gradient shader
			RadialGradient alphaThumbShader  = new RadialGradient(alphaThumbX,  alphaThumbY,  mThumbRadius, toAlpha, null, Shader.TileMode.MIRROR);
			Paint alphaThumbGradientPaint = new Paint();
			alphaThumbGradientPaint.setAntiAlias(true);
			alphaThumbGradientPaint.setShader(alphaThumbShader);
			canvas.drawCircle(alphaThumbX,alphaThumbY, mThumbHeight/2, alphaThumbGradientPaint);
		}

		super.onDraw(canvas);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		x = event.getX();
		y = event.getY();
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(isOnBar(mColorRect, x, y)){
				mMovingColorBar = true;
			}else if(mIsShowAlphaBar){
				if(isOnBar(mAlphaRect, x, y)){
					mMovingAlphaBar = true;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			 if(mMovingColorBar){
				 float value = (x - realLeft) / mBarWidth * mMaxValue;
				 mColorBarValue =(int)value ;
				 if (mColorBarValue < 0) mColorBarValue = 0;
				 if (mColorBarValue > mMaxValue) mColorBarValue = mMaxValue;
			 }else if(mIsShowAlphaBar){
				 if(mMovingAlphaBar){
					 float value = (x - realLeft) / mBarWidth * 255;
					 mAlphaBarValue = (int)value;
					 if (mAlphaBarValue < 0 ) mAlphaBarValue = 0;
					 if (mAlphaBarValue > 255 ) mAlphaBarValue = 255;
					 setAlphaValue();
				 }
			 }
            if(mOnColorChangeLister != null && (mMovingAlphaBar || mMovingColorBar))
				mOnColorChangeLister.onColorChangeListener(mColorBarValue, mAlphaBarValue,getColor());
            invalidate();
			 break;
		case MotionEvent.ACTION_UP:
			mMovingColorBar = false;
			mMovingAlphaBar = false;
			break;
		}
		return true;
	}


	/**
	 *
	 * @param r
	 * @param x
	 * @param y
	 * @return whether MotionEvent is performing on bar or not
	 */
	 private boolean isOnBar(Rect r, float x, float y){
		 if(r.left - mThumbRadius < x && x < r.right + mThumbRadius && r.top - mThumbRadius < y && y < r.bottom + mThumbRadius){
			 return true;
		 }else{
			 return false;
		 }
	 }

	/**
	 *
	 * @param position
	 * @return color
     */
	 private int pickColor(float position) {
		 float unit = position / mBarWidth;
         if (unit <= 0.0)
             return mColors[0];

         if (unit >= 1)
             return mColors[mColors.length - 1];

         float colorPosition = unit * (mColors.length - 1);
         int i = (int)colorPosition;
         colorPosition -= i;
         c0 = mColors[i];
         c1 = mColors[i+1];
//         mAlpha = mix(Color.alpha(c0), Color.alpha(c1), colorPosition);
         mRed = mix(Color.red(c0), Color.red(c1), colorPosition);
		 mGreen = mix(Color.green(c0), Color.green(c1), colorPosition);
		 mBlue = mix(Color.blue(c0), Color.blue(c1), colorPosition);
         return Color.rgb( mRed, mGreen, mBlue);
    }

	/**
	 *
	 * @param start
	 * @param end
	 * @param position　
     * @return
     */
	private int mix(int start, int end, float position) {
		return start + Math.round(position * (end - start));
	}
	 
	public int getColor() {
		if(mIsShowAlphaBar) return Color.argb(mAlpha, mRed, mGreen, mBlue);
		return Color.rgb( mRed, mGreen, mBlue);
    }


	public int getAlphaValue(){
		return mAlpha;
	}


    /**
     * @return color with alpha value
     */
    public int getColorWithAlpha(){
		return Color.argb(mAlpha, mRed, mGreen, mBlue);
	}

	
    public interface OnColorChangeListener {
        /**
         * @param colorBarValue between 0-maxValue
         * @param alphaValue between 0-255
         * @param color return the color contains alpha value whether showAlphaBar is true or without alpha value
         */
        public void onColorChangeListener(int colorBarValue, int alphaValue, int color);
    }

    /**
     * @param onColorChangeListener
     */
    public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener){
        this.mOnColorChangeLister = onColorChangeListener;
    }


	public int dp2px(float dpValue) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * Set colors by resource id. The resource's type must be color
	 * @param resId
     */
	public void setColors(int resId){
		setColors(getColorsById(resId));
	}

	public void setColors(int[] colors){
		mColors = colors;
		invalidate();
		setAlphaValue();
		if(mOnColorChangeLister != null)
			mOnColorChangeLister.onColorChangeListener(mColorBarValue, mAlphaBarValue,getColor());
	}

	public int[] getColors(){
		return mColors;
	}

	public boolean isShowAlphaBar(){
		return mIsShowAlphaBar;
	}

	private void refreshLayoutParams(){
		mThumbHeight = mThumbHeight < 2? 2:mThumbHeight;
		mBarHeight = mBarHeight < 2? 2:mBarHeight;

		int singleHeight = mThumbHeight + mBarHeight;
		int doubleHeight = mThumbHeight *2 + mBarHeight * 2 + mBarMargin;

		if(getLayoutParams().height == -2) {
			if(mIsShowAlphaBar){
				getLayoutParams().height = doubleHeight;
				setLayoutParams(getLayoutParams());
			}else{
				getLayoutParams().height = singleHeight ;
				setLayoutParams(getLayoutParams());
			}
		}else if (getLayoutParams().height >= 0){
			if(mIsShowAlphaBar){
				getLayoutParams().height = doubleHeight;
				setLayoutParams(getLayoutParams());
			}else{
				getLayoutParams().height = singleHeight;
				setLayoutParams(getLayoutParams());
			}
		}
	}

	public void setShowAlphaBar(boolean show){
		mIsShowAlphaBar = show;
		refreshLayoutParams();
		invalidate();
		if(mOnColorChangeLister != null)
			mOnColorChangeLister.onColorChangeListener(mColorBarValue, mAlphaBarValue,getColor());
	}

	/**
	 * @param dp
     */
	public void setBarHeight(float dp){
		mBarHeight = dp2px(dp);
		refreshLayoutParams();
		invalidate();
	}

	/**
	 * @param px
	 */
	public void setBarHeightPx(int px){
		mBarHeight = px;
		refreshLayoutParams();
		invalidate();
	}

	private void setAlphaValue(){
		mAlpha = 255 - mAlphaBarValue;
	}

	public void setAlphaBarValue(int value) {
		this.mAlphaBarValue = value;
		setAlphaValue();
		invalidate();
	}

	public int getMaxValue() {
		return mMaxValue;
	}

	public void setMaxValue(int value) {
		this.mMaxValue = value;
		invalidate();
	}

	/**
	 * set margin between bars
	 * @param mBarMargin
	 */
	public void setBarMargin(float mBarMargin) {
		this.mBarMargin = dp2px(mBarMargin);
		refreshLayoutParams();
		invalidate();
	}

	/**
	 * set margin between bars
	 * @param mBarMargin
	 */
	public void setBarMarginPx(int mBarMargin) {
		this.mBarMargin = mBarMargin;
		refreshLayoutParams();
		invalidate();
	}


	/**
	 * Set the value of color bar, if out of bounds , it will be 0 or maxValue;
	 * @param value
     */
	public void setColorBarValue(int value) {
		this.mColorBarValue = value;
		invalidate();
		if(mOnColorChangeLister != null)
			mOnColorChangeLister.onColorChangeListener(mColorBarValue, mAlphaBarValue,getColor());
	}

	/**
	 * set thumb's height by dpi
	 * @param dp
     */
	public void setThumbHeight(float dp) {
		this.mThumbHeight = dp2px(dp);
		refreshLayoutParams();
		invalidate();
	}

	/**
	 * set thumb's height by pixels
	 * @param px
	 */
	public void setThumbHeightPx(int px) {
		this.mThumbHeight = px;
		refreshLayoutParams();
		invalidate();
	}

	public int getBarHeight() {
		return mBarHeight;
	}

	public int getThumbHeight() {
		return mThumbHeight;
	}

	public int getBarMargin() {
		return mBarMargin;
	}

	public float getColorPosition() {
		return mColorBarValue;
	}


}