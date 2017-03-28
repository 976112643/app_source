package com.wq.businessdirectory.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.widget.recyclerview.swipe.Closeable;
import com.widget.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.widget.recyclerview.swipe.SwipeMenu;
import com.widget.recyclerview.swipe.SwipeMenuCreator;
import com.widget.recyclerview.swipe.SwipeMenuItem;
import com.widget.recyclerview.swipe.SwipeMenuRecyclerView;
import com.wq.businessdirectory.MainActivity;
import com.wq.businessdirectory.R;
import com.wq.businessdirectory.common.adapter.OnItemClickListener;
import com.wq.businessdirectory.common.db.mode.CollectCompanyBean;
import com.wq.businessdirectory.common.db.mode.CompanyBean;
import com.wq.businessdirectory.common.ui.RecycleViewFragment;
import com.wq.businessdirectory.home.adapter.HomeAdapter;
import com.wq.businessdirectory.services.DataSynService;
import com.wq.support.utils.ToastUtil;

import io.realm.Realm;

import static android.R.attr.width;
import static com.widget.recyclerview.swipe.SwipeMenuWrapAdapter.wrapAdapter;
import static com.wq.businessdirectory.common.db.DBHelper.generateId;
import static com.wq.businessdirectory.common.net.API.COMPANYS;

/**
 * Created by WQ on 2017/3/22.
 */

public class HomeFragment extends RecycleViewFragment {
    int colorGreeen;
    int page=1;
    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        colorGreeen=getResources().getColor(R.color.colorGreeen);
        SwipeMenuRecyclerView swipeMenuRecyclerView = (SwipeMenuRecyclerView) mRecyclerView;
//        swipeMenuRecyclerView.setItemViewSwipeEnabled(true);
        // 创建菜单：
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem callItem = new SwipeMenuItem(that)
                        .setBackgroundDrawable(new ColorDrawable(colorGreeen))
                        .setText("呼叫") // 文字。
                        .setTextColor(Color.WHITE) // 文字的颜色。
                        .setWidth(200) // 菜单宽度。
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT); // 菜单高度。
                rightMenu.addMenuItem(callItem); // 在左侧添加一个菜单。
                // 还有添加，这里各添加一个吧...
            }
        };
        // 设置监听器。
        swipeMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        swipeMenuRecyclerView.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, @SwipeMenuRecyclerView.DirectionMode int direction) {
                ToastUtil.shortM(menuPosition+"");
            }
        });
    }

    @Override
    public void adviceRefresh() {
//        fetchLatest();
        registerReceiver(COMPANYS);
        Intent intent=new Intent(that, DataSynService.class);
        intent.setAction(COMPANYS);
        intent.putExtra("page",page=1);
        that.startService(intent);
        super.adviceRefresh();
    }

    @Override
    protected void loadingMore() {
        Intent intent=new Intent(that, DataSynService.class);
        intent.setAction(COMPANYS);
        intent.putExtra("page",page++);
        that.startService(intent);
    }

    @Override
    protected void fetchLatest() {
        adviceRefresh();
    }

    @Override
    protected int getLastVisiblePos() {
        return ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_list;
    }

    @Override
    protected int getRefreshLayoutId() {
        return R.id.refresh_layout;
    }

    @Override
    protected RecyclerView.Adapter initAdapter() {
        HomeAdapter homeAdapter=new HomeAdapter(that, mRealm.where(CompanyBean.class).findAll());
        homeAdapter.setOnItemClickListener(new OnItemClickListener<CompanyBean>() {
            @Override
            public void onItemClick(RecyclerView.Adapter adapter, View view, CompanyBean mItem, int position) {
                CollectCompanyBean.addOrDelCollect(mRealm,mItem);
            }
        });
        return wrapAdapter(homeAdapter);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(that);
    }

    @Override
    protected int getRecyclerViewId() {
        return R.id.list_company;
    }
}
