package com.wq.support.net.down;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wq.support.net.down.DownloadRecord.STATUS;

/**
 * 文件下载器
 */
public class Downloader {
	int THREAD_NUM = 3;
	DownloadRecord record;// 下载记录类
	DownloaderCoreThread[] threads;// 下载器核心线程组
	private final int minBlockSize = 1024 * 1024 * 2;// 文件分块时的最小值,低于该值单线程下载
	Executor threadPool;// 下载线程池
	int rangeSize = minBlockSize;
	final int maxThreadNum = 4;// 最大线程数
	static Pattern patter = Pattern.compile("filename=\".*\"");// 匹配文件名

	public void start() {
		if (record.getStatus() == STATUS._PAUSE) {
			for (DownloaderCoreThread downloaderCoreThread : threads) {
				downloaderCoreThread.resume();
			}
			record.setStatus(STATUS._DOWNLOADING);
			return;
		}
		try {
			initThread();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DownloadRecord getRecord() {
		return record;
	}

	/**
	 * 计算线程数量
	 * 
	 * @param filesize
	 * @return
	 */
	protected int calThreadNum(long filesize) {
		int num = (int) (filesize / minBlockSize);
		if (num == 0) {
			num = 1;
		} else if (num > maxThreadNum) {
			num = maxThreadNum;
		}
		return num;
	}

	boolean initThread() throws IOException {
		URL url = new URL(record.downloadUrl);
		URLConnection conn = url.openConnection();
		// 读取下载文件总大小
		record.countSize = conn.getContentLength();
		record.setStatus(STATUS._DOWNLOADING);
		if (record.countSize <= 0 || "text/html".equals(conn.getContentType())) {
			// isFaild = true;
			record.setStatus(STATUS._FAILD);
			System.out.println("读取文件失败");
			return false;
		} else if (record.countSize <= rangeSize) {
			THREAD_NUM = 1;
		}
		conn.connect();
		THREAD_NUM = calThreadNum(record.countSize);
		pareHead(conn.getHeaderFields());
		if (record.saveName == null) {
			record.saveName = new File(url.getPath()).getName();
		}

		System.out.println("准备下载文件 大小:" + record.countSize);
		threads = new DownloaderCoreThread[THREAD_NUM];
		// 计算每条线程下载的数据长度
		int blockSize = (int) (record.countSize / THREAD_NUM);
		int residueSize = (int) (record.countSize % THREAD_NUM);
		if (record.sections == null) {
			// 初始化下载量数组
			record.sections = new long[THREAD_NUM];
		}
		new File(record.saveDir).mkdirs();// 建立目录
		if (!record.isOverride) {
			File file = new File(record.saveDir, record.saveName);
			if (file.exists() && file.length() == record.countSize) {//文件存在,并且大小一致
				record.setStatus(STATUS.COMPLETE);//提示完成
				return true;
			}else if(file.exists()){
				file.delete();//删除原来的文件
			}
		}
		for (int i = 0; i < threads.length; i++) {
			// 启动线程，分别下载每个线程需要下载的部分
			if (i == threads.length - 1) {
				threads[i] = new DownloaderCoreThread(this, i, blockSize * i,
						blockSize * (i + 1) + residueSize, record.sections[i]);
			} else {
				threads[i] = new DownloaderCoreThread(this, i, blockSize * i,
						blockSize * (i + 1), record.sections[i]);
			}
			threads[i].setName("Thread:" + i);
			threadPool.execute(threads[i]);
		}
		record.setStatus(STATUS._DOWNLOADING);
		return true;
	}

	/**
	 * 解析响应头,对配置做些调整
	 * 
	 * @param map
	 */
	private void pareHead(Map<String, List<String>> map) {
		if (map != null) {
			if (!map.containsKey("Accept-Ranges")) {
				THREAD_NUM = 1;// 不支持断点
				record.sections = new long[THREAD_NUM];// 重置下载量数组
			}
			// 尝试获取下载文件的名称
			if (record.saveName == null
					&& map.get("Content-Disposition") != null) {
				List<String> temps = map.get("Content-Disposition");
				for (String value : temps) {
					Matcher matcher = patter.matcher(value);
					if (matcher.find()) {
						String filename = matcher.group();
						filename = filename.substring(
								filename.indexOf('"') + 1,
								filename.lastIndexOf('"'));
						if (filename.length() != 0) {
							record.saveName = filename;
						}
					}

				}
			}
		}
		for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
		}
	}

	/**
	 * 检查下载状态
	 * 
	 * @return
	 */
	public STATUS checkStatus() {
		if (record.getStatus() == STATUS.COMPLETE) {
			return record.getStatus();
		} else if (record.getStatus() == STATUS._PAUSE) {
			pause();
			return record.getStatus();
		} else if (record.getStatus() == STATUS._FAILD) {
			try {
				if (!initThread()) { return record.getStatus(); }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return record.getStatus();
			}
		} else if (record.getStatus() == STATUS._CANCEL) {
			cancel();
			return record.getStatus();

		}
		boolean isCompleted = true;
		if (threads != null) {
			int err_num = 0;
			for (int i = 0; i < threads.length; i++) {
				if (threads[i].hasErr) {
					err_num++;
				}
			}
			if (err_num >= threads.length) {
				record.setStatus(STATUS._FAILD);
				return record.getStatus();
			}
			for (int i = 0; i < threads.length; i++) {
				isCompleted &= threads[i].isCompleted();
				if (threads[i].isPause) {
					threads[i].resume();
				}
				 if (threads[i].hasErr) {
					try {
						// 尝试重建下载出错的子线程
						threads[i] = new DownloaderCoreThread(this, i,
								threads[i].mStartPos, threads[i].mEndPos,
								threads[i].downloadLength);
						threads[i].setName("Thread:" + i);
						threadPool.execute(threads[i]);
						System.out.println("尝试重新启动下载出错的线程 " + threads[i]);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		record.setStatus(isCompleted ? STATUS.COMPLETE : record.getStatus());
		return record.getStatus();
	}

	public void pause() {
		for (DownloaderCoreThread downloaderCoreThread : threads) {
			downloaderCoreThread.pause();
		}
		record.setStatus(STATUS._PAUSE);
	}

	public void cancel() {
		record.setStatus(STATUS._CANCEL);
		for (DownloaderCoreThread downloaderCoreThread : threads) {
			downloaderCoreThread.resume();
		}
	}

	// long get
	void updateSection(int id, long length) {
		record.sections[id] = length;
	}

	public int getDownloadLength() {
		int downloadedAllSize = 0;// 当前所有线程下载总量

		for (int i = 0; i < threads.length; i++) {
			downloadedAllSize += threads[i].getDownloadLength();
		}
		return downloadedAllSize;
	}

	public boolean isCompleted() {
		for (int i = 0; i < threads.length; i++) {
			if (!threads[i].isCompleted()) { return false; }
		}
		return true;

	}

	public Downloader(DownloadRecord record, Executor threadPool) {
		this.record = record;
		this.threadPool = threadPool;
	}

	/**
	 * 下载器核心线程
	 * 
	 * @author WQ 下午5:44:49
	 */
	private class DownloaderCoreThread implements Runnable {
		/**
		 * @param record
		 *            下载记录
		 * @param threadId
		 *            线程id
		 * @param startPos
		 *            下载起始位置
		 * @param endPos
		 *            下载结束位置
		 * @param downloadLength
		 *            已下载长度
		 * @throws MalformedURLException
		 */
		public DownloaderCoreThread(Downloader downloader, int threadId,
				long startPos, long endPos, long downloadLength)
				throws MalformedURLException {
			// TODO Auto-generated constructor stub
			this.downloader = downloader;
			this.downloadUrl = new URL(downloader.getRecord().downloadUrl);
			this.file = new File(downloader.getRecord().saveDir,
					downloader.getRecord().saveName);
			this.downloadLength = downloadLength;
			this.mStartPos = startPos;
			this.mEndPos = endPos;
			this.threadId = threadId;
		}

		public void resume() {
			if (isPause) {
				synchronized (this) {
					isPause = false;
					notifyAll();
					return;
				}
			}
			hasErr = false;
			// err_num = 0;
		}

		/** 当前下载是否完成 */
		private boolean isCompleted = false;
		/** 线程出错次数 */
		private boolean hasErr;
		/** 线程暂停标示 */
		private boolean isPause = false;
		/** 当前已下载长度 */
		private long downloadLength = 0;
		/** 文件保存路径 */
		private File file;
		/** 文件下载路径 */
		private URL downloadUrl;
		/** 当前下载线程ID */
		private int threadId;
		/** 线程下载数据长度 */
		private String name;// 线程名
		/** 下载起始点,下载结束点 */
		private long mStartPos, mEndPos;
		private Downloader downloader;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 检查下载线程是否已暂停
		 */
		void checkPause() {
			while (isPause) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public void run() {
			if (downloader.getRecord().getStatus() == STATUS._CANCEL)
				return;
			resume();
			checkPause();
			BufferedInputStream bis = null;
			RandomAccessFile raf = null;
			long startPos = mStartPos + downloadLength;// 开始位置,起始位置加上已下载长度
			long endPos = mEndPos;// 结束位置
			long downSize = endPos - startPos;// 需要下载的长度
			if (isCompleted || endPos == startPos) {
				isCompleted = true;
				return;
			}
			try {
				URLConnection conn = downloadUrl.openConnection();
				conn.setAllowUserInteraction(true);

				// 设置10秒时超
				conn.setReadTimeout(10000);
				// 设置当前线程下载的起点、终点
				conn.setRequestProperty("Range", "bytes=" + startPos + "-"
						+ endPos);
				conn.connect();
				byte[] buffer = new byte[1024];
				bis = new BufferedInputStream(conn.getInputStream());
				if (!file.exists()) {

				}
				raf = new RandomAccessFile(file, "rwd");
				raf.seek(startPos);
				int len;
				while ((len = bis.read(buffer, 0, 1024)) != -1) {
					// System.out.println(getName()+"  ");
					if (len + downloadLength < downSize) {// 未达到要下载的长度
						raf.write(buffer, 0, len);
					} else {// 已达到要下载的长度,写入剩余长度的数据,标示为已完成
						raf.write(buffer, 0,
								len = (int) (downSize - downloadLength));
						isCompleted = true;
					}
					downloadLength = downloadLength + len;// 更新下载进度
					downloader.updateSection(threadId, downloadLength);
					if (isCompleted) {
						break;
					}
					checkPause();
					if (downloader.getRecord().getStatus() == STATUS._CANCEL) { return; }
				}
				isCompleted = true;
			} catch (IOException e) {
				hasErr = true;
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						hasErr = true;
						e.printStackTrace();
					}
				}
				if (raf != null) {
					try {
						raf.close();
					} catch (IOException e) {
						hasErr = true;
						e.printStackTrace();
					}
				}
				if (downloader.getRecord().getStatus() == STATUS._CANCEL) {
					file.delete();
				}
			}
			// if (hasErr) {
			// err_num++;// 计算出错次数
			// }
		}

		/**
		 * 进入暂停状态
		 */
		public void pause() {
			isPause = true;
		}

		/**
		 * 线程文件是否下载完毕
		 */
		public boolean isCompleted() {
			return isCompleted;
		}

		/**
		 * 线程下载文件长度
		 */
		public long getDownloadLength() {
			return downloadLength;
		}

	}
}