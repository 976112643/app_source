package com.example.main;

import com.wq.support.utils.log.Logger;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class DeamService extends Service implements ServiceConnection{
	@Override
	public void onCreate() {
		super.onCreate();
		Intent backroundService=new Intent(this, BackroundService.class);
		startService(backroundService);
		bindService(backroundService, this, Context.BIND_AUTO_CREATE );
		Logger.D("DeamService onCreate");
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Logger.D("BackroundService onServiceConnected");
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Logger.D("BackroundService onServiceDisconnected");
		Intent backroundService=new Intent(this, BackroundService.class);
		startService(backroundService);
	}

}
