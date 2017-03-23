package com.wq.support.utils.music;

public interface OnMusicStateListener {
	public void onStart(String path,long length);
	public void onPause(String path,long currentSeek,long length);
	public void onStop(String path,long currentSeek,long length);
	public void onProgressChange(String path,long currentSeek,long length);
	public void onErr(String path,long currentSeek,long length);
	public void onComplete(String path);
	public void onDestory(String path);
}
