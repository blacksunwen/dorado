package com.bstek.dorado.jdbc.oracle;

import com.bstek.dorado.jdbc.TestJdbcUtils;

public class IdeTest extends OracleJdbcTestCase {

	public void testOutputEmployee() {
		String xml = TestJdbcUtils.outputTable(JDBC_ENV_NAME, Employee.TABLE);
		
		System.out.println("XML:" + xml);
	}
	
	public void testOutputDept() {
		String xml = TestJdbcUtils.outputTable(JDBC_ENV_NAME, Dept.TABLE);
		
		System.out.println("XML:" + xml);
	}

	public void testOutputSP_noop1() {
		String procedureName = "noop1";
		outSP(procedureName);
	}
	
	
	public void testOutputSP_noop2() {
		String procedureName = "noop2";
		outSP(procedureName);
	}
	
	public void testOutputSP_p1() {
		String procedureName = "p1";
		outSP(procedureName);
	}
	
	public void testOutputSP_p2() {
		String procedureName = "p2";
		outSP(procedureName);
	}
	
	public void testOutputSP_fn1() {
		String procedureName = "fn1";
		outSP(procedureName);
	}
	
	public void testOutputSP_fn2() {
		String procedureName = "fn2";
		outSP(procedureName);
	}
	
	public void testOutputSP_fn3() {
		String procedureName = "fn3";
		outSP(procedureName);
	}
	
	protected void outSP(String procedureName) {
		String jdbcEnvName = JDBC_ENV_NAME;
		String catalog = "DORADO";
		String schema = null;

		String xml = TestJdbcUtils.outputSP(jdbcEnvName, catalog, schema, procedureName);
		System.out.println("XML:" + xml);
	}
	
}
