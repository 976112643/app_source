package com.wq.support.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.wq.support.utils.PathDeal;

/**
 * 视图工具,视图相关工具类
 */
public class UiUtils {

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
	 * 设置视图宽高(含weight属性时无效)
	 * 
	 * @param view
	 * @param W
	 * @param H
	 */
	public static void setViewWH(View view, int W, int H) {
		if (view == null)
			return;
		android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
		if (params == null)
			return;
		if (W > 0)
			params.width = W;
		if (H > 0)
			params.height = H;
		view.setLayoutParams(params);
	}
//----------------------------------------px 与其他单位互转-------------------------------------------
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
	 * 将px值转换为dp值
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dp值转换为px值
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}




	

	

	

	/**
	 * 当EditText获得焦点时自动隐藏提示文字
	 * 
	 * @param edit
	 */
	public static void hideHintTextOnFocus(final EditText... edits) {
		for (final EditText edit : edits) {
			edit.setOnFocusChangeListener(new EditText.OnFocusChangeListener() {
				String hintText = edit.getHint().toString();

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						edit.setHint("");
					} else {
						edit.setHint(hintText);
					}
				}
			});
		}
	}


	/**
	 * 调用拍照
	 * 
	 * @param activity
	 * @return
	 */
	public static Uri startCamera(Activity activity) {
		Uri cameraUri = null;
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			File file = new File(path, System.currentTimeMillis() + ".jpg");
			cameraUri = Uri.fromFile(file);
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		}
		activity.startActivityForResult(intentFromCapture, 1);
		return cameraUri;
	}

	/**
	 * 选择图片
	 * 
	 * @param activity
	 */
	public static void selectPhoto(Activity activity) {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		Log.d("weiquan ", "相册");
		activity.startActivityForResult(intentFromGallery, 2);
	}

//	/**
//	 * 选择图片,多选
//	 * 
//	 * @param activity
//	 */
//	public static void selectMuitPhoto(Activity activity, Intent intent) {
//		intent.setClass(activity, MulitImageAct.class);
//		Log.d("weiquan ", "相册");
//		activity.startActivityForResult(intent, 3);
//	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static void startPhotoZoom(Activity activity, Uri uri, int W, int H) {
		Log.d("weiquan ", "图片剪裁");
		Intent intent = new Intent("com.android.camera.action.CROP");
		 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {  
             String url=PathDeal.getPath(activity,uri);  
             intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");  
         }else{  
             intent.setDataAndType(uri, "image/*");  
         }  
		//intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", W);
		intent.putExtra("outputY", H);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, 0);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public static void startPhotoZoom(Activity activity, int aspectX,
			int aspectY, Uri uri) {
		Log.d("weiquan ", "图片剪裁");
		Intent intent = new Intent("com.android.camera.action.CROP");
		 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {  
             String url=PathDeal.getPath(activity,uri);  
             intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");  
         }else{  
             intent.setDataAndType(uri, "image/*");  
         }  
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		// outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", W);
		// intent.putExtra("outputY", H);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, 0);
	}

	private final static int UPPER_LEFT_X = 0;
	private final static int UPPER_LEFT_Y = 0;

	public static Drawable convertViewToDrawable(View view) {
		int spec = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(spec, spec);
		view.layout(UPPER_LEFT_X, UPPER_LEFT_Y, view.getMeasuredWidth(),
				view.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(c);
		view.setDrawingCacheEnabled(true);
		Bitmap cacheBmp = view.getDrawingCache();
		Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
		cacheBmp.recycle();
		view.destroyDrawingCache();
		return new BitmapDrawable(viewBmp);
	}

	/**
	 * 测算视图宽高
	 * @param view
	 */
	public static void measure(View view) {
		view.measure(0, 0);
	}

	/**
	 * 测量一个粗劣的文字行数
	 * 
	 * @param act
	 * @param paint
	 * @param txt
	 * @return
	 */
	public static float meauTextLine(Activity act, TextPaint paint, String txt,
			int max) {
		if (txt.length() == 0)
			return 0;
		float width = paint.measureText("字");
		int WH[] = initWHD(act);
		float numOfLine = WH[0] / width;// 一行多少字
		float lineNum = txt.length() / numOfLine;// 粗略的行数
		int num = 0;
		for (int i = 0, len = txt.length(); i < len; i++) {// 统计换行和回车的个数,
			char ch = txt.charAt(i);
			if (ch == '\r' || ch == '\n') {
				num++;
				if (lineNum + num >= max)
					return lineNum + num;
			}
		}
		lineNum = lineNum + num;
		return lineNum;

	}
	
	/**
	 * 重置webview中的图片
	 * @param webview
	 */
	public static void imgReset(WebView webview) {
		webview.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%';   " +
                "}" +
                "})()");
    }
	/**
	 * 计算ListView 高度
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) { return; }

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
