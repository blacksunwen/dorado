package com.bstek.dorado.jdbc.ide;

import java.util.Map;

import javax.sql.DataSource;

import org.w3c.dom.Document;

/**
 * 为了配合IDE离线功能的代理接口
 * 
 * @author mark.li@bstek.com
 *
 */
public interface IAgent {

	public static final String NAMESPACE       = "namespace";
	public static final String QUERY_SQL       = "querySql";
	public static final String TABLE_NAME      = "tableName";
	public static final String XML             = "xml";
	public static final String TABLE_TYPE      = "tableType";//Table,SqlTable
	
	Document listTables(Map<String, Object> parameters, DataSource dataSource) throws Exception;
	
	Document createColumns(Map<String, Object> parameters, DataSource dataSource) throws Exception;
}
