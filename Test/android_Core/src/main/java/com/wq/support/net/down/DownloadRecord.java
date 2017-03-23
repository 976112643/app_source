package com.wq.support.net.down;

import java.io.File;

public class DownloadRecord {
	public long countSize;// 总大小
	// public long currentPosition;// 当前下载位置
	public long add_time=System.currentTimeMillis();// 添加时间
	public long update_time;
	public String downloadUrl;// 下载路径
	public String saveDir;// 保存目录
	public String saveName;// 保存文件名,为空时自动获取
	public long sections[];// len-len-len;
	public boolean isOverride = false;
	private STATUS status = STATUS._READY;// 0
	public File getFilePath(){
		return  new File(saveDir,saveName);
	}
	public long getgetDownloadLength() {
		int downloadLength = 0;
		if (sections != null)
			for (long l : sections) {
				downloadLength += l;
			}
		return downloadLength;
	}

	public STATUS getStatus() {
		return status;
	}

	public synchronized void setStatus(STATUS status) {
		this.status = status;
	}

	/**
	 * 文件下载状态枚举
	 * 
	 * @author WQ 下午7:17:04
	 */
	public enum STATUS {

		_READY("准备"), _DOWNLOADING("下载中"), _PAUSE("暂停"), _FAILD("失败"), _ERR(
				"出错"), COMPLETE("完成"), _CANCEL("取消");
		STATUS(String name) {
			this.name = name;
		}

		private String name;

		@Override
		public String toString() {
			return name;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) { return super.equals(o); }
		return hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("%s_%s", downloadUrl, saveDir);
	}
}