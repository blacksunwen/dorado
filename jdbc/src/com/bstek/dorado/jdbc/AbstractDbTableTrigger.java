package com.bstek.dorado.jdbc;

import com.bstek.dorado.jdbc.support.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.support.JdbcRecordOperation;

/**
 * 抽象的{@link DbTableTrigger}
 * @author mark.li@bstek.com
 *
 */
public abstract class AbstractDbTableTrigger implements DbTableTrigger {

	public void doQuery(JdbcDataProviderOperation operation) throws Exception {
		operation.execute();
	}

	public void doSave(JdbcRecordOperation operation) throws Exception {
		operation.execute();
	}

}
