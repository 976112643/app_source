package com.wq.businessdirectory.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.sendMessageBroadcast;

/**
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public SMSBroadcastReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        LogUtils.d("----> SMSBroadcastReceiver onReceive");
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            PhoneMessage phoneMessage = SMSUtil.generateMessage(intent);
            sendMessageBroadcast(context,phoneMessage);
            //abortBroadcast();
        }

    }
}