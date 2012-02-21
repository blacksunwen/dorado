package com.bstek.dorado.jdbc;

public interface JdbcIntercepter {

	JdbcDataProviderOperation getJdbcDataProviderOperation(JdbcDataProviderOperation operation);
	
	JdbcDataResolverOperation getJdbcDataResolverOperation(JdbcDataResolverOperation operation);
	
	JdbcRecordOperation getJdbcRecordOperation(JdbcRecordOperation operation);
}
