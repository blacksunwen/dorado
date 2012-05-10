package com.bstek.dorado.core.resource;

import java.util.Locale;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-8
 */
public class EmptyLocaleResolver implements LocaleResolver {
	public Locale resolveLocale() throws Exception {
		return null;
	}

}
