package com.bstek.dorado.jdbc.mssql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.AbstractJdbcTestCase;
import com.bstek.dorado.jdbc.JdbcUtils;

public class MssqlJdbcTestCase extends AbstractJdbcTestCase {

	public static final String ENV_2008 = "mssql2008";
	public static final String ENV_2000 = "mssql2000";
	
	@Override
	protected List<String> getExtConfigLocations() {
		List<String> locations = new ArrayList<String>();
		locations.add("com/bstek/dorado/jdbc/mssql/context.xml");
		return locations;
	}

	protected static class Dept {
		public static String TABLE = "DEPT";
		
		public static Record random(){
			Record r = new Record();
			r.put("DEPT_ID", RandomStringUtils.randomAlphabetic(6));
			r.put("DEPT_NAME", RandomStringUtils.randomAlphabetic(6));
			return r;
		}
		
		public static Record get(String id) {
			Collection<Record> records = JdbcUtils.query(TABLE, 
					Collections.singletonMap("ID", id));
			Assert.assertEquals("Dept id=" + id, 1, records.size());
			
			Record dept = (Record)records.iterator().next();
			return dept;
		}
	}
	
	protected static class Employee {
		public static String TABLE = "EMPLOYEE";
		
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
		
		public static Record get(Integer id) {
			Collection<Record> records = JdbcUtils.query(TABLE, 
					Collections.singletonMap("ID", id));
			Assert.assertEquals("Employee id=" + id, 1, records.size());
			
			Record employee = (Record)records.iterator().next();
			return employee;
		}
		
		public static boolean has(Integer id) {
			Collection<Record> records = JdbcUtils.query(TABLE, 
					Collections.singletonMap("ID", id));
			Assert.assertTrue("Employee id=" + id, records.size() <= 1);
			
			if (records.size() == 0) {
				return false;
			} else {
				return true;
			} 
		}
	}
	
}
