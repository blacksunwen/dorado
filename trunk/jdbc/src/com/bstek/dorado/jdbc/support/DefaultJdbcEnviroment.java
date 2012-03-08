package com.bstek.dorado.jdbc.support;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.util.Assert;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public class DefaultJdbcEnviroment extends AbstractJdbcEnviroment implements JdbcEnviroment {

	private DataSource dataSource;
	private PlatformTransactionManager transactionManager;
	private TransactionDefinition transactionDefinition;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.namedParameterJdbcDaoSupport = null;
	}

	private NamedParameterJdbcDaoSupport namedParameterJdbcDaoSupport;

	public NamedParameterJdbcDaoSupport getNamedDao() {
		if (namedParameterJdbcDaoSupport == null) {
			NamedParameterJdbcDaoSupport dao = new NamedParameterJdbcDaoSupport();
			dao.setDataSource(getDataSource());
			dao.afterPropertiesSet();

			namedParameterJdbcDaoSupport = dao;
		}

		return namedParameterJdbcDaoSupport;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.getName(), "name can not be null.");
		Assert.notNull(this.getDataSource(), "dataSource can not be null.");
		Assert.notNull(this.getDialect(), "dialect can not be null.");
		
		super.afterPropertiesSet();
	}

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public TransactionDefinition getTransactionDefinition() {
		return transactionDefinition;
	}

	public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
	}

}
