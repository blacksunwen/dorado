package com.bstek.dorado.jdbc.oracle.v11;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bstek.dorado.jdbc.ide.DefaultAgent;
import com.bstek.dorado.jdbc.ide.IAgent;

public class AgentTest {

	@Test
	public void testListTables() throws Exception {
		Map<String, Object> parameters = makeParamerters();
		parameters.put(IAgent.NAMESPACE, "DORADO");
		
		DefaultAgent agent = new DefaultAgent();
		agent.listTables(parameters);
	}
	
	private Map<String, Object> makeParamerters() {
		Map<String, Object> parameters = new HashMap<String,Object>();
		
		parameters.put(IAgent.URL, "jdbc:oracle:thin:@192.168.18.90:1521/DORADO");
		parameters.put(IAgent.USER, "dorado");
		parameters.put(IAgent.PASSWORD, "dorado");
		parameters.put(IAgent.DRIVER, "oracle.jdbc.OracleDriver");
		return parameters;
	}
	
}
