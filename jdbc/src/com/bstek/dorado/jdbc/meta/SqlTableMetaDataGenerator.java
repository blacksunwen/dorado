package com.bstek.dorado.jdbc.meta;

import org.w3c.dom.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 数据库SQL属性的输出
 * 
 * @author mark.li@bstek.com
 *
 */
public interface SqlTableMetaDataGenerator {
	
	/**
	 * 构造特定SQL的Document对象
	 * @param jdbcEnv
	 * @param sql
	 * @return
	 */
	Document create(JdbcEnviroment jdbcEnv, String sql);
	
	/**
	 * 在已有的Document基础上输出特定SQL的Document对象
	 * @param jdbcEnv
	 * @param sql
	 * @param oldDocument
	 * @return
	 */
	Document merge(JdbcEnviroment jdbcEnv, String sql, Document oldDocument);
}
