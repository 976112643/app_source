package com.wq.support.net;

import java.io.File;
import java.io.IOException;
import org.apache.http.Header;
import android.content.Context;
import android.os.Environment;
import com.loopj.android.http.RangeFileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

/**
 * 文件请求handler 简单封装了一个静态方法
 * 
 * @author WQ 下午4:56:27
 */
public class FileDownHandler extends RangeFileAsyncHttpResponseHandler {
	File saveFile, tmpFile;

	public FileDownHandler(Context context, String filename) {
		super(getCacheDownFile(context, filename));
		saveFile = getCacheDownFile(context, filename);
		tmpFile = new File(saveFile.getParentFile(), "tmp/" + filename);
	}

	public File getFilePath() {
		return saveFile;
	}

	@Override
	public void onFailure(int status, Header[] arg1, Throwable arg2, File arg3) {

	}

	@Override
	final public void onSuccess(int status, Header[] arg1, File file) {
		if (!tmpFile.getParentFile().exists()) {
			tmpFile.getParentFile().mkdirs();
		}
		try {
			tmpFile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		onSuccess(file);
	}

	public void onSuccess(File file) {

	}

	/**
	 * 判断下载文件是否存在,本地文件存在,并且下载完成的标识文件也存在
	 * @param context
	 * @param filename
	 * @return
	 */
	public static boolean isExists(Context context, String filename) {
		return getCacheDownFile(context, filename).exists()
				&& getTmpDownFile(context, filename).exists();
	}

	public static File getCacheDownFile(Context context, String filename) {
		return new File(getAppCacheDir(context), filename);
	}

	public static File getTmpDownFile(Context context, String filename) {
		return new File(getAppCacheDir(context), "tmp/" + filename);
	}

	/**
	 * @param context
	 * @param fileHandler
	 * @param isOverride
	 *            是否覆盖
	 * @return
	 */
	public static RequestHandle startDown(Context context, String url,
			FileDownHandler fileHandler, boolean isOverride) {
		RequestHandle handler = null;
		if (isOverride) {
			fileHandler.getFilePath().delete();
			fileHandler.tmpFile.delete();
		}
		if (fileHandler.tmpFile != null && fileHandler.tmpFile.exists()
				&& fileHandler.getFilePath().exists()) {
			fileHandler.onSuccess(fileHandler.getFilePath());
			return null;
		}
			handler = NetUtils.doGet(url, fileHandler);
		return handler;
	}
	/**
	 * 获取App使用的缓存目录, 外部存储可用时 ,默认使用外部,不可用时,使用内部缓存区域
	 * @param context
	 * @return
	 */
	public static File getAppCacheDir(Context context){
		File cacheDir=null;
		if(Environment.getExternalStorageDirectory().canWrite()){
			cacheDir=new File(Environment.getExternalStorageDirectory(),"/musiccache");
			cacheDir.mkdirs();
		}else {
			cacheDir=new File(context.getExternalCacheDir(),"/musiccache");
			cacheDir.mkdirs();
		}
		return cacheDir;
	}
}
