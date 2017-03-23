package com.wq.support.utils.system;

import java.io.IOException;
import java.util.Calendar;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.wq.base.Config;
import com.wq.bean.LoginInfo;

/**
 * Created by WQ on 2017/1/13.
 */

public class VoiceUtil {

    /**
     * 判断是否为夜间
     * @return
     */
    public  static  boolean isNeedNofity(){
        LoginInfo info= Config.getLoginInfo();
        return  info.isNofity();
    }
    public  static  boolean isNight (){
        LoginInfo info= Config.getLoginInfo();
        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        if(hour>Config.nightMax ||hour<Config.nightMin){
            return  info.isNight();
        }
        return  false;
    }
    /**
     * 设置提醒模式
     * @param notification
     */
    public static void setSoundMode( Notification notification){
        LoginInfo info= Config.getLoginInfo();
        if(info.isNofity()){
            notification.defaults=0;
            if(info.isSOUND()){
                notification.defaults|= Notification.DEFAULT_SOUND;
            }
            if(info.isVIBRATE()){
                notification.defaults|=Notification.DEFAULT_VIBRATE;
                long[] vibrate =
                        { 0, 100, 200, 300 };
                notification.vibrate = vibrate;
            }
        }
    }

    /**
     * 播放消息通知
     * @param context
     */
    public static void playNofity(Context context){

        LoginInfo info= Config.getLoginInfo();
        if(isNight())return;
        if(!info.isNofity()){//是否需要系统通知,不需要则播放通知声音
            if(info.isSOUND()) {
                playNotifyVoice(context);
            }
            if(info.isVIBRATE()) {
                playVibrator(context);
            }
        }
    }

    /**
     * 播放通知
     * @param context
     * @throws IOException
     */
    public static void playNotifyVoice(Context context)  {
        try {
            // TODO Auto-generated method stub
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
                player.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                player.setLooping(false);
                player.prepare();
                player.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 振动
     * @param context
     */
    public static void playVibrator(Context context)
    {
        Vibrator vibrator;
        // 获取系统的Vibrator服务
        vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]
                { 0, 100, 200, 300 }, -1);
    }
}
