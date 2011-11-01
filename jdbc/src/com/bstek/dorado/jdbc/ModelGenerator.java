package com.bstek.dorado.jdbc;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.model.ColumnDefinition;
import com.bstek.dorado.jdbc.support.TableGeneratorOption;

public interface ModelGenerator {

	String[] listCatalogs(JdbcEnviroment jdbcEnv);
	
	String[] listTableTypes(JdbcEnviroment jdbcEnv);
	
	String[] defaultTableTypes(JdbcEnviroment jdbcEnv);
	
	List<Map<String,String>> listTables(JdbcEnviroment jdbcEnv, 
			String catalogPattern, String schemaPattern, String tableNamePattern, String[] types);
	
	Map<String,String> singleTableMeta(JdbcEnviroment jdbcEnv, String catalog, String schema, String table);
	
	List<Map<String,String>> listSchemas(JdbcEnviroment jdbcEnv);
	
	List<Map<String,String>> listSchemas(JdbcEnviroment jdbcEnv, String catalog);
	
	String name(Map<String,String> tableMeta, JdbcEnviroment jdbcEnv);
	
	List<Map<String,String>> listColumns(JdbcEnviroment jdbcEnv, String catalog, String schema, String tableName);
	
	Map<String, String> columnProperties(Map<String, String> columnMeta, JdbcEnviroment jdbcEnv);

	ColumnDefinition createColumnDefinition(Map<String, String> tableMeta, Map<String, String> columnProperties, JdbcEnviroment jdbcEnv);
	
	Document createTableDocument(String catalog, String schema, String table, TableGeneratorOption option);
	
	Document createSqlTableDocument(JdbcEnviroment jdbcEnv, String sql);
}
