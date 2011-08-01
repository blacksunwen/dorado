package com.bstek.dorado.web;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-12-7
 */
public abstract class ConsoleUtils {

	public static void outputConfigureItem(String item) {
		String value = Configure.getString(item);
		if (StringUtils.isEmpty(value)) {
			value = "<empty>";
		}
		outputLoadingInfo("[" + item + "=" + value + "]");
	}

	public static void outputLoadingInfo(String s) {
		System.out.print(" * ");
		System.out.print(s);
		System.out.println();
	}
}
