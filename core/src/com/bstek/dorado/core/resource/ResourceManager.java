package com.bstek.dorado.core.resource;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.io.ResourceUtils;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-8
 */
public class ResourceManager extends AbstractResourceManagerSupport {
	private static final Log logger = LogFactory.getLog(ResourceManager.class);
	private static final String RESOURCE_FILE_SUFFIX = ".properties";

	private String bundleName;

	public void init(String bundleName) {
		this.bundleName = bundleName;
	}

	private void checkBundleName() {
		if (StringUtils.isEmpty(bundleName)) {
			throw new IllegalArgumentException(
					"ResourceManager not initilized.");
		}
	}

	protected ResourceBundle getBundle(Locale locale) throws Exception {
		ResourceBundle bundle = null;
		Resource resource = findResource(locale);
		if (resource != null) {
			InputStream in = resource.getInputStream();
			try {
				Properties properties = new Properties();
				properties.load(in);
				bundle = new DefaultResourceBundle(properties);
			} finally {
				in.close();
			}
		}
		// else {
		// throw new FileNotFoundException(
		// "Can not found resource file for \"" + bundleName + "\".");
		// }
		return bundle;
	}

	protected Resource findResource(Locale locale) throws Exception {
		Resource resource = null;

		String path = StringUtils.replace(bundleName, ".", "/");
		if (locale != null) {
			String localeSuffix = '.' + locale.toString();
			resource = ResourceUtils.getResource(path + localeSuffix
					+ RESOURCE_FILE_SUFFIX);
			if (resource != null && resource.exists()) {
				return resource;
			}
		}

		resource = ResourceUtils.getResource(path + RESOURCE_FILE_SUFFIX);
		if (resource != null && resource.exists()) {
			return resource;
		}

		return null;
	}

	public ResourceBundle getBundle() throws Exception {
		checkBundleName();
		Locale locale = getLocale();
		return getBundle(locale);
	}

	public String getString(String path, Object... args) {
		try {
			checkBundleName();

			Locale locale = getLocale();
			String result = null;
			ResourceBundle bundle = getBundle(locale);
			if (bundle != null) {
				result = bundle.getString(path, args);
			}
			if (result == null) {
				result = getString(locale, path, args);
			}
			return result;
		} catch (Exception e) {
			logger.error(e, e);
			return null;
		}
	}
}
