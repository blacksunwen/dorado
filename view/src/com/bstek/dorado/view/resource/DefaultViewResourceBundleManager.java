package com.bstek.dorado.view.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.keyvalue.MultiKey;

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
		path.substring(0, path.length() - VIEW_FILE_SUFFIX_LENTH);

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
