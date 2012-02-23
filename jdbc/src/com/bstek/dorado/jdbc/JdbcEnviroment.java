package com.bstek.dorado.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * JDBC环境配置，主要配置数据库方言和数据源连接，以及事务信息<br>
 * 
 * @author mark.li@bstek.com
 * 
 */
public interface JdbcEnviroment {

	/**
	 * 名称
	 * @return
	 */
	public String getName();

	/**
	 * 是否是默认的JDBC环境
	 * @return
	 */
	public boolean isDefault();

	/**
	 * 获取数据库方言
	 * @return
	 */
	public Dialect getDialect();

	/**
	 * 获取数据库连接池
	 * @return
	 */
	public DataSource getDataSource();

	/**
	 * 获取数据库操作对象
	 * @return
	 */
	public NamedParameterJdbcDaoSupport getNamedDao();

	/**
	 * 获取数据库事务管理器
	 * @return
	 */
	public PlatformTransactionManager getTransactionManager();
	
	/**
	 * 获取数据库事务定义
	 * @return
	 */
	public TransactionDefinition getTransactionDefinition();

}
