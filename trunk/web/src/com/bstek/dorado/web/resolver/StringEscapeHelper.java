package com.bstek.dorado.web.resolver;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-2-20
 */
public class StringEscapeHelper {
	public String html(String s) {
		return StringEscapeUtils.escapeHtml(s);
	}
}
