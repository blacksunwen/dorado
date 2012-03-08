package com.bstek.dorado.jdbc.mysql.v55;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bstek.dorado.jdbc.ide.DefaultAgent;
import com.bstek.dorado.jdbc.ide.IAgent;
import com.bstek.dorado.jdbc.model.sqltable.SqlTable;
import com.bstek.dorado.jdbc.model.table.Table;

public class AgentTest {

//	@Test
//	public void testListSpaces() throws Exception {
//		Map<String, String> paramerters = makeParamerters();
//		
//		DefaultAgent agent = new DefaultAgent();
//		String spaces = agent.listSpaces(paramerters);
//		
//		Assert.assertTrue(spaces.indexOf("dorado") >= 0);
//	}
	
	@Test
	public void testListTables() throws Exception {
		Map<String, String> paramerters = makeParamerters();
		paramerters.put(IAgent.NAMESPACE, "dorado");
		
		DefaultAgent agent = new DefaultAgent();
		String tables = agent.listTables(paramerters);
	}
	
	@Test
	public void testTableCreateColumns() throws Exception {
		Map<String, String> paramerters = makeParamerters();
		paramerters.put(IAgent.TABLE_TYPE, Table.TYPE);
		paramerters.put(IAgent.NAMESPACE, "dorado");
		paramerters.put(IAgent.TABLE_NAME, "employee");
		paramerters.put(IAgent.XML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Table name=\"EMPLOYEE\" tableName=\"EMPLOYEE\"></Table>");
		
		{
			DefaultAgent agent = new DefaultAgent();
			String xml = agent.createColumns(paramerters);
		}
		
		{
			DefaultAgent agent = new DefaultAgent();
			paramerters.put(IAgent.XML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Table name=\"EMPLOYEE\" tableName=\"EMPLOYEE\"><Columns><KeyColumn jdbcType=\"INTEGER-Integer\" name=\"ID\"/></Columns></Table>");
			String xml = agent.createColumns(paramerters);
		}
	}
	
	@Test
	public void testSqlTableCreateColumns() throws Exception {
		Map<String, String> paramerters = makeParamerters();
		paramerters.put(IAgent.TABLE_TYPE, SqlTable.TYPE);
		paramerters.put(IAgent.XML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SqlTable name=\"sql_dept\" mainTable=\"DEPT\"></SqlTable>");
		paramerters.put(IAgent.QUERY_SQL, "select * from DEPT");
	
		{
			DefaultAgent agent = new DefaultAgent();
			String xml = agent.createColumns(paramerters);
		}
	}
	
	private Map<String, String> makeParamerters() {
		Map<String, String> paramerters = new HashMap<String,String>();
		
		paramerters.put(IAgent.URL, "jdbc:mysql://192.168.18.95/DORADO?useUnicode=true&amp;characterEncoding=UTF-8");
		paramerters.put(IAgent.DRIVER, "com.mysql.jdbc.Driver");
		paramerters.put(IAgent.USER, "dorado");
		paramerters.put(IAgent.PASSWORD, "dorado");
		
		return paramerters;
	}
}
