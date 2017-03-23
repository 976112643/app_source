package com.wq.support.ui.intf;

/**
 * Created by WQ on 2017/3/22.
 */

public  interface UIStatusCallback {
    public void loading(float progress);
    public void loadFinish();
    public void loadError(int errorCode);
}
