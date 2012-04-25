package com.bstek.dorado.jdbc;

import java.util.Iterator;
import java.util.Set;

import junit.framework.Assert;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.config.JdbcEnviromentManager;

public class TestJdbcUtils {
	
	@SuppressWarnings("unchecked")
	private static <T> T getServiceBean(String serviceName) {
		Context ctx = Context.getCurrent();
		try {
			return (T)ctx.getServiceBean(serviceName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static JdbcEnviromentManager getEnviromentManager() {
		JdbcEnviromentManager manager = getServiceBean("jdbc.enviromentManager");
		return manager;
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
