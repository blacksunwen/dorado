package com.bstek.dorado.jdbc.test;

import org.apache.commons.lang.StringUtils;

import com.bstek.dorado.jdbc.config.JdbcConfigLoader;
import com.bstek.dorado.junit.TestUtils;

public class TestJdbcConfigLoader extends JdbcConfigLoader {

	@Override
	public void afterPropertiesSet() throws Exception {
		Class<?> clazz = TestUtils.getCurrentClassClass();
		String configLocation = getDbmLocation(clazz);
		this.setConfigLocation(configLocation);
		
		super.afterPropertiesSet();
	}

	String getDbmLocation(Class<?> clazz) {
		String[] tokens = StringUtils.split(clazz.getName(), '.');
		
		String[] pkgTokens = new String[tokens.length];
		System.arraycopy(tokens, 0, pkgTokens, 0, tokens.length-1);
		pkgTokens[pkgTokens.length-1] = "*.dbm.xml";
		
		String modelClassPath = StringUtils.join(pkgTokens, '/');
		
		return "classpath:" + modelClassPath;
	}
}
