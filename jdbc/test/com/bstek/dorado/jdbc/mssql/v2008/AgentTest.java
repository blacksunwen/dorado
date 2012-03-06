package com.bstek.dorado.jdbc.mssql.v2008;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.bstek.dorado.jdbc.ide.DefaultAgent;
import com.bstek.dorado.jdbc.ide.IAgent;

public class AgentTest {
	@Test
	public void testListSpaces() throws Exception {
		DefaultAgent agent = new DefaultAgent();;
		Map<String, String> paramerters = makeParamerters();
		
		String spaces = agent.listSpaces(paramerters);
		System.out.println("Spaces: " + spaces);
		
		Assert.assertTrue(spaces.indexOf("dbo") >= 0);
	}
	
	@Test
	public void testListTables() throws Exception {
		Map<String, String> paramerters = makeParamerters();
		paramerters.put(IAgent.NAMESPACE, "dbo");
		
		DefaultAgent agent = new DefaultAgent();
		String tables = agent.listTables(paramerters);
		System.out.println("Tables: " + tables);
	}
	
	private Map<String, String> makeParamerters() {
		Map<String, String> parameters = new HashMap<String,String>();
		
		parameters.put(IAgent.URL, "jdbc:sqlserver://192.168.18.92:1433;databaseName=DORADO");
		parameters.put(IAgent.USER, "dorado");
		parameters.put(IAgent.PASSWORD, "dorado");
		parameters.put(IAgent.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		return parameters;
	}
}
