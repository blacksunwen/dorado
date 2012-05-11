package com.bstek.dorado.view.config;

import com.bstek.dorado.view.config.definition.ViewConfigDefinition;

/**
 * 视图配置声明对象工厂的通用接口。
 * <p>
 * 用于根据视图名称获得视图的配置声明对象。
 * </p>
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Jan 18, 2008
 */
public interface ViewConfigDefinitionFactory {
	/**
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	ViewConfigInfo getViewConfigInfo(String viewName) throws Exception;

	/**
	 * 根据视图名称获得视图的配置声明对象。
	 * 
	 * @param viewConfigInfo
	 *            视图的配置文件的模型描述对象。
	 * @return 视图的配置声明对象。
	 * @throws Exception
	 */
	ViewConfigDefinition create(String viewName) throws Exception;
}
