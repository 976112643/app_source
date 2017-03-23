package com.widget.recyclerview.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WQ on 2017/3/22.
 */

public interface ISwipeMenuAdapter {
    public View onCreateContentView(ViewGroup parent, int viewType) ;

    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) ;
    public RecyclerView.Adapter getAdapterSelf();
}
