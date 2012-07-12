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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;
import com.bstek.dorado.web.loader.Package;

public class LibraryFileResolver extends
		com.bstek.dorado.web.resolver.LibraryFileResolver {
	private static final String MIN_JAVASCRIPT_SUFFIX = ".min.js";
	private static final String MIN_STYLESHEET_SUFFIX = ".min.css";
	private static final String I18N = "i18n";
	private static final String I18N_PREFIX = "resources/i18n/";
	private static final String I18N_FILE_SUFFIX = ".properties";
	private static final String DEFAULT_SKIN = "default";
	private static final String INHERENT_SKIN = "inherent";
	private static final String CURRENT_SKIN = "~current";
	private static final String CURRENT_SKIN_PREFIX = "skins/" + CURRENT_SKIN
			+ '/';

	private Map<String, Resource[]> resourcesCache = new HashMap<String, Resource[]>();

	private static class I18NResource implements Resource {
		protected Resource cachedResource;
		private static Map<Resource, Resource> cacheFileMap = new HashMap<Resource, Resource>();

		public I18NResource(Context context, String packageName,
				Resource resource) throws IOException {
			synchronized (cacheFileMap) {
				cachedResource = cacheFileMap.get(resource);
				if (cachedResource == null) {
					Properties properties = new Properties();
					properties.load(resource.getInputStream());

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
					cacheFileMap.put(resource, cachedResource);
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

	public LibraryFileResolver() {
		setUseResourcesCache(false);
	}

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

	protected String getResourceCacheKey(HttpServletRequest request)
			throws Exception {
		Locale locale = localeResolver.resolveLocale();
		if (locale == null) {
			locale = defaultLocale;
		}
		return getRelativeRequestURI(request) + "|" + locale.toString();
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
			String packageName, String resourcePrefix, String fileName,
			String resourceSuffix) throws Exception {
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
	protected Resource[] getResourcesByFileInfo(DoradoContext context,
			FileInfo fileInfo, String resourcePrefix, String resourceSuffix)
			throws Exception {
		String packageName = fileInfo.getPackageName();
		String fileName = fileInfo.getFileName();
		if (JAVASCRIPT_SUFFIX.equals(resourceSuffix)) {
			if (fileName.indexOf(I18N_PREFIX) >= 0
					|| I18N.equals(fileInfo.getFileType())) { // 国际化资源
				return getI18NResources(context, packageName, resourcePrefix,
						fileName, resourceSuffix);
			} else {
				return getJavaScriptResources(context, packageName,
						resourcePrefix, fileName, resourceSuffix);
			}
		} else if (STYLESHEET_SUFFIX.equals(resourceSuffix)) {
			return getStyleSheetResources(context, packageName, resourcePrefix,
					fileName, resourceSuffix);

		} else {
			return doGetResourcesByFileName(context, packageName,
					resourcePrefix, fileName, resourceSuffix);
		}
	}

	private String getCacheKey(String resourcePrefix, String fileName,
			String resourceSuffix, Object... params) {
		StringBuffer key = (new StringBuffer(resourcePrefix)).append('+')
				.append(fileName).append('+').append(resourceSuffix)
				.append('+');
		if (params != null && params.length > 0) {
			key.append(StringUtils.join(params, '+'));
		}
		return key.toString();
	}

	protected List<FileInfo> getFileInfos(DoradoContext context, Package pkg,
			String resourcePrefix, String resourceSuffix) throws Exception {
		List<FileInfo> fileInfos = super.getFileInfos(context, pkg,
				resourcePrefix, resourceSuffix);
		if (JAVASCRIPT_SUFFIX.equals(resourceSuffix)) {
			ClientI18NFileRegistry.FileInfo fileInfo = clientI18NFileRegistry
					.getFileInfo(pkg.getName());
			if (fileInfo != null) {
				if (fileInfo.isReplace()) {
					for (int i = fileInfos.size() - 1; i >= 0; i--) {
						FileInfo fi = fileInfos.get(i);
						if (I18N.equals(fi.getFileType())) {
							fileInfos.remove(i);
						}
					}
				}
				fileInfos.add(new FileInfo(pkg.getName(), fileInfo.getPath(),
						I18N));
			}
		}
		return fileInfos;
	}

	protected Resource[] getI18NResources(DoradoContext context,
			String packageName, String resourcePrefix, String fileName,
			String resourceSuffix) throws Exception {
		Locale locale = localeResolver.resolveLocale();
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				locale);
		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				Resource resource = getI18NResource(context,
						PathUtils.concatPath(resourcePrefix, fileName), locale);
				resources = new Resource[] { new I18NResource(context,
						packageName, resource) };
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
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

	protected Resource[] getJavaScriptResources(DoradoContext context,
			String packageName, String resourcePrefix, String fileName,
			String resourceSuffix) throws Exception {
		String skin = getSkin();
		boolean useMinJs = Configure.getBoolean("view.useMinifiedJavaScript");
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				skin, useMinJs);
		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				if (fileName.startsWith(CURRENT_SKIN_PREFIX)) {
					fileName = fileName.replace(CURRENT_SKIN, getSkin());
				}
				if (useMinJs) {
					resources = doGetResourcesByFileName(context, packageName,
							resourcePrefix, fileName, MIN_JAVASCRIPT_SUFFIX);
					if (!resources[0].exists()) {
						throw new FileNotFoundException(resources[0].getPath());
					}
				}
				if (resources == null) {
					resources = doGetResourcesByFileName(context, packageName,
							resourcePrefix, fileName, resourceSuffix);
				}
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}

	protected Resource[] getStyleSheetResources(DoradoContext context,
			String packageName, String resourcePrefix, String fileName,
			String resourceSuffix) throws Exception {
		String skin = getSkin();
		boolean useMinCss = Configure.getBoolean("view.useMinifiedStyleSheet");
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				skin, useMinCss);
		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				String template = fileName;
				if (template.indexOf(CURRENT_SKIN) >= 0) {
					fileName = template.replace(CURRENT_SKIN, INHERENT_SKIN);
					Resource inherentResource = null;
					if (useMinCss) {
						inherentResource = doGetResourcesByFileName(context,
								packageName, resourcePrefix, fileName,
								MIN_STYLESHEET_SUFFIX)[0];
						if (!inherentResource.exists()) {
							inherentResource = null;
						}
					}
					if (inherentResource == null) {
						inherentResource = doGetResourcesByFileName(context,
								packageName, resourcePrefix, fileName,
								resourceSuffix)[0];
					}

					fileName = template.replace(CURRENT_SKIN, skin);
					Resource concreteResource = null;
					if (useMinCss) {
						concreteResource = doGetResourcesByFileName(context,
								packageName, resourcePrefix, fileName,
								MIN_STYLESHEET_SUFFIX)[0];
						if (!concreteResource.exists()) {
							throw new FileNotFoundException(
									concreteResource.getPath());
						}
					}
					if (concreteResource == null) {
						concreteResource = doGetResourcesByFileName(context,
								packageName, resourcePrefix, fileName,
								resourceSuffix)[0];
					}

					if (!concreteResource.exists()) {
						fileName = template.replace(CURRENT_SKIN, DEFAULT_SKIN);
						Resource defaultResource = null;
						if (useMinCss) {
							defaultResource = super.getResourcesByFileName(
									context, resourcePrefix, fileName,
									MIN_STYLESHEET_SUFFIX)[0];
							if (!defaultResource.exists()) {
								defaultResource = null;
							}
						}
						if (defaultResource == null) {
							defaultResource = doGetResourcesByFileName(context,
									packageName, resourcePrefix, fileName,
									resourceSuffix)[0];
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
					if (useMinCss) {
						resources = doGetResourcesByFileName(context,
								packageName, resourcePrefix, fileName,
								MIN_STYLESHEET_SUFFIX);
						if (!resources[0].exists()) {
							throw new FileNotFoundException(
									resources[0].getPath());
						}
					}
					if (resources == null) {
						resources = doGetResourcesByFileName(context,
								packageName, resourcePrefix, fileName,
								resourceSuffix);
					}
				}
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}
}
