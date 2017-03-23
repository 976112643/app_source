package com.wq.support.ui.intf;

import android.app.Dialog;

import com.wq.support.uibase.inf.UIDialog;

public interface DialogInterface  extends UIDialog {
	public void cancelLoadDialog() ;
	/**
	 * 显示加载对话框,已创建时,执行更新操作
	 * @param msg
	 * @param type
	 */
	public void showLoadDialog(String msg, int type) ;

	/**
	 * 延时显示加载对话框
	 *
	 * @param msg
	 * @param type
	 * @param dealy
	 */
	public void showLoadDialog(final String msg, final int type, long dealy) ;
//	public Dialog updateLoadDialog(Dialog dialog, String msg, int type) ;
}
