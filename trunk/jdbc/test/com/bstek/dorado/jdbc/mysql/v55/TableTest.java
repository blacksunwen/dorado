package com.bstek.dorado.jdbc.mysql.v55;

import junit.framework.Assert;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.TestJdbcUtils;

public class TableTest extends Mysql55JdbcTestCase {

	public void testEmployee() throws Exception {
		Record employee = Employee.random();
		Integer id = (Integer)employee.get("ID");
		{
			//保存前后比较
			getDao().insert(Employee.TABLE, employee);
			Record employee2 = Employee.get(id);
			TestJdbcUtils.assertEquals(employee, employee2);
		}
		{
			//更新前后比较
			Record employee2 = Employee.random();
			employee2.put("ID", id);
			
			getDao().update(Employee.TABLE, employee2);
			
			Record employee3 = Employee.get(id);
			TestJdbcUtils.assertEquals(employee2, employee3);
		}
		{
			//删除是否成功
			getDao().delete(Employee.TABLE, employee);
			Assert.assertTrue(!Employee.has(id));
		}
	}
}
