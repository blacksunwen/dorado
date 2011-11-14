package com.bstek.dorado.jdbc.meta;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;
import com.bstek.dorado.jdbc.model.ColumnDefinition;
import com.bstek.dorado.jdbc.support.TableGeneratorOption;

public interface TableMetaDataGenerator {

	List<Map<String,String>> listTableMetas(JdbcEnviroment jdbcEnv, 
			String catalogPattern, String schemaPattern, String tableNamePattern, String[] types);
	
	Map<String,String> tableMeta(JdbcEnviroment jdbcEnv, String catalog, String schema, String table);
	
	String tableName(Map<String,String> tableMeta, JdbcEnviroment jdbcEnv);
	
	List<Map<String,String>> listColumnMetas(JdbcEnviroment jdbcEnv, String catalog, String schema, String tableName);
	
	Map<String, String> columnProperties(Map<String, String> columnMeta, JdbcEnviroment jdbcEnv);

	ColumnDefinition createColumnDefinition(Map<String, String> tableMeta, Map<String, String> columnProperties, JdbcEnviroment jdbcEnv);
	
	Document createDocument(String catalog, String schema, String table, TableGeneratorOption option);
	
	Document mergeDocument(String catalog, String schema, String table, TableGeneratorOption option, Document oldDocument);
}
