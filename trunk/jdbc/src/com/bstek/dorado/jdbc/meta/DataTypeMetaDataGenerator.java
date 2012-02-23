package com.bstek.dorado.jdbc.meta;

import org.w3c.dom.Document;

/**
 * 
 * @author mark.li@bstek.com
 *
 */
public interface DataTypeMetaDataGenerator {

//	Document create(JdbcEnviroment jdbcEnv, String tableName);
//	
//	Document merge(JdbcEnviroment jdbcEnv, String tableName, Document oldDocument);
	
	Document create(String tableName);
	
	Document merge(String tableName, Document oldDocument);
}
