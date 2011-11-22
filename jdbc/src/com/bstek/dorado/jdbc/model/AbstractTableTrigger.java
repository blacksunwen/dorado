package com.bstek.dorado.jdbc.model;

import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperation;

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
