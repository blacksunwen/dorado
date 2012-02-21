package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.util.Assert;

public class JdbcDataResolverOperation {

	private JdbcDataResolverContext jdbcContext;
	
	private TransactionOperations transactionOperations;
	
	private boolean _processDefault = true;

	public JdbcDataResolverOperation (JdbcDataResolverContext jdbcContext) {
		this(jdbcContext, null);
	}
	
	public JdbcDataResolverOperation (JdbcDataResolverContext jdbcContext, TransactionOperations transactionOperations) {
		this.jdbcContext = jdbcContext;
		this.transactionOperations = transactionOperations;
	}
	
	public boolean isProcessDefault() {
		return _processDefault;
	}

	public void setProcessDefault(boolean _processDefault) {
		this._processDefault = _processDefault;
	}
	
	public JdbcDataResolverContext getJdbcContext() {
		return this.jdbcContext;
	}
	
	public void setJdbcContext(JdbcDataResolverContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public TransactionOperations getTransactionOperations() {
		return transactionOperations;
	}

	public void setTransactionOperations(TransactionOperations transactionOperations) {
		this.transactionOperations = transactionOperations;
	}

	public void execute() {
		if (transactionOperations != null) {
			transactionOperations.execute(new TransactionCallback<Object>() {
				@Override
				public Object doInTransaction(TransactionStatus status) {
					doExecute();
					return null;
				}
			});
		} else {
			doExecute();
		}
	}
	
	protected void doExecute() {
		List<JdbcDataResolverItem> items = jdbcContext.getResolverItems();
		if (items.isEmpty()) {
			return;
		}
		
		for (JdbcDataResolverItem item: items) {
			String iName = item.getName();
			String eName = item.getTableName();
			
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
		DbTable dbTable = JdbcUtils.getDbTable(eName); 
		JdbcRecordOperation operation = new JdbcRecordOperation(dbTable, record, jdbcContext);
		
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
		
		DbTable table = operation.getDbTable();
		DbTableTrigger trigger = table.getTrigger();
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
