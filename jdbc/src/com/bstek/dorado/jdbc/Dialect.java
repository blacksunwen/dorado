package com.bstek.dorado.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.jdbc.model.AutoTable;
import com.bstek.dorado.jdbc.model.Table;
import com.bstek.dorado.jdbc.model.autotable.Order;
import com.bstek.dorado.jdbc.sql.DeleteAllSql;
import com.bstek.dorado.jdbc.sql.DeleteSql;
import com.bstek.dorado.jdbc.sql.InsertSql;
import com.bstek.dorado.jdbc.sql.RetrieveSql;
import com.bstek.dorado.jdbc.sql.SelectSql;
import com.bstek.dorado.jdbc.sql.SqlConstants.JoinOperator;
import com.bstek.dorado.jdbc.sql.UpdateSql;
import com.bstek.dorado.jdbc.support.DeleteAllOperation;
import com.bstek.dorado.jdbc.support.JdbcDataProviderOperation;
import com.bstek.dorado.jdbc.support.JdbcRecordOperation;

/**
 * 数据库方言
 * 
 * @author mark.li@bstek.com
 * 
 */
public interface Dialect {

	static final Log logger = LogFactory.getLog(Dialect.class);
	
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
	String toSQL(SelectSql sql) throws Exception;
	
	String toSQL(DeleteSql sql) throws Exception;
	
	String toSQL(DeleteAllSql sql) throws Exception;
	
	String toSQL(InsertSql sql) throws Exception;
	
	String toSQL(RetrieveSql sql) throws Exception;
	
	String toSQL(UpdateSql sql) throws Exception;
	
	/**
	 * 根据{@link com.bstek.dorado.jdbc.sql.SelectSql}输出成查询记录总数的SQL
	 * 
	 * @param selectSql
	 * @return
	 */
	String toCountSQL(SelectSql selectSql) throws Exception;

	/**
	 * 将查询语句包装成查询记录总数的SQL
	 * 
	 * @param sql
	 * @return
	 */
	String toCountSQL(String sql) throws Exception;

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
	String narrowSql(SelectSql selectSql, int maxResults, int firstResult) throws Exception;

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
	 * 执行{@link com.bstek.dorado.jdbc.JdbcDataProvider}操作
	 * @param operation
	 */
	boolean execute(JdbcDataProviderOperation operation) throws Exception;
	
	/**
	 * 执行数据库操作
	 * @param operation
	 * @return 是否成功执行
	 */
	boolean execute(JdbcRecordOperation operation) throws Exception;
	
	boolean execute(DeleteAllOperation operation) throws Exception;
	
	/**
	 * 获取Table所在的空间类型
	 * @return
	 */
	JdbcSpace getTableJdbcSpace();
}
