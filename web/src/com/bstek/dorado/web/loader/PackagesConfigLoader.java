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
package com.bstek.dorado.web.loader;

import org.springframework.beans.factory.InitializingBean;

/**
 * 用于配置在Spring中以自动完成资源包配置文件装载的类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Sep 24, 2008
 */
public class PackagesConfigLoader implements InitializingBean {
	private PackagesConfigManager packagesConfigManager;
	private String configLocation;

	/**
	 * 资源包配置的管理器。
	 */
	public void setPackagesConfigManager(
			PackagesConfigManager packagesConfigManager) {
		this.packagesConfigManager = packagesConfigManager;
	}

	/**
	 * 设置要装载的资源包配置文件的路径。
	 */
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public void afterPropertiesSet() throws Exception {
		if (configLocation != null) {
			packagesConfigManager.addConfigLocation(configLocation);
		}
	}

}
