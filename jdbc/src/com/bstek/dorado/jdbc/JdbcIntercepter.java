package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.config.DbElementDefinition;

/**
 * JDBC模块的拦截器
 * 
 * @author mark.li@bstek.com
 *
 */
public interface JdbcIntercepter {

	JdbcDataProviderOperation getOperation(JdbcDataProviderOperation operation);
	
	JdbcDataResolverOperation getOperation(JdbcDataResolverOperation operation);
	
	JdbcRecordOperation getOperation(JdbcRecordOperation operation);
	
	DbElementDefinition getDefinition(DbElementDefinition def);
}
