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

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.WebConfigure;

public class LibraryFileResolver extends PackageFileResolver {
	private static final String I18N_PREFIX = "resources/i18n/";
	private static final String INHERENT_SKIN = "inherent";
	private static final String DEFAULT_SKIN = "default";
	private static final String DEFAULT_SKIN_PREFIX = DEFAULT_SKIN + '.';
	private static final String CURRENT_SKIN = "~current";
	private static final String CURRENT_SKIN_PREFIX = "skins/" + CURRENT_SKIN
			+ '/';

	private Set<String> absolutePaths = new HashSet<String>();

	private String getDefaultPlatformSkin(String currentSkin) {
		int i = currentSkin.indexOf('.');
		if (i > 0) {
			return DEFAULT_SKIN_PREFIX
					+ StringUtils.substringAfter(currentSkin, ".");
		} else {
			return DEFAULT_SKIN;
		}
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
			if (absolutePaths.contains(fileName + resourceSuffix)) {
				resources = super.getResourcesByFileName(context, null,
						fileName, resourceSuffix);
			} else {
				resources = super.getResourcesByFileName(context,
						resourcePrefix, fileName, resourceSuffix);
				Resource resource = resources[0];
				if (!resource.exists()) {
					if (!dontRecordAbsolutePath) {
						absolutePaths.add(fileName + resourceSuffix);
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

	@Override
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
		boolean useMinJs = WebConfigure
				.getBoolean("view.useMinifiedJavaScript");
		boolean isSkinFile = fileName.startsWith(CURRENT_SKIN_PREFIX);
		String cacheKey, skin = null;

		if (isSkinFile) {
			skin = context.getRequest().getParameter("skin");
			cacheKey = getFileCacheKey(resourcePrefix, fileName,
					resourceSuffix, useMinJs, skin);
		} else {
			cacheKey = getFileCacheKey(resourcePrefix, fileName,
					resourceSuffix, useMinJs);
		}

		synchronized (resourcesCache) {
			Resource[] resources = resourcesCache.get(cacheKey);
			if (resources == null) {
				if (isSkinFile) {
					fileName = fileName.replace(CURRENT_SKIN, skin);
				}

				resources = doGetJavaScriptResources(context, resourcePrefix,
						resourceSuffix, fileName, useMinJs, resources);
				if (isSkinFile && !resources[0].exists()) {
					if (!skin.equals(DEFAULT_SKIN)
							&& !skin.startsWith(DEFAULT_SKIN_PREFIX)) {
						String defaultPlatformSkin = getDefaultPlatformSkin(skin);
						fileName = fileInfo.getFileName().replace(CURRENT_SKIN,
								defaultPlatformSkin);
						resources = doGetJavaScriptResources(context,
								resourcePrefix, resourceSuffix, fileName,
								useMinJs, resources);
					}
				}
				resourcesCache.put(cacheKey, resources);
			}
			return resources;
		}
	}

	private Resource[] doGetJavaScriptResources(DoradoContext context,
			String resourcePrefix, String resourceSuffix, String fileName,
			boolean useMinJs, Resource[] resources) throws Exception {
		if (useMinJs) {
			resources = doGetResourcesByFileName(context, resourcePrefix,
					fileName, MIN_JAVASCRIPT_SUFFIX);
			if (!resources[0].exists()) {
				resources = null;
			}
		}
		if (resources == null) {
			resources = doGetResourcesByFileName(context, resourcePrefix,
					fileName, resourceSuffix);
		}
		return resources;
	}

	@Override
	protected Resource[] getStyleSheetResources(DoradoContext context,
			FileInfo fileInfo, String resourcePrefix, String resourceSuffix)
			throws Exception {
		String fileName = fileInfo.getFileName();

		String uri = context.getRequest().getRequestURI();
		int i2 = uri.lastIndexOf('/');
		int i1 = uri.lastIndexOf('/', i2 - 1);
		String skin = uri.substring(i1 + 1, i2);

		boolean useMinCss = WebConfigure
				.getBoolean("view.useMinifiedStyleSheet");
		String cacheKey = getFileCacheKey(resourcePrefix, fileName,
				resourceSuffix, skin, useMinCss);
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
					}
					if (concreteResource == null) {
						concreteResource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, resourceSuffix)[0];
					}

					if (!skin.equals(DEFAULT_SKIN)
							&& !concreteResource.exists()) {
						boolean shouldTryDefaultSkin = true;
						if (!skin.startsWith(DEFAULT_SKIN_PREFIX)) {
							String defaultPlatformSkin = getDefaultPlatformSkin(skin);
							fileName = template.replace(CURRENT_SKIN,
									defaultPlatformSkin);
							Resource defaultResource = null;
							if (useMinCss) {
								defaultResource = doGetResourcesByFileName(
										context, resourcePrefix, fileName,
										MIN_STYLESHEET_SUFFIX)[0];
								if (!defaultResource.exists()) {
									defaultResource = null;
								}
							}
							if (defaultResource == null) {
								defaultResource = doGetResourcesByFileName(
										context, resourcePrefix, fileName,
										resourceSuffix)[0];
							}
							if (defaultResource.exists()) {
								shouldTryDefaultSkin = false;
								concreteResource = defaultResource;
							}
						}

						if (shouldTryDefaultSkin) {
							fileName = template.replace(CURRENT_SKIN,
									DEFAULT_SKIN);
							Resource defaultResource = null;
							if (useMinCss) {
								defaultResource = doGetResourcesByFileName(
										context, resourcePrefix, fileName,
										MIN_STYLESHEET_SUFFIX)[0];
								if (!defaultResource.exists()) {
									defaultResource = null;
								}
							}
							if (defaultResource == null) {
								defaultResource = doGetResourcesByFileName(
										context, resourcePrefix, fileName,
										resourceSuffix)[0];
							}
							if (defaultResource.exists()) {
								concreteResource = defaultResource;
							}
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
					} else {
						resource = doGetResourcesByFileName(context,
								resourcePrefix, fileName, resourceSuffix)[0];
						if (!resource.exists()) {
							throw new FileNotFoundException("File ["
									+ resource.getPath() + "] not found.");
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
