package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.support.QueryOperation;
import com.bstek.dorado.jdbc.support.SaveOperation;
import com.bstek.dorado.jdbc.support.SaveRecordOperation;

/**
 * 抽象的{@link DbTableTrigger}
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractDbTableTrigger implements DbTableTrigger {

	public void doQuery(QueryOperation operation) throws Exception {
		operation.run();
	}
	
	public void doSave(SaveOperation operation) throws Exception {
		operation.run();
	}

	public void doSave(SaveRecordOperation operation) throws Exception {
		operation.run();
	}
}
