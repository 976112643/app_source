//package com.cnsunrun.support.utils;
//
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Enumeration;
//
//import android.content.Context;
//
//import com.file.zip.ZipEntry;
//import com.file.zip.ZipFile;
//
///**
// * 
// * 从assets目录解压zip到本地
// *
// */
//public class UnzipFromAssets {
//
//	/**
//	 * 解压assets的zip压缩文件到指定目录
//	 * 
//	 * @param context上下文对象
//	 * @param assetName压缩文件名
//	 * @param outputDirectory输出目录
//	 * @param isReWrite是否覆盖
//	 * @throws IOException
//	 */
//	public static void unZip(Context context, String assetName,
//			String outputDirectory, boolean isReWrite) {
//		// 创建解压目标目录
//		File file = new File(outputDirectory);
//		// 如果目标目录不存在，则创建
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		try {
//
//			// java.nio.charset.CharsetDecoder.
//			// 打开压缩文件
//			InputStream inputStream = context.getAssets().open(assetName);
//			File zipFilePath = null;
//			FileOutputStream io = new FileOutputStream(zipFilePath = new File(
//					context.getCacheDir(), assetName));
//			int count = 0;
//			byte[] buffer = new byte[1024 * 1024];
//			while ((count = inputStream.read(buffer)) > 0) {
//				io.write(buffer, 0, count);
//			}
//			inputStream.close();
//			io.close();
//
//			// 使用1Mbuffer
//			// 解压时字节计数
//			// 如果进入点为空说明已经遍历完所有压缩包中文件和目录
//
//			ZipFile zipFile = new ZipFile(zipFilePath, "GBK");// 设置压缩文件的编码方式为GBK
//			Enumeration<ZipEntry> entris = zipFile.getEntries();
//			ZipEntry zipEntry = null;
//			File tmpFile = null;
//			BufferedOutputStream bos = null;
//			InputStream is = null;
//			byte[] buf = new byte[1024];
//			int len = 0;
//			while (entris.hasMoreElements()) {
//				zipEntry = entris.nextElement();
//				// 不进行文件夹的处理,些为特殊处理
//				tmpFile = new File(outputDirectory + File.separator
//						+ zipEntry.getName());
//				if (zipEntry.isDirectory()) {// 当前文件为目录
//					if (isReWrite || !tmpFile.exists()) {
//						tmpFile.mkdir();
//					}
//				} else {
//					if (isReWrite || !tmpFile.exists()) {
//						Logger.E(tmpFile);
//						tmpFile.createNewFile();
//						is = zipFile.getInputStream(zipEntry);
//						bos = new BufferedOutputStream(new FileOutputStream(
//								tmpFile));
//						while ((len = is.read(buf)) > 0) {
//							bos.write(buf, 0, len);
//						}
//						bos.flush();
//						bos.close();
//					}
//				}
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//}