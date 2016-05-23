package com.anybeen.mark.imageeditor.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anybeen.mark.imageeditor.entity.CardTemplateInfo;
import com.anybeen.mark.imageeditor.utils.Const;
import com.anybeen.mark.imageeditor.utils.ToastUtil;
import com.anybeen.mark.yinjiimageeditorlibrary.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

/**
 * Created by maidou on 2016/3/28.
 */
public class CardTemplateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private Bitmap mBitmap;
    private ArrayList<CardTemplateInfo> mList;
    public CardTemplateAdapter(Context context, ArrayList<CardTemplateInfo> list) {
        this.context = context;
        this.mList = list;
    }
    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_editor_recycle_view_card_template, null);
        TemplatesHolder holder = new TemplatesHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TemplatesHolder templatesHolder = (TemplatesHolder) holder;
        Uri aniImageUri;
        ImageRequest request;
        DraweeController controller;
        final CardTemplateInfo templateInfo;
        if (position == mList.size()) {
            templateInfo = null;    // 把点击判断迁移到外部?
            // aniImageUri = Uri.parse("res://com.anybeen.mark.yinjiimageeditorlibrary/" + R.mipmap.pic_templates_more);
            aniImageUri = Uri.parse(Const.TEMPLATE_PIC_LOAD_MORE_IN_ASSETS);// +  + "");
            request = ImageRequestBuilder.newBuilderWithSource(aniImageUri)
                    .build();
            controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
            templatesHolder.icon.setController(controller);
        } else {
            templateInfo = mList.get(position);
            aniImageUri = Uri.parse(Const.FILE_PREFIX + templateInfo.sampleUrl);
            request = ImageRequestBuilder.newBuilderWithSource(aniImageUri)
                    .build();
            controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setAutoPlayAnimations(true)
                    .build();
            templatesHolder.icon.setController(controller);
        }

        //如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            templatesHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == mList.size()) {
                        ToastUtil.makeText(context, "点击了更多卡片，打开 act 或请求下载");
                    } else {
                        mOnItemClickListener.onItemClick(templatesHolder.itemView, position, templateInfo);
                    }
                }
            });
        }
    }

    public class TemplatesHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView icon;
        public TemplatesHolder(View itemView) {
            super(itemView);
            this.icon = (SimpleDraweeView) itemView.findViewById(R.id.icon);
        }
    }

    /**
     * 处理点击事件的监听接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position, CardTemplateInfo templateInfo);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}