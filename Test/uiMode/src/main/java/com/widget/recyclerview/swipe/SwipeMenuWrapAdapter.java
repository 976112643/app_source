package com.widget.recyclerview.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by WQ on 2017/3/22.
 */

public class SwipeMenuWrapAdapter extends SwipeMenuAdapter {
    ISwipeMenuAdapter iSwipeMenuAdapter;
    RecyclerView.Adapter sourceAdapter;

    public SwipeMenuWrapAdapter(ISwipeMenuAdapter sourceAdapter) {
        this.iSwipeMenuAdapter=sourceAdapter;
        this.sourceAdapter = iSwipeMenuAdapter.getAdapterSelf();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return iSwipeMenuAdapter.onCreateContentView(parent,viewType);
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return iSwipeMenuAdapter.onCompatCreateViewHolder(realContentView,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        sourceAdapter.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {
        return sourceAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return sourceAdapter.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        sourceAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return sourceAdapter.getItemId(position);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        sourceAdapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return sourceAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        sourceAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        sourceAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        sourceAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        sourceAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        sourceAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        sourceAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    public static SwipeMenuWrapAdapter wrapAdapter(ISwipeMenuAdapter sourceAdapter){
        return new SwipeMenuWrapAdapter(sourceAdapter);
    }
}
