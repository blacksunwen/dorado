package com.bstek.dorado.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.core.Configure;

/**
 * 用于生成特征码字符串的工具类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 19, 2009
 */
public abstract class StringAliasUtils {
	private static final Log logger = LogFactory.getLog(StringAliasUtils.class);

	private static final int TOTAL_CHAR_NUM = 62;

	private static Map<String, String> aliasMap = new HashMap<String, String>();
	private static Map<String, String> stringMap = new HashMap<String, String>();

	private static int toOrd(char c) {
		if (c >= 'A' && c <= 'Z')
			return c - 'A';
		else if (c >= 'a' && c <= 'z')
			return c - 'a' + 26;
		else if (c >= '0' && c <= '9')
			return c - '0' + 52;
		else
			throw new IllegalArgumentException("Unsupported char [" + c + "].");
	}

	private static char toChar(int i) {
		if (i >= 0 && i < 26)
			return (char) (i + 'A');
		else if (i >= 26 && i < 52)
			return (char) (i + 'a' - 26);
		else if (i >= 52 && i < 62)
			return (char) (i + '0' - 52);
		else
			throw new IllegalArgumentException("Unsupported char code [" + i
					+ "].");
	}

	private static String toHexString(char c) {
		if (c > 127) {
			throw new IllegalArgumentException("Unsupported char [" + c + "].");
		}

		String s = Integer.toHexString(c);
		if (s.length() == 1)
			return '0' + s;
		else
			return s;
	}

	/**
	 * 为一段较长的文本生成一个相应的较短的唯一性特征码字符串。
	 */
	public static String getUniqueAlias(String s) {
		if (StringUtils.isEmpty(s)) {
			return "";
		}

		if (Configure.getBoolean("view.useStringAlias", false)) {
			String alias = stringMap.get(s);
			if (alias == null) {
				if (Configure.getBoolean("view.useRandomStringAlias", true)) {
					do {
						alias = RandomStringUtils.randomAlphanumeric(10);
					} while (aliasMap.containsKey(alias));
				} else {
					alias = generateOrganizedAlias(s);
				}
				aliasMap.put(alias, s);
				stringMap.put(s, alias);
			}
			return alias;
		} else {
			return s;
		}
	}

	private static String generateOrganizedAlias(String s) {
		String alias;
		int len = s.length();
		int[] bv = new int[len * 2];
		for (int i = 0; i < len; i++) {
			String hex = toHexString(s.charAt(i));
			bv[i * 2] = toOrd(hex.charAt(0));
			bv[i * 2 + 1] = toOrd(hex.charAt(1));
		}

		int code1 = 0, code2 = 0, code3 = 0, code4 = 1, code5 = 1, code6 = 1, code7 = 1, code8 = 1;
		for (int i = 0; i < bv.length; i++) {
			int n = bv[i];

			code1 += n;
			code1 %= TOTAL_CHAR_NUM;

			code2 += (i * n);
			code2 %= TOTAL_CHAR_NUM;

			code3 += ((bv.length - i) * n) + i;
			code3 %= TOTAL_CHAR_NUM;

			code4 *= n;
			code4 %= TOTAL_CHAR_NUM;
			if (code4 == 0)
				code4 = 1;

			code5 *= (i * (n + 1));
			code5 %= TOTAL_CHAR_NUM;
			if (code5 == 0)
				code5 = 1;

			code6 *= ((bv.length - i) * n) + i;
			code6 %= TOTAL_CHAR_NUM;
			if (code6 == 0)
				code6 = 1;

			code7 *= (i * (n + 7));
			code7 %= TOTAL_CHAR_NUM;
			if (code7 == 0)
				code7 = 1;

			code8 *= ((bv.length - i) * (n + 3));
			code8 %= TOTAL_CHAR_NUM;
			if (code8 == 0)
				code8 = 1;
		}
		StringBuffer sb = new StringBuffer(8);
		sb.append(toChar(code1)).append(toChar(code2)).append(toChar(code3))
				.append(toChar(code4)).append(toChar(code5))
				.append(toChar(code6)).append(toChar(code7))
				.append(toChar(code8));
		alias = sb.toString();

		while (aliasMap.containsKey(alias)) {
			logger.warn("The string alias [" + alias + "] -> [" + s
					+ "] is already exists.");
			alias += toChar((int) (Math.random() * TOTAL_CHAR_NUM));
		}
		return alias;
	}

	/**
	 * 根据别名返回原始的较长的文本字符串。
	 */
	public static String getOriginalString(String alias) {
		if (Configure.getBoolean("view.useStringAlias", false)) {
			return aliasMap.get(alias);
		} else {
			return alias;
		}
	}
}
