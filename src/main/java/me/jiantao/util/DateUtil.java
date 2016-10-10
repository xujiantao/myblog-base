package me.jiantao.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private DateUtil(){};
	
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static long getNowMs() {
		return System.currentTimeMillis();
	}

	public static String DateToString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static String DateToString(Date date) {
		return DateToString(date, DEFAULT_PATTERN);
	}

	public static String MsToString(long ms, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date(ms));
	}
	
	public static String MsToString(long ms) {
		return MsToString(ms, DEFAULT_PATTERN);
	}

	public static Date StringToDate(String source, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException("时间转换异常", e);
		}
	}
	
	public static Date StringToDate(String source) {
		return StringToDate(source, DEFAULT_PATTERN);
	}

	public static long StringToMs(String source, String pattern) {
		return StringToDate(source, pattern).getTime();
	}
	
	public static long StringToMs(String source) {
		return StringToMs(source, DEFAULT_PATTERN);
	}
	
}
