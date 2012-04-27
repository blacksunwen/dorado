package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.JdbcIntercepter;
import com.bstek.dorado.jdbc.config.DbElementDefinition;

/**
 * 默认的JDBC模块的拦截器
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultJdbcIntercepter implements JdbcIntercepter {

	public QueryOperation getOperation(QueryOperation operation) {
		return operation;
	}

	public DataResolverOperation getOperation(DataResolverOperation operation) {
		return operation;
	}

	public TableRecordOperation getOperation(TableRecordOperation operation) {
		return operation;
	}

	public DeleteAllOperation getOperation(DeleteAllOperation operation) {
		return operation;
	}
	
	public DbElementDefinition getDefinition(DbElementDefinition def) {
		return def;
	}

	public SaveOperation getOperation(SaveOperation operation) {
		return operation;
	}

	public SaveRecordOperation getOperation(SaveRecordOperation operation) {
		return operation;
	}

}
