package com.wq.support.net;


import com.wq.support.net.bean.BaseBean;

/**
 * Created by WQ on 2017/1/6.
 */

public interface NetResultDispatch {
   public boolean dispatch(final NAction action, BaseBean bean);
}
