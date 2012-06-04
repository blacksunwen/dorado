package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.util.Assert;

public class SaveRecordOperation extends AbstractDbTableOperation<DataResolverContext, DbTable> {

	private Record record;
	private BatchSql batchSql;
	
	public SaveRecordOperation(BatchSql batchSql, DbTable dbTable, Record record, DataResolverContext jdbcContext) {
		super(dbTable, jdbcContext);
		this.batchSql = batchSql;
		this.record = record;
	}
	
	public Record getRecord() {
		Assert.notNull(record, "record must not be null.");
		return record;
	}

	public BatchSql getBatchSql() {
		return batchSql;
	}

	@Override
	protected boolean doRun() throws Exception {
		return getDialect().execute(this);
	}
}
