package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.type.AggregationDataType;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;
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
		if (this.isProcessDefault()) {
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
	}
	
	private static class RecordItem {
		JdbcDataResolverItem resolverItem;
		Record record;
		
		RecordItem(JdbcDataResolverItem resolverItem,
				Record record) {
			this.resolverItem = resolverItem;
			this.record = record;
		}
	}
	
	protected void doExecute() {
		List<JdbcDataResolverItem> resolverItems = jdbcContext.getResolverItems();
		if (!resolverItems.isEmpty()) {
			this.doChildrenExecute(null, resolverItems);
		} 
	}

	private void doChildrenExecute(RecordItem parentRecordItem, List<JdbcDataResolverItem> resolverItems) {
		for (JdbcDataResolverItem resolverItem: resolverItems) {
			String resolverItemName = resolverItem.getName();
			Object dataValue = (parentRecordItem !=null) ? parentRecordItem.record.get(resolverItemName) : jdbcContext.getDataItems().get(resolverItemName);;
			if (dataValue != null) {
				if (dataValue instanceof Record) {
					Record record = (Record)dataValue;
					
					this.calculateResolverItem(resolverItem, record);
					this.doExecute(parentRecordItem, new RecordItem (resolverItem, record));
				} else if (dataValue instanceof Collection) {
					@SuppressWarnings("unchecked")
					Collection<Record> records = (Collection<Record>)dataValue;
					if (!records.isEmpty()) {
						this.calculateResolverItem(resolverItem, records);
						for (Record record: records) {
							this.doExecute(parentRecordItem, new RecordItem (resolverItem, record));
						}
					}
				}
			}
		}
	}

	private void doExecute(RecordItem parentRecordItem, RecordItem recordItem) {
		JdbcDataResolverItem resolverItem = recordItem.resolverItem;
		Record record = recordItem.record;
		
		if (parentRecordItem != null) {
			this.syncWithParent(resolverItem, record, parentRecordItem.record);
		} 
		
		String tableName = resolverItem.getTableName();
		DbTable dbTable = JdbcUtils.getDbTable(tableName);
		JdbcUtils.doSave(dbTable, record, jdbcContext);
		
		if (!EntityState.DELETED.equals(EntityUtils.getState(record))) {
			List<JdbcDataResolverItem> items = resolverItem.getResolverItems();
			
			if (StringUtils.isNotEmpty(resolverItem.getRecursiveProperty())){
				List<JdbcDataResolverItem> items2 = new ArrayList<JdbcDataResolverItem>(items.size() + 1);
				JdbcDataResolverItem recursiveItemm = resolverItem.clone();
				recursiveItemm.setName(resolverItem.getRecursiveProperty());
				
				items2.add(recursiveItemm);
				items2.addAll(items);
				
				this.doChildrenExecute(recordItem, items2);
			} else {
				if (items.size() > 0) {
					this.doChildrenExecute(recordItem, items);
				}
			}
		}
	}
	
	private void syncWithParent(JdbcDataResolverItem item, Record record, Record parentRecord) {
		String[] parentKeyProperties = item.getParentKeyProperties();
		String[] foreignKeyProperties = item.getForeignKeyProperties();
		if (parentKeyProperties!= null && foreignKeyProperties != null) {
			if (parentKeyProperties.length > 0 && foreignKeyProperties.length > 0) {
				Assert.isTrue(parentKeyProperties.length == foreignKeyProperties.length, "the length of parentKeyProperties and foreignKeyProperties does not equals.");
				
				for (int i=0; i<parentKeyProperties.length; i++) {
					String parentPropery = parentKeyProperties[i];
					String property = foreignKeyProperties[i];
					Object parentPropertyValue = parentRecord.get(parentPropery);
					
					record.set(property, parentPropertyValue);
				}
			}
		}
	}
	
	private void calculateResolverItem(JdbcDataResolverItem resolverItem,
			Collection<Record> records) {
		String tableName = resolverItem.getTableName();
		if (StringUtils.isEmpty(tableName)) {
			AggregationDataType aggDataType = EntityUtils.getDataType((Collection<?>)records);
			DataType dataType = null;
			if (aggDataType != null) {
				dataType = aggDataType.getElementDataType();
			}
			if (dataType != null) {
				String dataTypeName = dataType.getName();
				tableName = dataTypeName;
				resolverItem.setTableName(tableName);
			}
		}
	}

	private void calculateResolverItem(JdbcDataResolverItem resolverItem,
			Record record) {
		String tableName = resolverItem.getTableName();
		if (StringUtils.isEmpty(tableName)) {
			EntityDataType dataType = EntityUtils.getDataType(record);
			if (dataType != null) {
				String dataTypeName = dataType.getName();
				tableName = dataTypeName;
				resolverItem.setTableName(tableName);
			}
		}
	}
}
