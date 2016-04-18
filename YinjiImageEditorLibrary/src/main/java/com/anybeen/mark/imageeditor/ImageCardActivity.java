package com.anybeen.mark.imageeditor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.anybeen.mark.yinjiimageeditorlibrary.R;

/**
 * Created by maidou on 2016/4/18.
 */
public class ImageCardActivity extends Activity{
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_image);
        mContext = this;



    }
}
