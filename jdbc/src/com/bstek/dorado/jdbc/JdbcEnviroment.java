package com.bstek.dorado.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.transaction.support.TransactionTemplate;

import com.bstek.dorado.util.Assert;

/**
 * JDBC环境配置，主要配置数据库方言和数据源连接<br>
 * 
 * @author mark
 * 
 */
public class JdbcEnviroment implements InitializingBean {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private boolean isdefault = false;

	public boolean isDefault() {
		return isdefault;
	}

	public void setDefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	private Dialect dialect;

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	private DataSource dataSource;

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
			dao.setDataSource(dataSource);
			dao.afterPropertiesSet();

			namedParameterJdbcDaoSupport = dao;
		}

		return namedParameterJdbcDaoSupport;
	}

	private TransactionTemplate transactionTemplate;
	
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.getName(), "name can not be null.");
		Assert.notNull(this.getDataSource(), "dataSource can not be null.");
		Assert.notNull(this.getDialect(), "dialect can not be null.");
	}

}
