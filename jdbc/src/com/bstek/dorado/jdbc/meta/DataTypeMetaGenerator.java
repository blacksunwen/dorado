package com.bstek.dorado.jdbc.meta;

import org.w3c.dom.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

public interface DataTypeMetaGenerator {

	Document createDocument(JdbcEnviroment jdbcEnv, String tableName);
	
	Document mergeDocument(JdbcEnviroment jdbcEnv, String tableName, Document oldDocument);
}
