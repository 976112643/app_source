package com.wq.support.net;

/**
 * 网络请求接口,回调模式
 * @author WQ 下午3:17:56
 */
public interface NetRequestCallback {
	public void doGet(NAction action,JsonHandler handler);
	public void doPost(NAction action,JsonHandler handler);
}
