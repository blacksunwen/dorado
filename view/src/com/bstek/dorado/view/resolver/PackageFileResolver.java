/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.view.resolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

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
import com.bstek.dorado.view.loader.Package;
import com.bstek.dorado.view.loader.PackagesConfig;
import com.bstek.dorado.view.loader.PackagesConfigManager;
import com.bstek.dorado.view.loader.Pattern;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.resolver.HttpConstants;
import com.bstek.dorado.web.resolver.ResourcesWrapper;
import com.bstek.dorado.web.resolver.WebFileResolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-23
 */
public class PackageFileResolver extends WebFileResolver {
	private static final String NONE_FILE = "(none)";

	protected static final String I18N = "i18n";
	protected static final String I18N_FILE_SUFFIX = ".properties";
	protected static final String LIBRARY_PACKAGE_SUFFIX = ".dpkg";
	protected static final String JAVASCRIPT_SUFFIX = ".js";
	protected static final String STYLESHEET_SUFFIX = ".css";
	protected static final String MIN_JAVASCRIPT_SUFFIX = ".min.js";
	protected static final String MIN_STYLESHEET_SUFFIX = ".min.css";

	protected Map<String, Resource[]> resourcesCache = new HashMap<String, Resource[]>();

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

	public static class FileInfo {
		private String fileName;
		private String packageName;
		private String fileType;

		public FileInfo(String packageName, String fileName) {
			this.packageName = packageName;
			this.fileName = fileName;
		}

		public FileInfo(String packageName, String fileName, String fileType) {
			this(packageName, fileName);
			this.fileType = fileType;
		}

		public String getFileName() {
			return fileName;
		}

		public String getPackageName() {
			return packageName;
		}

		public String getFileType() {
			return fileType;
		}
	}

	private LocaleResolver localeResolver;
	private Locale defaultLocale;
	private ClientI18NFileRegistry clientI18NFileRegistry;

	private PackagesConfigManager packagesConfigManager;

	public PackageFileResolver() {
		setUseResourcesCache(true);
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

	@Override
	protected String getResourceCacheKey(HttpServletRequest request)
			throws Exception {
		Locale locale = localeResolver.resolveLocale();
		if (locale == null) {
			locale = defaultLocale;
		}
		return getRelativeRequestURI(request) + "|" + locale.toString();
	}

	/**
	 * 返回资源包配置的管理器。
	 * 
	 * @throws Exception
	 */
	protected PackagesConfigManager getPackagesConfigManager() throws Exception {
		if (packagesConfigManager == null) {
			Context context = Context.getCurrent();
			packagesConfigManager = (PackagesConfigManager) context
					.getServiceBean("packagesConfigManager");
		}
		return packagesConfigManager;
	}

	@Override
	protected ResourcesWrapper createResourcesWrapper(
			HttpServletRequest request, DoradoContext context) throws Exception {
		String packageName = StringUtils.substringAfterLast(
				request.getRequestURI(), "/");

		String resourcePrefix = getResourcePrefix();
		String resourceSuffix = getUriSuffix(request);
		packageName = packageName.substring(0, packageName.length()
				- resourceSuffix.length());

		Resource[] resources;
		PackagesConfig packagesConfig = getPackagesConfigManager()
				.getPackagesConfig();
		Map<String, Pattern> patterns = packagesConfig.getPatterns();
		Map<String, Package> packages = packagesConfig.getPackages();

		String[] pkgNames = packageName.split(",");
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		for (int i = 0; i < pkgNames.length; i++) {
			String pkgName = pkgNames[i];
			Package pkg = packages.get(pkgName);
			if (pkg == null) {
				throw new FileNotFoundException("Package [" + pkgName
						+ "] not found.");
			}

			if (i == 0) {
				String contentType = pkg.getContentType();
				if (StringUtils.isEmpty(contentType)) {
					Pattern pattern = patterns.get(pkg.getPattern());
					if (pattern != null) {
						resourceSuffix = pattern.getResourceSuffix();
						contentType = pattern.getContentType();
					}
				}

				if (StringUtils.isEmpty(resourceSuffix)) {
					if (HttpConstants.CONTENT_TYPE_CSS
							.equalsIgnoreCase(contentType)) {
						resourceSuffix = STYLESHEET_SUFFIX;
					} else {
						resourceSuffix = JAVASCRIPT_SUFFIX;
					}
				}
			}

			collectFileInfos(context, fileInfos, pkg, resourcePrefix,
					resourceSuffix);
		}

		List<Resource> resourceList = new ArrayList<Resource>();
		for (FileInfo fileInfo : fileInfos) {
			Resource[] resourceArray = getResourcesByFileInfo(context,
					fileInfo, resourcePrefix, resourceSuffix);
			if (resourceArray != null) {
				for (int j = 0; j < resourceArray.length; j++) {
					resourceList.add(resourceArray[j]);
				}
			}
		}

		resources = new Resource[resourceList.size()];
		resourceList.toArray(resources);

		return new ResourcesWrapper(resources, getResourceTypeManager()
				.getResourceType(resourceSuffix));
	}

	private void collectFileInfos(DoradoContext context,
			List<FileInfo> fileInfos, Package pkg, String resourcePrefix,
			String resourceSuffix) throws Exception {
		String[] fileNameArray = pkg.getFileNames();
		if (fileNameArray != null) {
			for (int j = 0; j < fileNameArray.length; j++) {
				String fileName = StringUtils.trim(fileNameArray[j]);
				if (fileName.indexOf(NONE_FILE) < 0) {
					fileInfos.add(new FileInfo(pkg.getName(), fileName));
				}
			}
		}

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
	}

	protected Resource[] getResourcesByFileInfo(DoradoContext context,
			FileInfo fileInfo, String resourcePrefix, String resourceSuffix)
			throws Exception {
		if (JAVASCRIPT_SUFFIX.equals(resourceSuffix)) {
			if (I18N.equals(fileInfo.getFileType())) { // 国际化资源
				return getI18NResources(context, fileInfo, resourcePrefix,
						resourceSuffix);
			} else {
				return getJavaScriptResources(context, fileInfo,
						resourcePrefix, resourceSuffix);
			}
		} else if (STYLESHEET_SUFFIX.equals(resourceSuffix)) {
			return getStyleSheetResources(context, fileInfo, resourcePrefix,
					resourceSuffix);

		} else {
			return doGetResourcesByFileName(context, resourcePrefix,
					fileInfo.getFileName(), resourceSuffix);
		}
	}

	protected Resource[] doGetResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		return getResourcesByFileName(context, resourcePrefix, fileName,
				resourceSuffix);
	}

