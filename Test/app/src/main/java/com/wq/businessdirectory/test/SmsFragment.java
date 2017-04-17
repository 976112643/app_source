package com.wq.businessdirectory.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wq.businessdirectory.R;
import com.wq.businessdirectory.common.ui.RecycleViewFragment;
import com.wq.businessdirectory.receiver.mode.PhoneMessage;
import com.wq.businessdirectory.test.adapter.SmsAdapter;

import io.realm.Sort;

/**
 * Created by WQ on 2017/4/14.
 */

public class SmsFragment extends RecycleViewFragment {
    public static SmsFragment newInstance() {
        Bundle args = new Bundle();
        SmsFragment fragment = new SmsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void adviceRefresh() {
        super.adviceRefresh();
    }

    @Override
    protected void loadingMore() {

    }

    @Override
    protected void fetchLatest() {
        setRefreshLayout(false);
    }

    @Override
    protected int getLastVisiblePos() {
        return ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

    }

    @Override
    protected int getLayoutResId() {
        return  R.layout.test_list;
    }

    @Override
    protected int getRefreshLayoutId() {
        return R.id.refresh_layout;
    }

    @Override
    protected RecyclerView.Adapter initAdapter() {
        return new SmsAdapter(that,mRealm.where(PhoneMessage.class).findAllSorted("add_time", Sort.DESCENDING));
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(that);
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.list;
    }
}
