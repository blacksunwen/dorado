package com.bstek.dorado.data.provider.manager;

import com.bstek.dorado.data.config.definition.DataCreationContext;
import com.bstek.dorado.data.config.definition.DataProviderDefinition;
import com.bstek.dorado.data.config.definition.DataProviderDefinitionManager;
import com.bstek.dorado.data.provider.DataProvider;

/**
 * 默认的DataProvider管理类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 8 2007
 */
public class DefaultDataProviderManager implements DataProviderManager {
	private DataProviderDefinitionManager dataProviderDefinitionManager;

	/**
	 * 设置DataProvider配置声明管理器。
	 */
	public void setDataProviderDefinitionManager(
			DataProviderDefinitionManager dataProviderDefinitionManager) {
		this.dataProviderDefinitionManager = dataProviderDefinitionManager;
	}

	public DataProviderDefinitionManager getDataProviderDefinitionManager() {
		return dataProviderDefinitionManager;
	}

	public DataProvider getDataProvider(String name) throws Exception {
		DataProviderDefinition definition = dataProviderDefinitionManager
				.getDefinition(name);
		return (definition != null) ? (DataProvider) definition
				.create(new DataCreationContext()) : null;
	}

}
