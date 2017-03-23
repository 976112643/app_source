package com.wq.support.net.down;

import com.wq.support.net.down.DownloadRecord.STATUS;

public interface RecordUpdate {
	public void onRecordUpdate(DownloadRecord record, STATUS status);
}
