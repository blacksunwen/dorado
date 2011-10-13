package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcDataResolverOperation;

public abstract class AbstractDbElementTrigger implements DbElementTrigger {

	@Override
	public void doQuery(JdbcDataProviderOperation operation) {
		operation.execute();
	}

	@Override
	public void doResolve(JdbcDataResolverOperation operation) {
		operation.execute();
	}

}
