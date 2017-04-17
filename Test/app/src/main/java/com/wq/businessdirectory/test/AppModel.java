package com.wq.businessdirectory.test;

import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by WQ on 2017/4/17.
 */

@Module
public class AppModel {
    Context mContext;

    public AppModel(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    Context provideContext(){
        return  mContext;
    }
    @Provides
    LocalBroadcastManager provideLocalBroadcastManager(Context context){
      return   LocalBroadcastManager.getInstance(context);
    }
    @Provides
    Realm provideRealm(){
        return  Realm.getDefaultInstance();
    }
}
