package com.bstek.dorado.view.resource;

import java.util.Locale;

import com.bstek.dorado.core.resource.AbstractResourceManagerSupport;
import com.bstek.dorado.core.resource.ResourceBundle;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-6
 */
public class ViewResourceManager extends AbstractResourceManagerSupport {
	private ViewResourceBundleManager viewResourceBundleManager;

	public void setViewResourceBundleManager(
			ViewResourceBundleManager viewResourceBundleManager) {
		this.viewResourceBundleManager = viewResourceBundleManager;
	}

	protected ResourceBundle getBundle(
			ViewConfigDefinition viewConfigDefinition, Locale locale)
			throws Exception {
		return viewResourceBundleManager
				.getBundle(viewConfigDefinition, locale);
	}

	public ResourceBundle getBundle(ViewConfigDefinition viewConfigDefinition)
			throws Exception {
		Locale locale = getLocale();
		return getBundle(viewConfigDefinition, locale);
	}

	public String getString(ViewConfigDefinition viewConfigDefinition,
			String path, Object... args) throws Exception {
		Locale locale = getLocale();
		String result = null;
		ResourceBundle bundle = getBundle(viewConfigDefinition, locale);
		if (bundle != null) {
			result = bundle.getString(path, args);
		}
		if (result == null) {
			result = getString(locale, path, args);
		}
		return result;
	}
}
