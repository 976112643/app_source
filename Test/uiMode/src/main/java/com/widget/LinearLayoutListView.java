package com.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * linearlayout 方式实现的listview ,嵌套时使用,由于无法复用控件,不建议加载过多的数据,
 * Created by WQ on 2017/1/24.
 */

public class LinearLayoutListView extends LinearLayout   {
    ListAdapter mAdapter;
    AdapterDataSetObserver mDataSetObserver;
    AdapterView.OnItemClickListener listener;
    int dividerHeight=0;
    public int getDividerHeight() {
        return dividerHeight;
    }

    public void setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
    }
    public void   setOnItemClickListener(AdapterView.OnItemClickListener listener){
     this.listener=listener;
 }
    public LinearLayoutListView(Context context) {
        super(context);
    }

    public LinearLayoutListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public LinearLayoutListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public  ListAdapter getAdapter(){
        return mAdapter;
    }

    /**
     * Sets the adapter that provides the data and the views to represent the data
     * in this widget.
     * @param adapter The adapter to use to create this view's content.
     */
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        } else {

        }
        updateAllView();
    }

    /**
     * 刷新视图数据
     */
    public  void  updateAllView(){
        this.removeAllViews();
        if(mAdapter==null)return;
        for (int i = 0,count=mAdapter.getCount(); i <count ; i++) {
            View itemView=mAdapter.getView(i,null,this);
           final int position=i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onItemClick(null,v,position,position);
                    }
                }
            });
            LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.topMargin=dividerHeight;
            addView(itemView,params);
        }
    }
    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            updateAllView();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            updateAllView();
        }
    }
}
