package com.anybeen.mark.imageeditor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.anybeen.mark.yinjiimageeditorlibrary.R;
import com.anybeen.mark.imageeditor.utils.Const;

import java.util.ArrayList;

/**
 * Created by maidou on 2016/3/28.
 */
public class StickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    // Integer 表示 resId，资源文件
    private ArrayList<Integer> mList;
    // 标签名称
    private ArrayList<String> mListNames;




    private Bitmap iconBitmap;

    public StickerAdapter(Context context) {
        this.mContext = context;
    }
    public StickerAdapter(Context context, ArrayList<Integer> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemCount() {
        return Const.STICKERS_NAME.length;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycle_view_filter, null);
        StickerHolder holder = new StickerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StickerHolder sh = (StickerHolder) holder;
        String name = Const.STICKERS_NAME[position];
        sh.text.setText(name);


        if (iconBitmap != null && !iconBitmap.isRecycled()) {
            iconBitmap = null;
        }
        iconBitmap = BitmapFactory.decodeResource(
                mContext.getResources(),
                Const.STICKERS_VALUES[position]
        );
        sh.icon.setImageBitmap(iconBitmap);

        // 保存 position 来还原贴纸
        sh.icon.setOnClickListener(new StickerClickListener(position));
    }
    private class StickerClickListener implements View.OnClickListener {
        private int mPos;
        public StickerClickListener (int position) {
            this.mPos = position;
        }
        @Override
        public void onClick(View v) {
            if (sil != null) {
                sil.onStickerIndex(mPos);
            }
        }
    }

    public StickerIndexListener getSil() {
        return sil;
    }

    public void setSil(StickerIndexListener sil) {
        this.sil = sil;
    }

    private StickerIndexListener sil;

    public interface StickerIndexListener {
        void onStickerIndex(int position);
    }

    private class StickerHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView text;
        public StickerHolder(View itemView) {
            super(itemView);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
