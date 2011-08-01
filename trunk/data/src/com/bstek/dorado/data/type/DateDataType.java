package com.bstek.dorado.data.type;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;

import com.bstek.dorado.util.Assert;

/**
 * 用于描述java.util.Date的数据类型。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Feb 13, 2007
 */
public class DateDataType extends SimpleDataType {

	private static SimpleDateFormat DATE_FORMAT_DEFAULT = new SimpleDateFormat();

	private static SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat(
			com.bstek.dorado.core.Constants.ISO_DATE_FORMAT);
	private static SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat(
			com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT1);
	private static SimpleDateFormat DATE_FORMAT_3 = new SimpleDateFormat(
			com.bstek.dorado.core.Constants.ISO_DATETIME_FORMAT2);
	private static SimpleDateFormat DATE_FORMAT_4 = new SimpleDateFormat(
			"HH:mm:ss");
	private static SimpleDateFormat DATE_FORMAT_5 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static int DATE_FORMAT_1_LEN = DATE_FORMAT_1.toPattern().length();
	private static int DATE_FORMAT_2_LEN = DATE_FORMAT_2.toPattern().length() - 4;
	private static int DATE_FORMAT_3_LEN = DATE_FORMAT_3.toPattern().length() - 4;
	private static int DATE_FORMAT_4_LEN = DATE_FORMAT_4.toPattern().length();
	private static int DATE_FORMAT_5_LEN = DATE_FORMAT_5.toPattern().length();

	public String toText(Object value) {
		if (value == null) {
			return null;
		} else {
			Assert.isInstanceOf(Date.class, value);
			Date date = (Date) value;
			return String.valueOf(date.getTime());
		}
	}

	public Object fromText(String text) {
		if (text == null) {
			return null;
		} else {
			return convertText2Date(text);
		}
	}

	/**
	 * 尝试将一段文本转换成日期对象。
	 * 
	 * @param text
	 *            文本
	 * @return 转换得到的日期对象
	 * @throws ValueConvertException
	 * @throws NumberFormatException
	 */
	protected Date convertText2Date(String text) throws DataConvertException,
			NumberFormatException {
		if (NumberUtils.isNumber(text)) {
			long time = Long.parseLong(text);
			return new Date(time);
		} else {
			try {
				Date date = null;
				int len = text.length();
				try {
					if (len == DATE_FORMAT_1_LEN) {
						date = DATE_FORMAT_1.parse(text);
					} else if (len == DATE_FORMAT_2_LEN) {
						date = DATE_FORMAT_2.parse(text);
					} else if (len == DATE_FORMAT_3_LEN) {
						date = DATE_FORMAT_3.parse(text);
					} else if (len == DATE_FORMAT_4_LEN) {
						date = DATE_FORMAT_4.parse(text);
					} else if (len == DATE_FORMAT_5_LEN) {
						date = DATE_FORMAT_5.parse(text);
					}
				} catch (ParseException ex) {
					// do nothing
				}

				if (date == null) {
					date = DATE_FORMAT_DEFAULT.parse(text);
				}
				return date;
			} catch (ParseException ex) {
				throw new DataConvertException(text, Date.class);
			}
		}
	}

	@Override
	public Object fromObject(Object value) {
		if (value == null) {
			return null;
		} else {
			return convertObject2Date(value);
		}
	}

	/**
	 * 尝试将一个任意对象转换成日期对象。
	 * 
	 * @param value
	 *            任意对象
	 * @return 转换得到的日期对象
	 * @throws ValueConvertException
	 */
	protected Date convertObject2Date(Object value) throws DataConvertException {
		Class<?> targetType = this.getMatchType();
		if (targetType != null && targetType.isAssignableFrom(value.getClass())) {
			return (Date) value;
		} else if (value instanceof String) {
			return convertText2Date((String) value);
		} else if (value instanceof Long) {
			return new Date(((Long) value).longValue());
		} else {
			throw new DataConvertException(value.getClass(), getMatchType());
		}
	}

}
