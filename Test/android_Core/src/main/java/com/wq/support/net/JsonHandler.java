package com.wq.support.net;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import com.wq.support.net.bean.BaseBean;
import com.wq.support.utils.ToastUtil;
import com.wq.support.utils.log.Logger;
import com.google.gson.internal.$Gson$Types;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * 封装json处理的网络请求句柄
 * @author WQ 下午5:45:21
 * @param <T>
 */
public class JsonHandler<T> extends TextHttpResponseHandler {
	public Type mType;
	NAction action;
	TextHttpResponseHandler handler;
	public JsonHandler() {
		mType = getSuperclassTypeParameter(getClass());
	}
	
	public JsonHandler(NAction action) {
		this.action=action;
	}
	
	public void setNAction(NAction action){
		this.action=action;
	}

	public void setHandler(TextHttpResponseHandler handler) {
		this.handler = handler;
	}

	static Type getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) { 
//			throw new RuntimeException("Missing type parameter.");
		return null;	
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
	}
	@Override
	public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
		if (arg3 instanceof SocketTimeoutException
				|| arg3 instanceof ConnectTimeoutException) {
			ToastUtil.shortM("请求超时!");
		} else if (arg3 instanceof HttpHostConnectException ||arg3  instanceof HttpResponseException) {
			ToastUtil.shortM(arg2==null? "网络连接不可用":"数据请求失败!");
		} else {
			ToastUtil.shortM("网络连接不可用");
		}
		if(handler!=null){
			handler.onFailure(arg0, arg1, arg2, arg3);
		}
		Logger.E("请求失败:"+arg2+ " "+arg3);
	}
	public  void onSuccess(BaseBean bean,int status,String msg){
		try{
		onSuccess((T) bean.Data(), status, msg);
		}catch (Exception e){
			e.printStackTrace();
			onSuccess((T)null, status, msg);
		}
	}
	public  void onSuccess(T t,int status,String msg){
		
	}
	@Override
	public void onFinish() {
		super.onFinish();
		if(handler!=null){
			handler.onFinish();
		}
	}
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		super.onCancel();
		if(handler!=null){
			handler.onCancel();
		}
	}
	@Override
	public void onSuccess(int arg0, Header[] arg1, String arg2) {
		Logger.D("请求地址 "+	getRequestURI()+"");
		Logger.D("请求完毕:"+arg2);
		BaseBean bean=null;
		if(mType!=null){
			bean=JsonDeal.createBean(arg2, mType);
		}else {
			if(action!=null){
				if(action.resultDataType!=null){
					bean=JsonDeal.createBean(arg2, action.resultDataType);
				}else if(action.typeToken!=null){
					bean=JsonDeal.createBean(arg2, action.typeToken.getType());
				}
			}
		}
		onSuccess(bean, bean.status, bean.msg);
		if(handler!=null){
			handler.onSuccess(arg0, arg1, arg2);;
		}
	}

}
