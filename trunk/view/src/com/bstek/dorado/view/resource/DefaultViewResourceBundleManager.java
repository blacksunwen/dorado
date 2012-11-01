/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.resource.DefaultResourceBundle;
import com.bstek.dorado.core.resource.ResourceBundle;
import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-5
 */
public class DefaultViewResourceBundleManager implements
		ViewResourceBundleManager {
	private static final String VIEW_FILE_SUFFIX = ".view.xml";
	private static final int VIEW_FILE_SUFFIX_LENTH = VIEW_FILE_SUFFIX.length();
	private static final String RESOURCE_FILE_SUFFIX = ".properties";

	private Ehcache cache;

	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

	protected Resource findResource(ViewConfigDefinition viewConfigDefinition,
			Locale locale) throws IOException {
		Resource viewResource = viewConfigDefinition.getResource();
		if (viewResource != null) {
			Resource resource;
			String path = viewResource.getPath();
			if (StringUtils.isEmpty(path)) {
				return null;
			}

			path = path.substring(0, path.length() - VIEW_FILE_SUFFIX_LENTH);

			int i = path.lastIndexOf('/');
			if (i >= 0) {
				path = path.substring(i + 1);
			} else {
				i = path.lastIndexOf(':');
				if (i >= 0) {
					path = path.substring(i + 1);
				}
			}

			if (locale != null) {
				String localeSuffix = '.' + locale.toString();
				resource = viewResource.createRelative(path + localeSuffix
						+ RESOURCE_FILE_SUFFIX);
				if (resource != null && resource.exists()) {
					return resource;
				}
			}

			resource = viewResource.createRelative(path + RESOURCE_FILE_SUFFIX);
			if (resource != null && resource.exists()) {
				return resource;
			}
		}
		return null;
	}

	public ResourceBundle doGetBundle(
			ViewConfigDefinition viewConfigDefinition, Locale locale)
			throws Exception {
		Resource resource = findResource(viewConfigDefinition, locale);
		if (resource != null) {
			InputStream in = resource.getInputStream();
			try {
				Properties properties = new Properties();
				properties.load(in);
				return new DefaultResourceBundle(properties);
			} finally {
				in.close();
			}
		}
		return null;
	}

	public ResourceBundle getBundle(ViewConfigDefinition viewConfigDefinition,
			Locale locale) throws Exception {
		Resource resource = viewConfigDefinition.getResource();
		if (resource == null) {
			return null;
		}
		String path = resource.getPath();
		if (StringUtils.isEmpty(path) || !path.endsWith(VIEW_FILE_SUFFIX)) {
			return null;
		}
		path = path.substring(0, path.length() - VIEW_FILE_SUFFIX_LENTH);

		Object cacheKey = new MultiKey(path, locale);
		synchronized (cache) {
			Element element = cache.get(cacheKey);
			if (element == null) {
				ResourceBundle bundle = doGetBundle(viewConfigDefinition,
						locale);
				element = new Element(cacheKey, bundle);
				cache.put(element);
			}
			return (ResourceBundle) element.getValue();
		}
	}

}
