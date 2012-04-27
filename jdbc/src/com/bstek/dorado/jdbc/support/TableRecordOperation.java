package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.Table;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataResolver}对应的数据库操作
 * 
 * @author mark.li@bstek.com
 *
 */
public class TableRecordOperation extends
		AbstractDbTableOperation<DataResolverContext, Table> {

	private TableRecordOperation parent;
	private Record record;

	public TableRecordOperation(Table dbTable, Record record,
			DataResolverContext jdbcContext) {
		super(dbTable, jdbcContext);
		this.setRecord(record);
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public TableRecordOperation getParent() {
		return this.parent;
	}

	@Override
	protected boolean doRun() throws Exception{
		return getDialect().execute(this);
	}
}
