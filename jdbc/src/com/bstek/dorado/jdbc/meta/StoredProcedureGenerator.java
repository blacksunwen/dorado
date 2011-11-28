package com.bstek.dorado.jdbc.meta;

import org.dom4j.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

public interface StoredProcedureGenerator {

	Document createDocument(JdbcEnviroment jdbcEnv, String catalog, String schema, String procedureName);

	Document mergeDocument(JdbcEnviroment jdbcEnv, String catalog, String schema, String procedureName, Document oldDocument);
}
