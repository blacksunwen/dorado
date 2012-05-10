package com.bstek.dorado.data.resource;

import java.io.FileNotFoundException;
import java.util.Locale;

import com.bstek.dorado.config.definition.Definition;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.resource.AbstractResourceManagerSupport;
import com.bstek.dorado.core.resource.ResourceBundle;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-6
 */
public class ModelResourceManager extends AbstractResourceManagerSupport {
	private ModelResourceBundleManager modelResourceBundleManager;

	public void setModelResourceBundleManager(
			ModelResourceBundleManager modelResourceBundleManager) {
		this.modelResourceBundleManager = modelResourceBundleManager;
	}

	public ResourceBundle getBundle(Resource modelResource) throws Exception {
		Locale locale = getLocale();
		return modelResourceBundleManager.getBundle(modelResource, locale);
	}

	public String getString(Definition definition, String path, Object... args)
			throws Exception {
		String result = null;
		Locale locale = getLocale();
		ResourceBundle bundle = modelResourceBundleManager.getBundle(
				definition, locale);
		if (bundle != null) {
			result = bundle.getString(path, args);
		}
		if (result == null) {
			try {
				result = getString(locale, path, args);
			} catch (FileNotFoundException e) {
				if (bundle == null) {
					throw e;
				}
			}
		}
		return result;
	}
}
