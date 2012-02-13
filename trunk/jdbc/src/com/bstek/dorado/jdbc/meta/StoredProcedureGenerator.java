package com.bstek.dorado.jdbc.meta;

import org.w3c.dom.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

public interface StoredProcedureGenerator {

	Document createDocument(JdbcEnviroment jdbcEnv, String catalog, String schema, String procedureName);

	Document mergeDocument(JdbcEnviroment jdbcEnv, String catalog, String schema, String procedureName, Document oldDocument);
}
