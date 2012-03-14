package com.bstek.dorado.jdbc.oracle.v11;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.JdbcUtils;

public class AutoTableTest extends AbstractOracle11JdbcTestCase {

	public void testEmp1() {
		String tableName = "emp_auto1";
		
		Collection<Record> emps = JdbcUtils.query(tableName, null);
		Assert.assertTrue(!emps.isEmpty());
		{
			Record emp = emps.iterator().next();
			int id = emp.getInt("n_id");
			emp.put("n_first_name", RandomStringUtils.randomAlphabetic(6));
			emp.put("n_last_name", RandomStringUtils.randomAlphabetic(6));
			JdbcUtils.update(tableName, emp);
			
			Record emp2 = Employee.get(id);
			Assert.assertEquals(emp.get("n_first_name"), emp2.getString("FIRST_NAME"));
			Assert.assertEquals(emp.get("n_last_name"), emp2.getString("LAST_NAME"));
		}
		{
			Record emp = Employee.random();
			Integer id = (Integer)emp.get("ID");
			System.out.println("ID: " + id);
			
			Record autoEmp = new Record();
			autoEmp.put("n_id",         emp.get("ID"));
			autoEmp.put("n_last_name",  emp.get("LAST_NAME"));
			autoEmp.put("n_first_name", emp.get("FIRST_NAME"));
			autoEmp.put("n_title",      emp.get("TITLE"));
			autoEmp.put("n_sex",        emp.get("SEX"));
			autoEmp.put("n_birthday",   emp.get("BIRTHDAY"));
			
			{
				JdbcUtils.insert(tableName, autoEmp);
				
				Record emp2 = Employee.get(id);
				Assert.assertEquals(emp.get("ID"), emp2.get("ID"));
				Assert.assertEquals(emp.get("LAST_NAME"), emp2.get("LAST_NAME"));
				Assert.assertEquals(emp.get("FIRST_NAME"), emp2.get("FIRST_NAME"));
				Assert.assertEquals(emp.get("TITLE"), emp2.get("TITLE"));
				Assert.assertEquals(emp.get("SEX"), emp2.get("SEX"));
				Assert.assertEquals(emp.get("BIRTHDAY"), emp2.get("BIRTHDAY"));
			}{
				autoEmp.put("n_last_name", RandomStringUtils.randomAlphabetic(6));
				JdbcUtils.update(tableName, autoEmp);
				
				Record emp2 = Employee.get(id);
				Assert.assertEquals(autoEmp.get("n_id"),         emp2.get("ID"));
				Assert.assertEquals(autoEmp.get("n_last_name"),  emp2.get("LAST_NAME"));
				Assert.assertEquals(autoEmp.get("n_first_name"), emp2.get("FIRST_NAME"));
				Assert.assertEquals(autoEmp.get("n_title"),      emp2.get("TITLE"));
				Assert.assertEquals(autoEmp.get("n_sex"),        emp2.get("SEX"));
				Assert.assertEquals(autoEmp.get("n_birthday"),   emp2.get("BIRTHDAY"));
			}{
				JdbcUtils.delete(tableName, autoEmp);
				Assert.assertEquals(false, Employee.has(id));
			}
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
			parameter.put("lastName", "last777");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("firstName", "firstklhlkh");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("lastName", "last777hg");
			parameter.put("firstName", "first;lj;j;o");
			
			Collection<Record> records = JdbcUtils.query(tableName, parameter);
			Assert.assertTrue(records.isEmpty());
		}
		{
			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("title", "last777hg");
			parameter.put("phone", "last777hg");
			
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
