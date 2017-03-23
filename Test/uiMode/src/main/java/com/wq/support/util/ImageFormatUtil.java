package com.wq.support.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageFormatUtil {
	/**
	 * 判断文件是否是gif格式的
	 * @param file
	 * @return
	 */
	public static  boolean isGifFile(File file) {
	    try {
	        FileInputStream inputStream = new FileInputStream(file);
	        int[] flags = new int[5];
	        flags[0] = inputStream.read();
	        flags[1] = inputStream.read();
	        flags[2] = inputStream.read();
	        flags[3] = inputStream.read();
	        inputStream.skip(inputStream.available() - 1);
	        flags[4] = inputStream.read();
	        inputStream.close();
	        return flags[0] == 71 && flags[1] == 73 && flags[2] == 70 && flags[3] == 56 ;
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	/**
	 * 判断文件是否是jpg格式的
	 * @param file
	 * @return
	 */
	public static  boolean isJpgFile(File file){
	    try {
	        FileInputStream bin = new FileInputStream(file);
	        int b[] = new int[4];
	        b[0] = bin.read();
	        b[1] = bin.read();
	        bin.skip(bin.available() - 2);
	        b[2] = bin.read();
	        b[3] = bin.read();
	        bin.close();
	        return b[0] == 255 && b[1] == 216 && b[2] == 255 && b[3] == 217;
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}
}
