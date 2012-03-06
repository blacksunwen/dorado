package com.bstek.dorado.jdbc;

import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import com.bstek.dorado.jdbc.model.autotable.AutoTable;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.model.table.Table;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinOperator;

/**
 * 数据库方言
 * 
 * @author mark.li@bstek.com
 * 
 */
public interface Dialect {

	/**
	 * 生成SQL的片段
	 * @param table
	 * @return
	 */
	String token(Table table);
	
	/**
	 * 生成SQL的片段
	 * @param table
	 * @param alias
	 * @return
	 */
	String token(Table table, String alias);
	
	/**
	 * 生成SQL的片段
	 * @param autoTable
	 * @param joinModel
	 * @return
	 */
	String token(AutoTable autoTable, JoinOperator joinModel);
	
	/**
	 * 生成SQL的片段
	 * @param autoTable
	 * @param order
	 * @return
	 */
	String token(AutoTable autoTable, Order order);

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
	 * 执行查询操作
	 * @param operation
	 */
	boolean execute(JdbcDataProviderOperation operation);
	
	/**
	 * 执行数据库操作
	 * @param operation
	 * @return 是否成功执行
	 */
	boolean execute(JdbcRecordOperation operation);
	
	/**
	 * 默认的数据库catalog，用于IDE
	 * @param dataSource
	 * @param databaseMetaData
	 * @return
	 */
	String defaultCatalog(DataSource dataSource, DatabaseMetaData databaseMetaData);
	
	/**
	 * 默认的数据库catalog，用于IDE
	 * @param dataSource
	 * @param databaseMetaData
	 * @return
	 */
	String defaultSchema(DataSource dataSource, DatabaseMetaData databaseMetaData);
	
	/**
	 * 获取Table所在的空间类型
	 * @return
	 */
	JdbcSpace getTableJdbcSpace();
}
