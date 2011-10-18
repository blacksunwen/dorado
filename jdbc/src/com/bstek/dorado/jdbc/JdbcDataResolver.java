package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.bstek.dorado.data.entity.EntityState;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.resolver.AbstractDataResolver;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbElement;
import com.bstek.dorado.jdbc.model.DbElementTrigger;
import com.bstek.dorado.jdbc.model.JdbcEnviroment;
import com.bstek.dorado.util.Assert;

/**
 * JDBC模块的{@link com.bstek.dorado.data.resolver.DataResolver}
 * 
 * @author mark
 * 
 */
public class JdbcDataResolver extends AbstractDataResolver {

	private List<JdbcDataResolverItem> items;
	
	private JdbcEnviroment jdbcEnviroment;
	
	private TransactionTemplate transactionTemplate;
	
	public List<JdbcDataResolverItem> getItems() {
		return items;
	}

	public void setItems(List<JdbcDataResolverItem> items) {
		this.items = items;
	}

	public JdbcEnviroment getJdbcEnviroment() {
		return jdbcEnviroment;
	}

	public void setJdbcEnviroment(JdbcEnviroment jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}
	
	@Override
	protected Object internalResolve(final DataItems dataItems, final Object parameter)
			throws Exception {
		if (items != null && !items.isEmpty()) {
			final JdbcEnviroment jdbcEnv = this.getJdbcEnviroment();
			TransactionTemplate transactionTemplate = this.getTransactionTemplate();
			if (transactionTemplate == null && jdbcEnv != null) {
				transactionTemplate = jdbcEnv.getTransactionTemplate();
			}
			if (transactionTemplate != null) {
				transactionTemplate.execute(new TransactionCallbackWithoutResult() {

					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						JdbcDataResolver.this.doResolve(dataItems, parameter, jdbcEnv);
					}
					
				});
			} else {
				this.doResolve(dataItems, parameter, jdbcEnv);
			}
			
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	protected void doResolve(DataItems dataItems, Object parameter, JdbcEnviroment jdbcEnv) {
		JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(jdbcEnv, parameter);
		for (JdbcDataResolverItem item: items) {
			String name = item.getName();
			String eName = item.getDbElement();
			Assert.notEmpty(name, "value of name property must not be empty.");
			
			if (StringUtils.isNotEmpty(eName)) {
				if (dataItems.containsKey(name)) {
					Object dataItem = dataItems.get(name);
					if (dataItem instanceof Record) {
						Record record = (Record) dataItem;
						this.doResolve(item, record, jdbcContext);
					} else if (dataItem instanceof Collection) {
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
		String eName = item.getDbElement();
		DbElement dbElement = JdbcUtils.getDbElement(eName); 
		JdbcRecordOperation operation = new JdbcRecordOperation(dbElement, record, jdbcContext);
		
		this.doResolve(item, operation);
	}
	
	protected void doResolve(JdbcDataResolverItem item, JdbcRecordOperation operation) {
		String parentPropertiesStr = item.getReferencedKeyProperties();
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
		DbElementTrigger trigger = dbElement.getTrigger();
		if (trigger == null) {
			operation.execute();
		} else {
			trigger.doResolve(operation);
		}
		
		if (isContinue(operation)) {
			List<JdbcDataResolverItem> items = item.getItems();
			if (!items.isEmpty()) {
				for (JdbcDataResolverItem i: items) {
					JdbcRecordOperation[] childOperations = operation.create(i);
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
