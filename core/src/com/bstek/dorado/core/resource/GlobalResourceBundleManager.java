package com.bstek.dorado.core.resource;

import java.util.Locale;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-5
 */
public interface GlobalResourceBundleManager {
	public ResourceBundle getBundle(String bundleName, Locale locale)
			throws Exception;
}
