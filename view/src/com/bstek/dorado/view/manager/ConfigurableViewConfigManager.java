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
