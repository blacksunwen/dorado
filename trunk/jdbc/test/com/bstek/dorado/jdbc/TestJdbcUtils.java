package com.bstek.dorado.jdbc;

import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import org.w3c.dom.Document;

import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.config.DomHelper;
import com.bstek.dorado.jdbc.support.DefaultStoredProcedureGenerator;

public class TestJdbcUtils {
	
	public static String outputSP(String jdbcEnvName, String catalog, String schema, String procedureName) {
		JdbcEnviroment jdbcEnv = JdbcUtils.getEnviromentManager().getEnviroment(jdbcEnvName);
		DefaultStoredProcedureGenerator generator = new DefaultStoredProcedureGenerator();
		Document document = generator.create(jdbcEnv, catalog, schema, procedureName);
		
		String xml = DomHelper.toString(document);
		return xml;
	}
	
	public static boolean assertEquals(Record r1, Record r2) {
		Assert.assertEquals(r1.size(), r2.size());
		
		Set<String> keySet1 = r1.keySet();
		Set<String> keySet2 = r2.keySet();
		Assert.assertEquals(keySet1, keySet2);
		
		Iterator<String> keyItr1 = keySet1.iterator();
		while (keyItr1.hasNext()) {
			String key = keyItr1.next();
			Object value1 = r1.get(key);
			Object value2 = r2.get(key);
			
			Assert.assertEquals("key=" + key, value1, value2);
		}
		
		return true;
	}
}
