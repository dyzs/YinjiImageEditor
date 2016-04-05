package com.anybeen.mark.imageeditor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.anybeen.mark.yinjiimageeditorlibrary.R;
import com.anybeen.mark.imageeditor.utils.DensityUtils;

/**
 * Created by maidou on 2016/3/23.
 */
public class CarrotEditText extends EditText{
    private Drawable deleteIcon;
    private Context mContext;
    public CarrotEditText(Context context) {
        this(context, null);
    }

    public CarrotEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        // this.setSingleLine();
        this.setTextColor(Color.GRAY);
        this.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        this.setLines(1);
        deleteIcon = getResources().getDrawable(R.mipmap.icon_delete_image_editor);
        deleteIcon.setBounds(
                0,
                0,
                DensityUtils.dp2px(getContext(), DensityUtils.px2dp(getContext(), 50)),
                DensityUtils.dp2px(getContext(), DensityUtils.px2dp(getContext(), 50))
        );
        setCompoundDrawables(null, null, deleteIcon, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.setFocusable(true);
        boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (deleteIcon != null && event.getAction() == MotionEvent.ACTION_UP) {
                    if (touchable) {
                        if (getText().length() == 0) {
                            this.setText("");
                            break;
                        }
                        String text = getText().toString();
                        if (text != null) {
                            this.setText("");
                            // this.setText(text.substring(0, text.length() - 1));
//                            this.setClickable(true);
//                            this.setFocusable(true);
//                            this.requestFocus();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
