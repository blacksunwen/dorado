package com.bstek.dorado.view.resolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
import com.bstek.dorado.util.PathUtils;
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
	private static final String CURRENT_SKIN = "~current";
	private static final String CURRENT_SKIN_PREFIX = "skins/" + CURRENT_SKIN
			+ '/';

	private static class I18NResource extends AbstractResourceAdapter {
		private static Map<Resource, File> cacheFileMap = new HashMap<Resource, File>();

		public I18NResource(Resource adaptee) {
			super(adaptee);
		}

		@Override
		public InputStream getInputStream() throws IOException {
			File file = cacheFileMap.get(adaptee);
			if (file == null) {
				Properties properties = new Properties();
				properties.load(adaptee.getInputStream());

				file = File.createTempFile("i18n-", JAVASCRIPT_SUFFIX);
				file.deleteOnExit();

				Writer writer = new BufferedWriter(new FileWriter(file));
				try {
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
											.escapeJavaScript(value))
									.append('\"');
							jsonBuilder.endValue();
						}
					}
					jsonBuilder.endObject();

					writer.append("\n);");
					writer.flush();
				} finally {
					writer.close();
				}
				cacheFileMap.put(adaptee, file);
			}
			return new FileInputStream(file);
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
		return WebConfigure.getString("view.skin");
	}

	@Override
	protected Resource[] getResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		if (JAVASCRIPT_SUFFIX.equals(resourceSuffix)) {
			if (fileName.indexOf(I18N) >= 0) { // 国际化资源
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
				Resource[] resources = context.getResources(fileName
						+ localeSuffix + I18N_FILE_SUFFIX);
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
					resources = context.getResources(fileName
							+ defaultLocaleSuffix + I18N_FILE_SUFFIX);
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
							+ localeSuffix + I18N_FILE_SUFFIX + "] or ["
							+ fileName + defaultLocaleSuffix + I18N_FILE_SUFFIX
							+ "] not found.");
					logger.warn(e, e);
					resources = null;
				} else {
					for (int i = 0; i < resources.length; i++) {
						resources[i] = new I18NResource(resources[i]);
					}
				}
				return resources;
			} else if (fileName.startsWith(CURRENT_SKIN_PREFIX)) {
				fileName = fileName.replace(CURRENT_SKIN, getSkin());
			}

			if (Configure.getBoolean("view.useMinifiedJavaScript")) {
				resourceSuffix = MIN_JAVASCRIPT_SUFFIX;
			}
		} else if (STYLESHEET_SUFFIX.equals(resourceSuffix)) {
			fileName = fileName.replace(CURRENT_SKIN, getSkin());

			if (Configure.getBoolean("view.useMinifiedJavaScript")) {
				resourceSuffix = MIN_STYLESHEET_SUFFIX;
			}
		}
		return super.getResourcesByFileName(context, resourcePrefix, fileName,
				resourceSuffix);
	}
}
