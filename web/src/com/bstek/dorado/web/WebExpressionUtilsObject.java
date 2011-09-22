package com.bstek.dorado.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.util.PathUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-18
 */
public class WebExpressionUtilsObject {

	public String getContextPath() {
		try {
			HttpServletRequest request = DoradoContext.getAttachedRequest();
			return WebConfigure.getString("web.contextPath",
					request.getContextPath());
		} catch (Exception e) {
			return "/";
		}
	}

	public String url(String urlPattern) {
		if (StringUtils.isNotEmpty(urlPattern)) {
			if (urlPattern.charAt(0) == '>') {
				return PathUtils.concatPath(getContextPath(),
						urlPattern.substring(1));
			}
		}
		return urlPattern;
	}
}
