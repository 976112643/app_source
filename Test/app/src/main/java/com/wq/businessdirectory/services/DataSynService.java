package com.wq.businessdirectory.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.wq.businessdirectory.services.mode.ActionMode;
import com.wq.businessdirectory.services.mode.CompanySynMode;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by WQ on 2017/3/21.
 */

public class DataSynService extends IntentService {
    private static final String TAG = "DataSynService";
    private LocalBroadcastManager mLocalBroadcastManager;
    Map<String, Class<? extends ActionMode>> actionMaps = new HashMap<>();

    public DataSynService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        mExceptionCode = Constants.NETWORK_EXCEPTION.DEFAULT;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ActionMode.execute(intent,actionMaps.get(action));
        ActionMode.execute(intent,actionMaps.get("def"));
    }

    private void sendResult(Intent intent, Realm realm, int fetched) {
        realm.close();

        Log.d(TAG, "finished fetching, actual fetched " + fetched);

        Intent broadcast = new Intent();
//        broadcast.putExtra(EXTRA_FETCHED, fetched)
//                .putExtra(EXTRA_TRIGGER, intent.getAction())
//                .putExtra(EXTRA_EXCEPTION_CODE, mExceptionCode)
//                .putExtra(EXTRA_TYPE, type);

        mLocalBroadcastManager.sendBroadcast(broadcast);
    }

    {
        actionMaps.put("companys", CompanySynMode.class);
    }
}
