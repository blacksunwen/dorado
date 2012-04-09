package com.bstek.dorado.jdbc.ide;

import org.junit.Test;

public class JdbcIdeHostTest {
//---------------- oracle ---------------------
//	"-driver", "oracle.jdbc.OracleDriver",
//	"-password", "dorado",
//	"-user", "dorado",
//	"-url", "jdbc:oracle:thin:@192.168.18.90:1521/DORADO"
	
//---------------- mysql ---------------------
//	"-password", "dorado",
//	"-user", "dorado",
//	"-url", "jdbc:mysql://192.168.18.95/DORADO?useUnicode=true&amp;characterEncoding=UTF-8"
	
	@Test
	public void testlistTables() throws Exception {
		String[] args = new String[] {
				"-jdbcAgent", "com.bstek.dorado.jdbc.ide.DefaultAgent",
				"-service", "listTables",
				"-file", "C:\\Users\\TD\\tmp.d7\\abc.de",
				"-namespace", "DORADO",
				
				"-password", "dorado",
				"-user", "dorado",
				"-url", "jdbc:mysql://192.168.18.95/DORADO?useUnicode=true&amp;characterEncoding=UTF-8"
			};
		JdbcIdeHost.main(args);
	}
	
	@Test
	public void testCreateColumns_Table() throws Exception {
		String[] args = new String[] {
				"-jdbcAgent", "com.bstek.dorado.jdbc.ide.DefaultAgent",
				"-service", "createColumns",
				"-file", "C:\\Users\\TD\\tmp.d7\\abc.de",
				"-tableType", "Table",
				"-tableName", "DEPT",
				"-namespace", "DORADO",
				
				"-xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <Table name=\"DEPT\" tableName=\"DEPT\"></Table>",
				
				"-password", "dorado",
				"-user", "dorado",
				"-url", "jdbc:mysql://192.168.18.95/DORADO?useUnicode=true&amp;characterEncoding=UTF-8"
			};
		JdbcIdeHost.main(args);
	}
	
	@Test
	public void testCreateColumns_SqlTable() throws Exception {
		String[] args = new String[] {
				"-jdbcAgent", "com.bstek.dorado.jdbc.ide.DefaultAgent",
				"-service", "createColumns",
				"-file", "C:\\Users\\TD\\tmp.d7\\abc.de",
				"-tableType", "SqlTable",
				"-querySql", "select * from EMPLOYEE",
				"-namespace", "DORADO",
				
				"-xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <SqlTable name=\"DEPT_SQL\"></SqlTable>",
				
				"-password", "dorado",
				"-user", "dorado",
				"-url", "jdbc:mysql://192.168.18.95/DORADO?useUnicode=true&amp;characterEncoding=UTF-8"
			};
		JdbcIdeHost.main(args);
	}
}
