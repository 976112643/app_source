package com.wq.businessdirectory.base;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.wq.base.BaseApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by WQ on 2017/3/3.
 */

public class App extends BaseApplication {
    public App() {
//        https://beta.bugly.qq.com/pi9q
//        super(ShareConstants.TINKER_ENABLE_ALL, SampleApplicationLike.class.getName(),
//                "com.tencent.tinker.loader.TinkerLoader", false);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(this, "0c3b5f9893", true);
        CrashReport.initCrashReport(getApplicationContext(), "0c3b5f9893", true);
//        Realm.init(this);
//        Realm.getInstance()
//        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
//                .schemaVersion(1)
//                .deleteRealmIfMigrationNeeded()
//                .build());
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build());
    }
}
