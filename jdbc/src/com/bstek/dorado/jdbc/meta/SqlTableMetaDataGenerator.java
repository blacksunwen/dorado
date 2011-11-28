package com.bstek.dorado.jdbc.meta;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 数据库SQL属性的输出
 * @author mark
 *
 */
public interface SqlTableMetaDataGenerator {
	
	/**
	 * 构造特定SQL的Document对象
	 * @param jdbcEnv
	 * @param sql
	 * @return
	 */
	Document createDocument(JdbcEnviroment jdbcEnv, String sql);
	
	/**
	 * 在已有的Document基础上输出特定SQL的Document对象
	 * @param jdbcEnv
	 * @param sql
	 * @param oldDocument
	 * @return
	 */
	Document mergeDocument(JdbcEnviroment jdbcEnv, String sql, Document oldDocument);
}
