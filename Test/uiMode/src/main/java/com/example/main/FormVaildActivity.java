//package com.example.main;
//
//import java.io.IOException;
//
//import android.app.Dialog;
//import android.content.ComponentName;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//import android.view.View;
//import android.widget.Button;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//
//import com.cnsunrun.support.annotation.Click;
//import com.cnsunrun.support.annotation.ViewInject;
//import com.cnsunrun.support.ui.UIBaseActivity;
//import com.cnsunrun.support.uibase.BaseActivity;
//import com.cnsunrun.support.utils.ToastUtil;
//import com.cnsunrun.support.utils.music.MusicBinder;
//import com.cnsunrun.support.utils.music.MusicHelper;
//import com.cnsunrun.support.utils.music.SimpleMusicStateListener;
//
//@ViewInject(R.layout.ui_test)
//public class FormVaildActivity extends UIBaseActivity {
//	@ViewInject Button leftBtn, rightBtn, centerBtn;
//	@ViewInject SeekBar seekBar;
//	String path = "http://upload.qiyuinfo.com/uservoice/20170106/38ec120031b326cd46bb9019af2bee74.mp4";
//	boolean isPlay = false;
//
//	@Override
//	protected void initView() {
//		super.initView();
//		MusicHelper.registerMusicStateListener(this,
//				new SimpleMusicStateListener() {
//					@Override
//					public void onStart(String path, long length) {
//						centerBtn.setText("暂停");
//						isPlay = true;
//						super.onStart(path, length);
//					}
//					@Override
//					public void onProgressChange(String path, long currentSeek,
//							long length) {
//						seekBar.setProgress((int) currentSeek);
//						seekBar.setMax((int) length);
//						super.onProgressChange(path, currentSeek, length);
//					}
//					@Override
//					public void onPause(String path, long currentSeek,
//							long length) {
//						centerBtn.setText("播放");
//						isPlay = false;
//						super.onPause(path, currentSeek, length);
//					}
//
//					@Override
//					public void onStop(String path, long currentSeek,
//							long length) {
//						centerBtn.setText("播放");
//						isPlay = false;
//						super.onStop(path, currentSeek, length);
//					}
//					@Override
//					public void onErr(String path, long currentSeek, long length) {
//						ToastUtil.shortM("播放出错");
//						super.onErr(path, currentSeek, length);
//					}
//				});
//		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//
//			}
//
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				if(fromUser){
//					MusicHelper.seekToMusic(that, path, progress);
//				}
//			}
//		});
//
//		try {
//			DiskLruCache lruCache=DiskLruCache.open(getCacheDir(), 1, 20, 1024*1024*5);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Click(R.id.centerBtn)
//	public void centerBtn(View view) {
//		if (isPlay) {
//			MusicHelper.pauseMusic(this, path);
//		} else {
//			MusicHelper.startMusic(this, path);
//		}
//	}
//
//	@Override
//	protected void onDestroy() {
//		MusicHelper.stopMusic(this, path);
//		super.onDestroy();
//	}
//
//}
