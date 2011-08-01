package com.bstek.dorado.data.resolver.manager;

import com.bstek.dorado.data.config.definition.DataCreationContext;
import com.bstek.dorado.data.config.definition.DataResolverDefinition;
import com.bstek.dorado.data.config.definition.DataResolverDefinitionManager;
import com.bstek.dorado.data.resolver.DataResolver;

/**
 * 默认的DataResolver管理类。
 * 
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Mar 8 2007
 */
public class DefaultDataResolverManager implements DataResolverManager {
	private DataResolverDefinitionManager dataResolverDefinitionManager;

	/**
	 * 设置DataResolver配置声明管理器。
	 */
	public void setDataResolverDefinitionManager(
			DataResolverDefinitionManager dataResolverDefinitionManager) {
		this.dataResolverDefinitionManager = dataResolverDefinitionManager;
	}

	public DataResolverDefinitionManager getDataResolverDefinitionManager() {
		return dataResolverDefinitionManager;
	}

	public DataResolver getDataResolver(String name) throws Exception {
		DataResolverDefinition definition = dataResolverDefinitionManager
				.getDefinition(name);
		return (definition != null) ? (DataResolver) definition
				.create(new DataCreationContext()) : null;
	}

}
