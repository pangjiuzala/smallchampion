package com.nuc.smallchampion.util;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

/**
 * 手机震动工具类
 *
 * @author Administrator
 *
 */
public class VibratorUtils {

	private static boolean isVibrate;

	public static final void initIsVibrate(boolean isVibrate) {
		VibratorUtils.isVibrate = isVibrate;
	}

	/**
	 * final Activity activity ：调用该方法的Activity实例 long milliseconds ：震动的时长，单位是毫秒
	 * long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
	 */
	public static final void Vibrate(final Activity activity, long milliseconds) {
		if (isVibrate) {
			Vibrator vib = (Vibrator) activity
					.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(milliseconds);
		}
	}
	/*
	 * public static void Vibrate(final Activity activity, long[] pattern) { if
	 * (PrefsActivity.getVibrator(activity)) { Vibrator vib = (Vibrator)
	 * activity.getSystemService(Service.VIBRATOR_SERVICE); vib.vibrate(pattern,
	 * -1); } }
	 */
}
