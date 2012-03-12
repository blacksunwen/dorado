package com.bstek.dorado.jdbc.mssql.v2008;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bstek.dorado.jdbc.ide.DefaultAgent;
import com.bstek.dorado.jdbc.ide.IAgent;

public class AgentTest {
	
	@Test
	public void testListTables() throws Exception {
		Map<String, Object> parameters = makeParamerters();
		parameters.put(IAgent.NAMESPACE, "dbo");
		
		DefaultAgent agent = new DefaultAgent();
		String tables = agent.listTables(parameters);
		System.out.println("Tables: " + tables);
	}
	
	private Map<String, Object> makeParamerters() {
		Map<String, Object> parameters = new HashMap<String,Object>();
		
		parameters.put(IAgent.URL, "jdbc:sqlserver://192.168.18.92:1433;databaseName=DORADO");
		parameters.put(IAgent.USER, "dorado");
		parameters.put(IAgent.PASSWORD, "dorado");
		parameters.put(IAgent.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		return parameters;
	}
}
