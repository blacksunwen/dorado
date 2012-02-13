package com.bstek.dorado.jdbc.meta;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.ColumnDefinition;

/**
 * 数据库Table属性的输出
 * 
 * @author mark
 *
 */
public interface TableMetaDataGenerator {

	/**
	 * 列出{@link JdbcEnviroment}的Table原始属性的列表
	 * @param jdbcEnv
	 * @param catalogPattern
	 * @param schemaPattern
	 * @param tableNamePattern
	 * @param types
	 * @return
	 */
	List<Map<String,String>> listTableMetas(JdbcEnviroment jdbcEnv, 
			String catalogPattern, String schemaPattern, String tableNamePattern, String[] types);
	
	/**
	 * 输出特定Table的原始属性
	 * 
	 * @param jdbcEnv
	 * @param catalog
	 * @param schema
	 * @param table
	 * @return
	 */
	Map<String,String> tableMeta(JdbcEnviroment jdbcEnv, String catalog, String schema, String table);
	
	/**
	 * 输出程序内部使用的名称，即name属性
	 * 
	 * @param tableMeta
	 * @param jdbcEnv
	 * @return
	 */
	String tableName(Map<String,String> tableMeta, JdbcEnviroment jdbcEnv);
	
	/**
	 * 输出Table的Column原始属性的列表
	 * 
	 * @param jdbcEnv
	 * @param catalog
	 * @param schema
	 * @param tableName
	 * @return
	 */
	List<Map<String,String>> listColumnMetas(JdbcEnviroment jdbcEnv, String catalog, String schema, String tableName);
	
	/**
	 * 根据Column的原始属性，输出程序内部使用的属性
	 * 
	 * @param columnMeta
	 * @param jdbcEnv
	 * @return
	 */
	Map<String, String> columnProperties(Map<String, String> columnMeta, JdbcEnviroment jdbcEnv);

	/**
	 * 根据Column属性构造{@link com.bstek.dorado.config.definition.Definition}对象
	 * @param tableMeta
	 * @param columnProperties
	 * @param jdbcEnv
	 * @return
	 */
	ColumnDefinition createColumnDefinition(Map<String, String> tableMeta, Map<String, String> columnProperties, JdbcEnviroment jdbcEnv);
	
	/**
	 * 构造特定Table的Document对象
	 * @param catalog
	 * @param schema
	 * @param table
	 * @param jdbcEnv
	 * @return
	 */
	Document createDocument(String catalog, String schema, String table, JdbcEnviroment jdbcEnv);
	
	/**
	 * 在已有的Document基础上输出特定Table的Document对象
	 * @param catalog
	 * @param schema
	 * @param table
	 * @param jdbcEnv
	 * @param oldDocument
	 * @return
	 */
	Document mergeDocument(String catalog, String schema, String table, JdbcEnviroment jdbcEnv, Document oldDocument);
}
