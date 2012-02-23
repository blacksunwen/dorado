package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcDataResolverOperation;
import com.bstek.dorado.jdbc.JdbcIntercepter;
import com.bstek.dorado.jdbc.JdbcRecordOperation;

/**
 * 默认的JDBC模块的拦截器
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultJdbcIntercepter implements JdbcIntercepter {

	@Override
	public JdbcDataProviderOperation getOperation(
			JdbcDataProviderOperation operation) {
		return operation;
	}

	@Override
	public JdbcDataResolverOperation getOperation(
			JdbcDataResolverOperation operation) {
		return operation;
	}

	@Override
	public JdbcRecordOperation getOperation(JdbcRecordOperation operation) {
		return operation;
	}

}
