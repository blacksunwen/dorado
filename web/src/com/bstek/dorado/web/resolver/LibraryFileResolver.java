package com.bstek.dorado.web.resolver;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.core.io.Resource;
import com.bstek.dorado.web.DoradoContext;
import com.bstek.dorado.web.loader.Package;
import com.bstek.dorado.web.loader.PackagesConfig;
import com.bstek.dorado.web.loader.PackagesConfigManager;
import com.bstek.dorado.web.loader.Pattern;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-1-23
 */
public class LibraryFileResolver extends WebFileResolver {
	private static final String NONE_FILE = "(none)";

	protected static final String LIBRARY_PACKAGE_SUFFIX = ".dpkg";
	protected static final String JAVASCRIPT_SUFFIX = ".js";
	protected static final String STYLESHEET_SUFFIX = ".css";

	private PackagesConfigManager packagesConfigManager;

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
		List<String> fileNames = new ArrayList<String>();
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
						contentType = pattern.getContentType();
					}
				}
				if (HttpConstants.CONTENT_TYPE_CSS
						.equalsIgnoreCase(contentType)) {
					resourceSuffix = STYLESHEET_SUFFIX;
				} else {
					resourceSuffix = JAVASCRIPT_SUFFIX;
				}
			}

			String[] fileNameArray = pkg.getFileNames();
			if (fileNameArray != null) {
				for (int j = 0; j < fileNameArray.length; j++) {
					String fileName = StringUtils.trim(fileNameArray[j]);
					if (fileName.indexOf(NONE_FILE) < 0) {
						fileNames.add(fileName);
					}
				}
			}
		}

		List<Resource> resourceList = new ArrayList<Resource>();
		for (String fileName : fileNames) {
			Resource[] resourceArray = getResourcesByFileName(context,
					resourcePrefix, fileName, resourceSuffix);
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
}
