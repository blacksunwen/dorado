package com.bstek.dorado.jdbc;

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
