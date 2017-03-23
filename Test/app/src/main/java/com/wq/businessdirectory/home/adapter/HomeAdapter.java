package com.wq.businessdirectory.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.widget.recyclerview.swipe.ISwipeMenuAdapter;
import com.wq.businessdirectory.R;
import com.wq.businessdirectory.common.db.mode.CompanyBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WQ on 2017/3/22.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements ISwipeMenuAdapter {
    private static final String TAG = "GirlsAdapter";

    private final Context mContext;
    private  List<CompanyBean> mData;
    private OnItemClickListener mOnItemClickListener;
    public HomeAdapter(Context mContext,List<CompanyBean>mData) {
        this.mContext = mContext;
        this.mData=mData;
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
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        CompanyBean mItem=getItem(position);
        holder.text1.setText(mItem.companyName);
        holder.text2.setText(mItem.companyPhone);
    }

    public CompanyBean getItem(int position){
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
