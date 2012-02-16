package com.bstek.dorado.jdbc;


public abstract class AbstractTableTrigger implements TableTrigger {

	@Override
	public void doQuery(JdbcDataProviderOperation operation) {
		operation.execute();
	}

	@Override
	public void doSave(JdbcRecordOperation operation) {
		operation.execute();
	}

}
