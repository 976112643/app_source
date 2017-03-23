package com.wq.support.utils.music;

/**
 * 状态码,及指令集
 * @author WQ 下午4:53:53
 *
 */
public interface MusicCode {
	public static int ERR=0x000;
	public static int PLAY=0x001;
	public static int PAUSE=0x002;
	public static int STOP=0x003;
	public static int DOWN=0x004;
	public static int DESTROY=0x005;
	public static int SEEKTO=0x006;
	public static String CODE="music_code";
	public static String PATH="music_path";
	public static String SEEK="music_seek";
	public static long PROGRESS_UPDATE_DEALY=200;
}
