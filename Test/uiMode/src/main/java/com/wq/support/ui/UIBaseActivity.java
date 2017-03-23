package com.wq.support.ui;

import android.app.Dialog;
import android.widget.TextView;

import com.wq.base.BaseApplication;
import com.wq.support.dialogs.DialogFactory;
import com.wq.support.dialogs.NetRequestDialog;
import com.wq.support.ui.intf.DialogInterface;
import com.wq.support.uibase.BaseActivity;
import com.wq.support.uibase.inf.UIDialog;
import com.wq.support.utils.AHandler;
import com.wq.uicore.R;

import java.util.TimerTask;

/**
 * UI基础Activity 目前只封装了对话框创建和显示方法的实现
 * @author WQ 下午5:50:29
 */
public class UIBaseActivity extends BaseActivity implements DialogInterface {
	NetRequestDialog netRequestDialog=new NetRequestDialog(this);
	public void cancelLoadDialog() {
		netRequestDialog.cancelLoadDialog();
	}

	public void showLoadDialog(String msg, int type) {
		netRequestDialog.showLoadDialog(msg, type);
	}
	public void showLoadDialog(final String msg, final int type, long dealy) {
		netRequestDialog.showLoadDialog(msg, type, dealy);
	}
	@Override
	public void requestFinish() {
		cancelLoadDialog();
	}

	@Override
	public void requestCancel() {
		cancelLoadDialog();
	}
	@Override
	protected void onDestroy() {
		netRequestDialog.onDestroy();
		super.onDestroy();
	}
}
