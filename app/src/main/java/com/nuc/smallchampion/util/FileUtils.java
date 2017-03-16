package com.nuc.smallchampion.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import android.graphics.Bitmap;
import android.text.TextUtils;

public class FileUtils {

	public static String getFolderSize() {
		File file = new File(Constant.path);
		double size = 0;
		String str = null;
		DecimalFormat df = new DecimalFormat("0.000");

		try {
			size = getFolderSize(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (size >= 1073741824) {
			str = df.format(size / 1073741824) + "GB";
		} else if (size >= 1048576) {
			str = df.format(size / 1048576) + "MB";
		} else {// if (size >= 1024) {
			str = df.format(size / 1048576) + "KB";
		}
		return str;
	}

	/**
	 * 获取文件夹大小
	 *
	 * @param file
	 *            File实例
	 * @return long 单位为M
	 * @throws Exception
	 */
	private static double getFolderSize(java.io.File file) throws Exception {
		double size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size;
	}

	/**
	 * 删除指定目录下文件及目录
	 *
	 * @param deleteThisPath
	 * @param filepath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath)
			throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);

			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFolderFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}

	public static void savePic(Bitmap bitmap, String time) {
		if (ensureSDCardAccess(Constant.path)) {
			File file = new File(Constant.path + time + ".png");
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				// Assist.imagePath = file.getPath();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean ensureSDCardAccess(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		} else if (file.mkdirs()) {
			return true;
		}
		return false;
	}
}
