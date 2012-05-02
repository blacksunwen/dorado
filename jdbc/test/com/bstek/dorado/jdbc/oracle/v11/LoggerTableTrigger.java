package com.bstek.dorado.jdbc.oracle.v11;

import java.util.Date;

import com.bstek.dorado.jdbc.model.AbstractDbTableTrigger;
import com.bstek.dorado.jdbc.support.QueryOperation;
import com.bstek.dorado.jdbc.support.SaveOperation;

public class LoggerTableTrigger extends AbstractDbTableTrigger {

	@Override
	public void doQuery(QueryOperation operation) throws Exception{
		System.out.println("Query:" + operation.getDbTable().getName() + ";Date=" + new Date());
		super.doQuery(operation);
	}

	@Override
	public void doSave(SaveOperation operation) throws Exception {
		System.out.println("Save:" + operation.getDbTable().getName() + ";Date=" + new Date());
		super.doSave(operation);
	}

}
