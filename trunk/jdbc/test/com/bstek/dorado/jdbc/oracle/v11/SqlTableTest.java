package com.bstek.dorado.jdbc.oracle.v11;

import java.util.Collection;

import junit.framework.Assert;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.TestJdbcUtils;

public class SqlTableTest  extends Oracle11JdbcTestCase {

	public void testSqlDept() {
		String tableName = "sql_dept";
		Collection<Record> depts = JdbcUtils.query(tableName, null);
		Assert.assertEquals(2, depts.size());
		
		Assert.assertTrue(depts.size() > 0);
		{
			Record dept = depts.iterator().next();
			String id = dept.getString("DEPT_ID");
			
			Record dept2 = Dept.random();
			dept2.put("DEPT_ID", id);
			
			JdbcUtils.update(tableName, dept2);
			
			Record dept3 = Dept.get(id);
			TestJdbcUtils.assertEquals(dept2, dept3);
		}
	}
	
}
