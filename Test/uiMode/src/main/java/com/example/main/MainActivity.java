package com.example.main;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;

import com.wq.support.ui.UIBaseActivity;
import com.wq.support.utils.ToastUtil;
import com.wq.uicore.R;

public class MainActivity extends UIBaseActivity {
	/**
	 * 音频录制相关配置
	 */
	public static final int FREQUENCY = 16000;// 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	public static final int CHANNELCONGIFIGURATION = AudioFormat.CHANNEL_IN_MONO;// 设置单声道声道
	public static final int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;// 音频数据格式：每个样本16位
	public final static int AUDIO_SOURCE = MediaRecorder.AudioSource.CAMCORDER;// 音频获取源
	public int recBufSize=512;// 录音最小buffer大小
	public AudioRecord audioRecord;
	public String saveWavPath;// 保存wav文件路径

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		saveWavPath=new File(getCacheDir().getAbsolutePath() + "/","1.wav").getAbsolutePath();
		startService(new Intent(this, BackroundService.class));
	}

	public void click(View view) {
		if(audioRecord!=null){
			ToastUtil.shortM("录音结束");
			audioRecord.stop();
			audioRecord.release();
			return ;
		}
		ToastUtil.shortM("录音开始");
		recBufSize = AudioRecord.getMinBufferSize(FREQUENCY,
				CHANNELCONGIFIGURATION, AUDIOENCODING);// 录音组件
		audioRecord = new AudioRecord(AUDIO_SOURCE,// 指定音频来源，这里为麦克风
				FREQUENCY, // 16000HZ采样频率
				CHANNELCONGIFIGURATION,// 录制通道
				AUDIOENCODING,// 录制编码格式
				recBufSize);// 录制缓冲区大小 //先修改
		new Thread(new WriteRunnable(saveWavPath)).start();// 开线程写文件
	}

	/**
	 * 异步写文件
	 * 
	 * @author cokus
	 * 
	 */
	class WriteRunnable implements Runnable {
		String savePcmPath;
		
		public WriteRunnable(String savePcmPath) {
			super();
			this.savePcmPath = savePcmPath;
		}

		@Override
		public void run() {
			try {
				FileOutputStream fos2wav = null;
				File file2wav = null;
				try {
					file2wav = new File(savePcmPath);
					if (file2wav.exists()) {
						file2wav.delete();
					}
					fos2wav = new FileOutputStream(file2wav);// 建立一个可存取字节的文件
				} catch (Exception e) {
					e.printStackTrace();
				}
				short[] buffer = new short[recBufSize];
				audioRecord.startRecording();// 开始录制
				int readsize = 0;
				while (AudioRecord.ERROR_INVALID_OPERATION != (readsize = audioRecord
						.read(buffer, 0, recBufSize))) {
					fos2wav.write(short2Byte(buffer, readsize));
					fos2wav.flush();
				}

				fos2wav.close();
			} catch (Throwable t) {}
		}

		public byte[] short2Byte(short[] buffer, int readsize) {
			byte bys[] = new byte[readsize * 2];
			// 因为arm字节序问题，所以需要高低位交换
			for (int i = 0; i < readsize; i++) {
				byte ss[] = getBytes(buffer[i]);
				bys[i * 2] = ss[0];
				bys[i * 2 + 1] = ss[1];
			}
			return bys;
		}

		public byte[] getBytes(short s) {
			byte[] buf = new byte[2];
			for (int i = 0; i < buf.length; i++) {
				buf[i] = (byte) (s & 0x00ff);
				s >>= 8;
			}
			return buf;
		}
	}

}
