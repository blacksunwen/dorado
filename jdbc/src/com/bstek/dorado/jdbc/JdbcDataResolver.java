package com.bstek.dorado.jdbc;

import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlNodeWrapper;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.data.resolver.AbstractDataResolver;
import com.bstek.dorado.data.resolver.DataItems;

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
	
	@XmlSubNode(wrapper = @XmlNodeWrapper(nodeName = "Items"))
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
	
	protected JdbcDataResolverOperation creatOperation(DataItems dataItems, Object parameter) {
		JdbcEnviroment jdbcEnv = this.getJdbcEnviroment();
		
		JdbcDataResolverContext jdbcContext = new JdbcDataResolverContext(jdbcEnv, parameter, dataItems, this.getItems());
		JdbcDataResolverOperation operation = new JdbcDataResolverOperation(jdbcContext);
		
		return operation;
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
	
}
