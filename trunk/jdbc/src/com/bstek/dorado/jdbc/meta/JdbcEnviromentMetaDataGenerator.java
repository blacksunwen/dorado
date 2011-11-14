package com.bstek.dorado.jdbc.meta;

import java.util.List;
import java.util.Map;

import com.bstek.dorado.jdbc.JdbcEnviroment;

public interface JdbcEnviromentMetaDataGenerator {
	
	String[] listCatalogs(JdbcEnviroment jdbcEnv);
	
	List<Map<String,String>> listSchemas(JdbcEnviroment jdbcEnv, String catalog);
	
	String[] listTableTypes(JdbcEnviroment jdbcEnv);
	
	String[] defaultTableTypes(JdbcEnviroment jdbcEnv);
	
	String[] listJdbcTypes(JdbcEnviroment jdbcEnv);
	
}
