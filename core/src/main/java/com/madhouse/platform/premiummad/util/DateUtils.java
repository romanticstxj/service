package com.madhouse.platform.premiummad.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {


	public static Date getYmd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR,0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/*
	 * 根据pattern转换date
	 */
	public static Date getFormatDateByPattern(String pattern, String dateStr) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setLenient(false);
		return simpleDateFormat.parse(dateStr);
	}

	/**
	 * 两个日期多少秒
	 * 
	 * @param date1
	 *            Date1
	 * @param date2
	 *            Date2
	 * @return long
	 */
	public static long getDateSecondSubtract(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime()) / (1000);
	}

	/**
	 * 两个日期相差几天
	 * 
	 * @param date1
	 *            Date1
	 * @param date2
	 *            Date2
	 * @return long
	 */
	public static long getDateSubtract(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
	}
}
