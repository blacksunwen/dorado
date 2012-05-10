package com.bstek.dorado.core.resource;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-5-11
 */
public class GlobalResourceSearchPathRegister implements InitializingBean {
	private DefaultGlobalResourceBundleManager globalResourceBundleManager;
	private String searchPath;
	private List<String> searchPaths;

	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}

	public void setSearchPaths(List<String> searchPaths) {
		this.searchPaths = searchPaths;
	}

	public void setGlobalResourceBundleManager(
			DefaultGlobalResourceBundleManager globalResourceBundleManager) {
		this.globalResourceBundleManager = globalResourceBundleManager;
	}

	public void afterPropertiesSet() throws Exception {
		if (StringUtils.isNotBlank(searchPath)) {
			globalResourceBundleManager.addSearchPath(searchPath);
		}
		if (searchPaths != null && !searchPaths.isEmpty()) {
			globalResourceBundleManager.addSearchPaths(searchPaths);
		}
	}
}
