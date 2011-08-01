package com.bstek.dorado.data.provider.manager;

import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.provider.DataProvider;

/**
 * DataProvider管理类的通用接口。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 8, 2007
 * @see com.bstek.dorado.data.provider.DataProvider
 * @see com.bstek.dorado.data.config.definition.DataProviderDefinitionManager
 */
public interface DataProviderManager {
	/**
	 * 返回DataProvider配置声明管理器。
	 */
	DataProviderDefinitionManager getDataProviderDefinitionManager();

	/**
	 * 根据名称返回一个DataProvider。
	 * 
	 * @param name
	 *            DataProvider的名称。
	 * @throws Exception
	 */
	DataProvider getDataProvider(String name) throws Exception;

}
