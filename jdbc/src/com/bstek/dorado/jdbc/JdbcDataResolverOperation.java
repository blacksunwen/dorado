package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.model.TableTrigger;
import com.bstek.dorado.util.Assert;

public class JdbcDataResolverOperation {

	private JdbcDataResolverContext jdbcContext;
	
	public JdbcDataResolverContext getJdbcContext() {
		return this.jdbcContext;
	}
	
	public JdbcDataResolverOperation (JdbcDataResolverContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public void execute() {
		for (JdbcDataResolverItem item: jdbcContext.getResolverItems()) {
			String iName = item.getName();
			String eName = item.getTableName();
			Assert.notEmpty(iName, "value of name property must not be empty.");
			
			if (StringUtils.isNotEmpty(eName)) {
				DataItems dataItems = jdbcContext.getDataItems();
				if (dataItems.containsKey(iName)) {
					Object dataItem = dataItems.get(iName);
					if (dataItem instanceof Record) {
						Record record = (Record) dataItem;
						this.doResolve(item, record, jdbcContext);
					} else if (dataItem instanceof Collection) {
						@SuppressWarnings("unchecked")
						Collection<Record> records = (Collection<Record>)dataItem;
						for (Record record: records) {
							this.doResolve(item, record, jdbcContext);
						}
					} else {
						throw new IllegalArgumentException("Unknown dataItem class [" + dataItem.getClass().getName() + "]");
					}
				} 
			}
		}
	}
	
	protected void doResolve(JdbcDataResolverItem item, Record record, JdbcDataResolverContext jdbcContext) {
		String eName = item.getTableName();
		DbElement dbElement = JdbcUtils.getDbElement(eName); 
		JdbcRecordOperation operation = new JdbcRecordOperation(dbElement, record, jdbcContext);
		
		this.doResolve(item, operation);
	}
	
	protected void doResolve(JdbcDataResolverItem item, JdbcRecordOperation operation) {
		String parentPropertiesStr = item.getParentKeyProperties();
		String propertiesStr = item.getForeignKeyProperties();
		if (StringUtils.isNotEmpty(parentPropertiesStr) && StringUtils.isNotEmpty(propertiesStr)) {
			JdbcRecordOperation parentOperation = operation.getParent();
			Assert.notNull(parentOperation, "parent operation must not be null.");
			
			String[] parentProperties = StringUtils.split(parentPropertiesStr,',');
			String[] properties = StringUtils.split(propertiesStr, ',');
			
			Assert.isTrue(parentProperties.length == properties.length, "the count of propertyName not equals " +
					"[" + parentPropertiesStr+"]["+propertiesStr+"]");
			
			Record parentRecord = parentOperation.getRecord();
			Record record = operation.getRecord();
			for (int i=0; i<parentProperties.length; i++) {
				String parentPropery = parentProperties[i];
				String property = properties[i];
				Object parentPropertyValue = parentRecord.get(parentPropery);
				
				record.set(property, parentPropertyValue);
			}
		}
		
		DbElement dbElement = operation.getDbElement();
		Assert.isTrue(dbElement instanceof DbTable, "["+dbElement.getName()+"] is not table.");

		DbTable table = (DbTable)dbElement;
		TableTrigger trigger = table.getTrigger();
		if (trigger == null) {
			operation.execute();
		} else {
			trigger.doSave(operation);
		}
		
		if (isContinue(operation)) {
			List<JdbcDataResolverItem> items = item.getItems();
			if (!items.isEmpty()) {
				for (JdbcDataResolverItem i: items) {
					JdbcRecordOperation[] childOperations = operation.children(i);
					for (JdbcRecordOperation childOperation: childOperations) {
						this.doResolve(i, childOperation);
					}
				}
			}
		}
	}
	
	protected boolean isContinue(JdbcRecordOperation operation) {
		Record record = operation.getRecord();
		return EntityUtils.getState(record) != EntityState.DELETED;
	}
}
