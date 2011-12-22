package com.bstek.dorado.data.resolver.manager;

import com.bstek.dorado.data.config.definition.DataResolverDefinitionManager;
import com.bstek.dorado.data.resolver.DataResolver;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since Apr 29, 2009
 * @see com.com.bstek.dorado.data.annotation.DataResolver
 * @see com.bstek.dorado.data.config.definition.DataResolverDefinitionManager
 */
public interface DataResolverManager {

	/**
	 * 返回DataResolver配置声明管理器。
	 */
	DataResolverDefinitionManager getDataResolverDefinitionManager();

	/**
	 * 根据名称返回一个DataResolver。
	 * 
	 * @param name
	 *            DataResolver的名称。
	 * @throws Exception
	 */
	DataResolver getDataResolver(String name) throws Exception;
}
