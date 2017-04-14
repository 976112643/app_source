package com.wq.businessdirectory.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wq.businessdirectory.R;
import com.wq.businessdirectory.common.adapter.OnItemClickListener;
import com.wq.businessdirectory.common.db.mode.CompanyBean;
import com.wq.businessdirectory.home.adapter.HomeAdapter;

import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by WQ on 2017/4/14.
 */

public abstract class BaseAdapter<E extends RealmObject> extends RecyclerView.Adapter {

    private final Context mContext;
    private RealmResults<E> mData;
    private OnItemClickListener mOnItemClickListener;

    protected BaseAdapter(Context mContext, RealmResults<E> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mData = mData;
//        mImages = Image.all(realm);
        setHasStableIds(true);
        mData.addChangeListener(new RealmChangeListener<RealmResults<E>>() {
            @Override
            public void onChange(RealmResults<E> element) {
                notifyDataSetChanged();
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public View getContentView(ViewGroup parent, int viewType){
        View rootView=LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
     return rootView;

    }
    public abstract int getLayoutId();
    public E getItem(int position) {
        return mData.get(position);
    }

    @Override
   final public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(BaseAdapter.this, view, getItem(position), position);
                    }
                }
            });
        BindViewHolder(holder,getItem(position),position);
        }

    public abstract void BindViewHolder(RecyclerView.ViewHolder holder,E mItem, int position);

}