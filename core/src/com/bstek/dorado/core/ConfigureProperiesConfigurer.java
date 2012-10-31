/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
