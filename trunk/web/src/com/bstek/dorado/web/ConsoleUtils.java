/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
