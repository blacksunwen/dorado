package com.bstek.dorado.jdbc;


public interface TableTrigger {

	void doQuery(JdbcDataProviderOperation operation);
	
	void doSave(JdbcRecordOperation operation);
}
