package com.bstek.dorado.jdbc.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.dorado.jdbc.KeyGeneratorManager;
import com.bstek.dorado.jdbc.model.table.KeyGenerator;
import com.bstek.dorado.util.Assert;

public class DefaultKeyGeneratorManager implements KeyGeneratorManager, InitializingBean, ApplicationContextAware {

	private static Log logger = LogFactory.getLog(DefaultKeyGeneratorManager.class);
	
	private ApplicationContext applicationContext;
	
	@SuppressWarnings("rawtypes")
	private Map<String, KeyGenerator> elements = new HashMap<String, KeyGenerator>();
	
	@SuppressWarnings("rawtypes")
	@Override
	public void register(KeyGenerator keyGenerator) {
		elements.put(keyGenerator.getName(), keyGenerator);
	}

	@SuppressWarnings("unchecked")
	@Override
	public KeyGenerator<Object>[] list() {
		return elements.values().toArray(new KeyGenerator[0]);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T>KeyGenerator<T> get(String name) {
		KeyGenerator<T> jdbcType = (KeyGenerator<T>)elements.get(name);
		Assert.notNull(jdbcType, "no any KeyGenerator named [" + name + "]");
		
		return jdbcType;
	}

	@Override
	public boolean has(String name) {
		return elements.containsKey(name);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, KeyGenerator> typeMap = applicationContext.getBeansOfType(KeyGenerator.class, false, true);
		Collection<KeyGenerator> types = typeMap.values();
		for (KeyGenerator type: types) {
			this.register(type);
		}
		
		doLog();
	}

	private void doLog() {
		if (logger.isInfoEnabled()) {
			String msg = "Registered KeyGenerators: [";
			KeyGenerator<Object>[] types = list();
			List<String> names = new ArrayList<String>();
			for (KeyGenerator<Object> type: types) {
				names.add(type.getName());
			}
			
			msg += StringUtils.join(names, ',');
			msg += "]";
			logger.info(msg);
		}
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
