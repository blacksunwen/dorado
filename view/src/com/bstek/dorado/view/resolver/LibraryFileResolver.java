package com.bstek.dorado.view.resolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.LocaleResolver;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.AbstractResourceAdapter;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.util.FileHandler;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.util.TempFileUtils;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;

public class LibraryFileResolver extends
		com.bstek.dorado.web.resolver.LibraryFileResolver {
	private static Log logger = LogFactory.getLog(LibraryFileResolver.class);

	private static final String MIN_JAVASCRIPT_SUFFIX = ".min.js";
	private static final String MIN_STYLESHEET_SUFFIX = ".min.css";
	private static final String I18N = "resources/i18n/";
	private static final String I18N_FILE_SUFFIX = ".properties";
	private static final String DEFAULT_SKIN = "default";
	private static final String INHERENT_SKIN = "inherent";
	private static final String CURRENT_SKIN = "~current";
	private static final String CURRENT_SKIN_PREFIX = "skins/" + CURRENT_SKIN
			+ '/';

	private Map<String, Resource[]> resourcesCache = new HashMap<String, Resource[]>();

	private static class I18NResource extends AbstractResourceAdapter {
		private static Map<Resource, FileHandler> cacheFileMap = new HashMap<Resource, FileHandler>();

		public I18NResource(Resource adaptee) {
			super(adaptee);
		}

		@Override
		public InputStream getInputStream() throws IOException {
			FileHandler fileHandler = cacheFileMap.get(adaptee);
			if (fileHandler == null) {
				Properties properties = new Properties();
				properties.load(adaptee.getInputStream());

				final String tempFileId = adaptee.getPath();
				fileHandler = TempFileUtils.createTempFile(tempFileId,
						"client-i18n-", JAVASCRIPT_SUFFIX);

				Writer writer = fileHandler.getWriter();
				String namespace = properties.getProperty("namespace");
				properties.remove("namespace");
				writer.append("dorado.util.Resource.append(");
				if (StringUtils.isEmpty(namespace)) {
					writer.append("\n");
				} else {
					writer.append("\"")
							.append(StringEscapeUtils
									.escapeJavaScript(namespace))
							.append("\",\n");
				}

				JsonBuilder jsonBuilder = new JsonBuilder(writer);
				jsonBuilder.object();
				for (Map.Entry<?, ?> entry : properties.entrySet()) {
					String value = (String) entry.getValue();
					if (value != null) {
						jsonBuilder.key((String) entry.getKey());
						jsonBuilder.beginValue();
						writer.append('\"')
								.append(StringEscapeUtils
										.escapeJavaScript(value)).append('\"');
						jsonBuilder.endValue();
					}
				}
				jsonBuilder.endObject();

				writer.append("\n);");
				writer.flush();

				cacheFileMap.put(adaptee, fileHandler);
			}
			return fileHandler.createInputStream();
		}
	}

	private LocaleResolver localeResolver;
	private Locale defaultLocale;

	/**
	 * 设置用于确定国际化区域、语种信息的处理器。
	 */
	public void setLocaleResolver(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	/**
	 * 设置默认的国际化区域、语种。
	 */
	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	/**
	 * 返回客户端使用的皮肤的名称。
	 */
	protected String getSkin() {
		String skin = WebConfigure.getString("view.skin");
		if (INHERENT_SKIN.equals(skin)) {
			throw new IllegalArgumentException("\"" + INHERENT_SKIN
					+ "\" is not a valid dorado skin.");
		}
		return skin;
	}

	@Override
	protected Resource[] getResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		if (JAVASCRIPT_SUFFIX.equals(resourceSuffix)) {
			if (fileName.indexOf(I18N) >= 0) { // 国际化资源
				return getI18NResources(context, resourcePrefix, fileName,
						resourceSuffix);
			} else {
				return getJavaScriptResources(context, resourcePrefix,
						fileName, resourceSuffix);
			}
		} else if (STYLESHEET_SUFFIX.equals(resourceSuffix)) {
			return getStyleSheetResources(context, resourcePrefix, fileName,
					resourceSuffix);

		} else {
			return super.getResourcesByFileName(context, resourcePrefix,
					fileName, resourceSuffix);
		}
	}

	protected Resource[] getI18NResources(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		Resource[] resources;
		String localeSuffix = "", defaultLocaleSuffix = "";
		Locale locale = localeResolver.resolveLocale(DoradoContext
				.getAttachedRequest());
		boolean hasCustomLocale = (locale != null);
		if (hasCustomLocale) {
			String localeString = locale.toString();
			if (StringUtils.isNotEmpty(localeString)) {
				localeSuffix = '.' + localeString;
			}
		}

		fileName = PathUtils.concatPath(resourcePrefix, fileName);
		resources = context.getResources(fileName + localeSuffix
				+ I18N_FILE_SUFFIX);
		boolean resourcesValid = (resources.length > 0);
		if (resourcesValid) {
			for (Resource resource : resources) {
				if (!resource.exists()) {
					resourcesValid = false;
					break;
				}
			}
		}

		// 尝试获取默认的国际化资源文件
		if (!resourcesValid && hasCustomLocale) {
			String localeString = (defaultLocale != null) ? defaultLocale
					.toString() : null;
			if (StringUtils.isNotEmpty(localeString)) {
				defaultLocaleSuffix = '.' + localeString;
			}
			resources = context.getResources(fileName + defaultLocaleSuffix
					+ I18N_FILE_SUFFIX);
		}
		resourcesValid = (resources.length > 0);
		if (resourcesValid) {
			for (Resource resource : resources) {
				if (!resource.exists()) {
					resourcesValid = false;
					break;
				}
			}
		}

		if (!resourcesValid) {
			Exception e = new FileNotFoundException("File [" + fileName
					+ localeSuffix + I18N_FILE_SUFFIX + "] or [" + fileName
					+ defaultLocaleSuffix + I18N_FILE_SUFFIX + "] not found.");
			logger.warn(e, e);
			resources = null;
		} else {
			for (int i = 0; i < resources.length; i++) {
				resources[i] = new I18NResource(resources[i]);
			}
		}
		return resources;
	}

	private String getCacheKey(String resourcePrefix, String fileName,
			String resourceSuffix, String skin) {
		return (new StringBuffer(resourcePrefix)).append('+').append(fileName)
				.append('+').append(resourceSuffix).append('+').append(skin)
				.toString();
	}

	protected synchronized Resource[] getJavaScriptResources(
			DoradoContext context, String resourcePrefix, String fileName,
			String resourceSuffix) throws Exception {
		String skin = getSkin();
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				skin);
		Resource[] resources = resourcesCache.get(cacheKey);
		if (resources == null) {
			if (fileName.startsWith(CURRENT_SKIN_PREFIX)) {
				fileName = fileName.replace(CURRENT_SKIN, getSkin());
			}
			if (Configure.getBoolean("view.useMinifiedJavaScript")) {
				resources = super.getResourcesByFileName(context,
						resourcePrefix, fileName, MIN_JAVASCRIPT_SUFFIX);
				if (!resources[0].exists()) {
					resources = null;
				}
			}
			if (resources == null) {
				resources = super.getResourcesByFileName(context,
						resourcePrefix, fileName, resourceSuffix);
			}
			resourcesCache.put(cacheKey, resources);
		}
		return resources;
	}

	protected synchronized Resource[] getStyleSheetResources(
			DoradoContext context, String resourcePrefix, String fileName,
			String resourceSuffix) throws Exception {
		String skin = getSkin();
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				skin);
		boolean useMinifiedStyleSheet = Configure
				.getBoolean("view.useMinifiedStyleSheet");
		Resource[] resources = resourcesCache.get(cacheKey);
		if (resources == null) {
			String template = fileName;
			if (template.indexOf(CURRENT_SKIN) >= 0) {
				fileName = template.replace(CURRENT_SKIN, INHERENT_SKIN);
				Resource inherentResource = null;
				if (useMinifiedStyleSheet) {
					inherentResource = super.getResourcesByFileName(context,
							resourcePrefix, fileName, MIN_STYLESHEET_SUFFIX)[0];
					if (!inherentResource.exists()) {
						inherentResource = null;
					}
				}
				if (inherentResource == null) {
					inherentResource = super.getResourcesByFileName(context,
							resourcePrefix, fileName, resourceSuffix)[0];
				}

				fileName = template.replace(CURRENT_SKIN, skin);
				Resource concreteResource = null;
				if (useMinifiedStyleSheet) {
					concreteResource = super.getResourcesByFileName(context,
							resourcePrefix, fileName, MIN_STYLESHEET_SUFFIX)[0];
					if (!concreteResource.exists()) {
						concreteResource = null;
					}
				}
				if (concreteResource == null) {
					concreteResource = super.getResourcesByFileName(context,
							resourcePrefix, fileName, resourceSuffix)[0];
				}

				if (!concreteResource.exists()) {
					fileName = template.replace(CURRENT_SKIN, DEFAULT_SKIN);
					Resource defaultResource = null;
					if (useMinifiedStyleSheet) {
						defaultResource = super
								.getResourcesByFileName(context,
										resourcePrefix, fileName,
										MIN_STYLESHEET_SUFFIX)[0];
						if (!defaultResource.exists()) {
							defaultResource = null;
						}
					}
					if (defaultResource == null) {
						defaultResource = super.getResourcesByFileName(context,
								resourcePrefix, fileName, resourceSuffix)[0];
					}
					if (defaultResource.exists()) {
						concreteResource = defaultResource;
					}
				}

				if (inherentResource.exists()) {
					resources = new Resource[] { inherentResource,
							concreteResource };
				} else {
					resources = new Resource[] { concreteResource };
				}
			} else {
				if (useMinifiedStyleSheet) {
					resources = super.getResourcesByFileName(context,
							resourcePrefix, fileName, MIN_STYLESHEET_SUFFIX);
					if (!resources[0].exists()) {
						resources = null;
					}
				}
				if (resources == null) {
					resources = super.getResourcesByFileName(context,
							resourcePrefix, fileName, resourceSuffix);
				}
			}
			resourcesCache.put(cacheKey, resources);
		}
		return resources;
	}
}
