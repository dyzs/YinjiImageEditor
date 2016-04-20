package com.anybeen.mark.imageeditor.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by maidou on 2016/4/20.
 */
public abstract class BasicAdapter<T> extends BaseAdapter {
    protected ArrayList<T> list;
    public BasicAdapter(ArrayList<T> list) {
        super();
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}