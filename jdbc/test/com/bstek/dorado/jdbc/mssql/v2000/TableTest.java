package com.bstek.dorado.jdbc.mssql.v2000;

import junit.framework.Assert;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcOperationUtils;
import com.bstek.dorado.jdbc.TestJdbcUtils;

public class TableTest extends Mssql2000JdbcTestCase {
	
	public void testEmployee() {
		Record employee = Employee.random();
		Integer id = (Integer)employee.get("ID");
		{
			//保存前后比较
			JdbcOperationUtils.insert(Employee.TABLE, employee);
			Record employee2 = Employee.get(id);
			TestJdbcUtils.assertEquals(employee, employee2);
		}
		{
			//更新前后比较
			Record employee2 = Employee.random();
			employee2.put("ID", id);
			
			JdbcOperationUtils.update(Employee.TABLE, employee2);
			
			Record employee3 = Employee.get(id);
			TestJdbcUtils.assertEquals(employee2, employee3);
		}
		{
			//删除是否成功
			JdbcOperationUtils.delete(Employee.TABLE, employee);
			Assert.assertTrue(!Employee.has(id));
		}
	}
}
