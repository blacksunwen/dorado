package com.bstek.dorado.jdbc.meta;

import org.w3c.dom.Document;

import com.bstek.dorado.jdbc.JdbcEnviroment;

/**
 * 存储程序输出
 * 
 * @author mark.li@bstek.com
 *
 */
public interface StoredProcedureGenerator {

	Document create(JdbcEnviroment jdbcEnv, String catalog, String schema, String procedureName);

	Document merge(JdbcEnviroment jdbcEnv, String catalog, String schema, String procedureName, Document oldDocument);
}
