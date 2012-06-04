package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionOperations;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.type.EntityDataType;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcDataResolverItem;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.util.Assert;

public class DataResolverOperation {

	private DataResolverContext jdbcContext;
	
	private TransactionOperations transactionOperations;
	
	private boolean _processDefault = true;

	public DataResolverOperation (DataResolverContext jdbcContext) {
		this(jdbcContext, null);
	}
	
	public DataResolverOperation (DataResolverContext jdbcContext, TransactionOperations transactionOperations) {
		this.jdbcContext = jdbcContext;
		this.transactionOperations = transactionOperations;
	}
	
	public boolean isProcessDefault() {
		return _processDefault;
	}

	public void setProcessDefault(boolean _processDefault) {
		this._processDefault = _processDefault;
	}
	
	public DataResolverContext getJdbcContext() {
		return this.jdbcContext;
	}
	
	public void setJdbcContext(DataResolverContext jdbcContext) {
		this.jdbcContext = jdbcContext;
	}
	
	public TransactionOperations getTransactionOperations() {
		return transactionOperations;
	}

	public void setTransactionOperations(TransactionOperations transactionOperations) {
		this.transactionOperations = transactionOperations;
	}

	private class TransactionCallback implements org.springframework.transaction.support.TransactionCallback<Object> {

		public Object doInTransaction(TransactionStatus status) {
			try {
				DataResolverOperation.this.doExecute();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		}
		
	}
	
	private TransactionCallback transactionCallback = new TransactionCallback();
	
	public void execute() throws Exception {
		if (this.isProcessDefault()) {
			if (transactionOperations != null) {
				transactionOperations.execute(transactionCallback);
			} else {
				this.doExecute();
			}
		}
	}
	
	protected void doExecute() throws Exception {
		List<JdbcDataResolverItem> resolverItems = jdbcContext.getResolverItems();
		if (!resolverItems.isEmpty()) {
			this.doChildrenExecute(null, resolverItems);
		} 
	}

	private void doChildrenExecute(Record parentRecord, List<JdbcDataResolverItem> resolverItems) throws Exception {
		for (JdbcDataResolverItem resolverItem: resolverItems) {
			String resolverItemName = resolverItem.getName();
			Object dataValue = (parentRecord !=null) ? parentRecord.get(resolverItemName) : jdbcContext.getDataItems().get(resolverItemName);;
			if (dataValue != null) {
				this.calculate(resolverItem, dataValue);
				this.doExecute(parentRecord, resolverItem, dataValue);
			}
		}
	}
	
	private void calculate(JdbcDataResolverItem resolverItem, Object dataObject) {
		String tableName = resolverItem.getTableName();
		if (StringUtils.isEmpty(tableName)) {
			EntityDataType dataType = JdbcUtils.getEntityDataType(dataObject);
			String dataTypeName = dataType.getName();
			tableName = dataTypeName;
			resolverItem.setTableName(tableName);
		}
		
		EntityDataType entityDataType = resolverItem.getDataType();
		if (entityDataType == null) {
			entityDataType = JdbcUtils.getEntityDataType(dataObject);
			resolverItem.setDataType(entityDataType);
		}
		
		if (resolverItem.isSupportBatchSql()) {
			Assert.notEmpty(entityDataType.getPropertyDefs(), "propertyDefs of " + entityDataType.getName() +" must not be empty.[" + resolverItem.getName() + "]");
			BatchSql batchSql = resolverItem.getBatchSql();
			if (batchSql == null) {
				batchSql = new BatchSql(entityDataType, resolverItem.getDbTable());
				resolverItem.setBatchSql(batchSql);
			}
		}
	}
	
	private void doExecute(Record parentRecord, JdbcDataResolverItem resolverItem, Object dataObject) throws Exception {
		if (parentRecord != null) {
			String[] parentKeyProperties = resolverItem.getParentKeyProperties();
			String[] foreignKeyProperties = resolverItem.getForeignKeyProperties();
			
			if (parentKeyProperties!= null && foreignKeyProperties != null) {
				if (parentKeyProperties.length > 0 && foreignKeyProperties.length > 0) {
					Assert.isTrue(parentKeyProperties.length == foreignKeyProperties.length, 
							"the length of parentKeyProperties and foreignKeyProperties does not equals.");
					
					if (dataObject instanceof Collection) {
						@SuppressWarnings("unchecked")
						Collection<Record> records = (Collection<Record>)dataObject;
						for (Record record: records) {
							this.syncWithParent(parentKeyProperties, foreignKeyProperties, record, parentRecord);
						}
					} else {
						Record record = (Record)dataObject;
						this.syncWithParent(parentKeyProperties, foreignKeyProperties, record, parentRecord);
					}
				}
			}
		}
		
		DbTable dbTable = resolverItem.getDbTable();
		SaveOperation saveOper = new SaveOperation(resolverItem.getBatchSql(), dbTable, dataObject, jdbcContext);
		saveOper.getJdbcDao().doSave(saveOper);
		
		if (dataObject instanceof Collection) {
			@SuppressWarnings("unchecked")
			Collection<Record> records = (Collection<Record>)dataObject;
			for (Record record: records) {
				if (!EntityState.DELETED.equals(EntityUtils.getState(record))) {
					this.afterSave(resolverItem, record);
				}
			}
		} else {
			Record record = (Record)dataObject;
			if (!EntityState.DELETED.equals(EntityUtils.getState(record))) {
				this.afterSave(resolverItem, record);
			}
		}
	}
	
	private void afterSave(JdbcDataResolverItem resolverItem, Record record) throws Exception {
		List<JdbcDataResolverItem> items = resolverItem.getResolverItems();
		
		if (StringUtils.isNotEmpty(resolverItem.getRecursiveProperty())){
			List<JdbcDataResolverItem> items2 = new ArrayList<JdbcDataResolverItem>(items.size() + 1);
			JdbcDataResolverItem recursiveItemm = resolverItem.clone();
			recursiveItemm.setName(resolverItem.getRecursiveProperty());
			
			items2.add(recursiveItemm);
			items2.addAll(items);
			
			this.doChildrenExecute(record, items2);
		} else {
			if (items.size() > 0) {
				this.doChildrenExecute(record, items);
			}
		}
	}
	
	private void syncWithParent(String[] parentKeyProperties,String[] foreignKeyProperties,
			Record record, Record parentRecord) {
		for (int i=0; i<parentKeyProperties.length; i++) {
			String parentPropery = parentKeyProperties[i];
			String property = foreignKeyProperties[i];
			Object parentPropertyValue = parentRecord.get(parentPropery);
			
			record.set(property, parentPropertyValue);
		}
	}

}
