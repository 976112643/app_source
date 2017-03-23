package com.wq.support.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

public class FileUtils {

	/**
	 * 清空指定目录
	 * 
	 * @param dir
	 */
	public static void delFile(File dir) {
		File[] files = dir.listFiles();
		if (files != null)
			for (File file : files) {
				file.delete();
			}
	}

	/**
	 * 合并指定目录下的文件
	 * 
	 * @param dir
	 * @param outPath
	 * @param offect
	 */
	public static void combineFile(File dir, String outPath, int offect) {
		combineFile(dir.listFiles(), outPath, offect);
	}

	/**
	 * 拷贝文件到指定目录
	 * @param src
	 * @param aim
	 * @return
	 */
	public static boolean fileCopy(File src, File aim) {
		FileOutputStream out = null;
		FileInputStream in = null;
		byte buff[] = new byte[512];
		try {
			aim.getParentFile().mkdirs();//建立储存目录
			in = new FileInputStream(src);
			out = new FileOutputStream(aim);
			int len = 0;
			while ((len = in.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
			in.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 合并指定文件列表中的文件
	 * 
	 * @param files
	 * @param outPath
	 * @param offect
	 */
	public static void combineFile(File[] files, String outPath, int offect) {
		if (new File(outPath).getParentFile().mkdirs())
			;
		FileOutputStream out = null;
		FileInputStream in = null;
		try {
			byte buff[] = new byte[512];
			out = new FileOutputStream(outPath);
			boolean isFirst = true;
			if (files != null)
				for (File string : files) {
					in = new FileInputStream(string);
					if (!isFirst) {
						in.skip(offect);// 跳过偏移量
					} else {
						isFirst = false;
					}
					int len = 0;
					while ((len = in.read(buff)) != -1) {
						out.write(buff, 0, len);
					}
					in.close();
				}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	   /**
     * 从Assess目录读取字符串
     * @param context
     * @param filename
     * @return
     */
    public static String getStringForAssess(Context context,String filename){
        BufferedReader reader=null;
        try {
            StringBuilder stringBuilder=new StringBuilder();
            reader=new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            String line;
            while ((line=reader.readLine())!=null){
                stringBuilder.append(line).append('\n');
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
