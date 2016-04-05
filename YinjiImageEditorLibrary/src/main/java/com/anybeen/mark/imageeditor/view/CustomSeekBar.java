package com.anybeen.mark.imageeditor.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;


import com.anybeen.mark.imageeditor.entity.ProgressItem;

import java.util.ArrayList;

public class CustomSeekBar extends SeekBar {

	private ArrayList<ProgressItem> mProgressItemsList;

	public CustomSeekBar(Context context) {
		super(context);
	}

	public CustomSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initData(ArrayList<ProgressItem> progressItemsList) {
		this.mProgressItemsList = progressItemsList;
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onDraw(Canvas canvas) {
		if (mProgressItemsList.size() > 0) {
			int progressBarWidth = getWidth();
			int progressBarHeight = getHeight();
			int thumbOffset = getMinimumHeight();//getThumbOffset();
			int lastProgressX = 0;

//			Paint rectPaint = new Paint();
//			rectPaint.setStyle(Paint.Style.STROKE);
//			rectPaint.setColor(Color.BLACK);
			// rectPaint.setTextSize(1);
//			rectPaint.setStrokeWidth(1.0f);
			// 绘制矩形边框
			// canvas.drawRect(rect, rectPaint);
			// 绘制两边圆形
			float circleRadius = thumbOffset / 2;
			Paint circlePaint = new Paint();
			circlePaint.setAntiAlias(true);
			circlePaint.setStyle(Paint.Style.FILL);
			circlePaint.setTextSize(1);
			circlePaint.setStrokeWidth(1.0f);
			float circlePointX = 0f, circlePointY = 0f;

			int progressItemWidth, progressItemRight;
			for (int i = 0; i < mProgressItemsList.size(); i++) {
				ProgressItem progressItem = mProgressItemsList.get(i);
				Paint progressPaint = new Paint();
				progressPaint.setColor(getResources().getColor(
						progressItem.color));
				circlePaint.setColor(getResources().getColor(	// 设置半圈的颜色
						progressItem.color));
				// 把宽度四舍五入，因为平分 item 后得到的是xx.xx的值，直接强转会丢失原有的宽度
				progressItemWidth = floatToInt (progressItem.progressItemPercentage
						* progressBarWidth / 100);

				progressItemRight = lastProgressX + progressItemWidth;

				// for last item give right to progress item to the width
				if (i == mProgressItemsList.size() - 1
						&& progressItemRight != progressBarWidth) {
					progressItemRight = progressBarWidth;
				}
				Rect progressRect = new Rect();
				int left = lastProgressX;
				int top = thumbOffset / 2 + 1;
				int right = progressItemRight;
				int bottom = progressBarHeight - thumbOffset / 2 - 1;
				if (i == 0) {
					progressRect.set(left + thumbOffset / 2,
							top,
							right,
							bottom);
					circlePointX = left + thumbOffset / 2;
					circlePointY = progressBarHeight / 2;
					canvas.drawCircle(circlePointX, circlePointY, circleRadius, circlePaint);
				}
				else if (i == (mProgressItemsList.size() - 1)) {
					progressRect.set(left,
							top,
							right - thumbOffset / 2,
							bottom);
					circlePointX = right - thumbOffset / 2;
					circlePointY = progressBarHeight / 2;
					canvas.drawCircle(circlePointX, circlePointY, circleRadius, circlePaint);
				}
				else {
					progressRect.set(left,
							top,
							right,
							bottom);
				}
				canvas.drawRect(progressRect, progressPaint);
				lastProgressX = progressItemRight;
			}
			super.onDraw(canvas);
		}

	}
	private int floatToInt(float f){
		int i = 0;
		if(f>0) //正数
			i = (int) ((f*10 + 5)/10);
		else if(f<0) //负数
			i = (int) ((f*10 - 5)/10);
		else i = 0;
		return i;
	}
}
