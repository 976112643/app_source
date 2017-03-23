package com.wq.support.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * @时间: 2016/2/25 11:30
 * @功能描述:dp、sp 转换为 px 的工具类
 */

public class DisplayUtil {
	public static int WHD[];

	/**
	 * 获取设备宽高密度等信息信息
	 * 
	 * @param act
	 */
	public static int[] initWHD(Activity act) {
		if (WHD == null)
			WHD = WHD(act);
		return WHD;
	}

	/**
	 * 获取宽高密度信息
	 * 
	 * @param context
	 * @return [0]宽 [1]高 [2]密度
	 */
	public static int[] WHD(@NonNull Context context) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		WindowManager mm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mm.getDefaultDisplay().getMetrics(outMetrics);
		return new int[]
		{ outMetrics.widthPixels, outMetrics.heightPixels,
				(int) outMetrics.density };
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取默认字体,解决系统设置自定义字体大小后,App显示错乱的问题
	 * @param sourceResource
	 * @return
     */
	public static Resources getDefaultFontResource(Resources sourceResource){
		Resources res = sourceResource;
		Configuration config=new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config,res.getDisplayMetrics() );
		return res;
	}
}