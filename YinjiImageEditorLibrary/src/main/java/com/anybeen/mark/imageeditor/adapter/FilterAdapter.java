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
import com.xinlan.imageeditlibrary.editimage.fliter.PhotoProcessing;

/**
 * Created by maidou on 2016/3/28.
 */
public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private Bitmap filterSampleIconBitmap;
    public FilterAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return PhotoProcessing.FILTERS.length;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_editor_recycle_view_filter, null);
        FilterHolder holder = new FilterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FilterHolder h = (FilterHolder) holder;
        String name = PhotoProcessing.FILTERS[position];
        h.text.setText(name);
        if (filterSampleIconBitmap != null && !filterSampleIconBitmap.isRecycled()) {
            filterSampleIconBitmap = null;
        }
        filterSampleIconBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.pic_icon_filter_sample);
        if (position == 0) {
            h.icon.setImageBitmap(filterSampleIconBitmap);
        }
        else {
            h.icon.setImageBitmap(PhotoProcessing.filterPhoto(filterSampleIconBitmap, position));
        }
        h.icon.setOnClickListener(new FilterClickListener(position));
    }
    private class FilterClickListener implements View.OnClickListener{
        private int clickPosition;
        public FilterClickListener(int position) {
            this.clickPosition = position;
        }
        @Override
        public void onClick(View v) {
            if (mHandleFilterListener != null) {
                mHandleFilterListener.handleFilter(clickPosition);
            }
//            FilterTask filterTask = new FilterTask();
//            filterTask.execute(clickPosition + "");
        }
    }
    public class FilterHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView text;
        public FilterHolder(View itemView) {
            super(itemView);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    private HandleFilterListener mHandleFilterListener;
    public HandleFilterListener getHandleFilterListener() {return mHandleFilterListener;}
    public void setHandleFilterListener(HandleFilterListener mHandleFilterListener) {this.mHandleFilterListener = mHandleFilterListener;}
    public interface HandleFilterListener {
        void handleFilter(int position);
    }


//    private final class FilterTask extends AsyncTask<String, Void, Bitmap> {
//        private ProgressDialog loadDialog;
//        public FilterTask() {
//            super();
//            // warning progress use context！！！！！！
//            loadDialog = new ProgressDialog(context, R.style.MyDialog);
//            loadDialog.setCancelable(false);
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadDialog.show();
//        }
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            int position = Integer.parseInt(params[0]);
//            return PhotoProcessing.filterPhoto(
//                    filterCopyBitmap,
//                    position
//            );
////            return null;
//        }
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            loadDialog.dismiss();
//            iv_main_image.setImageBitmap(result);
//            System.gc();
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            loadDialog.dismiss();
//        }
//    }// end inner class
}