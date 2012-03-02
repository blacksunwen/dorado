package com.bstek.dorado.jdbc.ide;

import java.util.Map;


public interface IAgent {

	public static final String URL             = "url";
	public static final String USER            = "user";
	public static final String PASSWORD        = "password";
	public static final String NAMESPACE       = "namespace";
	public static final String QUERY_SQL       = "querySql";
	public static final String TABLE_NAME      = "tableName";
	public static final String JDBC_ENVIROMENT = "jdbcEnviroment";
	public static final String XML             = "xml";
	public static final String TABLE_TYPE      = "tableType";//Table,SqlTable
	
	String[] listSpaces(Map<String, String> paramerters) throws Exception;
	
	String[] listTables(Map<String, String> paramerters) throws Exception;
	
	String[] listJdbcTypes(Map<String, String> paramerters) throws Exception;
	
	String[] listKeyGenerators(Map<String, String> paramerters) throws Exception;

	String createColumns(Map<String, String> paramerters) throws Exception;
	
}
