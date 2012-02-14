package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.entity.EntityUtils;
import com.bstek.dorado.data.resolver.AbstractDataResolver;
import com.bstek.dorado.data.resolver.DataItems;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.data.type.EntityDataType;

/**
 * JDBC模块的{@link com.bstek.dorado.data.resolver.DataResolver}
 * 
 * @author mark
 * 
 */
@XmlNode(
	fixedProperties = "type=jdbc"
)
public class JdbcDataResolver extends AbstractDataResolver {

	private List<JdbcDataResolverItem> items;
	
	private JdbcEnviroment jdbcEnviroment;
	
	private TransactionTemplate transactionTemplate;
	
	private boolean autoCreateItems = false;
	
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Items"))
	public List<JdbcDataResolverItem> getItems() {
		return items;
	}

	public void setItems(List<JdbcDataResolverItem> items) {
		this.items = items;
	}

	public boolean isAutoCreateItems() {
		return autoCreateItems;
	}

	public void setAutoCreateItems(boolean autoCreateItems) {
		this.autoCreateItems = autoCreateItems;
	}

	@XmlProperty(parser="spring:dorado.jdbc.jdbcEnviromentParser")
	public JdbcEnviroment getJdbcEnviroment() {
		return jdbcEnviroment;
	}

	public void setJdbcEnviroment(JdbcEnviroment jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	@XmlProperty(parser="spring:dorado.jdbc.transactionTemplateParser")
	public TransactionTemplate getTransactionTemplate() {
		if (transactionTemplate == null) {
			JdbcEnviroment jdbcEnv = this.getJdbcEnviroment();
			if (jdbcEnv != null) {
				transactionTemplate = jdbcEnv.getTransactionTemplate();
			}
		}
		
		return transactionTemplate;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object internalResolve(final DataItems dataItems, final Object parameter)
			throws Exception {
		TransactionTemplate transactionTemplate = this.getTransactionTemplate();
		if (transactionTemplate != null) {
			return transactionTemplate.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {
					return doResolve(dataItems, parameter);
				}
			});
		} else {
			return this.doResolve(dataItems, parameter);
		}
	}

	protected Object doResolve(DataItems dataItems, Object parameter) {
		JdbcDataResolverOperation operation = creatOperation(dataItems, parameter);
		operation.execute();
		return operation.getJdbcContext().getReturnValue();
	}
	
	protected JdbcDataResolverOperation creatOperation(DataItems dataItems, Object parameter) {
		JdbcEnviroment jdbcEnv = this.getJdbcEnviroment();
		
		List<JdbcDataResolverItem> resolverItems = createResolverItems(dataItems);
		
		JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(jdbcEnv, parameter, dataItems, resolverItems);
		JdbcDataResolverOperation operation = new JdbcDataResolverOperation(jdbcContext);
		
		return operation;
	}
	
	protected List<JdbcDataResolverItem> createResolverItems(DataItems dataItems) {
		if (!this.isAutoCreateItems()) {
			List<JdbcDataResolverItem> items = this.getItems();
			if (items == null || items.isEmpty()) {
				return null;
			} else {
				return items;
			}
		} else {
			List<JdbcDataResolverItem> resolverItems = this.getItems();
			if (resolverItems == null || resolverItems.isEmpty()) {
				resolverItems = new ArrayList<JdbcDataResolverItem>();
				if (dataItems.isEmpty()) {
					return null;
				}
				
				Set<String> nameSet = dataItems.keySet();
				for (String name: nameSet) {
					Object dataObject = dataItems.get(name);
					DataType dataType = getDataType(dataObject);
					JdbcDataResolverItem resolverItem = createItem(name, dataType);
					if (resolverItem != null) {
						resolverItems.add(resolverItem);
					}
				}
				
				return resolverItems;
			} else {
				List<JdbcDataResolverItem> items2 = new ArrayList<JdbcDataResolverItem>();
				for (JdbcDataResolverItem item: resolverItems) {
					items2.add(item.clone());
				}
				
				Set<String> nameSet = dataItems.keySet();
				for (String name: nameSet) {
					JdbcDataResolverItem newItem = null;
					for (JdbcDataResolverItem item: items2) {
						if (name.equals(item.getName())) {
							newItem = item;
							break;
						}
					}
					
					if (newItem == null) {
						Object dataObject = dataItems.get(name);
						DataType dataType = getDataType(dataObject);
						newItem = createItem(name, dataType);
						
						if (newItem != null) {
							items2.add(newItem);
						}
					}
				}
				
				return items2;
			}
		}
	}
	
	protected DataType getDataType(Object dataObject) {
		if (dataObject instanceof Collection) {
			return EntityUtils.getDataType((Collection<?>) dataObject); 
		} else {
			return EntityUtils.getDataType(dataObject);
		}
	}
	
	protected JdbcDataResolverItem createItem(String name, DataType dataType) {
		if (dataType instanceof EntityDataType) {
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName(name);
			String tableName = dataType.getName();
			item.setTableName(tableName);
			return item;
		}
		
		return null;
	}
}
