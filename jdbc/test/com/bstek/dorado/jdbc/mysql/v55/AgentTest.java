package com.bstek.dorado.jdbc.mysql.v55;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.bstek.dorado.jdbc.ide.DefaultAgent;
import com.bstek.dorado.jdbc.ide.IAgent;
import com.bstek.dorado.jdbc.model.table.Table;

public class AgentTest {

	@Test
	public void testListSpaces() throws Exception {
		Map<String, String> paramerters = makeParamerters();
		
		DefaultAgent agent = new DefaultAgent();
		String spaces = agent.listSpaces(paramerters);
		
		System.out.println("Spaces: " + spaces);
		Assert.assertTrue(spaces.indexOf("dorado") >= 0);
	}
	
	@Test
	public void testListTables() throws Exception {
		Map<String, String> paramerters = makeParamerters();
		paramerters.put(IAgent.NAMESPACE, "dorado");
		
		DefaultAgent agent = new DefaultAgent();
		String tables = agent.listTables(paramerters);
		System.out.println("Tables: " + tables);
	}
	
	@Test
	public void testCreateColumns() throws Exception {
		Map<String, String> paramerters = makeParamerters();
		paramerters.put(IAgent.NAMESPACE, "dorado");
		paramerters.put(IAgent.TABLE_NAME, "employee");
		paramerters.put(IAgent.TABLE_TYPE, Table.TYPE);
		paramerters.put(IAgent.XML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Table name=\"EMPLOYEE\" tableName=\"EMPLOYEE\"></Table>");
		
		{
			DefaultAgent agent = new DefaultAgent();
			String xml = agent.createColumns(paramerters);
			System.out.println(xml);
		}
		
		{
			DefaultAgent agent = new DefaultAgent();
			paramerters.put(IAgent.XML, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Table name=\"EMPLOYEE\" tableName=\"EMPLOYEE\"><Columns><KeyColumn jdbcType=\"INTEGER-Integer\" name=\"ID\"/></Columns></Table>");
			String xml = agent.createColumns(paramerters);
			System.out.println(xml);
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
