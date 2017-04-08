package com.wq.businessdirectory.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.widget.recyclerview.swipe.ISwipeMenuAdapter;
import com.wq.businessdirectory.R;
import com.wq.businessdirectory.common.adapter.OnItemClickListener;
import com.wq.businessdirectory.common.db.mode.CompanyBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by WQ on 2017/3/22.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements ISwipeMenuAdapter {
    private static final String TAG = "GirlsAdapter";

    private final Context mContext;
    private RealmResults<CompanyBean> mData;
    private OnItemClickListener mOnItemClickListener;

    public HomeAdapter(Context mContext, RealmResults<CompanyBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
//        mImages = Image.all(realm);
        setHasStableIds(true);
        mData.addChangeListener(new RealmChangeListener<RealmResults<CompanyBean>>() {
            @Override
            public void onChange(RealmResults<CompanyBean> element) {
                notifyDataSetChanged();
            }
        });
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
        return LayoutInflater.from(mContext).inflate(R.layout.item_home_company, parent, false);
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new ViewHolder(realContentView);
    }

    @Override
    public RecyclerView.Adapter getAdapterSelf() {
        return this;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_company, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(HomeAdapter.this, view, getItem(position), position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        CompanyBean mItem = getItem(position);
        holder.tvCompanyName.setText(mItem.company_name);
        holder.tvLinkman.setText(mItem.company_phone_name + " " + mItem.company_phone);
    }

    public CompanyBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_company_name)
        TextView tvCompanyName;
        @Bind(R.id.tv_linkman)
        TextView tvLinkman;
        @Bind(R.id.tv_company_address)
        TextView tvCompanyAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
