package com.bstek.dorado.core.el;

import java.text.SimpleDateFormat;

import com.bstek.dorado.core.Constants;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-24
 */
public class ExpressionUtilsObject {

	public Date getDate() {
		return new Date();
	}
}

class Date extends java.util.Date {
	private static final long serialVersionUID = 4952841582905835573L;

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			Constants.ISO_DATETIME_FORMAT1);

	@Override
	public String toString() {
		return DATE_FORMAT.format(this);
	}

}
