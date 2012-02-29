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
		List<JdbcDataResolverItem> resolverItems = jdbcContext.getResolverItems();
		if (!resolverItems.isEmpty()) {
			for (JdbcDataResolverItem resolverItem: resolverItems) {
				String resolverItemName = resolverItem.getName();
				String tableName = resolverItem.getTableName();
				if (StringUtils.isNotEmpty(tableName)) {
					DataItems dataItems = jdbcContext.getDataItems();
					if (dataItems.containsKey(resolverItemName)) {
						Object dataValue = dataItems.get(resolverItemName);
						if (dataValue != null) {
							if (dataValue instanceof Record) {
								Record record = (Record)dataValue;
								this.doExecute(null, resolverItem, record);
							} else if (dataValue instanceof Collection) {
								@SuppressWarnings("unchecked")
								Collection<Record> records = (Collection<Record>)dataValue;
								for (Record record: records) {
									this.doExecute(null, resolverItem, record);
								}
							}
						}
					}
				}
			}
		} 
	}
	
	private void doChildrenExecute(Record parentRecord, List<JdbcDataResolverItem> resolverItems) {
		for (JdbcDataResolverItem resolverItem: resolverItems) {
			String resolverItemName = resolverItem.getName();
			String tableName = resolverItem.getTableName();
			if (StringUtils.isNotEmpty(tableName)) {
				Object dataValue = parentRecord.get(resolverItemName);
				if (dataValue != null) {
					if (dataValue instanceof Record) {
						Record record = (Record)dataValue;
						this.doExecute(parentRecord, resolverItem, record);
					} else if (dataValue instanceof Collection) {
						@SuppressWarnings("unchecked")
						Collection<Record> records = (Collection<Record>)dataValue;
						for (Record record: records) {
							this.doExecute(parentRecord, resolverItem, record);
						}
					}
				}
			}
		}
	}

	private void doExecute(Record parentRecord, JdbcDataResolverItem resolverItem, Record record) {
		if (parentRecord != null) {
			this.syncWithParent(resolverItem, record, parentRecord);
		} 
		this.doExecute(resolverItem, record);
		if (!EntityState.DELETED.equals(EntityUtils.getState(record))) {
			this.doChildrenExecute(record, resolverItem.getItems());
		}
	}
	
	private void doExecute(JdbcDataResolverItem item, Record record) {
		String eName = item.getTableName();
		DbTable dbTable = JdbcUtils.getDbTable(eName);
		JdbcUtils.doSave(dbTable, record, jdbcContext);
	}
	
	private void syncWithParent(JdbcDataResolverItem item, Record record, Record parentRecord) {
		String parentPropertiesStr = item.getParentKeyProperties();
		String propertiesStr = item.getForeignKeyProperties();
		if (StringUtils.isNotEmpty(parentPropertiesStr) && StringUtils.isNotEmpty(propertiesStr)) {
			Assert.notNull(record);
			Assert.notNull(parentRecord);
			
			String[] parentProperties = StringUtils.split(parentPropertiesStr,',');
			String[] properties = StringUtils.split(propertiesStr, ',');
			
			Assert.isTrue(parentProperties.length == properties.length, "the count of propertyName not equals " +
					"[" + parentPropertiesStr +"][" + propertiesStr + "]");
			
			for (int i=0; i<parentProperties.length; i++) {
				String parentPropery = parentProperties[i];
				String property = properties[i];
				Object parentPropertyValue = parentRecord.get(parentPropery);
				
				record.set(property, parentPropertyValue);
			}
		}
	}
}
