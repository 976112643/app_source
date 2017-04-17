package com.wq.businessdirectory.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.wq.businessdirectory.services.BackgroundService.startService;

/**
 * 电话广播监听器
 */
public class PhoneBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //电话播出
            String phoneNumber = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            startService(context,phoneNumber,true);
        } else {
            //来电及其他状态改变
            startService(context,null,false);
        }
    }

}