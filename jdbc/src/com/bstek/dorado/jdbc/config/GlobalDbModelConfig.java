package com.bstek.dorado.jdbc.config;

import java.util.List;

/**
 * 全局共享的{@link DbElement}的配置对象
 * @author mark
 *
 */
public class GlobalDbModelConfig {
	/**
	 * 配置文件的位置
	 */
	private List<String> configLocations;

	public List<String> getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(List<String> configLocations) {
		this.configLocations = configLocations;
	}
	
}
