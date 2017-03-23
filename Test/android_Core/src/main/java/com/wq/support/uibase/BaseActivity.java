package com.wq.support.uibase;

import java.util.List;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.wq.base.BaseApplication;
import com.wq.support.annotation.InjectUtility;
import com.wq.support.net.JsonHandler;
import com.wq.support.net.NAction;
import com.wq.support.net.NetRequestCallback;
import com.wq.support.net.NetRequestHandler;
import com.wq.support.net.NetServer;
import com.wq.support.net.bean.BaseBean;
import com.wq.support.net.cache.NetSession;
import com.wq.support.uibase.inf.UIDialog;
import com.wq.support.utils.AHandler;
import com.wq.support.utils.AHandler.Task;
import com.wq.support.utils.SystemBarTintManager;

/**
 * @Activity总父类
 */
@SuppressLint(
{ "Override", "NewApi" })
public abstract class BaseActivity extends FragmentActivity implements
		UIUpdateHandler, NetRequestHandler, NetRequestCallback{
	protected NetRequestHandler mNServer; // 网络请求对象
	protected SystemBarTintManager mTintManager;
	protected BaseActivity that;

	@Override
	protected void onCreate(Bundle arg0) {
		dealLongTimeBackGround(arg0);
		super.onCreate(arg0);
		that = this;
		InjectUtility.initInjectedView(this);
		if (SystemBarTintManager.isVersonEnough()) {
			setTranslucentStatus(true);
			mTintManager = new SystemBarTintManager(this);
			mTintManager.setStatusBarTintEnabled(true);
			mTintManager.setNavigationBarTintEnabled(false);
		}
		BaseApplication.getInstance().addActivity(this);
		initView();
	}
	protected void initView() {}
	/**
	 * 处理程序长时间后台的问题, 默认是关闭该Activity,子类若不想被关闭,将该方法置空
	 */
	public void dealLongTimeBackGround(Bundle arg0) {
		// if(arg0==null)finish();
	}

	/**
	 * 设置状态栏背景颜色 低于KITKAT 的版本该方法不生效
	 * 
	 * @param color
	 */
	public void setStatusBarTintColor(int color) {
		if (SystemBarTintManager.isVersonEnough())
			mTintManager.setStatusBarTintColor(color);
	}

	/**
	 * 设置状态栏背景Drawable对象 低于KITKAT 的版本该方法不生效
	 * 
	 * @param drawable
	 */
	public void setStatusBarTintDrawable(Drawable drawable) {
		if (SystemBarTintManager.isVersonEnough())
			mTintManager.setStatusBarTintDrawable(drawable);
	}

	@TargetApi(19)
	public SystemBarTintManager getSysemBarTintManager() {
		return mTintManager;
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

//	public int get_color(int colorId) {
//		return getResources().getColor(colorId);
//	}
//	/**
//	 * 启动Activity(使用当前Intent)
//	 * 
//	 * @param act
//	 */
//	public void start(Class<?> act) {
//		Intent intent = getIntent();
//		intent.setClass(this, act);
//		startActivity(intent);
//	}

	/**
	 * 获取全局Session对象,可用于数据传递,数据是持久化缓存的
	 */
	@Override
	public NetSession getSession() {
		return NetSession.instance(this);
	}

	/**
	 * 初始化网络请求服务
	 */
	final protected void initNetServer() {
		if (mNServer == null)
			mNServer = new NetServer(this, this);
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
	public void nofityUpdate(int requestCode, BaseBean bean) {}

	@Override
	public void nofityUpdate(int requestCode, float progress) {}

	@Override
	public void dealData(int requestCode, BaseBean bean) {}

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

	@Override
	protected void onDestroy() {
		cancelAllRequest();
		BaseApplication.getInstance().removeActivity(this);
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
		turnToFragment(toFragmentClass, layId, 0, 0);

	}

	/**
	 * 切换Fragment
	 * 
	 * @param toFragmentClass
	 * @param newAnimId
	 * @param oldAnimId
	 */
	public synchronized void turnToFragment(
			Class<? extends Fragment> toFragmentClass, int layId,
			int newAnimId, int oldAnimId) {
		FragmentManager fm = getSupportFragmentManager();
		// 切换到的Fragment标签
		String toTag = toFragmentClass.getSimpleName();
		// 查找切换的Fragment
		Fragment toFragment = fm.findFragmentByTag(toTag);
		// 如果要切换到的Fragment不存在，则创建
		if (toFragment == null) {
			try {
				toFragment = toFragmentClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		FragmentTransaction ft = fm.beginTransaction();
		if (newAnimId != 0 && oldAnimId != 0) {
			ft.setCustomAnimations(newAnimId, oldAnimId);
		}
		if (!toFragment.isAdded()) {
			ft.add(layId, toFragment, toTag);
		} else {
			ft.show(toFragment);
		}
		if (newAnimId != 0 && oldAnimId != 0) {
			ft.commit();
			ft = fm.beginTransaction();
		}
		ft.commitAllowingStateLoss();// 不保留状态提交事务
		List<Fragment> fs = getSupportFragmentManager().getFragments();
		if (fs != null)// 处理因为低内存产生的一些问题
			for (Fragment fragment2 : fs) {
				// 如果当前除了要跳转到的页面外还有其他Fragment正在显示,则隐藏
				if (!fragment2.isHidden() && fragment2 != toFragment)
					getSupportFragmentManager().beginTransaction()
							.hide(fragment2).commitAllowingStateLoss();
			}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 主动派发Activity回调给Fragment,解决supportv4包中 Fragment收不到回调的bug
		List<Fragment> fs = getSupportFragmentManager().getFragments();
		if (fs != null) {
			for (Fragment fragment : fs) {
				if (fragment.isVisible()) {//只派发给可见的Fragment
					fragment.onActivityResult(requestCode, resultCode, data);
				}
			}
		}
	}
}