	protected String getCacheKey(String resourcePrefix, String fileName,
			String resourceSuffix, Object... params) {
		StringBuffer key = new StringBuffer();
		if (resourcePrefix != null) {
			key.append(resourcePrefix);
		}
		key.append('+').append(fileName).append('+');
		if (resourceSuffix != null) {
			key.append(resourceSuffix).append('+');
		}
		key.append('|');
		if (params != null && params.length > 0) {
			key.append(StringUtils.join(params, '|'));
		}
		return key.toString();
	}

	protected Resource[] getI18NResources(DoradoContext context,
			FileInfo fileInfo, String resourcePrefix, String resourceSuffix)
			throws Exception {
		Locale locale = localeResolver.resolveLocale();
		String cacheKey = getCacheKey(resourcePrefix, fileInfo.getFileName(),
				resourceSuffix, locale);
		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				Resource resource = getI18NResource(context, resourcePrefix,
						fileInfo.getFileName(), resourceSuffix, locale);
				resources = new Resource[] { new I18NResource(context,
						fileInfo.getPackageName(), resource) };
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}

	protected final Resource getI18NResource(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix,
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

		return doGetI18NResource(context, resourcePrefix, fileName,
				localeSuffix);
	}

	protected Resource doGetI18NResource(DoradoContext context,
			String resourcePrefix, String fileName, String localeSuffix)
			throws Exception, FileNotFoundException {
		Resource resource = doGetResourcesByFileName(context, resourcePrefix,
				fileName, localeSuffix + I18N_FILE_SUFFIX)[0];
		if (resource == null || !resource.exists()) {
			if (StringUtils.isNotEmpty(localeSuffix)) {
				resource = doGetResourcesByFileName(context, resourcePrefix,
						fileName, I18N_FILE_SUFFIX)[0];
				if (resource == null || !resource.exists()) {
					throw new FileNotFoundException("File ["
							+ PathUtils.concatPath(resourcePrefix, fileName)
							+ localeSuffix + I18N_FILE_SUFFIX + "] or ["
							+ PathUtils.concatPath(resourcePrefix, fileName)
							+ I18N_FILE_SUFFIX + "] not found.");
				}
			} else {
				throw new FileNotFoundException("File ["
						+ PathUtils.concatPath(resourcePrefix, fileName)
						+ I18N_FILE_SUFFIX + "] not found.");
			}
		}
		return resource;
	}

	protected Resource[] getJavaScriptResources(DoradoContext context,
			FileInfo fileInfo, String resourcePrefix, String resourceSuffix)
			throws Exception {
		boolean useMinJs = Configure.getBoolean("view.useMinifiedJavaScript");
		String fileName = fileInfo.getFileName();
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				useMinJs);
		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				if (useMinJs) {
					resources = doGetResourcesByFileName(context,
							resourcePrefix, fileInfo.getFileName(),
							MIN_JAVASCRIPT_SUFFIX);
					if (!resources[0].exists()) {
						resources = null;
					}
				}
				if (resources == null) {
					resources = doGetResourcesByFileName(context,
							resourcePrefix, fileInfo.getFileName(),
							resourceSuffix);
				}
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}

	protected Resource[] getStyleSheetResources(DoradoContext context,
			FileInfo fileInfo, String resourcePrefix, String resourceSuffix)
			throws Exception {
		boolean useMinCss = Configure.getBoolean("view.useMinifiedStyleSheet");
		String fileName = fileInfo.getFileName();
		String cacheKey = getCacheKey(resourcePrefix, fileName, resourceSuffix,
				useMinCss);
		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				if (useMinCss) {
					resources = doGetResourcesByFileName(context,
							resourcePrefix, fileInfo.getFileName(),
							MIN_STYLESHEET_SUFFIX);
					if (!resources[0].exists()) {
						resources = null;
					}
				}
				if (resources == null) {
					resources = doGetResourcesByFileName(context,
							resourcePrefix, fileInfo.getFileName(),
							resourceSuffix);
				}
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}
}
