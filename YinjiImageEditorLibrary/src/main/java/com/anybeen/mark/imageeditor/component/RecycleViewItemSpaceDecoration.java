package com.anybeen.mark.imageeditor.component;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by maidou on 2016/4/11.
 */
public class RecycleViewItemSpaceDecoration extends RecyclerView.ItemDecoration {
    private int space;
    public RecycleViewItemSpaceDecoration(int space) {
        this.space = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}
