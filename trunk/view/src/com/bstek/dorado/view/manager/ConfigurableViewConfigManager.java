package com.bstek.dorado.view.manager;

import java.util.Map;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-28
 */
public class ConfigurableViewConfigManager extends ViewConfigManagerSupport {
	/**
	 * 设置包含视图工厂信息的Map集合。其中Map集合的键为视图名称或名称通配描述，值为相应的视图工厂。
	 */
	public void setViewConfigFactoryMap(Map<String, Object> viewConfigFactoryMap) {
		for (Map.Entry<String, Object> entry : viewConfigFactoryMap
				.entrySet()) {
			String pattern = entry.getKey();
			registerViewConfigFactory(pattern, entry.getValue());
		}
	}
}
