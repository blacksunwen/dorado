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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.FileResource;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.resource.LocaleResolver;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.util.TempFileUtils;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputUtils;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.loader.Package;
import com.bstek.dorado.web.loader.PackagesConfig;
import com.bstek.dorado.web.loader.PackagesConfigManager;
import com.bstek.dorado.web.loader.Pattern;
import com.bstek.dorado.web.resolver.CacheBusterUtils;
import com.bstek.dorado.web.resolver.ResourcesWrapper;
import com.bstek.dorado.web.resolver.WebFileResolver;

/**
 * 用于向客户端输出初始启动信息的处理器。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 26, 2008
 */
public class BootPackagesResolver extends WebFileResolver {
	private static final String JAVASCRIPT_SUFFIX = ".js";
	private static final String MIN_JAVASCRIPT_SUFFIX = ".min.js";
	private static final String CLIENT_PACKAGES_CONFIG = "$packagesConfig";
	private PackagesConfigManager packagesConfigManager;
	private LocaleResolver localeResolver;
	private String bootFile;
	private List<ClientSettingsOutputter> clientSettingsOutputters = new ArrayList<ClientSettingsOutputter>();

	public BootPackagesResolver() {
		setUseResourcesCache(true);
		addClientSettingsOutputter(new DefaultClientSettingsOutputter());
	}

	/**
	 * 设置用于确定国际化区域、语种信息的处理器。
	 */
	public void setLocaleResolver(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	public void setBootFile(String bootFile) {
		this.bootFile = bootFile;
	}

	public void addClientSettingsOutputter(
			ClientSettingsOutputter clientSettingsOutputter) {
		clientSettingsOutputters.add(clientSettingsOutputter);
	}

	@Override
	protected String getResourceCacheKey(HttpServletRequest request)
			throws Exception {
		Locale locale = localeResolver.resolveLocale();
		return (new StringBuffer(getRelativeRequestURI(request))).append('+')
				.append(Configure.getString("view.useMinifiedJavaScript"))
				.append('+')
				.append((locale != null) ? locale.toString() : null).toString();
	}

	/**
	 * 返回资源包配置的管理器。
	 * 
	 * @throws Exception
	 */
	protected PackagesConfigManager getPackagesConfigManager() throws Exception {
		if (packagesConfigManager == null) {
			DoradoContext context = DoradoContext.getCurrent();
			packagesConfigManager = (PackagesConfigManager) context
					.getServiceBean("packagesConfigManager");
		}
		return packagesConfigManager;
	}

	@Override
	protected Resource[] getResourcesByFileName(DoradoContext context,
			String resourcePrefix, String fileName, String resourceSuffix)
			throws Exception {
		if (Configure.getBoolean("view.useMinifiedJavaScript")) {
			resourceSuffix = MIN_JAVASCRIPT_SUFFIX;
		}
		return super.getResourcesByFileName(context, resourcePrefix, fileName,
				resourceSuffix);
	}

	@Override
	protected ResourcesWrapper createResourcesWrapper(
			HttpServletRequest request, DoradoContext context) throws Exception {
		File file = TempFileUtils.createTempFile("packages-config-",
				JAVASCRIPT_SUFFIX);

		PackagesConfig packagesConfig = getPackagesConfigManager()
				.getPackagesConfig();

		FileOutputStream fos = new FileOutputStream(file);
		Writer writer = new OutputStreamWriter(fos);
		try {
			outputPackagesConfig(writer, packagesConfig);
		} finally {
			writer.flush();
			writer.close();
			fos.flush();
			fos.close();
		}

		Resource resource = new FileResource(file);
		String resourcePrefix = getResourcePrefix();
		Resource[] bootResourceArray = getResourcesByFileName(context,
				resourcePrefix, bootFile, JAVASCRIPT_SUFFIX);

		Resource[] resourceArray = new Resource[bootResourceArray.length + 1];
		System.arraycopy(bootResourceArray, 0, resourceArray, 0,
				bootResourceArray.length);
		resourceArray[bootResourceArray.length] = resource;

		ResourcesWrapper resourcesWrapper = new ResourcesWrapper(resourceArray,
				getResourceTypeManager().getResourceType(JAVASCRIPT_SUFFIX));
		resourcesWrapper.setReloadable(false);
		return resourcesWrapper;
	}

	private void outputProperty(Writer writer, String owner, Object object,
			String property, Object escapeValue) throws Exception {
		Object value = PropertyUtils.getProperty(object, property);
		if (value == escapeValue
				|| (escapeValue != null && escapeValue.equals(value))) {
			return;
		}

		writer.append(owner).append('.').append(property).append('=');
		if (value == null) {
			writer.append("null");
		} else {
			writer.append("\"").append(value.toString()).append("\"");
		}
		writer.append(";\n");
	}

	/**
	 * 向客户端输出资源包的配置信息。
	 * 
	 * @throws IOException
	 */
	protected void outputPackagesConfig(Writer writer,
			PackagesConfig packagesConfig) throws Exception {
		writer.append("window.$setting={\n");

		for (ClientSettingsOutputter clientSettingsOutputter : clientSettingsOutputters) {
			clientSettingsOutputter.output(writer);
		}

		writer.append("\"timestamp\":")
				.append(String.valueOf(System.currentTimeMillis()))
				.append('\n');
		writer.append("};\n");

		writer.append(CLIENT_PACKAGES_CONFIG
				+ ".contextPath=$setting[\"common.contextPath\"];\n");
		outputProperty(writer, CLIENT_PACKAGES_CONFIG, packagesConfig,
				"defaultCharset", null);
		outputProperty(writer, CLIENT_PACKAGES_CONFIG, packagesConfig,
				"defaultContentType", null);

		JsonBuilder jsonBuilder = new JsonBuilder(writer);
		jsonBuilder.setPrettyFormat(Configure
				.getBoolean("view.outputPrettyJson"));
		Map<String, Pattern> patterns = packagesConfig.getPatterns();
		if (patterns != null) {
			writer.append(CLIENT_PACKAGES_CONFIG).append(".patterns=");
			jsonBuilder.object();
			for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
				jsonBuilder.key(entry.getKey());
				outputPattern(jsonBuilder, entry.getValue());
			}
			jsonBuilder.endObject();
			writer.append(";\n");
		}

		jsonBuilder = new JsonBuilder(writer);
		jsonBuilder.setPrettyFormat(Configure
				.getBoolean("view.outputPrettyJson"));
		Map<String, Package> packages = packagesConfig.getPackages();
		if (packages != null) {
			writer.append(CLIENT_PACKAGES_CONFIG).append(".packages=");
			jsonBuilder.object();
			for (Map.Entry<String, Package> entry : packages.entrySet()) {
				jsonBuilder.key(entry.getKey());
				outputPackage(jsonBuilder, entry.getValue());
			}
			jsonBuilder.endObject();
			writer.append(";\n");
		}
	}

