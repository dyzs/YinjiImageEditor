package com.anybeen.mark.imageeditor.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.anybeen.mark.imageeditor.ImageEditorActivity;
import com.anybeen.mark.yinjiimageeditorlibrary.R;


public class QuickOptionDialog extends Dialog implements View.OnClickListener {
    private boolean isShowContentPanel;
    private ImageEditorActivity activity;
    private InputMethodManager imm; // 创建软件盘对象

    private MovableTextView2 mMtv;
    private OnQuickOptionformClick mListener;
    private int inputMode;
    public interface OnQuickOptionformClick {
        void onQuickOptionClick(int id);
    }

    public QuickOptionDialog(Context context, MovableTextView2 movableTextView) {
        this(context, R.style.quick_option_dialog);
        System.out.println("go constructors");
        this.activity = (ImageEditorActivity) context;
        this.mMtv = movableTextView;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private QuickOptionDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    private EditText et_input_text;
    private LinearLayout ll_edit_panel_content;
    private ImageView iv_mode;
    @SuppressLint("InflateParams")
    private QuickOptionDialog(Context context, int defStyle) {
        super(context, defStyle);
        System.out.println("QuickOptionDialog inflate layout");
        // 564px 转 dp
        isShowContentPanel = false;
        View contentView = getLayoutInflater().inflate(R.layout.layout_image_editor_edit_panel, null);
        et_input_text = (EditText) contentView.findViewById(R.id.et_input_text);
        et_input_text.setOnFocusChangeListener(new FocusChangeListener());

//        ll_edit_panel_content = (LinearLayout) contentView.findViewById(R.id.ll_edit_panel_content);
//        iv_mode = (ImageView) contentView.findViewById(R.id.iv_mode);
//        iv_mode.setOnClickListener(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(contentView);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = 1.0f;
        layoutParams.width = display.getWidth();
        getWindow().setAttributes(layoutParams);
        et_input_text.setText(mMtv.getText().toString());
    }

    public void setOnQuickOptionformClickListener(OnQuickOptionformClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View v) {
        inputMode = getWindow().getAttributes().softInputMode;
        LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll_edit_panel_content.getLayoutParams();
        llParams.height = 564;
        llParams.width = 720;
        ll_edit_panel_content.invalidate();
        int visibility = ll_edit_panel_content.getVisibility();
        final int id = v.getId();
        isShowContentPanel = !isShowContentPanel;
        /**
        switch (id) {
            case R.id.iv_mode:
                if(visibility == View.INVISIBLE) {
                    System.out.println("SOFT_INPUT_STATE_VISIBLE");
                    ll_edit_panel_content.setVisibility(View.VISIBLE);
                    // 当前是键盘显示状态，隐藏和显示主要内容
//                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    // 隐藏键盘
                     hideKeyboard(et_input_text);
                }
                //inputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                else if(visibility == View.VISIBLE){
                    System.out.println("SOFT_INPUT_STATE_HIDDEN");
//                    et_input_text.requestFocus();
                    ll_edit_panel_content.setVisibility(View.INVISIBLE);
                    // 当前是键盘隐藏状态，显示键盘和隐藏主要内容
                    openKeybord(et_input_text);
                }

//                if (isShowContentPanel) {   // 打开软键盘
//                    ll_edit_panel_content.setVisibility(View.GONE);
//                    if (imm.isActive()) {
//                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                } else {
//                    ll_edit_panel_content.setVisibility(View.VISIBLE);
//                }
                break;
            default:
                break;
        }
         */
    }

    private class FocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
//            if (inputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
//                // 当前是键盘显示状态，隐藏然后显示主要内容
//                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//                // 隐藏键盘
//                hideKeyboard(et_input_text);
//                ll_edit_panel_content.setVisibility(View.VISIBLE);
//            } else {
//                // 当前是键盘隐藏状态，显示键盘然后隐藏主要内容
//                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//                openKeybord(et_input_text);
//                ll_edit_panel_content.setVisibility(View.GONE);
//            }
            if (ll_edit_panel_content.getVisibility() == View.VISIBLE) {
                ll_edit_panel_content.setVisibility(View.INVISIBLE);
            }
        }
    }

    //此方法只是关闭软键盘
    private void hideKeyboard(EditText edit) {
//        if(imm!=null) {
//            imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
//        }
//        方法二
        if(getCurrentFocus() != null) {
            ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void openKeybord(EditText mEditText) {
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

}