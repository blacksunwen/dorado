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
