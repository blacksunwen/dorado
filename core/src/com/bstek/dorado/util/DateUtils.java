/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

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
