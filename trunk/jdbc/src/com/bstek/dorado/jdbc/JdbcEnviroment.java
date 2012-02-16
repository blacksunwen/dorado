package com.bstek.dorado.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.transaction.support.TransactionOperations;

/**
 * JDBC环境配置，主要配置数据库方言和数据源连接<br>
 * 
 * @author mark
 * 
 */
public interface JdbcEnviroment {

	public String getName();

	public boolean isDefault();

	public Dialect getDialect();

	public DataSource getDataSource();

	public NamedParameterJdbcDaoSupport getNamedDao();

	public TransactionOperations getTransactionOperations();

}
