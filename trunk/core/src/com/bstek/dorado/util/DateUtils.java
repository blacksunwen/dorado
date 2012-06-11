package com.bstek.dorado.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-6-8
 */
public final class DateUtils {
	private DateUtils() {
	}

	public static Date parse(String dateText) throws ParseException {
		return new SimpleDateFormat().parse(dateText);
	}

	public static Date parse(String format, String dateText)
			throws ParseException {
		return new SimpleDateFormat(format).parse(dateText);
	}

	public static String format(Date date) {
		return new SimpleDateFormat().format(date);
	}

	public static String format(String format, Date date) {
		return new SimpleDateFormat(format).format(date);
	}
}
