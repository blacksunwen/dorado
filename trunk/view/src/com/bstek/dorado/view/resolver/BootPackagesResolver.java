package com.bstek.dorado.view.resolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.bstek.dorado.core.Configure;
import com.bstek.dorado.core.io.FileResource;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.core.pkgs.PackageManager;
import com.bstek.dorado.util.PathUtils;
import com.bstek.dorado.util.TempFileUtils;
import com.bstek.dorado.view.output.JsonBuilder;
import com.bstek.dorado.view.output.OutputUtils;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.loader.Package;
import com.bstek.dorado.web.loader.PackagesConfig;
import com.bstek.dorado.web.loader.PackagesConfigManager;
import com.bstek.dorado.web.loader.Pattern;
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

	private String bootFile;
	
	public BootPackagesResolver() {
		setUseResourcesCache(true);
	}

	public void setBootFile(String bootFile) {
		this.bootFile = bootFile;
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

	protected void writeSetting(Writer writer, String key, Object value,
			boolean quote, boolean comma) throws IOException {
		writer.append('"').append(key).append('"').append(':');
		if (quote)
			writer.append('"');
		writer.append(StringEscapeUtils.escapeJavaScript(String.valueOf(value)));
		if (quote)
			writer.append('"');
		if (comma)
			writer.append(",\n");
		else
			writer.append('\n');
	}

	/**
	 * 向客户端输出资源包的配置信息。
	 * 
	 * @throws IOException
	 */
	protected void outputPackagesConfig(Writer writer,
			PackagesConfig packagesConfig) throws Exception {
		writer.write("window.$setting={\n");
		writeSetting(writer, "core.debugEnabled",
				Configure.getBoolean("view.debugEnabled"), false, true);
		writeSetting(writer, "core.showExceptionStackTrace",
				Configure.getBoolean("view.showExceptionStackTrace"), false,
				true);
		String contextPath = DoradoContext.getAttachedRequest()
				.getContextPath();
		writeSetting(writer, "common.contextPath", contextPath, true, true);
		writeSetting(writer, "widget.skinRoot", ">dorado/client/skins/", true,
				false);
		writer.write("};\n");

		writer.write(CLIENT_PACKAGES_CONFIG
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
			writer.write(CLIENT_PACKAGES_CONFIG);
			writer.write(".patterns=");
			jsonBuilder.object();
			for (Map.Entry<String, Pattern> entry : patterns.entrySet()) {
				jsonBuilder.key(entry.getKey());
				outputPattern(jsonBuilder, entry.getValue());
			}
			jsonBuilder.endObject();
			writer.write(";\n");
		}

		jsonBuilder = new JsonBuilder(writer);
		jsonBuilder.setPrettyFormat(Configure
				.getBoolean("view.outputPrettyJson"));
		Map<String, Package> packages = packagesConfig.getPackages();
		if (packages != null) {
			writer.write(CLIENT_PACKAGES_CONFIG);
			writer.write(".packages=");
			jsonBuilder.object();
			for (Map.Entry<String, Package> entry : packages.entrySet()) {
				jsonBuilder.key(entry.getKey());
				outputPackage(jsonBuilder, entry.getValue());
			}
			jsonBuilder.endObject();
			writer.write(";\n");
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
		jsonBuilder.key("url").value(
				PathUtils.concatPath(
						pattern.getBaseUri(),
						"${fileName}.dpkg?cacheBuster="
								+ PackageManager.getPackageInfoMD5()));
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
		List<String> depends = pkg.getDepends();
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
