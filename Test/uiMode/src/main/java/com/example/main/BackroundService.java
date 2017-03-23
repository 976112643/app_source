package com.example.main;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.wq.support.utils.log.Logger;

public class BackroundService extends Service implements ServiceConnection {
	@Override
	public void onCreate() {
		super.onCreate();
		Intent deamService=new Intent(this, DeamService.class);
		startService(deamService);
		bindService(deamService, this,Context.BIND_AUTO_CREATE );
		Logger.D("BackroundService onCreate");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Logger.D("DeamService onServiceConnected");
	}
	@Override
	public void onServiceDisconnected(ComponentName name) {
		Logger.D("DeamService onServiceDisconnected");
		Intent deamService=new Intent(this, DeamService.class);
		startService(deamService);
//		bindService(deamService, this, Context.BIND_AUTO_CREATE);
	}

}
