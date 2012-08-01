package com.bstek.dorado.core.el;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Constants;
import com.bstek.dorado.util.DateUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-24
 */
public class ExpressionUtilsObject {

	public Date getDate() {
		return new Date();
	}

	public String getDate(String format) {
		return formatDate(new Date(), format);
	}

	public String formatDate(Date date, String format) {
		return DateUtils.format(format, date);
	}

	public String formatNumber(Number d, String format) {
		return new DecimalFormat(format).format(d);
	}

	public String trim(String s) {
		return StringUtils.trim(s);
	}

	public Object defaultValue(Object value, Object defaultValue) {
		if (value == null
				|| (value instanceof String && value.equals(""))
				|| (value instanceof Number && ((Number) value).doubleValue() == 0)
				|| (value instanceof Boolean && !((Boolean) value)
						.booleanValue())) {
			return defaultValue;
		}
		return value;
	}

}

class Date extends java.util.Date {
	private static final long serialVersionUID = 4952841582905835573L;

	@Override
	public String toString() {
		return DateUtils.format(Constants.ISO_DATETIME_FORMAT1, this);
	}

}
