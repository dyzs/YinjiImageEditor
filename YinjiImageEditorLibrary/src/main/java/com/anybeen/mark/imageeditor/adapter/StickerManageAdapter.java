//package com.anybeen.mark.imageeditor.adapter;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.anybeen.mark.imageeditor.utils.BitmapUtils;
//import com.anybeen.mark.imageeditor.utils.Const;
//import com.anybeen.mark.yinjiimageeditorlibrary.R;
//
//import java.util.ArrayList;
//
///**
// * Created by maidou on 2016/5/23.
// */
//public class StickerManageAdapter extends BasicAdapter {
//    private Context mContext;
//    private Bitmap bitmap;
//    public StickerManageAdapter(ArrayList list) {
//        super(list);
//    }
//
//    public StickerManageAdapter(ArrayList list, Context context) {
//        super(list);
//        this.mContext = context;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = View.inflate(mContext, R.layout.item_ie_gv_sticker, null);
//        }
//        StickerHolder holder = StickerHolder.getHolder(convertView);
//        if (bitmap != null && !bitmap.isRecycled()) {
//            bitmap.isRecycled();
//        }
//        bitmap = BitmapUtils.getSampledBitmap(Const.STICKERS_DIR + list.get(position), 50, 50);
//        holder.icon.setImageBitmap(bitmap);
//        return convertView;
//    }
//
//    static class StickerHolder{
//        public ImageView icon;
//        public TextView text;
//        public StickerHolder(View itemView) {
//            this.icon = (ImageView) itemView.findViewById(R.id.icon);
//            this.text = (TextView) itemView.findViewById(R.id.text);
//        }
//        public static StickerHolder getHolder(View convertView){
//            StickerHolder holder = (StickerHolder) convertView.getTag();
//            if (holder == null) {
//                holder = new StickerHolder(convertView);
//                convertView.setTag(holder);
//            }
//            return holder;
//        }
//    }
//}
