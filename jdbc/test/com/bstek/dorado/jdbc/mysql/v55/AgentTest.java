package com.bstek.dorado.jdbc.mysql.v55;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bstek.dorado.jdbc.ide.DefaultAgent;
import com.bstek.dorado.jdbc.ide.IAgent;
import com.bstek.dorado.jdbc.model.SqlTable;
import com.bstek.dorado.jdbc.model.Table;

public class AgentTest {
	
	@Test
	public void testListTables() throws Exception {
		Map<String, Object> parameters = makeParamerters();
		parameters.put(IAgent.NAMESPACE, "dorado");
		
		DefaultAgent agent = new DefaultAgent();
//		String tables = agent.listTables(parameters);
	}
	
	@Test
	public void testTableCreateColumns() throws Exception {
		Map<String, Object> parameters = makeParamerters();
		parameters.put(IAgent.TABLE_TYPE, Table.TYPE);
		parameters.put(IAgent.NAMESPACE, "dorado");
		parameters.put(IAgent.TABLE_NAME, "employee");
		parameters.put(IAgent.XML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Table name=\"EMPLOYEE\" tableName=\"EMPLOYEE\"></Table>");
		
		{
			DefaultAgent agent = new DefaultAgent();
//			String xml = (String)agent.createColumns(parameters);
		}
		
		{
			DefaultAgent agent = new DefaultAgent();
			parameters.put(IAgent.XML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Table name=\"EMPLOYEE\" tableName=\"EMPLOYEE\"><Columns><KeyColumn jdbcType=\"INTEGER-Integer\" name=\"ID\"/></Columns></Table>");
//			String xml = agent.createColumns(parameters);
		}
	}
	
	@Test
	public void testSqlTableCreateColumns() throws Exception {
		Map<String, Object> parameters = makeParamerters();
		parameters.put(IAgent.TABLE_TYPE, SqlTable.TYPE);
		parameters.put(IAgent.XML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SqlTable name=\"sql_dept\" mainTable=\"DEPT\"></SqlTable>");
		parameters.put(IAgent.QUERY_SQL, "select * from DEPT");
	
		{
			DefaultAgent agent = new DefaultAgent();
//			String xml = agent.createColumns(parameters);
		}
	}
	
	private Map<String, Object> makeParamerters() {
		Map<String, Object> parameters = new HashMap<String,Object>();
		
//		parameters.put(IAgent.URL, "jdbc:mysql://192.168.18.95/DORADO?useUnicode=true&amp;characterEncoding=UTF-8");
//		parameters.put(IAgent.DRIVER, "com.mysql.jdbc.Driver");
//		parameters.put(IAgent.USER, "dorado");
//		parameters.put(IAgent.PASSWORD, "dorado");
		
		return parameters;
	}
}
