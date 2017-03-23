package com.wq.support.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wq.support.net.NetRequestHandler;
import com.wq.support.ui.intf.DialogInterface;
import com.wq.support.utils.AHandler;
import com.wq.uicore.R;

import java.util.TimerTask;

/**
 * 对话框工厂,提供一些常用的对话框创建和显示,可自由扩展
 * @author WQ 上午11:19:10
 */
public class DialogFactory {
	
	
	public static Dialog createLoadDialog(Context context, String msg) {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.dialog_loading_, null);
		TextView msgText = (TextView) rootView.findViewById(R.id.msg);
		msgText.setText(msg);
		// ImageView img=(ImageView) rootView.findViewById(R.id.loading_anim);
		// playBackAnim(img,R.anim.dialog_loding);
	Dialog	dialog = new Dialog(context, R.style.CustomDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(rootView);
		dialog.show();
		return dialog;
	}
	/**
	 * 显示加载对话框
	 * 
	 * @param context
	 * @param msg
	 *            消息信息
	 */
//	public static Dialog showLoad(Context context, String msg) {
//		Dialog dialog = null;
//		cancelLoad(context);
//		View rootView = LayoutInflater.from(context).inflate(
//				R.layout.dialog_loading_, null);
//		TextView msgText = (TextView) rootView.findViewById(R.id.msg);
//		msgText.setText(msg);
//		// ImageView img=(ImageView) rootView.findViewById(R.id.loading_anim);
//		// playBackAnim(img,R.anim.dialog_loding);
//		dialog = new Dialog(context, R.style.CustomDialog);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.setContentView(rootView);
//		dialog.show();
////		dialogs.put(context, dialog);
//		return dialog;
//	}

//	public static void cancelLoad(Context context) {
//		final Dialog dialog = dialogs.get(context);
//		if (dialog != null) {
//			AHandler.runTask(new Task() {
//				@Override
//				public void update() {
//					dialog.cancel();
//				}
//			}, 1000);
//
//		}
//	}

//	/**
//	 * 显示加载对话框,默认文字为加载中.
//	 * 
//	 * @param context
//	 * @return
//	 */
//	public static Dialog showLoadDialog(Context context) {
//		return showLoadDialog(context, "加载中.");
//	}
	
	/**
	 * 创建对话框
	 * 
	 * @param context
	 * @param outCancel
	 *            是否可触摸外部取消
	 * @param msg
	 *            消息内容
	 * @param ok
	 *            确认按钮监听
	 * @param cancel
	 *            取消按钮监听
	 * @return
	 */
	public static Dialog createDialog(Context context, boolean outCancel,
			String msg, OnClickListener ok, OnClickListener cancel) {
		View dialogView = View.inflate(context, R.layout.dialog_alert_, null);
		if (msg != null)
			((TextView) dialogView.findViewById(R.id.msg)).setText(msg);
		dialogView.findViewById(R.id.submit).setOnClickListener(ok);
		dialogView.findViewById(R.id.cancel).setOnClickListener(cancel);
		return createDialog(context, dialogView, outCancel);
	}

	public static Dialog createDialog(Context context, View dialogView) {
		return createDialog(context, dialogView, 0, 0, true);
	}
	public static Dialog createDialog(Context context, View dialogView,
			boolean outCancel) {
		return createDialog(context, dialogView, 0, 0, outCancel);
	}

	public static Dialog createDialog(Context context, View dialogView,
			boolean outCancel, int dialogStyle) {
		return createDialog(context, dialogView, dialogStyle, 0, outCancel);
	}

	public static Dialog createDialog(Context context, View dialogView,
			int animStyle, boolean outCancel) {
		return createDialog(context, dialogView, 0, animStyle, outCancel);
	}
//	public static	void showConfim (Context that,String titleStr,String messageStr,String leftBtn,String rightBtn,boolean extrudeLeft,  final OnClickListener confimListener){
//		View dialogView = View.inflate(that, R.layout.dialog_confim, null);
//		final Dialog dialog = UiUtils.createDialog(that, dialogView, true);
//		TextView title=(TextView) dialogView.findViewById(R.id.title);
//		title.setText(titleStr);
//		
//		TextView message=(TextView) dialogView.findViewById(R.id.message);
//		message.setText(messageStr);
//		Button left=(Button) dialogView.findViewById(R.id.btn_cancel);
//		Button right=(Button) dialogView.findViewById(R.id.btn_confim);
//		if(leftBtn!=null){
//			left.setText(leftBtn);
//		}
//		if(rightBtn!=null){
//			right.setText(rightBtn);
//		}
//		if(!extrudeLeft){
//			left.setBackgroundResource(R.drawable.sel_delet_btn);
//			right.setBackgroundResource(R.drawable.sel_submit_y_btn);
//		}
//		left.setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dialog.cancel();
//					}
//				});
//		right.setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (confimListener != null) {
//							confimListener.onClick(v);
//						}
//						dialog.cancel();
//					}
//				});
//		dialog.show();
//	}
	
	public static Dialog createDialog(Context context, View dialogView,
			int dialogStyle, int animStyle, boolean outCancel) {
		if (dialogView == null)
			return null;
		final Dialog dialog = new Dialog(context,
				dialogStyle == 0 ? R.style.dialog : dialogStyle);
		dialog.getWindow().setWindowAnimations(
				animStyle == 0 ? R.style.dialogWindowAnim : animStyle);
		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(outCancel);
		dialog.show();
		if (outCancel) {
			View out = dialogView.findViewById(R.id.out);
			if (out != null)
				out.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		}
		return dialog;
	}


}
