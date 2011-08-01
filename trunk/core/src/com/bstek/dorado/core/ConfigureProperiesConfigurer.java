package com.bstek.dorado.core;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-6-29
 */
public class ConfigureProperiesConfigurer extends PropertyPlaceholderConfigurer {
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Properties properties = new Properties();
		ConfigureStore store = Configure.getStore();
		for (String key : store.keySet()) {
			properties.setProperty(key, store.getString(key));
		}
		processProperties(beanFactory, properties);
	}
}
