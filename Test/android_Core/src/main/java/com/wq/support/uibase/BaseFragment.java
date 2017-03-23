package com.wq.support.uibase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wq.base.BaseApplication;
import com.wq.support.net.JsonHandler;
import com.wq.support.net.NAction;
import com.wq.support.net.NetRequestCallback;
import com.wq.support.net.NetRequestHandler;
import com.wq.support.net.NetServer;
import com.wq.support.net.bean.BaseBean;
import com.wq.support.net.cache.NetSession;
import com.wq.support.utils.log.Logger;

/**
 * @fragment 基类,用于初始化一些公共的东西,使得Fragment更好使用
 */
public abstract class BaseFragment extends Fragment implements
		NetRequestHandler, UIUpdateHandler, NetRequestCallback {
	protected NetRequestHandler mNServer;
	protected Activity that;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		that = getActivity();
		BaseApplication.getInstance().addFragment(this);
		super.onCreate(savedInstanceState);
	}

	public Intent getIntent() {
		if (that != null)
			return that.getIntent();
		return getActivity().getIntent();
	}

	// --------------------------------Fragment初始视图处理相关-----------------------------------
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		if(isVisible()){
			adviceRefresh();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (getUserVisibleHint() ){
			adviceRefresh();
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	public void adviceRefresh(){
		Logger.D(this+"  adviceRefresh");
	}
// ----------------------------------------网络请求相关-----------------------------------------

	@Override
	public NetSession getSession() {
		return NetSession.instance(that);
	}

	/**
	 * 初始化网络请求服务
	 */
	final protected void initNetServer() {
		if (mNServer == null)
			mNServer = new NetServer(that, this);
	}

	@Override
	public void doGet(NAction action, JsonHandler handler) {
		initNetServer();
		doGet(action, handler);
	}

	@Override
	public void doPost(NAction action, JsonHandler handler) {
		initNetServer();
		doPost(action, handler);
	}

	@Override
	public void requestAsynGet(NAction action) {

		initNetServer();
		mNServer.requestAsynGet(action);
	}

	@Override
	public void requestAsynPost(NAction action) {
		initNetServer();
		mNServer.requestAsynPost(action);
	}

	@Override
	public void cancelRequest(int requestCode) {

		if (mNServer != null)
			mNServer.cancelRequest(requestCode);
	}

	@Override
	public void cancelAllRequest() {

		if (mNServer != null)
			mNServer.cancelAllRequest();
	}

	@Override
	public void loadFaild(int requestCode, BaseBean bean) {}

	@Override
	public void emptyData(int requestCode, BaseBean bean) {}

	@Override
	public void nofityUpdate(int requestCode, BaseBean bean) {

		// 更新界面
	}

	@Override
	public void nofityUpdate(int requestCode, float progress) {

		// 更新进度
	}

	@Override
	public void dealData(int requestCode, BaseBean bean) {

	}

	@Override
	public void requestFinish() {
	}

	@Override
	public void requestCancel() {
	}

	@Override
	public void requestStart() {}

	@Override
	public int getRequestNumber() {
		initNetServer();
		return mNServer.getRequestNumber();
	}

	@Override
	public void useCache(boolean useCache) {

		initNetServer();
		mNServer.useCache(useCache);

	}

	public void finish() {
		if (that != null)
			that.finish();
	}

	@Override
	public void onDestroy() {
		cancelAllRequest();
		BaseApplication.getInstance().removeFragment(this);
		Logger.D("视图销毁 " + this);
		that = null;
		super.onDestroy();
	}
	/**
	 * 切换Fragment
	 * 
	 * @param toFragmentClass
	 */
	public synchronized void turnToFragment(
			Class<? extends Fragment> toFragmentClass, int layId) {
		try {
			turnToFragment(toFragmentClass.newInstance(), layId);
		} catch (java.lang.InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		};
	}

	/**
	 * 切换Fragment
	 * 
	 * @param toFragmentClass
	 */
	public synchronized void turnToFragment(Fragment toFragmentClass, int layId) {
		FragmentManager fm = getChildFragmentManager();
		fm.beginTransaction().replace(layId, toFragmentClass)
				.commitAllowingStateLoss();
	}

}
