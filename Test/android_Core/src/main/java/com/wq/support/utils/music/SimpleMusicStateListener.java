package com.wq.support.utils.music;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class SimpleMusicStateListener implements OnMusicStateListener,
		ServiceConnection {
	public MusicService musicService;

	@Override
	public void onStart(String path, long length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause(String path, long currentSeek, long length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop(String path, long currentSeek, long length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProgressChange(String path, long currentSeek, long length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onErr(String path, long currentSeek, long length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestory(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		musicService = ((MusicBinder) service).getService();
		musicService.addOnMusicStateListener(this);
		if(musicService.isPlaying()){
			onStart(musicService.playPath, musicService.getDuration());
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		musicService.removeMusicStateListener(this);
	}

}
