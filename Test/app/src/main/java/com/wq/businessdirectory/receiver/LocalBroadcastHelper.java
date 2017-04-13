package com.wq.businessdirectory.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by WQ on 2017/4/7.
 */

public class LocalBroadcastHelper {
    public static final String ACTION_MESSAGE = "action_phone_message";
    public static final String ACTION_PHONE = "action_phone";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";
    public static final String EXTRA_PHONE_STATE = "EXTRA_PHONE_STATE";
    public static final int CALL_STATE_OUT = 0x0911;
    public static void sendMessageBroadcast(Context context, PhoneMessage phoneMessage) {
        Intent broadcast = new Intent(ACTION_MESSAGE)
                .putExtra(EXTRA_MESSAGE, phoneMessage);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcast);
    }

    public static void sendPhoneBroadcast(Context context, int state, String incomingNumber) {
        Intent broadcast = new Intent(ACTION_PHONE)
                .putExtra(EXTRA_PHONE_STATE, state)
                .putExtra(EXTRA_PHONE_NUMBER, incomingNumber);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcast);
    }

    public static IntentFilter getMessageFilter(){
       return new IntentFilter(ACTION_MESSAGE);
    }
    public static IntentFilter getPhoneFilter(){
        return new IntentFilter(ACTION_PHONE);
    }
}
