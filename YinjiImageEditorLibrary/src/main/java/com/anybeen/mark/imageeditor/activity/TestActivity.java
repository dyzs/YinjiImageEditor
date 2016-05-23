package com.anybeen.mark.imageeditor.activity;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.anybeen.mark.imageeditor.utils.Const;
import com.anybeen.mark.yinjiimageeditorlibrary.R;

/**
 * Created by maidou on 2016/4/25.
 */
public class TestActivity extends ImageEditorBaseActivity{
    private ImageView iv_templates_image;
    @Override
    public void initView() {
        setContentView(R.layout.activity_card_image);
        iv_templates_image = (ImageView) findViewById(R.id.iv_templates_image);
    }

    @Override
    public void initData() {
        LoadImageTask loadImageTask = new LoadImageTask(this);
        loadImageTask.execute(Const.SDCARD_ROOT + "/saves/aapic_bg_ch_style.jpg");
    }

    @Override
    public void setContentImage(Bitmap result) {
        iv_templates_image.setImageBitmap(result);
    }
}
