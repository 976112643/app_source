package com.wq.support.ui;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.wq.support.ui.intf.Pagingable;
import com.wq.support.ui.util.PagingHelp;
import com.pulltorefresh.PullToRefreshBase;

/**
 * @可刷新与分页的Fragment基类
 * @param <T>
 *            数据源类型
 */
@SuppressWarnings(
{ "unchecked", "rawtypes" })
public abstract class PagingFragment<T> extends UIBaseFragment implements  Pagingable<T> {
	protected List<T> mData = null;
	protected BaseAdapter mAdapter;
	protected PullToRefreshBase pullListView;
	protected PagingHelp pagingHelp=new PagingHelp<T>(this);

	public int getPageSize() {
		return pagingHelp.getPageSize();
	}

	public void setPageSize(int pageSize) {
		pagingHelp.setPageSize(pageSize);
	}

	public int getCurPage() {
		return pagingHelp.getCurPage();
	}

	public boolean canLoad(List<T> data) {
		return pagingHelp.canLoad(data);
	}
	public void setAddFirst(boolean isAddFirst){
		pagingHelp.setAddFirst(isAddFirst);
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden && mData == null) {
			loadCurrentPage();
			// 异常状态下再次请求数据
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		loadCurrentPage();
	}
	
@Override
public T getDataItem(int position) {
	return (T) pagingHelp.getDataItem(position);
}

	public abstract void loadData(int curPage);

	public abstract BaseAdapter getAdapter(List<T> mData);


	public boolean isFirst() {
		return pagingHelp.isFirst();
	}


	public void nextPage() {
		pagingHelp.nextPage();
	}

	public void reshPage() {
		pagingHelp.reshPage();
	}

	public synchronized BaseAdapter setDataToView(List<T> mData, AdapterView listView) {
		mAdapter = pagingHelp.setDataToView(mData, listView);
		this.mData = pagingHelp.getmData();
		return mAdapter;
	}

	@Override
	public void loadCurrentPage() {
		pagingHelp.loadCurrentPage();
	}
	@Override
	public void loadPage(int page) {
		pagingHelp.loadPage(page);
	}
}
