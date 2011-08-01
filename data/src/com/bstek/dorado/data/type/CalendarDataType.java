package com.bstek.dorado.data.type;

import java.util.Calendar;
import java.util.Date;

/**
 * 用于描述java.util.Calendar的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class CalendarDataType extends DateDataType {

	@Override
	public Object fromText(String text) {
		if (text == null) {
			return null;
		} else {
			Date date = convertText2Date(text);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		}
	}

	@Override
	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else {
			Date date = convertObject2Date(value);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		}
	}

}
