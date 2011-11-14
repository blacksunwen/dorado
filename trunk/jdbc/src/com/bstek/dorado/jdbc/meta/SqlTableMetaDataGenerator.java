package com.bstek.dorado.jdbc.meta;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

public interface SqlTableMetaDataGenerator {
	
	List<Map<String,String>> listColumnMetas(JdbcEnviroment jdbcEnv, String sql);
	
	Map<String, String> columnProperties(Map<String, String> columnMeta, JdbcEnviroment jdbcEnv);
	
	Document createDocument(JdbcEnviroment jdbcEnv, String sql);
	
	Document mergeDocument(JdbcEnviroment jdbcEnv, String sql, Document oldDocument);
}
