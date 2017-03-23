package com.wq.businessdirectory.collect;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.widget.recyclerview.swipe.Closeable;
import com.widget.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.widget.recyclerview.swipe.SwipeMenu;
import com.widget.recyclerview.swipe.SwipeMenuCreator;
import com.widget.recyclerview.swipe.SwipeMenuItem;
import com.widget.recyclerview.swipe.SwipeMenuRecyclerView;
import com.wq.businessdirectory.R;
import com.wq.businessdirectory.collect.adapter.CollectAdapter;
import com.wq.businessdirectory.common.db.mode.CollectCompanyBean;
import com.wq.businessdirectory.common.db.mode.CompanyBean;
import com.wq.businessdirectory.common.ui.RecycleViewFragment;
import com.wq.businessdirectory.home.adapter.HomeAdapter;
import com.wq.support.utils.ToastUtil;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.widget.recyclerview.swipe.SwipeMenuWrapAdapter.wrapAdapter;

/**
 * Created by WQ on 2017/3/22.
 */

public class CollectFragment extends RecycleViewFragment {
    int colorGreeen;
    public static CollectFragment newInstance() {
        Bundle args = new Bundle();
        CollectFragment fragment = new CollectFragment();
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
        super.adviceRefresh();
    }

    @Override
    protected void loadingMore() {
    }

    @Override
    protected void fetchLatest() {
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
        RealmResults<CollectCompanyBean>realmResults
                =mRealm.where(CollectCompanyBean.class).findAllSorted("add_time", Sort.DESCENDING);
        return wrapAdapter(new CollectAdapter(that, realmResults));
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
