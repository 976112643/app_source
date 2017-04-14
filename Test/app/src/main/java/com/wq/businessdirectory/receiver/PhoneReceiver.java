package com.wq.businessdirectory.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.wq.support.utils.DateUtil;
import com.wq.support.utils.log.Logger;

import io.realm.Realm;

import static android.content.Context.MODE_PRIVATE;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.CALL_STATE_OUT;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.sendMessageBroadcast;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.sendPhoneBroadcast;

public class PhoneReceiver extends BroadcastReceiver {
    private SharedPreferences sp;
    @Override
    public void onReceive(final Context context, Intent intent) {
        System.out.println("action" + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            sp = context.getSharedPreferences("config", MODE_PRIVATE);
            //拨出电话的广播——仅呼出时出现-设置呼出标记-获得呼出号码和呼出时间
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isCall", true);
            editor.putString("phoneNumber", phoneNumber);
            editor.putLong("start_time", System.currentTimeMillis());
            editor.commit();
            Logger.D("拨打电话: " + phoneNumber);
            sendPhoneBroadcast(context, CALL_STATE_OUT, phoneNumber);
        } else {
            //查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
            //如果我们想要监听电话的拨打状况，需要这么几步 :
            /**第一：获取电话服务管理器TelephonyManager manager = this.getSystemService(TELEPHONY_SERVICE);
             *第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new MyPhoneStateListener(),
             * PhoneStateListener.LISTEN_CALL_STATE);
             这里的PhoneStateListener.LISTEN_CALL_STATE就是我们想要
             * 监听的状态改变事件，初次之外，还有很多其他事件哦。
             *第三步：通过extends PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
             *第四步：这一步很重要，那就是给应用添加权限。android.permission.READ_PHONE_STATE*/
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(new PhoneStateListener() {

                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
                    super.onCallStateChanged(state, incomingNumber);
                    sendPhoneBroadcast(context, state, incomingNumber);

                    sp = context.getSharedPreferences("config", MODE_PRIVATE);

                    switch (state) {
                        case TelephonyManager.CALL_STATE_IDLE://变为空闲
                            PhoneRecord phoneRecord=new PhoneRecord();
                            phoneRecord.setPhone(sp.getString("phoneNumber",null));
                            phoneRecord.setType(sp.getBoolean("isCall",false)?"去电":"来电");
                            String start= DateUtil.getStringByFormat(sp.getLong("start_time",System.currentTimeMillis()),DateUtil.dateFormatYMDHMS);
                            phoneRecord.setRecord_time(start+" "+DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS));
                            PhoneRecord.addPhoneRecord(Realm.getDefaultInstance(),phoneRecord);
                            Logger.D("挂断: " + incomingNumber);
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK://接听时
                            sp.edit().putLong("start_time", System.currentTimeMillis()).commit();
                            Logger.D("接听: " + incomingNumber);
                            break;
                        case TelephonyManager.CALL_STATE_RINGING:
                            //响铃——仅呼入时出现-设置呼入标记-获得呼入电话和呼入时间

                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isCall", false);
                            editor.putString("phoneNumber", incomingNumber);
                            editor.commit();

                            Logger.D("响铃:来电号码: " + incomingNumber);
                            break;
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);

            //设置一个监听器
        }
    }

}