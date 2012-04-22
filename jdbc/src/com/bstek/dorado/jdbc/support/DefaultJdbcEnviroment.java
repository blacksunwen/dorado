package com.bstek.dorado.jdbc.support;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultJdbcEnviroment extends AbstractJdbcEnviroment {

	private DataSource dataSource;
	private PlatformTransactionManager transactionManager;
	private TransactionDefinition transactionDefinition;
	private NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport;
	
	public DefaultJdbcEnviroment() {
		transactionDefinition = new DefaultTransactionDefinition();
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.transactionManager = new DataSourceTransactionManager(this.dataSource);
		
		NamedParameterJdbcDaoSupport dao = new NamedParameterJdbcDaoSupport();
		dao.setDataSource(getDataSource());
		dao.afterPropertiesSet();
		this.namedParameterJdbcDaoSupport = dao;
	}

	public NamedParameterJdbcDaoSupport getNamedDao() {
		return namedParameterJdbcDaoSupport;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.getName(), "name can not be null.");
		Assert.notNull(this.getDataSource(), "dataSource can not be null.");
		Assert.notNull(this.getDialect(), "dialect can not be null.");
		
		super.afterPropertiesSet();
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public TransactionDefinition getTransactionDefinition() {
		return transactionDefinition;
	}

	public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
	}

}
