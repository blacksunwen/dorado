package com.bstek.dorado.sample.basic;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.web.DoradoContext;

public class LocaleResolver implements
		com.bstek.dorado.core.resource.LocaleResolver {
	private final static String CURRENT_LOCALE_KEY = "com.bstek.dorado.sample.CurrentLocale";

	public Locale resolveLocale() throws Exception {
		Locale locale = null;
		HttpServletRequest request = DoradoContext.getAttachedRequest();
		boolean isView = request.getRequestURI().endsWith(".d");
		if (isView) {
			String localeParam = request.getParameter("locale");
			if (StringUtils.isNotEmpty(localeParam)) {
				locale = new Locale(StringUtils.substringBefore(localeParam,
						"_"), StringUtils.substringAfter(localeParam, "_"));
			}
			request.getSession().setAttribute(CURRENT_LOCALE_KEY, locale);
		} else {
			locale = (Locale) request.getSession().getAttribute(
					CURRENT_LOCALE_KEY);
		}

		if (locale == null) {
			locale = request.getLocale();
		}
		return locale;
	}

}
