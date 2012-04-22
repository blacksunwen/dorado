package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcDataResolverOperation;
import com.bstek.dorado.jdbc.JdbcIntercepter;
import com.bstek.dorado.jdbc.JdbcRecordOperation;
import com.bstek.dorado.jdbc.config.DbElementDefinition;

/**
 * 默认的JDBC模块的拦截器
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultJdbcIntercepter implements JdbcIntercepter {

	public JdbcDataProviderOperation getOperation(
			JdbcDataProviderOperation operation) {
		return operation;
	}

	public JdbcDataResolverOperation getOperation(
			JdbcDataResolverOperation operation) {
		return operation;
	}

	public JdbcRecordOperation getOperation(JdbcRecordOperation operation) {
		return operation;
	}

	public DbElementDefinition getDefinition(DbElementDefinition def) {
		return def;
	}

}
