package com.wq.businessdirectory.test;


import android.content.Context;

import dagger.Component;

/**
 * Created by WQ on 2017/4/17.
 */
@Component(modules = AppModel.class)
public interface AppComponet {
   void inject(TestFragment testFragment);
}
