package com.bstek.dorado.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;

import com.bstek.dorado.annotation.ClientProperty;
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
 * @author mark.li@bstek.com
 * 
 */
@XmlNode(
	fixedProperties = "type=jdbc"
)
public class JdbcDataResolver extends AbstractDataResolver {

	private List<JdbcDataResolverItem> items;
	
	private JdbcEnviroment jdbcEnviroment;
	
	private PlatformTransactionManager transactionManager;

	private TransactionDefinition transactionDefinition;
	
	private boolean autoCreateItems = false;
	
	@XmlProperty(parser="spring:dorado.jdbc.platformTransactionManagerBeanPropertyParser")
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	@XmlProperty(parser="spring:dorado.jdbc.transactionDefinitionBeanPropertyParser")
	public TransactionDefinition getTransactionDefinition() {
		return transactionDefinition;
	}

	public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
	}

	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Items", fixed = true))
	public List<JdbcDataResolverItem> getItems() {
		return items;
	}

	public void setItems(List<JdbcDataResolverItem> items) {
		this.items = items;
	}

	@XmlProperty(parser="spring:dorado.jdbc.jdbcEnviromentParser")
	public JdbcEnviroment getJdbcEnviroment() {
		return jdbcEnviroment;
	}

	public void setJdbcEnviroment(JdbcEnviroment jdbcEnviroment) {
		this.jdbcEnviroment = jdbcEnviroment;
	}
	
	public boolean isAutoCreateItems() {
		return autoCreateItems;
	}

	@ClientProperty(escapeValue="false")
	public void setAutoCreateItems(boolean autoCreateItems) {
		this.autoCreateItems = autoCreateItems;
	}
	
	@Override
	protected Object internalResolve(DataItems dataItems, Object parameter)
			throws Exception {
		JdbcDataResolverOperation operation = createOperation(dataItems, parameter);
		
		JdbcIntercepter intercepter = JdbcUtils.getGlobalIntercepter();
		operation = intercepter.getOperation(operation);
		operation.execute();
		
		return operation.getJdbcContext().getReturnValue();
	}

	protected JdbcDataResolverOperation createOperation(DataItems dataItems, Object parameter) {
		PlatformTransactionManager transactionManager = this.getTransactionManager();
		TransactionDefinition transactionDefinition = this.getTransactionDefinition();
		JdbcEnviroment jdbcEnviroment = this.getJdbcEnviroment();
		
		TransactionOperations transactionOperations = null;
		if (transactionManager == null && jdbcEnviroment != null) {
			transactionManager = jdbcEnviroment.getTransactionManager();
		}
		
		if (transactionDefinition == null && jdbcEnviroment != null) {
			transactionDefinition = jdbcEnviroment.getTransactionDefinition();
		}
		
		if (transactionManager != null) {
			if (transactionDefinition != null) {
				transactionOperations = new TransactionTemplate(transactionManager, transactionDefinition);
			} else {
				transactionOperations = new TransactionTemplate(transactionManager);
			}
		}
		
		List<JdbcDataResolverItem> resolverItems = getResolverItems(dataItems);
		
		JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(jdbcEnviroment, parameter, dataItems, resolverItems);
		JdbcDataResolverOperation operation = new JdbcDataResolverOperation(jdbcContext, transactionOperations);
		
		return operation;
	}
	
	protected List<JdbcDataResolverItem> getResolverItems(DataItems dataItems) {
		if (!this.isAutoCreateItems()) {
			List<JdbcDataResolverItem> items = this.getItems();
			if (items == null || items.isEmpty()) {
				return null;
			} else {
				return items;
			}
		} else {
			return createResolverItems(dataItems);
		}
	}
	
	protected List<JdbcDataResolverItem> createResolverItems(DataItems dataItems) {
		List<JdbcDataResolverItem> resolverItems = this.getItems();
		if (resolverItems == null || resolverItems.isEmpty()) {
			if (dataItems.isEmpty()) {
				return null;
			}
			
			resolverItems = new ArrayList<JdbcDataResolverItem>();
			Set<String> nameSet = dataItems.keySet();
			for (String name: nameSet) {
				Object dataObject = dataItems.get(name);
				DataType dataType = getDataType(dataObject);
				JdbcDataResolverItem resolverItem = createResolverItem(name, dataType);
				if (resolverItem != null) {
					resolverItems.add(resolverItem);
				}
			}
			
			return resolverItems;
		} else {
			List<JdbcDataResolverItem> resolverItems2 = new ArrayList<JdbcDataResolverItem>();
			resolverItems2.addAll(resolverItems);
			
			Set<String> itemNameSet = dataItems.keySet();
			for (String itemName: itemNameSet) {
				JdbcDataResolverItem resolverItem = null;
				for (JdbcDataResolverItem item2: resolverItems2) {
					if (itemName.equals(item2.getName())) {
						resolverItem = item2;
						break;
					}
				}
				
				if (resolverItem == null) {
					Object dataObject = dataItems.get(itemName);
					DataType dataType = getDataType(dataObject);
					resolverItem = createResolverItem(itemName, dataType);
					
					if (resolverItem != null) {
						resolverItems2.add(resolverItem);
					}
				}
			}
			
			return resolverItems2;
		}
	}
	
	protected JdbcDataResolverItem createResolverItem(String name, DataType dataType) {
		if (dataType instanceof EntityDataType) {
			JdbcDataResolverItem item = new JdbcDataResolverItem();
			item.setName(name);
			String tableName = dataType.getName();
			item.setTableName(tableName);
			return item;
		}
		
		return null;
	}
	
	protected DataType getDataType(Object dataObject) {
		if (dataObject instanceof Collection) {
			return EntityUtils.getDataType((Collection<?>) dataObject).getElementDataType(); 
		} else {
			return EntityUtils.getDataType(dataObject);
		}
	}

}
