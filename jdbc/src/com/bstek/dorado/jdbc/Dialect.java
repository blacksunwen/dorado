package com.bstek.dorado.jdbc;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.bstek.dorado.jdbc.key.KeyGenerator;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinModel;
import com.bstek.dorado.jdbc.type.JdbcType;

/**
 * 数据库方言
 * 
 * @author mark
 * 
 */
public interface Dialect {

	/**
	 * 构造{@link JoinModel}的SQL语句的token
	 * @param joinModel
	 * @return
	 */
	String token(JoinModel joinModel);
	
	/**
	 * 构造{@link Order}的SQL语句的token
	 * @param order
	 * @return
	 */
	String token(Order order);

	/**
	 * 将{@link com.bstek.dorado.jdbc.sql.SelectSql}输出成SQL
	 * 
	 * @param selectSql
	 * @return
	 */
	String toSQL(SelectSql selectSql);

	/**
	 * 根据{@link com.bstek.dorado.jdbc.sql.SelectSql}输出成查询记录总数的SQL
	 * 
	 * @param selectSql
	 * @return
	 */
	String toCountSQL(SelectSql selectSql);

	/**
	 * 将查询语句包装成查询记录总数的SQL
	 * 
	 * @param sql
	 * @return
	 */
	String toCountSQL(String sql);

	/**
	 * 是否支持数据库分页查询
	 * 
	 * @return
	 */
	boolean isNarrowSupport();

	/**
	 * 输出分页查询SQL
	 * 
	 * @param selectSql
	 * @param maxResults
	 * @param firstResult
	 * @return
	 */
	String narrowSql(SelectSql selectSql, int maxResults, int firstResult);

	/**
	 * 是否支持序列
	 * 
	 * @return
	 */
	boolean isSequenceSupport();

	/**
	 * 输出序列查询下一个值的SQL
	 * 
	 * @param sequenceName
	 * @return
	 */
	String sequenceSql(String sequenceName);

	/**
	 * 根据名称获取{@link com.bstek.dorado.jdbc.type.JdbcType}
	 * 
	 * @param name
	 * @return
	 */
	JdbcType getJdbcType(String name);

	/**
	 * 获取全部的{@link com.bstek.dorado.jdbc.type.JdbcType}
	 * 
	 * @return
	 */
	List<JdbcType> getJdbcTypes();

	/**
	 * 根据名称获取{@link com.bstek.dorado.jdbc.key.KeyGenerator}
	 * 
	 * @param name
	 * @return
	 */
	KeyGenerator<Object> getKeyGenerator(String name);

	/**
	 * 获取全部的{@link com.bstek.dorado.jdbc.key.KeyGenerator}
	 * 
	 * @return
	 */
	List<KeyGenerator<Object>> getKeyGenerators();
	
	/**
	 * 执行查询操作
	 * @param operation
	 */
	void execute(JdbcDataProviderOperation operation);
	
	/**
	 * 执行数据库操作
	 * @param operation
	 */
	void execute(JdbcRecordOperation operation);
	
	/**
	 * 根据Column的数据库属性获取{@link com.bstek.dorado.jdbc.type.JdbcType}，用于IDE
	 * @param columnMeta
	 * @return
	 */
	JdbcType jdbcType(Map<String,String> columnMeta);
	
	/**
	 * 根据Column的数据库属性获取propertyName，用于IDE
	 * @param columnMeta
	 * @return
	 */
	String propertyName(Map<String,String> columnMeta);
	
	
	/**
	 * 默认的数据库catalog
	 * @param dataSource
	 * @param databaseMetaData
	 * @return
	 */
	String defaultCatalog(DataSource dataSource, DatabaseMetaData databaseMetaData);
	
	/**
	 * 默认的数据库catalog
	 * @param dataSource
	 * @param databaseMetaData
	 * @return
	 */
	String defaultSchema(DataSource dataSource, DatabaseMetaData databaseMetaData);
	
}
