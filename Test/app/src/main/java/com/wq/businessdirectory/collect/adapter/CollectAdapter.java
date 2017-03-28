package com.wq.businessdirectory.collect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.widget.recyclerview.swipe.ISwipeMenuAdapter;
import com.wq.businessdirectory.common.db.mode.CollectCompanyBean;
import com.wq.businessdirectory.common.db.mode.CompanyBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by WQ on 2017/3/22.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> implements ISwipeMenuAdapter {
    private static final String TAG = "GirlsAdapter";

    private final Context mContext;
    private RealmResults<CollectCompanyBean> mData;
    private OnItemClickListener mOnItemClickListener;
    public CollectAdapter(Context mContext, RealmResults<CollectCompanyBean>mData) {
        this.mContext = mContext;
        this.mData=mData;
        mData.addChangeListener(new RealmChangeListener<RealmResults<CollectCompanyBean>>() {
            @Override
            public void onChange(RealmResults<CollectCompanyBean> element) {
                notifyDataSetChanged();
            }
        });
//        mImages = Image.all(realm);
        setHasStableIds(true);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void updateRefreshed(int numImages) {
        notifyItemRangeInserted(0, numImages);
        Log.d(TAG, "updateInsertedData: from 0 to " + numImages);
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_2, parent, false);
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public RecyclerView.Adapter getAdapterSelf() {
        return this;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int pos);

        void onItemLongClick(View view, int pos);

    }

    @Override
    public CollectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(view,position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        CompanyBean mItem=getItem(position).companyDetails;
        holder.text1.setText(mItem.company_name+" "+getItem(position).getTime());
        holder.text2.setText(mItem.company_phone_name+" "+mItem.company_phone);
    }

    public CollectCompanyBean getItem(int position){
        return mData.get(position);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(android.R.id.text1)
        public TextView text1 ;
        @Bind(android.R.id.text2)
        public TextView text2 ;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
