package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;

/**
 * {@link com.bstek.dorado.jdbc.JdbcDataResolver}对应的数据库操作
 * 
 * @author mark
 * 
 */
public class JdbcRecordOperation extends
		AbstractDbTableOperation<JdbcDataResolverContext> {

	private Record record;

	private JdbcRecordOperation parent;
	
	private JdbcRecordOperation substitute;
	
	private Map<String, String> propertyMap;
	
	public JdbcRecordOperation(DbTable dbTable, Record record,
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
	
	public JdbcRecordOperation getSubstitute() {
		return substitute;
	}

	public void setSubstitute(JdbcRecordOperation substitute, Map<String, String> propertyMap) {
		this.substitute = substitute;
		this.propertyMap = propertyMap;
	}

	public Map<String, String> getPropertyMap() {
		return this.propertyMap;
	}
	
	@Override
	protected void doExecute() {
		JdbcEnviroment jdbcEnviroment = getJdbcEnviroment();
		jdbcEnviroment.getDialect().execute(this);
	}

	@SuppressWarnings("unchecked")
	public JdbcRecordOperation[] children(JdbcDataResolverItem item) {
		String eName = item.getTableName();
		Object childValue = record.get(item.getName());
		if (StringUtils.isNotEmpty(eName) && childValue != null) {
			DbTable dbTable = JdbcUtils.getDbTable(eName); 
			JdbcDataResolverContext jdbcContext = this.getJdbcContext();
			if (childValue instanceof Record) {
				Record childRecord = (Record)childValue;
				JdbcRecordOperation operation = new JdbcRecordOperation(dbTable, childRecord, jdbcContext);
				operation.parent = this;
				
				return new JdbcRecordOperation[]{operation};
			} else if (childValue instanceof Collection) {
				Collection<Record> childRecords = (Collection<Record>)childValue;
				Record[] records = childRecords.toArray(new Record[0]);
				JdbcRecordOperation[] operations = new JdbcRecordOperation[records.length];
				for (int i=0; i<records.length; i++) {
					Record record = records[i];
					JdbcRecordOperation operation = new JdbcRecordOperation(dbTable, record, jdbcContext);
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

}
