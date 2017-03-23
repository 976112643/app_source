package com.wq.support.net;

/**
 * Created by WQ on 2017/2/27.
 */

public interface ResultDataConvert<T>{

    public <T> T convert(NAction action, String json);

}
