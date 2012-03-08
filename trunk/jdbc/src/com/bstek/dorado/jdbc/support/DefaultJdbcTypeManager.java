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

import com.bstek.dorado.jdbc.JdbcTypeManager;
import com.bstek.dorado.jdbc.ide.AbstractDbColumnRuleTemplateInitializer;
import com.bstek.dorado.jdbc.type.JdbcType;
import com.bstek.dorado.util.Assert;

public class DefaultJdbcTypeManager implements JdbcTypeManager, InitializingBean, ApplicationContextAware {

	private static Log logger = LogFactory.getLog(DefaultJdbcTypeManager.class);
	
	private ApplicationContext applicationContext;
	private Map<String, JdbcType> types = new HashMap<String, JdbcType>();
	
	@Override
	public void register(JdbcType jdbcType) {
		types.put(jdbcType.getName(), jdbcType);
	}

	@Override
	public JdbcType[] list() {
		return types.values().toArray(new JdbcType[0]);
	}

	@Override
	public JdbcType get(String name) {
		JdbcType jdbcType = types.get(name);
		Assert.notNull(jdbcType, "no any JdbcType named [" + name + "]");
		
		return jdbcType;
	}

	@Override
	public boolean has(String name) {
		return types.containsKey(name);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, JdbcType> typeMap = applicationContext.getBeansOfType(JdbcType.class, false, true);
		Collection<JdbcType> types = typeMap.values();
		for (JdbcType type: types) {
			this.register(type);
		}
		
		doLog();
	}
	
	private void doLog() {
		if (logger.isInfoEnabled()) {
			String msg = "Registered JdbcTypes: "+ AbstractDbColumnRuleTemplateInitializer.DATA_TYPES_STORE_KEY +" [";
			JdbcType[] types = list();
			List<String> names = new ArrayList<String>();
			for (JdbcType type: types) {
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
