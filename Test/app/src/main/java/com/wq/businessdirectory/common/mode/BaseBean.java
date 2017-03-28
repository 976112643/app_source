package com.wq.businessdirectory.common.mode;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @网络请求数据基础Bean
 */
public class BaseBean <T>{
	public int status = 0;
	public String msg;
	public T info;
	public int count;
	public boolean isCache=false;
	public Object tag;

	/** 默认获取当前时间作为请求时间 */
	public String post_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
	/**
	 * page_count : 2201
	 * page : 2201
	 * page_size : 5
	 */

	public int page_count;
	public int page;
	public int page_size;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BaseBean() {
	}

	public boolean isCache() {
		return isCache;
	}

	public void setCache(boolean isCache) {
		this.isCache = isCache;
	}

	@Override
	public String toString() {
		return String.valueOf(info);
	}

	@SuppressWarnings("unchecked")
	public <T> T Data() {
		return (T) info;
	}

	/**
	 * 检查bean是否有效
	 * 
	 * @return
	 */
	public boolean isVaild() {
		return status == 1 && info != null && !"".equals(info);
	}

	public boolean isEmpty() {
		return info == null || "".equals(info)||"[]".equals(info) ||"{}".equals(info);
	}
}
