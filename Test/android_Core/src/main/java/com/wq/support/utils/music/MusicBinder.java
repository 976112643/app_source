package com.wq.support.utils.music;

import android.os.Binder;

public class MusicBinder extends Binder {
	MusicService  service;

	public MusicBinder(MusicService service) {
		super();
		this.service = service;
	}
	
	public MusicService getService(){
		return this.service;
	}
}
