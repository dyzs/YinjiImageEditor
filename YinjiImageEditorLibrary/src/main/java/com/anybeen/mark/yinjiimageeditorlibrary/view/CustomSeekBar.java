package com.anybeen.mark.yinjiimageeditorlibrary.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;


import com.anybeen.mark.yinjiimageeditorlibrary.R;
import com.anybeen.mark.yinjiimageeditorlibrary.entity.ProgressItem;
import com.anybeen.mark.yinjiimageeditorlibrary.utils.DensityUtils;

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
	private Drawable dot;
	private boolean firstCalc = true;
	private int dotWidth;
	private int dotHeight;
	private int SplitDot;

	public void initData(ArrayList<ProgressItem> progressItemsList) {
		this.mProgressItemsList = progressItemsList;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (firstCalc) {
			dot = getThumb();
			BitmapDrawable bd = (BitmapDrawable) dot;
			Bitmap bitmap = bd.getBitmap();
			dotWidth = bitmap.getWidth();
			dotHeight = bitmap.getHeight();
			System.out.println("---->" + bitmap.getWidth() + "-->" + bitmap.getHeight());
			firstCalc = !firstCalc;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mProgressItemsList.size() > 0) {
			int progressBarWidth = getWidth();
			int progressBarHeight = getHeight();
			int thumbOffset = getThumbOffset();
			int lastProgressX = dotWidth / 2;	// 0
			Rect rect = new Rect();
			Paint rectPaint = new Paint();
			rectPaint.setStyle(Paint.Style.STROKE);
			rectPaint.setColor(Color.BLACK);
			rectPaint.setTextSize(1);
			rect.set(lastProgressX,
					thumbOffset / 2,
					progressBarWidth - dotWidth / 2 + 1,
					progressBarHeight - thumbOffset / 2 + 1
			);
			// 绘制矩形边框
			canvas.drawRect(rect, rectPaint);

			// 绘制两边圆形
//			float circleRadius = 15f;
//			Paint circlePaint = new Paint();
//			circlePaint.setColor(Color.RED);
//			circlePaint.setTextSize(1);
//			float circlePointX = lastProgressX + (progressBarHeight - thumbOffset + 1) / 2;
//			float circlePointY = progressBarHeight / 2;
//			canvas.drawCircle(circlePointX, circlePointY, circleRadius, circlePaint);


			int progressItemWidth, progressItemRight;
			SplitDot = dotWidth / mProgressItemsList.size();
			for (int i = 0; i < mProgressItemsList.size(); i++) {
				ProgressItem progressItem = mProgressItemsList.get(i);
				Paint progressPaint = new Paint();
				progressPaint.setColor(getResources().getColor(
						progressItem.color));

				progressItemWidth = (int) (progressItem.progressItemPercentage
						* progressBarWidth / 100) - SplitDot;

				progressItemRight = lastProgressX + progressItemWidth;

				// for last item give right to progress item to the width
				if (i == mProgressItemsList.size() - 1
						&& progressItemRight != progressBarWidth) {
					progressItemRight = progressBarWidth;
				}
				Rect progressRect = new Rect();
				if (i == 8) {
					progressRect.set(lastProgressX,
							thumbOffset / 2,
							progressItemRight - dotWidth / 2,
							progressBarHeight - thumbOffset / 2);
				} else {
					progressRect.set(lastProgressX,
							thumbOffset / 2,
							progressItemRight,
							progressBarHeight - thumbOffset / 2);
				}
				canvas.drawRect(progressRect, progressPaint);
				lastProgressX = progressItemRight;
			}
			super.onDraw(canvas);
		}

	}

}
