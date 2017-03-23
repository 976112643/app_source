package com.wq.support.net;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.net.SocketTimeoutException;

/**
 * 网络工具,网络请求处理,网络状态等判断
 * 
 * @author cnsunrun
 */
public class NetUtils {
	private static AsyncHttpClient client = new AsyncHttpClient();
	private static SyncHttpClient synClient = new SyncHttpClient();
	static {
		client.setTimeout(15000);
		synClient.setTimeout(15000);
		SSLSocketFactoryEx.setSSLSocketFactory(client,synClient);
	}

	public static AsyncHttpClient getAsynHttpClient() {
		return client;
	}

	/**
	 * 设置全局网络请求时超
	 * 
	 * @param timeout
	 */
	public static void setTimeOut(int timeout) {
		client.setTimeout(timeout);
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param responseHandler
	 * @return
	 */
	public static RequestHandle doGet(String url, AsyncHttpResponseHandler responseHandler) {
		if (Looper.myLooper() == Looper.getMainLooper())
			return client.get(url, responseHandler);
		return synClient.get(url, responseHandler);
	}

	/**
	 * 取消所有网络请求,可能会造成线程阻塞
	 */
	public static void cancelAllRequest() {
		client.cancelAllRequests(true);
	}

	/**
	 * get方式的网络请求方法
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static RequestHandle doGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if (Looper.myLooper() == Looper.getMainLooper())
			return client.get(url, params, responseHandler);
		return synClient.get(url, params, responseHandler);
		// client.
	}

	/**
	 * Post方式的网络请求方法
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static RequestHandle doPost(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		return client.post(url, params, responseHandler);
	}

	private NetUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (null != info && info.isConnected())
			return info.getState() == NetworkInfo.State.CONNECTED;
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context) {
		return getConnectType(context) == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * 判读是否数据连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileData(Context context) {
		return getConnectType(context) == ConnectivityManager.TYPE_MOBILE;
	}

	/**
	 * 获取连接类型
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectType(Context context) {
		NetworkInfo info = getNetworkInfo(context);
		if (info == null)
			return Integer.MIN_VALUE;
		return info.getType();
	}

	/**
	 * 获取网络状态信息
	 * 
	 * @param context
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null)
			return cm.getActiveNetworkInfo();
		return null;

	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}
	public static String getExceptionMsg(Throwable arg3,String arg2){
		String msg="";
		if (arg3 instanceof SocketTimeoutException
				|| arg3 instanceof ConnectTimeoutException) {
			msg = "请求超时!";
		} else if (arg3 instanceof HttpHostConnectException ||arg3  instanceof HttpResponseException) {

			msg =arg2==null? "网络连接不可用":"数据请求失败!";
		} else {
			msg = "网络连接不可用";
		}
		return msg;
	}

}
