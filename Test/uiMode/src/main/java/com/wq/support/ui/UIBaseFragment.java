package com.wq.support.ui;

import android.app.Dialog;
import android.widget.TextView;

import com.wq.support.dialogs.DialogFactory;
import com.wq.support.dialogs.NetRequestDialog;
import com.wq.support.ui.intf.DialogInterface;
import com.wq.support.uibase.BaseFragment;
import com.wq.support.uibase.inf.UIDialog;
import com.wq.uicore.R;
/**
 *  UI基础Fragment 目前只封装了对话框创建和显示方法的实现
 * @author WQ 下午6:05:07
 */
public class UIBaseFragment extends BaseFragment  implements DialogInterface {

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
	public void onDestroyView() {
		netRequestDialog.onDestroy();
		super.onDestroyView();
	}
}
