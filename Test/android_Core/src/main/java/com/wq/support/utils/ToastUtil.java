package com.wq.support.utils;

import android.app.Activity;
import android.widget.Toast;

import com.wq.base.BaseApplication;
import com.wq.support.net.bean.BaseBean;

/**
 * 吐司显示工具
 * @author WQ 下午2:36:20
 */
public class ToastUtil {
	
	/**
	 * 显示短提示,可在线程中使用
	 * @param act
	 * @param msg
	 */
	public static void shortM(Activity act,final Object msg){
		act.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				shortM(msg);
			}
		});
	}
	/**
	 * 显示长提示,可在线程中使用
	 * @param act
	 * @param msg
	 */
	public static void longM(Activity act,final Object msg){
		act.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				longM(msg);
			}
		});
	}
	
	/**
	 * 显示短Toast
	 * 
	 * @param msg
	 */
	public static void shortM(Object msg) {
		if (msg instanceof BaseBean) {
			if (EmptyDeal.isEmpy(((BaseBean) msg).msg)) {
				msg = ((BaseBean) msg).data;
			} else {
				msg = ((BaseBean) msg).msg;
			}
			Toast.makeText(BaseApplication.getInstance(), String.valueOf(msg),
					Toast.LENGTH_SHORT).show();
		}else if( msg instanceof Integer){
			Toast.makeText(BaseApplication.getInstance(), (Integer)msg,
					Toast.LENGTH_SHORT).show();
		}
	}

/**
	 * 
	 * 显示长Toast
	 * 
	 * @param msg
	 */
	public static void longM(Object msg) {
		if (msg instanceof BaseBean) {
			if (EmptyDeal.isEmpy(((BaseBean) msg).msg)) {
				msg = ((BaseBean) msg).data;
			} else {
				msg = ((BaseBean) msg).msg;
			}
			Toast.makeText(BaseApplication.getInstance(), String.valueOf(msg),
					Toast.LENGTH_LONG).show();
		}else if(msg instanceof Integer){
			
			Toast.makeText(BaseApplication.getInstance(), (Integer)msg,
					Toast.LENGTH_LONG).show();
		}
	}
}