	protected void outputPattern(JsonBuilder jsonBuilder, Pattern pattern)
			throws Exception {
		jsonBuilder.object();
		if (!OutputUtils.isEscapeValue(pattern.getContentType())) {
			jsonBuilder.key("contentType").value(pattern.getContentType());
		}
		if (!OutputUtils.isEscapeValue(pattern.getCharset())) {
			jsonBuilder.key("charset").value(pattern.getCharset());
		}
		Locale locale = localeResolver.resolveLocale();
		jsonBuilder
				.key("url")
				.value(PathUtils.concatPath(
						pattern.getBaseUri(),
						"${fileName}.dpkg?cacheBuster="
								+ CacheBusterUtils
										.getCacheBuster((locale != null) ? locale
												.toString() : null)));
		if (pattern.isMergeRequests()) {
			jsonBuilder.escapeableKey("mergeRequests").value(
					pattern.isMergeRequests());
		}
		jsonBuilder.endObject();
	}

	protected void outputPackage(JsonBuilder jsonBuilder, Package pkg)
			throws Exception {
		jsonBuilder.object();
		if (!OutputUtils.isEscapeValue(pkg.getContentType())) {
			jsonBuilder.key("contentType").value(pkg.getContentType());
		}
		if (!OutputUtils.isEscapeValue(pkg.getCharset())) {
			jsonBuilder.key("charset").value(pkg.getCharset());
		}
		if (!OutputUtils.isEscapeValue(pkg.getPattern())) {
			jsonBuilder.key("pattern").value(pkg.getPattern());
		}
		Set<String> depends = pkg.getDepends();
		if (depends != null && depends.size() > 0) {
			jsonBuilder.key("depends").array();
			for (String depend : depends) {
				jsonBuilder.value(depend);
			}
			jsonBuilder.endArray();
		}
		if (!pkg.isMergeRequests()
				&& !OutputUtils.isEscapeValue(pkg.getFileNames())) {
			jsonBuilder.key("fileNames").value(pkg.getFileNames());
		}
		jsonBuilder.endObject();
	}
}
