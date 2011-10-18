package com.bstek.dorado.jdbc;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.AbstractJdbcOperation;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataResolver}对应的数据库操作
 * 
 * @author mark
 * 
 */
public class JdbcRecordOperation extends
		AbstractJdbcOperation<JdbcDataResolverContext> {

	private Record record;

	private JdbcRecordOperation parent;
	
	private JdbcRecordOperation substitute;
	
//	private boolean retrieveAfterExecute = false;
	
	public JdbcRecordOperation(DbElement dbElement, Record record,
			JdbcDataResolverContext jdbcContext) {
		super(dbElement, jdbcContext);
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
	
	public JdbcRecordOperation getSubstitute() {
		return substitute;
	}

	public void setSubstitute(JdbcRecordOperation substitute) {
		this.substitute = substitute;
	}

	@SuppressWarnings("unchecked")
	public JdbcRecordOperation[] create(JdbcDataResolverItem item) {
		String eName = item.getDbElement();
		Object childValue = record.get(item.getName());
		if (StringUtils.isNotEmpty(eName) && childValue != null) {
			DbElement dbElement = JdbcUtils.getDbElement(eName); 
			JdbcDataResolverContext jdbcContext = this.getJdbcContext();
			if (childValue instanceof Record) {
				Record childRecord = (Record)childValue;
				JdbcRecordOperation operation = new JdbcRecordOperation(dbElement, childRecord, jdbcContext);
				operation.parent = this;
				
				return new JdbcRecordOperation[]{operation};
			} else if (childValue instanceof Collection) {
				Collection<Record> childRecords = (Collection<Record>)childValue;
				Record[] records = childRecords.toArray(new Record[0]);
				JdbcRecordOperation[] operations = new JdbcRecordOperation[records.length];
				for (int i=0; i<records.length; i++) {
					Record record = records[i];
					JdbcRecordOperation operation = new JdbcRecordOperation(dbElement, record, jdbcContext);
					operation.parent = this;
					operations[i] = operation;
				}
				return operations;
			} else {
				throw new IllegalArgumentException("Unknown value class [" + childValue.getClass().getName() + "]");
			}
		} else {
			return new JdbcRecordOperation[0];
		}
	}
	
	/*
	 * @see com.bstek.dorado.jdbc.model.AbstractJdbcOperation#execute()
	 */
	@Override
	public void execute() {
		JdbcEnviroment jdbcEnviroment = this.getJdbcContext().getJdbcEnviroment();
		if (jdbcEnviroment == null) {
			jdbcEnviroment = this.getDbElement().getJdbcEnviroment();
		}
		jdbcEnviroment.getDialect().execute(this);
	}

}
