package com.bstek.dorado.jdbc;


public abstract class AbstractDbTableTrigger implements DbTableTrigger {

	@Override
	public void doQuery(JdbcDataProviderOperation operation) {
		operation.execute();
	}

	@Override
	public void doSave(JdbcRecordOperation operation) {
		operation.execute();
	}

}
