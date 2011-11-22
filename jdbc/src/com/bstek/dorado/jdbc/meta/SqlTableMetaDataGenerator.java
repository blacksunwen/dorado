package com.bstek.dorado.jdbc.meta;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 数据库SQL属性的输出
 * @author mark
 *
 */
public interface SqlTableMetaDataGenerator {
	
	/**
	 * 根据SQL输出全部列原始属性的列表
	 * @param jdbcEnv
	 * @param sql
	 * @return
	 */
	List<Map<String,String>> listColumnMetas(JdbcEnviroment jdbcEnv, String sql);
	
	/**
	 * 根据Column的原始属性，输出程序内部使用的属性
	 * @param columnMeta
	 * @param jdbcEnv
	 * @return
	 */
	Map<String, String> columnProperties(Map<String, String> columnMeta, JdbcEnviroment jdbcEnv);
	
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
