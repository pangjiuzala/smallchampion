package com.nuc.smallchampion.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String getTimeNow() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = dateFormat.format(date);
		return str;
	}
}
