package com.wq.support.net;


import com.wq.support.net.bean.BaseBean;

/**
 * Created by WQ on 2017/2/27.
 */

public class BaseBeanConvert implements ResultDataConvert<BaseBean> {
    @Override
    public  BaseBean convert(NAction action, String json) {
        BaseBean bean ;
        if (action.resultDataType != null)
            bean = JsonDeal.createBean(json, action.resultDataType);
        else if (action.typeToken != null)
            bean = JsonDeal.createBean(json, action.typeToken);
        else
            bean = JsonDeal.createBean(json);
        bean.tag=action.getTag();
        return  bean;
    }
}
