package com.wq.businessdirectory.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wq.businessdirectory.R;
import com.wq.businessdirectory.receiver.PhoneMessage;
import com.wq.businessdirectory.receiver.PhoneRecord;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by WQ on 2017/4/14.
 */

public class PhoneRecordAdapter extends BaseAdapter<PhoneRecord> {

    public PhoneRecordAdapter(Context mContext, RealmResults<PhoneRecord> mData) {
        super(mContext, mData);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_test_sms;
    }

    @Override
    public void BindViewHolder(RecyclerView.ViewHolder tmpHolder,PhoneRecord message, int position) {
        Holder holder= (Holder) tmpHolder;

        holder.tvPhone.setText(message.getPhone());
        holder.tvContent.setText(message.getRecord_time());
        holder.tvTime.setText(message.getType());
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(getContentView(parent, viewType));
    }

    class Holder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_phone)
        TextView tvPhone;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_time)
        TextView tvTime;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
