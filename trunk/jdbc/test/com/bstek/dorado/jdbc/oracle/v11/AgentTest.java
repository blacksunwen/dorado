package com.bstek.dorado.jdbc.oracle.v11;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bstek.dorado.jdbc.ide.DefaultAgent;
import com.bstek.dorado.jdbc.ide.IAgent;

public class AgentTest {

//	@Test
//	public void testListSpaces() throws Exception {
//		DefaultAgent agent = new DefaultAgent();;
//		Map<String, String> paramerters = makeParamerters();
//		
//		String spaces = agent.listSpaces(paramerters);
//		
//		Assert.assertTrue(spaces.indexOf("DORADO") >= 0);
//		System.out.println("Spaces: " + spaces);
//		
//	}

	@Test
	public void testListTables() throws Exception {
		Map<String, String> paramerters = makeParamerters();
		paramerters.put(IAgent.NAMESPACE, "DORADO");
		
		DefaultAgent agent = new DefaultAgent();
		String tables = agent.listTables(paramerters);
		System.out.println("Tables: " + tables);
	}
	
	private Map<String, String> makeParamerters() {
		Map<String, String> parameters = new HashMap<String,String>();
		
		parameters.put(IAgent.URL, "jdbc:oracle:thin:@192.168.18.90:1521/DORADO");
		parameters.put(IAgent.USER, "dorado");
		parameters.put(IAgent.PASSWORD, "dorado");
		parameters.put(IAgent.DRIVER, "oracle.jdbc.OracleDriver");
		return parameters;
	}
	
}
