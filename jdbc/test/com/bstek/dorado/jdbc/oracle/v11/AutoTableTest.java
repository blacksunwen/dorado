package com.bstek.dorado.jdbc.oracle.v11;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcUtils;

public class AutoTableTest extends Oracle11JdbcTestCase {

	public void testEmp1() {
		String tableName = "emp_auto1";
		
		Collection<Record> emps = JdbcUtils.query(tableName, null);
		Assert.assertTrue(!emps.isEmpty());
		{
			Record emp = emps.iterator().next();
			int id = emp.getInt("p_id");
			emp.put("p_first_name", RandomStringUtils.randomAlphabetic(6));
			emp.put("p_last_name", RandomStringUtils.randomAlphabetic(6));
			JdbcUtils.update(tableName, emp);
			
			Record emp2 = Employee.get(id);
			Assert.assertEquals(emp.get("p_first_name"), emp2.getString("FIRST_NAME"));
			Assert.assertEquals(emp.get("p_last_name"), emp2.getString("LAST_NAME"));
		}
	}
	
	public void testEmp2() {
		String tableName = "emp_auto2";
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("ID", 333);
		
		Collection<Record> records = JdbcUtils.query(tableName, parameter);
		Assert.assertTrue(records.isEmpty());
	}
	
	public void testEmp3() {
		String tableName = "emp_auto3";
		{
			Collection<Record> records = JdbcUtils.query(tableName, null);
			Assert.assertTrue(!records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("lastName", "last");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("firstName", "first");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("lastName", "last");
			parameter.put("firstName", "first");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
	}
	
	public void testEmp4() {
		String tableName = "emp_auto4";
		{
			Collection<Record> records = JdbcUtils.query(tableName, null);
			Assert.assertTrue(!records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("lastName", "last");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("firstName", "first");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("lastName", "last");
			parameter.put("firstName", "first");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("id", 2323000);
			parameter.put("firstName", "first");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("id", 2323000);
			parameter.put("lastName", "last");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("id", 2323000);
			parameter.put("lastName", "last");
			parameter.put("firstName", "first");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
	}
	
	
	public void testProductCat1() {
		String tableName = "product_cat_auto1";
		Collection<Record> records = JdbcUtils.query(tableName, null);
	}
	
	public void testProductCat2() {
		String tableName = "product_cat_auto2";
		Collection<Record> records = JdbcUtils.query(tableName, null);
	}
}
