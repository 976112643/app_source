package com.wq.businessdirectory.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wq.businessdirectory.R;
import com.wq.businessdirectory.common.ui.RecycleViewFragment;
import com.wq.businessdirectory.receiver.PhoneRecord;
import com.wq.businessdirectory.test.adapter.PhoneRecordAdapter;

import io.realm.Sort;

/**
 * Created by WQ on 2017/4/14.
 */

public class PhoneFragment extends RecycleViewFragment {
    public static PhoneFragment newInstance() {
        Bundle args = new Bundle();
        PhoneFragment fragment = new PhoneFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        return new PhoneRecordAdapter(that,mRealm.where(PhoneRecord.class).findAllSorted("add_time", Sort.DESCENDING));
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
