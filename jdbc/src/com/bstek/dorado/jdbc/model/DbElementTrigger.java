package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcDataResolverOperation;

public interface DbElementTrigger {

	void doQuery(JdbcDataProviderOperation operation);
	
	void doResolve(JdbcDataResolverOperation operation);
}
