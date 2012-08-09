package com.bstek.dorado.view.resolver;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;

public class LibraryFileResolver extends
		com.bstek.dorado.view.resolver.PackageFileResolver {
	private static final String I18N_PREFIX = "resources/i18n/";
	private static final String DEFAULT_SKIN = "default";
	private static final String INHERENT_SKIN = "inherent";
	private static final String CURRENT_SKIN = "~current";
	private static final String CURRENT_SKIN_PREFIX = "skins/" + CURRENT_SKIN
			+ '/';

	private Set<String> absolutePaths = new HashSet<String>();

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
	protected Resource[] doGetResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		return doGetResourcesByFileName(context, resourcePrefix, fileName,
				resourceSuffix, false);
	}

	protected Resource[] doGetResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix,
			boolean dontRecordAbsolutePath) throws Exception {
		if (StringUtils.isNotEmpty(resourcePrefix)) {
			Resource[] resources = null;
			if (absolutePaths.contains(fileName)) {
				resources = super.getResourcesByFileName(context, null,
						fileName, resourceSuffix);
			} else {
				resources = super.getResourcesByFileName(context,
						resourcePrefix, fileName, resourceSuffix);
				Resource resource = resources[0];
				if (!resource.exists()) {
					if (!dontRecordAbsolutePath) {
						absolutePaths.add(fileName);
					}
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
		String fileName = fileInfo.getFileName();
		if (JAVASCRIPT_SUFFIX.equals(resourceSuffix)) {
			if (fileName.indexOf(I18N_PREFIX) >= 0
					|| I18N.equals(fileInfo.getFileType())) { // 国际化资源
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
			return doGetResourcesByFileName(context, resourcePrefix, fileName,
					resourceSuffix);
		}
	}

	protected Resource doGetI18NResource(DoradoContext context,
			String resourcePrefix, String fileName, String localeSuffix)
			throws Exception, FileNotFoundException {
		Resource resource = doGetResourcesByFileName(context, resourcePrefix,
				fileName, localeSuffix + I18N_FILE_SUFFIX, true)[0];
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

	@Override
	protected Resource[] getJavaScriptResources(DoradoContext context,
			FileInfo fileInfo, String resourcePrefix, String resourceSuffix)
			throws Exception {
		String fileName = fileInfo.getFileName();
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

	@Override
	protected Resource[] getStyleSheetResources(DoradoContext context,
			FileInfo fileInfo, String resourcePrefix, String resourceSuffix)
			throws Exception {
		String fileName = fileInfo.getFileName();
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
					if (useMinCss) {
						concreteResource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, MIN_STYLESHEET_SUFFIX)[0];
						if (!concreteResource.exists()) {
							throw new FileNotFoundException(
									concreteResource.getPath());
						}
					}
					if (concreteResource == null) {
						concreteResource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, resourceSuffix)[0];
					}

					if (!concreteResource.exists()) {
						fileName = template.replace(CURRENT_SKIN, DEFAULT_SKIN);
						Resource defaultResource = null;
						if (useMinCss) {
							defaultResource = doGetResourcesByFileName(context,
									resourcePrefix, fileName,
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
					Resource minResource = null, resource = null;
					if (useMinCss) {
						minResource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, MIN_STYLESHEET_SUFFIX)[0];
						if (!minResource.exists()) {
							resource = doGetResourcesByFileName(context,
									resourcePrefix, fileName, resourceSuffix)[0];
							if (!resource.exists()) {
								throw new FileNotFoundException("Neither ["
										+ minResource.getPath() + "] nor ["
										+ resource.getPath()
										+ "] could be found.");
							}
						} else {
							resource = minResource;
						}
					}
					resources = new Resource[] { resource };
				}
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}
}
