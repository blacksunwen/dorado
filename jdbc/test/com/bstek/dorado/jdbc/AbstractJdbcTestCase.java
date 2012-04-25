package com.bstek.dorado.jdbc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.config.ConfigManagerTestSupport;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.model.DbTable;
import com.bstek.dorado.jdbc.support.JdbcDataProviderContext;
import com.bstek.dorado.jdbc.support.JdbcDataProviderOperation;

public abstract class AbstractJdbcTestCase extends ConfigManagerTestSupport {

	protected AbstractJdbcTestCase () {
		super();
		this.addExtensionContextConfigLocation("classpath:com/bstek/dorado/jdbc/context.xml");
		this.addExtensionContextConfigLocation("classpath:com/bstek/dorado/idesupport/context.xml");
		this.addExtensionContextConfigLocation("classpath:com/bstek/dorado/view/context.xml");
		for (String location: getExtConfigLocations()) {
			this.addExtensionContextConfigLocation(location);
		}
	}
	
	public JdbcDao getDao() {
	try {
		return (JdbcDao)Context.getCurrent().getServiceBean("jdbcDao");
	} catch (Exception e) {
		throw new RuntimeException(e);
	}
}

	
	protected JdbcDataProvider getProvider(String name) throws Exception {
		JdbcDataProvider provider = (JdbcDataProvider)this.getDataProviderManager().getDataProvider(name);
		Assert.assertNotNull("no provider named [" + name + "] defined.", provider);
		
		return provider;
	}
	
	protected JdbcDataResolver getResolver(String name) throws Exception {
		JdbcDataResolver resolver = (JdbcDataResolver)this.getDataResolverManager().getDataResolver(name);
		Assert.assertNotNull("no resolver named [" + name + "] defined.", resolver);
		
		return resolver;
	}
	
	protected abstract List<String> getExtConfigLocations(); 
	
	static abstract class TestTable {
		abstract String getTableName();
		
		Collection<Record> query(Object parameter)throws Exception{
			JdbcDataProviderContext jCtx = new JdbcDataProviderContext(null, parameter);
			DbTable table = JdbcUtils.getDbTable(getTableName());
			JdbcDataProviderOperation operation = new JdbcDataProviderOperation(table, jCtx);
			
			return jCtx.getJdbcEnviroment().getJdbcDao().query(operation);
		}
	}
	
	protected static class Dept extends TestTable {
		public static String TABLE = "DEPT";
		
		@Override
		String getTableName() {
			return TABLE;
		}
		
		public static Record random(){
			Record r = new Record();
			r.put("DEPT_ID", RandomStringUtils.randomAlphabetic(6));
			r.put("DEPT_NAME", RandomStringUtils.randomAlphabetic(6));
			r.put("PARENT_ID", RandomStringUtils.randomAlphabetic(6));
			return r;
		}
		
		public static Record get(String id) throws Exception{
			Dept t = new Dept();
			Collection<Record> records = t.query(Collections.singletonMap("ID", id));
			Assert.assertEquals("Dept id=" + id, 1, records.size());
			
			Record dept = (Record)records.iterator().next();
			return dept;
		}
		
		public static boolean has(String id) throws Exception{
			Dept t = new Dept();
			Collection<Record> records = t.query(Collections.singletonMap("ID", id));
			Assert.assertTrue("Dept id=" + id, records.size() <= 1);
			
			if (records.size() == 0) {
				return false;
			} else {
				return true;
			} 
		}

	}
	
	protected static class Employee extends TestTable  {
		public static String TABLE = "EMPLOYEE";
		
		@Override
		String getTableName() {
			return TABLE;
		}
		
		public static Record random() {
			Record r = new Record();
			r.put("ID", Integer.valueOf(RandomStringUtils.randomNumeric(7)));
			r.put("REPORT_TO", Integer.valueOf(RandomStringUtils.randomNumeric(7)));
			r.put("HIRE_DATE", java.sql.Date.valueOf("2006-11-30"));
			r.put("BIRTHDAY", java.sql.Date.valueOf("1977-05-22"));
			r.put("SEX", System.currentTimeMillis() % 2 == 0 ? true: false);
			r.put("NOTES", RandomStringUtils.randomAscii(100));
			r.put("FIRST_NAME", RandomStringUtils.randomAlphabetic(6));
			r.put("LAST_NAME", RandomStringUtils.randomAlphabetic(6));
			r.put("DEPT_ID", null);
			r.put("COUNTRY", RandomStringUtils.randomAlphabetic(6));
			r.put("ADDRESS", RandomStringUtils.randomAlphabetic(6));
			r.put("PHONE", Integer.valueOf(RandomStringUtils.randomNumeric(8)).toString());
			r.put("PHOTO_PATH", RandomStringUtils.randomAlphabetic(6));
			r.put("CITY", RandomStringUtils.randomAlphabetic(6));
			r.put("POSTAL_CODE", RandomStringUtils.randomNumeric(6));
			r.put("TITLE_OF_COURTESY", RandomStringUtils.randomAlphabetic(6));
			r.put("REGION", RandomStringUtils.randomAlphabetic(6));
			r.put("TITLE", RandomStringUtils.randomAlphabetic(6));
			
			return r;
		}
		
		public static Record get(Integer id) throws Exception{
			Employee t = new Employee();
			Collection<Record> records = t.query(Collections.singletonMap("ID", id));
			Assert.assertEquals("Employee id=" + id, 1, records.size());
			
			Record employee = (Record)records.iterator().next();
			return employee;
		}
		
		public static boolean has(Integer id) throws Exception{
			Employee t = new Employee();
			Collection<Record> records = t.query(Collections.singletonMap("ID", id));
			Assert.assertTrue("Employee id=" + id, records.size() <= 1);
			
			if (records.size() == 0) {
				return false;
			} else {
				return true;
			} 
		}

		
	}
}
