package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcDataResolverOperation;
import com.bstek.dorado.jdbc.JdbcIntercepter;
import com.bstek.dorado.jdbc.JdbcRecordOperation;

public class DefaultJdbcIntercepter implements JdbcIntercepter {

	@Override
	public JdbcDataProviderOperation getJdbcDataProviderOperation(
			JdbcDataProviderOperation operation) {
		return operation;
	}

	@Override
	public JdbcDataResolverOperation getJdbcDataResolverOperation(
			JdbcDataResolverOperation operation) {
		return operation;
	}

	@Override
	public JdbcRecordOperation getJdbcRecordOperation(
			JdbcRecordOperation operation) {
		return operation;
	}

}
