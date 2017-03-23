package com.wq.support.ui;

import java.util.List;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.wq.support.ui.intf.Pagingable;
import com.wq.support.ui.util.PagingHelp;
import com.pulltorefresh.PullToRefreshBase;

/**
 * @可刷新与分页的Activity基类
 * @param <T>
 *            数据源类型
 */
@SuppressWarnings(
{ "unchecked", "rawtypes" })
public abstract class PagingActivity<T> extends UIBaseActivity implements Pagingable<T> {
	protected List<T> mData = null;
	protected BaseAdapter mAdapter;
	PullToRefreshBase pullListView;
	PagingHelp pagingHelp=new PagingHelp<T>(this);
	public int getPageSize() {
		return pagingHelp.getPageSize();
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	public void setPageSize(int pageSize) {
		pagingHelp.setPageSize(pageSize);
	}

	public int getCurPage() {
		return pagingHelp.getCurPage();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadCurrentPage();
	}

	/**
	 * 加载数据,子类实现,并需要调用 setDataToView方法来将数据填充到视图
	 * 
	 * @param curPage
	 *            当前页数
	 */
	public abstract void loadData(int curPage);

	/**
	 * 获取适配器
	 * 
	 * @param mData
	 * @return
	 */
	public abstract BaseAdapter getAdapter(List<T> mData);



	public boolean canLoad(List<T> data) {
		return pagingHelp.canLoad(data);
	}
	
	@Override
	public T getDataItem(int position) {
		return (T) pagingHelp.getDataItem(position);
	}
	

	public boolean isFirst() {
		return pagingHelp.isFirst();
	}


	public void nextPage() {
		pagingHelp.nextPage();
	}

	public void reshPage() {
		pagingHelp.reshPage();
	}
	public void setAddFirst(boolean isAddFirst){
		pagingHelp.setAddFirst(isAddFirst);
	}
	public synchronized BaseAdapter setDataToView(List<T> mData, AdapterView listView) {
		mAdapter = pagingHelp.setDataToView(mData, listView);
		this.mData=pagingHelp.getmData();
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
