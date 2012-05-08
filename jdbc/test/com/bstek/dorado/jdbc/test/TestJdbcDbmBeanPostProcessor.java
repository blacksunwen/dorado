package com.bstek.dorado.jdbc.test;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.bstek.dorado.jdbc.config.JdbcConfigLoader;

public class TestJdbcDbmBeanPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		
		if (beanName.equals("testJdbcConfigLoader")) {
			Class<?> clazz = TestJdbcUtils.getCurrentTestClass();
			String configLocation = getDbmLocation(clazz);
			
			JdbcConfigLoader loader = (JdbcConfigLoader) bean;
			
			loader.setConfigLocation(configLocation);
		}
		return bean;
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
