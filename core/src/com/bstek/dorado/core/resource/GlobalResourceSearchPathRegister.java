/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
