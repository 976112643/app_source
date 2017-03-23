package com.wq.support.net;

/**
 * Created by WQ on 2017/2/27.
 */

public class BaseRequestPreproccess implements RequestPreproccess {
    @Override
    public NAction proccess(NAction action) {
        return action;
    }
}
