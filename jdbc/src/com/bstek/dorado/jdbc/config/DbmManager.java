package com.bstek.dorado.jdbc.config;

import java.util.Collection;

import com.bstek.dorado.config.definition.DefinitionManager;
import com.bstek.dorado.jdbc.model.DbElementDefinition;


/**
 * JDBC模块的配置管理器
 * 
 * @author mark
 * 
 */
public interface DbmManager extends DefinitionManager<DbElementDefinition>{

	void registerDbm(DbModel dbm);
	
	Collection<DbModel> listAllDbms();

	void clearDbms();
}
