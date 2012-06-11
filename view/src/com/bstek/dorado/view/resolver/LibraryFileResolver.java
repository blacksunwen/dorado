package com.bstek.dorado.view.resolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.FileResource;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.resource.LocaleResolver;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.util.TempFileUtils;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.resolver.ClientI18NFileRegistry.FileInfo;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;

public class LibraryFileResolver extends
		com.bstek.dorado.web.resolver.LibraryFileResolver {
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

	private static class I18NResource implements Resource {
		protected Resource cachedResource;
		private static Map<Resource[], Resource> cacheFileMap = new HashMap<Resource[], Resource>();

		public I18NResource(Context context, String packageName,
				Resource[] resources) throws IOException {
			synchronized (cacheFileMap) {
				cachedResource = cacheFileMap.get(resources);
				if (cachedResource == null) {
					String path = null;
					Properties properties = new Properties();
					for (Resource resource : resources) {
						if (path == null) {
							path = resource.getPath();
						} else {
							path += ';' + resource.getPath();
						}
						properties.load(resource.getInputStream());
					}

					File file = TempFileUtils.createTempFile("client-i18n-"
							+ packageName + '-', JAVASCRIPT_SUFFIX);

					FileOutputStream fos = new FileOutputStream(file);
					Writer writer = new OutputStreamWriter(fos);
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
					} finally {
						writer.flush();
						writer.close();
						fos.flush();
						fos.close();
					}

					cachedResource = new FileResource(file);
					cacheFileMap.put(resources, cachedResource);
				}
			}
		}

		public Resource createRelative(String relativePath) throws IOException {
			return cachedResource.createRelative(relativePath);
		}

		public boolean exists() {
			return cachedResource.exists();
		}

		public String getDescription() {
			return cachedResource.getDescription();
		}

		public File getFile() throws IOException {
			return cachedResource.getFile();
		}

		public String getFilename() {
			return cachedResource.getFilename();
		}

		public InputStream getInputStream() throws IOException {
			return cachedResource.getInputStream();
		}

		public String getPath() {
			return cachedResource.getPath();
		}

		public long getTimestamp() throws IOException {
			return cachedResource.getTimestamp();
		}

		public URL getURL() throws IOException {
			return cachedResource.getURL();
		}
	}

	private LocaleResolver localeResolver;
	private Locale defaultLocale;
	private ClientI18NFileRegistry clientI18NFileRegistry;
	private Set<String> absolutePaths = new HashSet<String>();

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

	public void setClientI18NFileRegistry(
			ClientI18NFileRegistry clientI18NFileRegistry) {
		this.clientI18NFileRegistry = clientI18NFileRegistry;
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

	protected Resource[] doGetResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		if (StringUtils.isNotEmpty(resourcePrefix)) {
			Resource[] resources = null;
			if (absolutePaths.contains(fileName)) {
				resources = super.getResourcesByFileName(context, null,
						fileName, resourceSuffix);
			} else {
				try {
					resources = super.getResourcesByFileName(context,
							resourcePrefix, fileName, resourceSuffix);
					for (Resource resouce : resources) {
						if (!resouce.exists()) {
							throw new FileNotFoundException(resouce.getPath());
						}
					}
				} catch (FileNotFoundException ex) {
					absolutePaths.add(fileName);
					resources = super.getResourcesByFileName(context, null,
							fileName, resourceSuffix);
				}
			}
			return resources;
		} else {
			return super.getResourcesByFileName(context, resourcePrefix,
					fileName, resourceSuffix);
		}
	}

	@Override
	protected final Resource[] getResourcesByFileName(DoradoContext context,
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
			return doGetResourcesByFileName(context, resourcePrefix, fileName,
					resourceSuffix);
		}
	}

	protected Resource[] getI18NResources(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		String packageName = fileName.substring(I18N.length());
		FileInfo fileInfo = clientI18NFileRegistry.getFileInfo(packageName);
		Locale locale = localeResolver.resolveLocale();

		Resource[] resources;
		if (fileInfo == null) {
			Resource resource = getI18NResource(context,
					PathUtils.concatPath(resourcePrefix, fileName), locale);
			resources = new Resource[] { resource };
		} else if (fileInfo.isReplace()) {
			Resource resource = getI18NResource(context, fileInfo.getPath(),
					locale);
			resources = new Resource[] { resource };
		} else {
			resources = new Resource[2];
			resources[0] = getI18NResource(context,
					PathUtils.concatPath(resourcePrefix, fileName), locale);
			resources[1] = getI18NResource(context, fileInfo.getPath(), locale);
		}
		return new Resource[] { new I18NResource(context, packageName,
				resources) };
	}

	protected Resource getI18NResource(DoradoContext context, String path,
			Locale locale) throws Exception {
		if (locale == null) {
			locale = defaultLocale;
		}

		String localeSuffix = "";
		if (locale != null) {
			String localeString = locale.toString();
			if (StringUtils.isNotEmpty(localeString)) {
				localeSuffix = '.' + localeString;
			}
		}

		Resource resource = context.getResource(path + localeSuffix
				+ I18N_FILE_SUFFIX);
		if (resource == null || !resource.exists()) {
			if (StringUtils.isNotEmpty(localeSuffix)) {
				resource = context.getResource(path + I18N_FILE_SUFFIX);
				if (resource == null || !resource.exists()) {
					throw new FileNotFoundException("File [" + path
							+ localeSuffix + I18N_FILE_SUFFIX + "] or [" + path
							+ I18N_FILE_SUFFIX + "] not found.");
				}
			} else {
				throw new FileNotFoundException("File [" + path + localeSuffix
						+ I18N_FILE_SUFFIX + "] not found.");
			}
		}
		return resource;
	}

	private String getCacheKey(String resourcePrefix, String fileName,
			String resourceSuffix, String skin) {
		return (new StringBuffer(resourcePrefix)).append('+').append(fileName)
				.append('+').append(resourceSuffix).append('+').append(skin)
				.toString();
	}

	protected Resource[] getJavaScriptResources(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		String skin = getSkin();
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				skin);
		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				if (fileName.startsWith(CURRENT_SKIN_PREFIX)) {
					fileName = fileName.replace(CURRENT_SKIN, getSkin());
				}
				if (Configure.getBoolean("view.useMinifiedJavaScript")) {
					resources = doGetResourcesByFileName(context,
							resourcePrefix, fileName, MIN_JAVASCRIPT_SUFFIX);
					if (!resources[0].exists()) {
						resources = null;
					}
				}
				if (resources == null) {
					resources = doGetResourcesByFileName(context,
							resourcePrefix, fileName, resourceSuffix);
				}
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}

	protected Resource[] getStyleSheetResources(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		String skin = getSkin();
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				skin);
		boolean useMinifiedStyleSheet = Configure
				.getBoolean("view.useMinifiedStyleSheet");
		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				String template = fileName;
				if (template.indexOf(CURRENT_SKIN) >= 0) {
					fileName = template.replace(CURRENT_SKIN, INHERENT_SKIN);
					Resource inherentResource = null;
					if (useMinifiedStyleSheet) {
						inherentResource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, MIN_STYLESHEET_SUFFIX)[0];
						if (!inherentResource.exists()) {
							inherentResource = null;
						}
					}
					if (inherentResource == null) {
						inherentResource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, resourceSuffix)[0];
					}

					fileName = template.replace(CURRENT_SKIN, skin);
					Resource concreteResource = null;
					if (useMinifiedStyleSheet) {
						concreteResource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, MIN_STYLESHEET_SUFFIX)[0];
						if (!concreteResource.exists()) {
							concreteResource = null;
						}
					}
					if (concreteResource == null) {
						concreteResource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, resourceSuffix)[0];
					}

					if (!concreteResource.exists()) {
						fileName = template.replace(CURRENT_SKIN, DEFAULT_SKIN);
						Resource defaultResource = null;
						if (useMinifiedStyleSheet) {
							defaultResource = super.getResourcesByFileName(
									context, resourcePrefix, fileName,
									MIN_STYLESHEET_SUFFIX)[0];
							if (!defaultResource.exists()) {
								defaultResource = null;
							}
						}
						if (defaultResource == null) {
							defaultResource = doGetResourcesByFileName(context,
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
						resources = doGetResourcesByFileName(context,
								resourcePrefix, fileName, MIN_STYLESHEET_SUFFIX);
						if (!resources[0].exists()) {
							resources = null;
						}
					}
					if (resources == null) {
						resources = doGetResourcesByFileName(context,
								resourcePrefix, fileName, resourceSuffix);
					}
				}
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}
}
