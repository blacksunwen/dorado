package com.bstek.dorado.jdbc.config;

import java.util.Collection;

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
	void registerDbm(DbModel dbm);
	
	/**
	 * 返回全部的{@link com.bstek.dorado.jdbc.config.DbModel}
	 * @return
	 */
	Collection<DbModel> listAllDbms();

	/**
	 * 清空全部的{@link com.bstek.dorado.jdbc.config.DbModel}
	 */
	void clearDbms();
}
