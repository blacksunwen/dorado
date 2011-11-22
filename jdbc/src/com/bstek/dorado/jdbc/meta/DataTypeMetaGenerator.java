package com.bstek.dorado.jdbc.meta;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

public interface DataTypeMetaGenerator {

	Document createDocument(JdbcEnviroment jdbcEnv, String tableName);
	
	Document mergeDocument(JdbcEnviroment jdbcEnv, String tableName, Document oldDocument);
}
