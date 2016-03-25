package com.anybeen.mark.yinjiimageeditorlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.SeekBar;


import com.anybeen.mark.yinjiimageeditorlibrary.entity.ProgressItem;

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

	protected void onDraw(Canvas canvas) {
		if (mProgressItemsList.size() > 0) {
			int progressBarWidth = getWidth();
			int progressBarHeight = getHeight();
			int thumbOffset = getThumbOffset();
			int lastProgressX = 0;
			int progressItemWidth, progressItemRight;
			for (int i = 0; i < mProgressItemsList.size(); i++) {
				ProgressItem progressItem = mProgressItemsList.get(i);
				Paint progressPaint = new Paint();
				progressPaint.setColor(getResources().getColor(
						progressItem.color));

				progressItemWidth = (int) (progressItem.progressItemPercentage
						* progressBarWidth / 100);

				progressItemRight = lastProgressX + progressItemWidth;

				// for last item give right to progress item to the width
				if (i == mProgressItemsList.size() - 1
						&& progressItemRight != progressBarWidth) {
					progressItemRight = progressBarWidth;
				}
				Rect progressRect = new Rect();
				progressRect.set(lastProgressX, thumbOffset / 2,
						progressItemRight, progressBarHeight - thumbOffset / 2);
				canvas.drawRect(progressRect, progressPaint);
				lastProgressX = progressItemRight;
			}
			super.onDraw(canvas);
		}

	}

}
