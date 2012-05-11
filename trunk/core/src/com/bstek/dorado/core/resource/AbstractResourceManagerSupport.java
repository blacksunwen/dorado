package com.bstek.dorado.core.resource;

import java.util.Locale;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-5
 */
public abstract class AbstractResourceManagerSupport {
	private static final String DEFUALT_BUNDLE_NAME = "Default";

	private GlobalResourceBundleManager globalResourceBundleManager;
	private LocaleResolver localeResolver;
	private Locale defaultLocale;

	public void setGlobalResourceBundleManager(
			GlobalResourceBundleManager globalResourceBundleManager) {
		this.globalResourceBundleManager = globalResourceBundleManager;
	}

	/**
	 * 设置用于确定国际化区域、语种信息的处理器。
	 */
	public void setLocaleResolver(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	protected Locale getLocale() throws Exception {
		Locale locale = null;
		if (localeResolver != null) {
			locale = localeResolver.resolveLocale();
		}
		return (locale != null) ? locale : defaultLocale;
	}

	protected String getString(Locale locale, String path, Object... args)
			throws Exception {
		String bundleName, key;
		int i = path.indexOf('/');
		if (i > 0) {
			bundleName = path.substring(0, i);
			key = path.substring(i + 1);
		} else {
			bundleName = DEFUALT_BUNDLE_NAME;
			key = path;
		}

		ResourceBundle bundle = globalResourceBundleManager.getBundle(
				bundleName, locale);
		return (bundle != null) ? bundle.getString(key) : null;
	}
}
