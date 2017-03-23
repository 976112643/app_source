package com.wq.support.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;

public class BitmapUtil {
	/**
	 * 获取图片文件的信息，是否旋转了90度，如果是则反转
	 * 
	 * @param bitmap
	 *            需要旋转的图片
	 * @param path
	 *            图片的路径
	 */
	public static Bitmap reviewPicRotate(Bitmap bitmap, String path) {
		int degree = getPicRotate(path);
		if (degree != 0) {
			Matrix m = new Matrix();
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			m.setRotate(degree); // 旋转angle度
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);// 从新生成图片
		}
		return bitmap;
	}

	/**
	 * 读取图片文件旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片旋转的角度
	 */
	public static int getPicRotate(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}



	/**
	 * 读取资源目录下的图片
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static Bitmap getBitmapForasses(Context context, String uri) {
		try {
			Bitmap bm = BitmapFactory.decodeStream(context.getAssets()
					.open(uri));
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int[] getBitmapWH(Bitmap bitmap) {
		return new int[]
		{ bitmap.getWidth(), bitmap.getHeight() };
	}
	/**
	 * Save Bitmap to a file.保存图片到SD卡。
	 * 
	 * @param bitmap
	 * @param file
	 * @return error message if the saving is failed. null if the saving is
	 *         successful.
	 * @throws IOException
	 */
	public static boolean saveBitmapToFile(Bitmap bitmap, String _file) {
		return saveBitmap(bitmap, _file, 100);
	}

	private static boolean saveBitmap(Bitmap bitmap, String _file, int quality) {
		BufferedOutputStream os = null;
		try {
			File file = new File(_file);
			// String _filePath_file.replace(File.separatorChar +
			// file.getName(), "");
			int end = _file.lastIndexOf(File.separator);
			String _filePath = _file.substring(0, end);
			File filePath = new File(_filePath);
			if (!filePath.exists()) {// 路径不存在时尝试创建路径
				filePath.mkdirs();
			}
			file.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
			return true;
		} catch (IOException e) {
			Log.e("-->", e.getMessage() + "  " + _file);
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Log.e("-->", e.getMessage(), e);
				}
			}
		}
		return false;
	}

	/**
	 * Save Bitmap to a file.保存图片到SD卡。
	 * 
	 * @param bitmap
	 * @param file
	 * @return error message if the saving is failed. null if the saving is
	 *         successful.
	 * @throws IOException
	 */
	public static String saveBitmapToFile(String aimpath, String _file,
			int quality) {
		File file = new File(_file);
		if (file.exists() && file.length() != 0)
			return _file;
		if (saveBitmap(BitmapFactory.decodeFile(aimpath), _file, quality)) { return _file; }
		return aimpath;
	}
	
	/**
	 * 获取图片宽高
	 * 
	 * @param path 图片文件路径
	 * @return
	 */
	public static int[] gePicWH(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);
		// 这里返回的bmp是null
		return new int[]
		{ options.outWidth, options.outHeight };
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public static Bitmap getImageThumbnail(Bitmap bitmap, int width, int height) {
		// int w = bitmap.getWidth();
		// int h = bitmap.getHeight();
		// int beWidth = w / width;
		// int beHeight = h / height;
		// bitmap=Bitmap.createScaledBitmap(bitmap, beWidth, beHeight, false);
		return ThumbnailUtils.extractThumbnail(bitmap, width, height);
	}
	

	/**
	 * 压缩图片
	 * 
	 * @param bitmap
	 *            位图对象
	 * @param quality
	 *            压缩质量 100为不压缩
	 * @return
	 */
	public static Bitmap compressImg(Bitmap bitmap, int quality) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			// 把压缩后的数据baos存放到ByteArrayInputStream中
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());
			// 把ByteArrayInputStream数据生成图片
			return BitmapFactory.decodeStream(isBm);
		} catch (Exception e) {}
		return bitmap;
	}

	/**
	 * 压缩图片
	 * 
	 * @param imgPath
	 *            图片绝对路径
	 * @param override
	 *            是否覆盖原图
	 * @param quality
	 *            压缩质量 100为不压缩
	 * @return 压缩成功则返回压缩后图片路径,否则返回原图路径
	 */
	public static String compressImg(String imgPath, String aimPath, int quality) {

		Bitmap bitmap = compressImg(BitmapFactory.decodeFile(imgPath), quality);
		boolean flag = saveBitmapToFile(bitmap, aimPath);
		return flag ? aimPath : imgPath;
	}

	public static int[] scalWH(String path, float scal, int minW, int minH) {
		int WH[] = gePicWH(path);
		int W = (int) (WH[0] * scal), H = (int) (WH[1] * scal);
		if (W < minW || H < minH) { return WH; }
		return new int[]
		{ W, H };
	}
}
