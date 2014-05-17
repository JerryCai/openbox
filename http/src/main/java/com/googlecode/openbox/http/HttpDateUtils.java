package com.googlecode.openbox.http;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HttpDateUtils {

	/** Date format pattern used to generate the header in RFC 1123 format. */
	public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

	/** The time zone to use in the date header. */
	public static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	public static final DateFormat HTTP_DATA_FORMAT = new SimpleDateFormat(
			PATTERN_RFC1123, Locale.US);

	private HttpDateUtils() {
	}

	public static String getDateString(long timeMillis) {
		return HTTP_DATA_FORMAT.format(new Date(timeMillis));
	}
}
