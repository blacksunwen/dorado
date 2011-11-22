package com.bstek.dorado.jdbc.meta;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 与{@link JdbcEnviroment}相关的属性输出
 * 
 * @author mark
 *
 */
public interface JdbcEnviromentMetaDataGenerator {
	
	/**
	 * 列出{@link JdbcEnviroment}全部的Catalog
	 * @param jdbcEnv
	 * @return
	 */
	String[] listCatalogs(JdbcEnviroment jdbcEnv);
	
	/**
	 * 列出{@link JdbcEnviroment}的Schema
	 * @param jdbcEnv
	 * @param catalog
	 * @return
	 */
	List<Map<String,String>> listSchemas(JdbcEnviroment jdbcEnv, String catalog);
	
	/**
	 * 列出{@link JdbcEnviroment}的TableType
	 * @param jdbcEnv
	 * @return
	 */
	String[] listTableTypes(JdbcEnviroment jdbcEnv);
	
	/**
	 * 列出{@link JdbcEnviroment}的{@link com.bstek.dorado.jdbc.type.JdbcType}的名称
	 * @param jdbcEnv
	 * @return
	 */
	String[] listJdbcTypes(JdbcEnviroment jdbcEnv);
	
}
