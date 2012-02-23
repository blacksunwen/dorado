package com.bstek.dorado.jdbc;

/**
 * 抽象的{@link DbTableTrigger}
 * @author mark.li@bstek.com
 *
 */
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
