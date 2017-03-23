package com.wq.support.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Base64;

import com.wq.base.Config;

/**
 * 常用工具类
 * 
 * @author cnsunrun
 * 
 */
public class Utils {
	private static long time;

	public static String getDistance(String latS, String lngS) {
		return getDistance(latS, lngS, Config.getDataCache("lat"),
				Config.getDataCache("lng"));
	}

	public static String getDistance(String latS, String lngS, String latS2,
			String lngS2) {
		String distanceDestript = "0";
		double lat = valueOf(latS, 0);
		double lng = valueOf(lngS, 0);
		double lat2 = valueOf(latS2, 0);
		double lng2 = valueOf(lngS2, 0);
		if (lat == 0 || lng == 0 || lat2 == 0 || lng2 == 0) { return distanceDestript; }
		double distance = getDistance(lng, lat, lng2, lat2);
		if (distance == 0) { return "0"; }
		if (distance < 1000) {
			distanceDestript = String.format("%d米", (int) distance);
		} else {
			distance = distance / 1000;
			distanceDestript = String.format("%.1f公里", distance);
		}
		return distanceDestript;
	}

	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param long1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param long2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	public static double getDistance(double long1, double lat1, double long2,
			double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
						* Math.cos(lat2) * sb2 * sb2));
		return d;
	}

	/**
	 * 隐藏电话号码中间数字
	 * 
	 * @param phone
	 * @return
	 */
	public static String hiddenPhone(String phone) {
		if (phone.length() > 6) {
			String temp = "*********************";
			phone.subSequence(3, phone.length() - 3);
			return phone.substring(0, 3)
					.concat(temp.substring(0, phone.length() - 6))
					.concat(phone.substring(phone.length() - 3));
		}
		return phone;
	}

	/**
	 * 格式化金额,每千位","号分隔
	 * 
	 * @param money
	 * @return
	 */
	public static String formatMoney(String money) {
		StringBuffer sb = new StringBuffer(money);
		int index = money.indexOf(".");
		index = index == -1 ? money.length() : index;
		for (int len = money.length(), i = len - 1; i >= 0; i--)
			if (i < index && (len - i - 1 - 2) % 3 == 0 && i != 0)
				sb.insert(i, ',');
		return sb.toString();
	}

	/**
	 * 判断是否快速点击
	 * 
	 * @param mm
	 *            间隔时间(毫秒)
	 * @return
	 */
	public static boolean isQuck(long mm) {
		long temp = System.currentTimeMillis();
		if (temp - time <= mm)
			return true;
		time = temp;
		return false;
	}

	public static String lastTime() {
		lastTime = System.currentTimeMillis();
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(
				lastTime));
	}

	public static <T> T search(List<T> data, Object... objs) {
		for (T t : data) {
			for (Object obj : objs)
				if (t.equals(obj)) { return t; }
		}
		return null;
	}

	public static long lastTime = System.currentTimeMillis();

	/**
	 * Base 64加密
	 * 
	 * @param str
	 * @return
	 */
	public static String base64Encode(String str) {
		if (str == null)
			return null;
		return new String(Base64.encode(str.getBytes(), Base64.DEFAULT)).trim();
	}

	/**
	 * Base 64解密
	 * 
	 * @param str
	 * @return
	 */
	public static String base64Decode(String str) {
		if (str == null)
			return null;
		return new String(Base64.decode(str, Base64.DEFAULT));
	}

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5(String string) {
		try {
			return getMD5(string.getBytes("utf-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getMD5(byte[] hash) {
		try {
			hash = MessageDigest.getInstance("MD5").digest(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (Exception e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * 获取当前进程名字
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) { return appProcess.processName; }
		}
		return "";
	}

	/**
	 * 字符串转float(忽略错误)
	 * 
	 * @param value
	 * @param defValue
	 * @return
	 */
	public static float valueOf(String value, float defValue) {
		try {
			return Float.parseFloat(String.valueOf(value));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defValue;
	}

	// /**
	// * 去除字符串左右指定字符 ,类似于去除左右空格 trim
	// * @param feed_lable
	// * @param str
	// * @return
	// */
	// public static String format(String feed_lable,String str){
	// System.out.println(feed_lable);
	// if(feed_lable.startsWith(str))
	// feed_lable= format(feed_lable.substring(feed_lable.indexOf(str)+1),str);
	// if(feed_lable.endsWith(str))
	// feed_lable= format(feed_lable.substring(0,
	// feed_lable.lastIndexOf(str)),str);
	// return feed_lable;
	// }
	/**
	 * 集合内容字符串拼接
	 * 
	 * @param data
	 * @param toStringEnable
	 * @param <T>
	 * @return
	 */
	public static <T> String arrayToString(T[] data,
			ToStringEnable<T> toStringEnable) {
		StringBuilder sb = new StringBuilder();
		sb.append(toStringEnable.startString());
		if (data != null)
			for (int i = 0; i < data.length; i++) {
				if (i != 0) {
					sb.append(toStringEnable.splitString());
				}
				sb.append(toStringEnable.getString(data[i]));
			}
		sb.append(toStringEnable.endString());
		return sb.toString();
	}

	/**
	 * 集合内容字符串拼接
	 * 
	 * @param data
	 * @param toStringEnable
	 * @param <T>
	 * @return
	 */
	public static <T> String listToString(List<T> data,
			ToStringEnable<T> toStringEnable) {
		StringBuilder sb = new StringBuilder();
		sb.append(toStringEnable.startString());
		if (data != null)
			for (int i = 0; i < data.size(); i++) {
				if (i != 0) {
					sb.append(toStringEnable.splitString());
				}
				sb.append(toStringEnable.getString(data.get(i)));
			}
		sb.append(toStringEnable.endString());
		return sb.toString();
	}

	public static class DefaultToString<T> implements ToStringEnable<T> {
		String splitStr = ",", startStr = "", endStr = "";

		public DefaultToString(String splitStr, String startStr, String endStr) {
			this.splitStr = splitStr;
			this.startStr = startStr;
			this.endStr = endStr;
		}

		public DefaultToString(String splitStr) {
			this.splitStr = splitStr;
		}

		@Override
		public String splitString() {
			return splitStr;
		}

		@Override
		public String getString(T t) {
			return String.valueOf(t);
		}

		@Override
		public String startString() {
			return startStr;
		}

		@Override
		public String endString() {
			return endStr;
		}
	}

	public interface ToStringEnable<T> {
		public String splitString();

		public String getString(T t);

		public String startString();

		public String endString();
	}

	// 休眠
	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
