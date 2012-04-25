package com.bstek.dorado.jdbc.support;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.Table;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataResolver}对应的数据库操作
 * 
 * @author mark.li@bstek.com
 *
 */
public class JdbcRecordOperation extends
		AbstractDbTableOperation<JdbcDataResolverContext, Table> {

	private JdbcRecordOperation parent;
	private Record record;

	public JdbcRecordOperation(Table dbTable, Record record,
			JdbcDataResolverContext jdbcContext) {
		super(dbTable, jdbcContext);
		this.setRecord(record);
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}

	public JdbcRecordOperation getParent() {
		return this.parent;
	}

	@Override
	protected boolean doExecute() throws Exception{
		JdbcEnviroment jdbcEnviroment = getJdbcEnviroment();
		return jdbcEnviroment.getDialect().execute(this);
	}
}
