package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperation;

public interface TableTrigger {

	void doQuery(JdbcDataProviderOperation operation);
	
	void doSave(JdbcRecordOperation operation);
}
