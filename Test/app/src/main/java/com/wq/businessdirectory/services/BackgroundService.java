package com.wq.businessdirectory.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.wq.businessdirectory.receiver.mode.PhoneRecord;
import com.wq.support.utils.DateUtil;
import com.wq.support.utils.log.Logger;

import io.realm.Realm;

import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.CALL_STATE_OUT;
import static com.wq.businessdirectory.receiver.LocalBroadcastHelper.sendPhoneBroadcast;

/**
 * 后台服务,用来监听通话状态,以及数据记录
 * Created by WQ on 2017/4/15.
 */

public class BackgroundService extends Service {
    private SharedPreferences sp;
    private PhoneStateListener phoneStateListener;//电话状态监听器
    private TelephonyManager telephonyManager ;//电话管理服务


    @Override
    public void onCreate() {
        super.onCreate();
        //设置一个监听器
        final Context context =this;
        telephonyManager = (TelephonyManager) getSystemService(Service.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener=new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
                super.onCallStateChanged(state, incomingNumber);
                sendPhoneBroadcast(context, state, incomingNumber);

                sp = context.getSharedPreferences("config", MODE_PRIVATE);

                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        PhoneRecord phoneRecord=new PhoneRecord();
                        phoneRecord.setPhone(sp.getString("phoneNumber",null));
                        phoneRecord.setType(sp.getBoolean("isCall",false)?"去电":"来电");
                        String start= DateUtil.getStringByFormat(sp.getLong("start_time",System.currentTimeMillis()),DateUtil.dateFormatYMDHMS);
                        phoneRecord.setRecord_time(start+" "+ DateUtil.getCurrentDate(DateUtil.dateFormatYMDHMS));
                        //电话挂断时,记录往数据库写入一条记录
                        PhoneRecord.addPhoneRecord(Realm.getDefaultInstance(),phoneRecord);
                        Logger.D("挂断: " + incomingNumber);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK://接听时,记录时间
                        sp.edit().putLong("start_time", System.currentTimeMillis()).commit();
                        Logger.D("接听: " + incomingNumber);
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        //来电时,记录来电号码,及通话方向
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isCall", false);
                        editor.putString("phoneNumber", incomingNumber);
                        editor.commit();
                        Logger.D("响铃:来电号码: " + incomingNumber);
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null && intent.getBooleanExtra("isCall",false)){
            sp = getSharedPreferences("config", MODE_PRIVATE);
            String phoneNumber=intent.getStringExtra("phoneNumber");
            //拨出电话的广播——仅呼出时出现-设置呼出标记-获得呼出号码和呼出时间
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isCall", true);
            editor.putString("phoneNumber", phoneNumber);
            editor.putLong("start_time", System.currentTimeMillis());
            editor.commit();
            Logger.D("拨打电话: " + phoneNumber);
            sendPhoneBroadcast(this, CALL_STATE_OUT, phoneNumber);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 启动服务
     * @param context
     * @param phoneNumber
     * @param isCall
     */
    public static void startService(Context context,String phoneNumber,boolean isCall){
        Intent intent=new Intent(context,BackgroundService.class);
        intent.putExtra("phoneNumber",phoneNumber);
        intent.putExtra("isCall",isCall);
        context.startService(intent);
    }

    @Override
    public void onDestroy() {
        if(telephonyManager!=null && phoneStateListener!=null) {
            //移除监听
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
