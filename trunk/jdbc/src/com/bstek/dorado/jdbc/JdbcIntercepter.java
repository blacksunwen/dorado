package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.config.DbElementDefinition;
import com.bstek.dorado.jdbc.support.DeleteAllOperation;
import com.bstek.dorado.jdbc.support.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.support.JdbcDataResolverOperation;
import com.bstek.dorado.jdbc.support.JdbcRecordOperation;

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
	
	DeleteAllOperation getOperation(DeleteAllOperation operation);
	
	DbElementDefinition getDefinition(DbElementDefinition def);
	
}
