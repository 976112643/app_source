package com.wq.support.utils.music;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import com.wq.support.net.FileDownHandler;

/**
 * 播放帮助类
 * @author WQ 下午4:53:39
 *
 */
public class MusicHelper implements MusicCode{
	/**
	 * 根据地址,获取缓存文件名
	 * @param path
	 * @return
	 */
	public static String getCacheMusicName(String path) {
		return String.valueOf(path.hashCode());
	}
	/**
	 * 根据地址,获取缓存文件
	 * @param context
	 * @param path
	 * @return
	 */
	public static String getCacheMusicFile(Context context, String path) {
		return FileDownHandler.getCacheDownFile(context,
				getCacheMusicName(path)).getAbsolutePath();
	}

	/**
	 * 注册播放状态监听
	 * @param context
	 * @param conn
	 */
	public static void registerMusicStateListener(Context context,ServiceConnection conn){
		Intent service=new Intent(context,MusicService.class);
		context.startService(service);
		context.bindService(service, conn, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * 开始
	 * @param context
	 * @param path
	 */
	public static void startMusic(Context context,String path){
		sendCodeIntent(context, PLAY, path, 0);
	}
	/**
	 * 暂停
	 * @param context
	 * @param path
	 */
	public static void pauseMusic(Context context,String path){
		sendCodeIntent(context, PAUSE, path, 0);
	}
	/**
	 * 停止
	 * @param context
	 * @param path
	 */
	public static void stopMusic(Context context,String path){
		sendCodeIntent(context, STOP, path, 0);
	}
	/**
	 * 调整进度,
	 * @param context
	 * @param path
	 * @param seek
	 */
	public static void seekToMusic(Context context,String path,int seek){
		sendCodeIntent(context, SEEKTO, path, seek);
	}
	
	/**
	 * 发送指令到播放服务
	 * @param context
	 * @param code
	 * @param path
	 * @param seek
	 */
	static void sendCodeIntent(Context context,int code,String path,long seek){
		Intent service=new Intent(context,MusicService.class);
		service.putExtra(CODE, code);
		service.putExtra(PATH, path);
		service.putExtra(SEEK, seek);
		context.startService(service);
	}
}
