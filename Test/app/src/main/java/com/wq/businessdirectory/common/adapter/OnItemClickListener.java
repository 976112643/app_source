package com.wq.businessdirectory.common.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WQ on 2017/3/22.
 */

public interface OnItemClickListener {
    void onItemClick(RecyclerView.Adapter adapter,View view, int position);
}
