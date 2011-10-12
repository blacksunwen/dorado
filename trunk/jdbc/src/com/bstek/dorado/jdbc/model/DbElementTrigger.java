package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;

public interface DbElementTrigger {

	void doQuery(JdbcDataProviderOperation operation);
}
