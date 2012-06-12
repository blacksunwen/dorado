package com.bstek.dorado.view.resource;

import java.util.Locale;

import com.bstek.dorado.core.resource.LocaleResolver;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-8
 */
public class SpringLocaleResolverAdapter implements LocaleResolver {
	private org.springframework.web.servlet.LocaleResolver springLocaleResolver;

	public void setSpringLocaleResolver(
			org.springframework.web.servlet.LocaleResolver springLocaleResolver) {
		this.springLocaleResolver = springLocaleResolver;
	}

	public Locale resolveLocale() throws Exception {
		Locale locale = null;
		if (springLocaleResolver != null) {
			locale = springLocaleResolver.resolveLocale(DoradoContext
					.getAttachedRequest());
		}
		return locale;
	}

}
