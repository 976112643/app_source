package com.wq.businessdirectory.base;

import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.webkit.WebView;

import com.facebook.stetho.Stetho;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
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
//        Realm.setDefaultConfiguration(new RealmConfiguration.Builder()
//                .schemaVersion(1)
//                .deleteRealmIfMigrationNeeded()
//                .build());
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build());
        Stetho.initialize(//Stetho初始化
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

    }
}
