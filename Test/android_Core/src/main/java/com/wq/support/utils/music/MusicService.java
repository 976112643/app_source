package com.wq.support.utils.music;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import com.wq.support.net.FileDownHandler;
import com.wq.support.utils.log.Logger;
import com.loopj.android.http.RequestHandle;

/**
 * 音乐播放服务
 * @author WQ 上午11:06:50
 *
 */
public class MusicService extends Service implements MusicCode,
		OnErrorListener, OnCompletionListener, OnPreparedListener,
		OnMusicStateListener, Runnable {
	boolean isRun = true;
	MediaPlayer mPlayer = new MediaPlayer();
	Set<OnMusicStateListener> onMusicStateListeners = new HashSet<OnMusicStateListener>();
	long mCurrentPosition, mDuration;
	boolean isPlaying = false;
	String playPath = "";
	RequestHandle handler;

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.D("MusicService onCreate");
		mPlayer.setOnErrorListener(this);
		mPlayer.setOnCompletionListener(this);
		mPlayer.setOnPreparedListener(this);
		Thread musicProgressThread = new Thread(this);
		musicProgressThread.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MusicBinder(this);
	}

	public void removeMusicStateListener(
			OnMusicStateListener onMusicStateListener) {
		onMusicStateListeners.remove(onMusicStateListener);
	}

	/**
	 * 注册音乐播放状态监听
	 * 
	 * @param onMusicStateListener
	 */
	public void addOnMusicStateListener(
			OnMusicStateListener onMusicStateListener) {
		this.onMusicStateListeners.add(onMusicStateListener);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.hasExtra(CODE)) {
			int code = intent.getIntExtra(CODE, ERR);
			doCode(intent, code);
		}
		return START_STICKY;
	}

	/**
	 * 处理播放指令
	 * 
	 * @param intent
	 * @param code
	 */
	private void doCode(Intent intent, int code) {
		String path = intent.getStringExtra(PATH);
		long seekTo = intent.getLongExtra(SEEK, ERR);
		switch (code) {
		case PLAY:
			play(path);
			break;
		case PAUSE:
			pause(path);
			break;
		case STOP:
			stop(path);
			break;
		case SEEKTO:
			seekTo(path, seekTo);
			break;
		case DESTROY:
			destory(path);
			break;
		case DOWN:
			down(path);
			break;
		default:
			break;
		}
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	private void down(String path) {
		checkLocaFileAndDown(path);
	}

	private void destory(String path) {
		stopThread();
		mPlayer.reset();
		mPlayer.release();
		onDestory(path);
		if (handler != null) {
			handler.cancel(true);
		}

		stopSelf();
	}

	private void seekTo(String path, long seekTo) {
		if (!mPlayer.isPlaying()) {
			play(path);
		}
		mPlayer.seekTo((int) seekTo);
	}

	private void stop(String path) {
		isPlaying = false;
		wakeUpThread();
		if (mPlayer.isPlaying()) {
			mPlayer.stop();
		}
		onStop(path, getCurrentPosition(), getDuration());
	}

	private void pause(String path) {
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
		}
		isPlaying = false;
		wakeUpThread();
		onPause(path, getCurrentPosition(), getDuration());
	}

	private void complete(String path) {
		isPlaying = false;
		wakeUpThread();
		onStop(path, getCurrentPosition(), getDuration());
		onComplete(path);
	}

	/**
	 * 播放音频
	 * 
	 * @param path
	 */
	private void play(String path) {
		boolean isErr = false;
		try {
			try {
				if (this.playPath.equals(path)) {
					if (isPlaying() && mPlayer.isPlaying()) {
						// pause(path);
					} else {
						mPlayer.start();
						isPlaying = true;
						onStart(playPath, getDuration());
						wakeUpThread();
					}
					return;
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			playPath = path;
			Logger.D("播放: " + playPath);
			path = checkLocaFileAndDown(path);
			mPlayer.reset();
			mPlayer.setDataSource(path);
			mPlayer.prepare();
			mPlayer.start();
			isPlaying = true;
			mDuration = mPlayer.getDuration();
			wakeUpThread();
			onStart(path, getDuration());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			isErr = true;
		} catch (SecurityException e) {
			e.printStackTrace();
			isErr = true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			isErr = true;
		} catch (IOException e) {
			e.printStackTrace();
			isErr = true;
		}
		if (isErr) {
			onErr(path, getCurrentPosition(), getDuration());
		}
	}

	private String checkLocaFileAndDown(String path) {
		if (isNetUrl(path)) {
			String filename = MusicHelper.getCacheMusicName(path);
			if (FileDownHandler.isExists(this, filename)) {
				Logger.D("使用缓存的音频 " + MusicHelper.getCacheMusicFile(this, path));
				return MusicHelper.getCacheMusicFile(this, path);
			}
			handler = FileDownHandler.startDown(this, path,
					new FileDownHandler(this, filename) {
						@Override
						public void onStart() {
							super.onStart();
						}

						@Override
						public void onProgress(long bytesWritten, long totalSize) {
							super.onProgress(bytesWritten, totalSize);
						}

						public void onSuccess(File file) {};

						public void onFailure(int status,
								org.apache.http.Header[] arg1, Throwable arg2,
								File arg3) {

						};

						@Override
						public void onFinish() {
							super.onFinish();
							handler = null;
						}
					}, false);
		}
		return path;
	}

	public long getCurrentPosition() {
		return mCurrentPosition;
	}

	public long getDuration() {
		return mDuration;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		complete(playPath);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		onErr(playPath, getCurrentPosition(), getDuration());
		stop(playPath);
		return true;
	}

	/**
	 * 唤醒进度更新线程
	 */
	public void wakeUpThread() {
		synchronized (MusicService.class) {
			MusicService.class.notifyAll();
		}
	}

	/**
	 * 停止线程
	 */
	public void stopThread() {
		isRun = false;
		isPlaying = false;
		synchronized (MusicService.class) {
			MusicService.class.notifyAll();
		}
	}

	@Override
	public void run() {
		while (isRun) {
			synchronized (MusicService.class) {
				if (!isPlaying()) {
					try {
						MusicService.class.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			getProgress();
			onProgressChange(playPath, getCurrentPosition(), getDuration());
			synchronized (MusicService.class) {
				try {
					// 延迟进度更新,减少资源消耗
					MusicService.class.wait(PROGRESS_UPDATE_DEALY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 判断是否是网络链接
	 * 
	 * @param path
	 * @return
	 */
	boolean isNetUrl(String path) {
		if (path.startsWith("http")) { return true; }
		return false;
	}

	/**
	 * 获取播放进度
	 */
	private void getProgress() {
		if (isPlaying() && mPlayer.isPlaying()) {
			mCurrentPosition = mPlayer.getCurrentPosition();
			mDuration = mPlayer.getDuration();
		}
	}

	@Override
	public void onDestroy() {
		destory(playPath);
		super.onDestroy();
	}

	/************************************ 播放器状态回调 *********************************************/
	@Override
	public void onStart(String path, long length) {
		Set<OnMusicStateListener> listeners = onMusicStateListeners;
		for (OnMusicStateListener onMusicStateListener : listeners) {
			if (onMusicStateListener != null) {
				onMusicStateListener.onStart(path, length);
			}
		}
	}

	@Override
	public void onPause(String path, long currentSeek, long length) {
		Set<OnMusicStateListener> listeners = onMusicStateListeners;
		for (OnMusicStateListener onMusicStateListener : listeners) {
			if (onMusicStateListener != null) {
				onMusicStateListener.onPause(path, currentSeek, length);
			}
		}
	}

	@Override
	public void onStop(String path, long currentSeek, long length) {
		Set<OnMusicStateListener> listeners = onMusicStateListeners;
		for (OnMusicStateListener onMusicStateListener : listeners) {
			if (onMusicStateListener != null) {
				onMusicStateListener.onStop(path, currentSeek, length);
			}
		}
	}

	@Override
	public void onProgressChange(String path, long currentSeek, long length) {
		Set<OnMusicStateListener> listeners = onMusicStateListeners;
		for (OnMusicStateListener onMusicStateListener : listeners) {
			if (onMusicStateListener != null) {
				onMusicStateListener
						.onProgressChange(path, currentSeek, length);
			}
		}
	}

	@Override
	public void onErr(String path, long currentSeek, long length) {
		Set<OnMusicStateListener> listeners = onMusicStateListeners;
		for (OnMusicStateListener onMusicStateListener : listeners) {
			if (onMusicStateListener != null) {
				onMusicStateListener.onErr(path, currentSeek, length);
			}
		}
	}

	@Override
	public void onComplete(String path) {
		Set<OnMusicStateListener> listeners = onMusicStateListeners;
		for (OnMusicStateListener onMusicStateListener : listeners) {
			if (onMusicStateListener != null) {
				onMusicStateListener.onComplete(path);
			}
		}
	}

	@Override
	public void onDestory(String path) {
		Set<OnMusicStateListener> listeners = onMusicStateListeners;
		for (OnMusicStateListener onMusicStateListener : listeners) {
			if (onMusicStateListener != null) {
				onMusicStateListener.onDestory(path);
			}
		}
	}
}
