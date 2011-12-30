package com.bstek.dorado.jdbc.oracle.v11;

import junit.framework.Assert;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.TestJdbcUtils;

public class TableTest extends Oracle11JdbcTestCase {
	
	public void testEmployee() {
		Record employee = Employee.random();
		Integer id = (Integer)employee.get("ID");
		{
			//保存前后比较
			JdbcUtils.insert(Employee.TABLE, employee);
			Record employee2 = Employee.get(id);
			TestJdbcUtils.assertEquals(employee, employee2);
		}
		{
			//更新前后比较
			Record employee2 = Employee.random();
			employee2.put("ID", id);
			
			JdbcUtils.update(Employee.TABLE, employee2);
			
			Record employee3 = Employee.get(id);
			TestJdbcUtils.assertEquals(employee2, employee3);
		}
		{
			//删除是否成功
			JdbcUtils.delete(Employee.TABLE, employee);
			Assert.assertTrue(!Employee.has(id));
		}
	}
	
	public void testDept() {
		Record dept = Dept.random();
		String id = dept.getString("DEPT_ID");
		{
			JdbcUtils.insert(Dept.TABLE, dept);
			Record dept2 = Dept.get(id);
			TestJdbcUtils.assertEquals(dept, dept2);
		}
		{
			Record dept2 = Dept.get(id);
			dept2.put("DEPT_ID", id);
			
			JdbcUtils.update(Dept.TABLE, dept2);
			
			Record dept3 = Dept.get(id);
			TestJdbcUtils.assertEquals(dept2, dept3);
		}
		{
			JdbcUtils.delete(Dept.TABLE, dept);
			Assert.assertTrue(!Dept.has(id));
		}
	}
	
}
