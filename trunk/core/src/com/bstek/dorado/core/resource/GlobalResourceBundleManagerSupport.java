package com.bstek.dorado.core.resource;

import java.util.Locale;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.collections.keyvalue.MultiKey;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-5
 */
public abstract class GlobalResourceBundleManagerSupport implements
		GlobalResourceBundleManager {
	private Ehcache cache;

	public void setCache(Ehcache cache) {
		this.cache = cache;
	}

	protected abstract ResourceBundle doGetBundle(String bundleName,
			Locale locale) throws Exception;

	public ResourceBundle getBundle(String bundleName, Locale locale)
			throws Exception {
		Object cacheKey = new MultiKey(bundleName, locale);
		synchronized (cache) {
			Element element = cache.get(cacheKey);
			if (element == null) {
				ResourceBundle bundle = doGetBundle(bundleName, locale);
				element = new Element(cacheKey, bundle);
				cache.put(element);
			}
			return (ResourceBundle) element.getValue();
		}
	}

}
