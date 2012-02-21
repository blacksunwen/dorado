package com.bstek.dorado.jdbc;


public interface DbTableTrigger {

	void doQuery(JdbcDataProviderOperation operation);
	
	void doSave(JdbcRecordOperation operation);
}
