package com.wq.support.ui.util;

import java.util.List;

import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.wq.support.ui.intf.Pagingable;
import com.wq.support.utils.ToastUtil;

import static com.wq.support.utils.EmptyDeal._SIZE;

/**
 * 分页帮助类,辅助可分页的页面进行分页操作,封装分页的具体实现 ,借由分页操作接口进行协调.便于分页的统一调整
 * 
 * @author WQ 上午10:46:55
 * @param <T>
 */
public class PagingHelp<T> implements Pagingable<T>{
	protected int curPage = 1;
	protected List<T> mData = null;
	protected BaseAdapter mAdapter;
	protected int pageSize = 1;
	Pagingable<T> pagingable;
	protected boolean addFirst=false;
	
	public boolean isAddFirst() {
		return addFirst;
	}

	public void setAddFirst(boolean addFirst) {
		this.addFirst = addFirst;
	}

	public static int size=20;
	boolean isNeedTip=true;
	public PagingHelp(Pagingable<T> pagingable) {
		this.pagingable = pagingable;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurPage() {
		return curPage;
	}

	public List<T> getmData() {
		return mData;
	}

	public boolean canLoad(List<T> data) {
		return _SIZE(data)>=pageSize;
	}

	public void loadData(int curPage) {
		pagingable.loadData(this.curPage = curPage);
	}

	public BaseAdapter getAdapter(List<T> mData) {
		return pagingable.getAdapter(mData);
	}



	public void nextPage() {
		isNeedTip=true;
		curPage++;
		loadData(curPage);
	}

	public void reshPage() {
		curPage = 1;
		loadData(curPage);
	}
	

	public synchronized BaseAdapter setDataToView(List<T> mData,
			AdapterView listView) {
		if (isFirst()) {
			if (this.mData == null) {
				// 这里是首次加载数据时候做的处理
				listView.setAdapter(mAdapter = getAdapter(this.mData = mData));
			} else {
				// 这里是刷新第1页做的操作
				if (this.mData != mData) {
					this.mData.clear();
					if (mData != null) {
						this.mData.addAll(mData);
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		} else {
			if (mData != null) {
				// 这里是在添加,加载更多之后拉下来的数据
				// 将数据 ,除相同项,
				addData2List(this.mData, mData,isAddFirst());
			}
			mAdapter.notifyDataSetChanged();
		}
		if (!canLoad(mData)  && curPage > 1 &&isNeedTip) {
				ToastUtil.shortM("没有更多啦~");
		} 
		return mAdapter;
	}

	/**
	 * 添加数据到源集合中,替换掉已存在的元素(去重)
	 * 
	 * @param src
	 * @param data
	 */
	public static void addData2List(List src, List data,boolean isAddFirst) {
		if (data != null)
			for (Object object : data) {
				int index = src.indexOf(object);
				if (index != -1) {
					src.set(index, object);
				} else {
					if(isAddFirst && src.size()>0){
						src.add(0,object);
					}else{
					src.add(object);
					}
				}
			}
	}

	@Override
	public void loadCurrentPage() {
		isNeedTip=false;
		loadData(curPage);
	}

	public boolean isFirst() {
		return curPage == 1 || this.mData == null;
	}

	@Override
	public void loadPage(int page) {
		isNeedTip=false;
		loadData(page);
	}

	@Override
	public T getDataItem(int position) {
		if(this.mData==null){
			return null;
		}
		return this.mData.get(position);
	}

}
