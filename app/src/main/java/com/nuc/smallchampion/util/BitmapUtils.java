package com.nuc.smallchampion.util;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class BitmapUtils {

	/**
	 * 读取src目录下的图片
	 *
	 * @param context
	 * @param path
	 * @return
	 */
	public static final Bitmap getSrcBitmap(Context context, String path) {
		try {
			// 读取图片成InputStream
			InputStream is = context.getClassLoader().getResourceAsStream(path);
			// InputStream转换成Bitmap
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();
			return bitmap;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * 按比例缩放图片
	 *
	 * @param bitmap
	 * @param scaleWidth
	 * @param scaleHeight
	 * @return
	 */
	public static final Bitmap setScaleBitmap(Bitmap bitmap, float scaleWidth,
											  float scaleHeight) {
		Matrix matrix = new Matrix();
		matrix.setScale(scaleWidth, scaleHeight); // 产生缩放后的Bitmap对象
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	public static final Bitmap setBackgroundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		float scale = (float) ((Assist.screenW + 200.0) / width);
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale); // 产生缩放后的Bitmap对象
		return Bitmap.createBitmap(bitmap, 0, 0, width, bitmap.getHeight(),
				matrix, true);
	}

	/*
	 * public static Bitmap createBitmap (Bitmap source, int x, int y, int
	 * width, int height, Matrix m, boolean filter)
	 * 最后的参数filter的作用是滤波作用，图像处理方面的知识，当对一个图像做线性变换（平移、旋转、缩放）时，
	 * 实际就是从原图像各点的像素值得到新图像各点像素值的过程，其中的旋转、缩放变换得到的新图像中各点的像素
	 * 值通常是经过各种插值获得的，这对于改善图像质量很有帮助，也就是滤波处理。所以，android文档中的说法就
	 * 是，当进行的不只是平移变换时，filter参数为true可以进行滤波处理，有助于改善新图像质量。
	 */

	public static final Bitmap setRotateBitmap(Bitmap bitmap, float degree) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree); // 设置翻转的角度
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}
}
