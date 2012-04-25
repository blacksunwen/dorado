package com.bstek.dorado.jdbc.oracle.v11;

import junit.framework.Assert;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcUtils;
import com.bstek.dorado.jdbc.TestJdbcUtils;
import com.bstek.dorado.jdbc.model.Table;

public class TableTest extends AbstractOracle11JdbcTestCase {
	
	public void testEmployee() throws Exception{
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
	
	public void testDept() throws Exception{
		Record dept = Dept.random();
		String id = dept.getString("DEPT_ID");
		{
			getDao().insert(Dept.TABLE, dept);
			Record dept2 = Dept.get(id);
			TestJdbcUtils.assertEquals(dept, dept2);
		}
		{
			Record dept2 = Dept.get(id);
			dept2.put("DEPT_ID", id);
			
			getDao().update(Dept.TABLE, dept2);
			
			Record dept3 = Dept.get(id);
			TestJdbcUtils.assertEquals(dept2, dept3);
		}
		{
			getDao().delete(Dept.TABLE, dept);
			Assert.assertTrue(!Dept.has(id));
		}
	}
	
	public void testByKey() throws Exception{
		Table table = (Table)JdbcUtils.getDbTable(Employee.TABLE);
		Record record = getDao().getByKey(table, 3959991);
		
		Assert.assertNotNull(record);
	}
}
