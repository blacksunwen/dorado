package com.bstek.dorado.data.config;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

/**
 * 用于配置在Spring中，定义要装载的数据配置文件的Bean。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 11, 2008
 */
public class DataConfigLoader implements InitializingBean {
	private String configLocation;
	private List<String> configLocations;
	private ConfigurableDataConfigManager dataConfigManager;

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * 设置要装载的配置文件。
	 * 
	 * @param configLocations
	 *            此参数是文件路径的集合，每个文件路径都是String类型的路径描述。
	 */
	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = configLocations;
	}

	/**
	 * 设置数据配置文件的管理器。
	 */
	public void setDataConfigManager(
			ConfigurableDataConfigManager dataConfigManager) {
		this.dataConfigManager = dataConfigManager;
	}

	public void afterPropertiesSet() throws Exception {
		if (configLocation != null) {
			dataConfigManager.addConfigLocation(configLocation);
		}
		if (configLocations != null) {
			dataConfigManager.addConfigLocations(configLocations);
		}
	}
}
