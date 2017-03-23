package com.wq.support.net.down;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wq.support.net.down.DownloadRecord.STATUS;

/**
 * @下载管理器
 */
public class DownloadManager implements Runnable,RecordUpdate {
	boolean isRun = false;// 是否运行中

	
	RecordUpdate updateListener;
	LinkedList<DownloadRecord> queue = new LinkedList<DownloadRecord>();// 下载列表
	LinkedList<DownloadRecord> downloading = new LinkedList<DownloadRecord>();// 下载中的列表
	LinkedList<Downloader> threadPool = new LinkedList<Downloader>();// 线程队列
	ExecutorService executor = Executors.newCachedThreadPool();
	int maxThreadPool = 10;
	static DownloadManager downloadManager;
	
	public RecordUpdate getUpdateListener() {
		return updateListener;
	}

	public void setUpdateListener(RecordUpdate updateListener) {
		this.updateListener = updateListener;
	}

	public synchronized boolean isRun() {
		
		return isRun;
	}

	public static synchronized DownloadManager instance() {
		if (downloadManager == null || !downloadManager.isRun()) {
			downloadManager = new DownloadManager();
		}
		return downloadManager;
	}

	public DownloadManager() {
		
		initPool();
	}

	private void initPool() {
		isRun=true;
		 executor = Executors.newCachedThreadPool();
		executor.execute(this);		
	}

	public void startDownload(DownloadRecord record) {
		
		synchronized (this) {
			if(!isRun || executor.isShutdown()){
				initPool();
			}
			if(downloading.contains(record)){
				DownloadRecord tmp=downloading.get(downloading.indexOf(record));
				tmp.setStatus(STATUS._DOWNLOADING);
			}else {
				queue.add(record);
				downloading.add(record);
			}
			notifyAll();
		}
	}
	
	public boolean isDownloading(DownloadRecord record){
		if(downloading.contains(record)){
			DownloadRecord tmp=downloading.get(downloading.indexOf(record));
			return tmp.getStatus()==STATUS._DOWNLOADING;
		}
		return false;
	}
	
	public void cancelDownload(DownloadRecord record){
		if(!downloading.contains(record))return ;
		synchronized (this) {
			DownloadRecord tmp=downloading.get(downloading.indexOf(record));
			tmp.setStatus(STATUS._CANCEL);
			notifyAll();
		}
	}
	public void pauseDownload(DownloadRecord record){
		synchronized (this) {
			DownloadRecord tmp=downloading.get(downloading.indexOf(record));
			tmp.setStatus(STATUS._PAUSE);
			notifyAll();
		}
	}
	/**
	 * 销毁下载器
	 */
	public void destory() {
		if(executor!=null){
			isRun=false;
			executor.shutdown();
			downloading.clear();
			queue.clear();
			threadPool.clear();
		}
	}

	/**
	 * 等待唤醒
	 * 
	 * @param time
	 */
	void waitRun(long time) {
		synchronized (this) {
			try {
				wait(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		isRun = true;
		waitRun(1000);
		while (isRun) {
			while (true) {
				// 如果有任务,并且线程池没满,尝试向下载器队列中添加新任务
				if (queue.size() != 0 && threadPool.size() <= maxThreadPool) {
					DownloadRecord record = queue.removeFirst();
					Downloader thread = new Downloader(record, executor);
					threadPool.add(thread);
//					downloading.addFirst(record);// 移动记录到下载中的队列
					thread.start(); // 启动下载器
					System.out.println("下载::" + record.downloadUrl);
				} else if (queue.size() != 0 || threadPool.size() != 0) {// 如果还有待下载任务
																			// 或者
																			// 还有正在下载任务
					waitRun(500);// 每秒自检一次
					System.out.println("下载自检..");
					break;
				} else if (threadPool.size() == 0) {// 如果没有正在下载的任务
//					destory();
					System.out.println("下载器休眠");
//					return ;
					waitRun(0);// 每秒自检一次
					continue;// 跳过
				}
				System.out.println("----");
			}

			// 检查更新下载器状态
			for (Iterator<Downloader> iterator = threadPool.iterator(); iterator.hasNext();) {
				Downloader downloader = (Downloader) iterator.next();
				switch (downloader.checkStatus()) {
				case COMPLETE:
				case _CANCEL:
				case _FAILD:
					// 将下载中的任务移到已完成列表中去
//					downloading.remove(downloader.record);
					// 从线程队列中移除该下载器
					iterator.remove();
					downloading.remove(downloader.record);
					System.out.println(downloader.record.downloadUrl + " 下载完成,保存在" + downloader.record.saveDir + "目录中");
					break;
				case _DOWNLOADING:
					System.out.println(downloader.record.saveName + "下载中.. " + downloader.getDownloadLength());
					break;
				case _ERR:
					iterator.remove();
					break;
				case _PAUSE:
					break;

				default:
					break;
				}
				onRecordUpdate(downloader.record,downloader.checkStatus());
				System.out.println(downloader.checkStatus()+" downloader.record.downloadUrl ");
			}
		}
	}

	@Override
	public void onRecordUpdate(DownloadRecord record,STATUS status) {
			if(updateListener!=null){
				updateListener.onRecordUpdate(record,status);
			}
	}
}
