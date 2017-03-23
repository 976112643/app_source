package com.wq.support.net;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;

import com.wq.base.Config;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

/**
 * 文件上传帮助类
 * @author WQ 下午3:21:09
 */
public class FileUploadHelp {
	Activity mContext;
	List<File> files = new ArrayList<>();
	HashMap<File, String> fileSet = new HashMap<>();
	boolean uploading = false;
String url;
	public static abstract class UploadListener {
		public int success = 0, faild = 0;
		public String errMsg;

		public abstract void onLoadFinish(int success, int faild,String errMsg);
	final	public  void onLoadFinish(int success, int faild){
			onLoadFinish(success, faild, null);
		}
	}

	public static class FileBean {
		public String img_id;
		public String path;
		public FileBean() {
		}
		public FileBean(String file_id, String file_path) {
			this.img_id = file_id;
			this.path = file_path;
		}

	}

	public FileUploadHelp(Activity mContext, List<File> files,String url) {
		if (files == null)
			throw new NullPointerException("files 字段为空");
		this.mContext = mContext;
		this.url=url;
		this.files = files;
	}

	public FileUploadHelp(Activity mContext,String url) {
		this.mContext = mContext;
		this.url=url;
	}

	public void uploadFile(boolean showDialog) {
		uploadFile(showDialog, new UploadListener() {

			@Override
			public void onLoadFinish(int success, int faild,String msg) {

			}
		});
	}

	public void uploadFile(final boolean showDialog, final UploadListener lin) {
		if (request != null) {
			request.cancel(true);
		}
		int num = clearInvalid();// 清除无效文件,同时检查还有几个文件待上传
		if (num == 0) {
			lin.onLoadFinish(lin.success, lin.faild);
			return;
		}

		if (showDialog) {
//			showLoadDialog("上传中");
		}
		new Thread() {
			public void run() {
				for (Iterator iterator = fileSet.keySet().iterator(); iterator
						.hasNext();) {
					final File key = (File) iterator.next();
					String val = fileSet.get(key);
					if (val == null) {
						JsonHandler<FileBean> callback = new JsonHandler<FileBean>() {
							public void onSuccess(FileBean t, int status,
									String msg) {

								if (status == 1) {
									lin.success++;
									fileSet.put(key, t.path);
								} else {
									lin.faild++;
								}
							};

							public void onFailure(int arg0, Header[] arg1,
									String arg2, Throwable arg3) {
								// doUpload(key, handle);
								lin.faild++;
								lin.errMsg=NetUtils.getExceptionMsg(arg3, arg2);
							};
						};
						RequestHandle request = doUpload(key, callback);
						if (request.isCancelled()) {
//							break;
						}
					}
				}
				mContext.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (showDialog) {
							
						}
						lin.onLoadFinish(lin.success, lin.faild,lin.errMsg);
					}
				});

			};
		}.start();

	};

	RequestHandle request;

	RequestHandle doUpload(File key, AsyncHttpResponseHandler handle) {
		NAction action = Config.getUidNAction();
		action.put("img", key);
		return request = NetUtils.doPost(url,
				action.params, handle);
	}

	/**
	 * 清除无效的
	 */
	int clearInvalid() {
		int num = 0;// 待上传文件数目
		for (File file : files) {
			if (!fileSet.containsKey(file)) {
				fileSet.put(file, null);
			}
			if (fileSet.get(file) == null) {
				num++;
			}

		}
		for (Iterator iterator = fileSet.keySet().iterator(); iterator
				.hasNext();) {
			File key = (File) iterator.next();
			if (!files.contains(key)) {
				iterator.remove();
			}
		}
		return num;
	}

	public List<String> getFileIds() {
		return new ArrayList<>(fileSet.values());
	}
	public String getFileId(int index) {
		if(fileSet.values().size()<=index || index<0)return null;
		
		return getFileIds().get(index);
	}

	public void addFile(File file, String netUrl) {
		fileSet.put(file, netUrl);
	}



}
