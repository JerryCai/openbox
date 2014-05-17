package com.googlecode.openbox.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateHelper {

	public static final String TIME_ZONE_GMT = "GMT";

	public static final String DATA_FORMAT_YEAR = "yyyy";
	public static final String DATA_FORMAT_MONTH = "MM";
	public static final String DATA_FORMAT_DAY = "dd";
	public static final String DATA_FORMAT_HOUR = "HH";
	public static final String DATA_FORMAT_MINUTE = "mm";
	public static final String DATA_FORMAT_SECOND = "ss";
	public static final String DATA_FORMAT_MILESECOND = "SSS";

	public static final String yyyyMMddHHmmssSSS = DATA_FORMAT_YEAR
			+ DATA_FORMAT_MONTH + DATA_FORMAT_DAY + DATA_FORMAT_HOUR
			+ DATA_FORMAT_MINUTE + DATA_FORMAT_SECOND + DATA_FORMAT_MILESECOND;

	public static String getTimeString() {
		return getTimeString(yyyyMMddHHmmssSSS);
	}

	public static String getTimeString(String formatter) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		String currentDate = sdf.format(date);
		return currentDate;
	}

	public static String getTimeStringByTimeZone(String timeZone) {
		return getTime(timeZone, "dd MMM yyyy HH:mm:ss SSS");
	}

	public static String getSeverGMT() {
		return getTimeStringByTimeZone(TIME_ZONE_GMT);
	}

	public static String getTime(String timeZone, String formatter) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(formatter);
		sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
		return sdf.format(date);
	}
}
