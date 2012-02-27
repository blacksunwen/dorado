package com.bstek.dorado.jdbc.config;

import com.bstek.dorado.config.definition.DefinitionManager;

/**
 * JDBC模块的模型管理器
 * 
 * @author mark.li@bstek.com
 *
 */
public interface DbmManager extends DefinitionManager<DbElementDefinition>{

	/**
	 * 注册一个{@link com.bstek.dorado.jdbc.config.DbModel}
	 * @param dbm
	 */
	void register(DbModel dbm);
	
	void refresh(GlobalDbModelConfig[] configs);
}
