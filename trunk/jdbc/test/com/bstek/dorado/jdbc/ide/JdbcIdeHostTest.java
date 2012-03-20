package com.bstek.dorado.jdbc.ide;

import org.junit.Test;

public class JdbcIdeHostTest {

	@Test
	public void testCreateColumns() throws Exception {
		String[] args = new String[] {
				"-jdbcAgent", "com.bstek.dorado.jdbc.ide.DefaultAgent",
				"-service", "createColumns",
				"-file", "C:\\Users\\TD\\tmp.d7\\abc.de",
				"-tableType", "Table",
				"-tableName", "DEPT",
				"-namespace", "DORADO",
				"-xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <Table name=\"DEPT\" tableName=\"DEPT\"></Table>",
				
				"-driver", "oracle.jdbc.OracleDriver",
				"-password", "dorado",
				"-user", "dorado",
				"-url", "jdbc:oracle:thin:@192.168.18.90:1521/DORADO"
			};
		JdbcIdeHost.main(args);
	}
}
