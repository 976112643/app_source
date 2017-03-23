package com.wq.support.dialogs;

import android.app.Dialog;
import android.widget.TextView;

import com.wq.support.net.NetRequestHandler;
import com.wq.support.ui.intf.DialogInterface;
import com.wq.support.uibase.BaseActivity;
import com.wq.support.uibase.BaseFragment;
import com.wq.support.utils.AHandler;
import com.wq.uicore.R;

import java.util.TimerTask;

public class NetRequestDialog implements DialogInterface {
    protected Dialog loadDialog;
    protected TimerTask mTask;
    protected BaseActivity activity;
    protected BaseFragment fragment;
    public NetRequestDialog(BaseActivity activity) {
        this.activity = activity;
    }

    public NetRequestDialog(BaseFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * 创建加载对话框 由子类来实现具体显示什么样子的对话框
     *
     * @return
     */
     Dialog createLoadDialog() {
        return DialogFactory.createLoadDialog(activity, "");
    }

    public void cancelLoadDialog() {
        if (getRequestNumber() == 0 && loadDialog != null
                && loadDialog.isShowing()) {
            loadDialog.dismiss();
        }
    }

    private int getRequestNumber() {
        if (fragment != null)
            return fragment.getRequestNumber();
        if (activity != null)
            return activity.getRequestNumber();
        return 0;
    }


    /**
     * 显示加载对话框,已创建时,执行更新操作
     *
     * @param msg
     * @param type
     */
    public void showLoadDialog(String msg, int type) {
        if ((loadDialog == null) && !isFinishing()) {
            loadDialog = createLoadDialog();
        }
        updateLoadDialog(loadDialog, msg, type);
        if (!loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    /**
     * 延时显示加载对话框
     *
     * @param msg
     * @param type
     * @param dealy
     */
    public void showLoadDialog(final String msg, final int type, long dealy) {
        AHandler.runTask((AHandler.Task) (mTask = new AHandler.Task() {
            @Override
            public void update() {
                if (isFinishing())
                    return;
                showLoadDialog(msg, type);
            }
        }), dealy);
    }

    private boolean isFinishing() {
        if (fragment != null)
            return fragment.isDetached();
        if (activity != null)
            return activity.isFinishing();
        return false;
    }

    /**
     * 更新加载对话框 由子类实现数据更新逻辑
     *
     * @param dialog
     * @param msg
     * @param type
     * @return
     */
    public Dialog updateLoadDialog(Dialog dialog, String msg, int type) {
        TextView msgText = (TextView) dialog.findViewById(R.id.msg);
        msgText.setText(msg);
        return dialog;
    }

    public void onDestroy() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        cancelLoadDialog();
    }
}