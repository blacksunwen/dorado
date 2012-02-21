package com.bstek.dorado.jdbc.oracle.v11;

import java.util.Date;

import com.bstek.dorado.jdbc.AbstractDbTableTrigger;
import com.bstek.dorado.jdbc.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.JdbcRecordOperation;

public class LoggerTableTrigger extends AbstractDbTableTrigger {

	@Override
	public void doQuery(JdbcDataProviderOperation operation) {
		System.out.println("Query:" + operation.getDbTable().getName() + ";Date=" + new Date());
		super.doQuery(operation);
	}

	@Override
	public void doSave(JdbcRecordOperation operation) {
		System.out.println("Save:" + operation.getDbTable().getName() + ";Date=" + new Date());
		super.doSave(operation);
	}

}
