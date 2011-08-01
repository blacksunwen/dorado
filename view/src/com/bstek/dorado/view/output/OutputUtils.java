package com.bstek.dorado.view.output;

import java.util.Collection;

import com.bstek.dorado.common.Ignorable;

/**
 * 用于支持客户端输出功能的工具类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 19, 2008
 */
public abstract class OutputUtils {
	/**
	 * 特殊的默认值，用于代表一些常用的属性值。
	 * <ul>
	 * <li>对于java.lang.String类型的属性，"#default"表示null和""。</li>
	 * <li>对于java.lang.Number类型的属性，"#default"表示0。</li>
	 * <li>对于java.lang.Boolean类型的属性，"#default"表示false。</li>
	 * <li>对于java.util.Collection类型的属性，"#default"表示集合的大小为0。</li>
	 * <li>对于其他类型的属性，"#default"表示null。</li>
	 * </ul>
	 */
	public static final String ESCAPE_VALUE = "#default";

	/**
	 * 特殊的用于标识此属性总是应被忽略的属性值。
	 */
	public static final String IGNORE_VALUE = "#ignore";

	/**
	 * 判断一个属性值是否与默认值一致。
	 * 
	 * @param value
	 *            属性值。
	 * @return 是否一致。
	 */
	public static boolean isEscapeValue(Object value) {
		return isEscapeValue(value, ESCAPE_VALUE);
	}

	/**
	 * 判断一个属性值是否与默认值一致。
	 * 
	 * @param value
	 *            属性值。
	 * @param escapeValue
	 *            默认值。
	 * @return 是否一致。
	 */
	public static boolean isEscapeValue(Object value, Object escapeValue) {
		if (value != escapeValue) {
			if (ESCAPE_VALUE.equals(escapeValue)) {
				return (value == null
						|| (value instanceof String && value.equals(""))
						|| (value instanceof Number && ((Number) value)
								.doubleValue() == 0)
						|| (value instanceof Boolean && !((Boolean) value)
								.booleanValue())
						|| (value instanceof Collection<?> && ((Collection<?>) value)
								.isEmpty()) || (value instanceof Ignorable && ((Ignorable) value)
						.isIgnored()));
			} else if (IGNORE_VALUE.equals(escapeValue)) {
				return true;
			}
			return String.valueOf(value).equals(String.valueOf(escapeValue));
		}
		return true;
	}

}
