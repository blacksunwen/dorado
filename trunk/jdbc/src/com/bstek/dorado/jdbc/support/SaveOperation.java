package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.util.Assert;

public class SaveOperation extends AbstractDbTableOperation<DataResolverContext, DbTable> {

	private Object data;
	private BatchSql batchSql;
	
	public SaveOperation(BatchSql batchSql, DbTable dbTable, Object data, DataResolverContext jdbcContext) {
		super(dbTable, jdbcContext);
		this.batchSql = batchSql;
		this.data = data;
	}
	
	public Object getData() {
		Assert.notNull(data, "data must not be null.");
		return data;
	}
	
	public BatchSql getBatchSql() {
		return batchSql;
	}

	@Override
	protected boolean doRun() throws Exception {
		return getDialect().execute(this);
	}
}
