package com.bstek.dorado.jdbc.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bstek.dorado.jdbc.JdbcEnviroment;


public class DefaultJdbcEnviromentManager implements JdbcEnviromentManager, ApplicationContextAware {

	private Map<String, JdbcEnviroment> enviroments = new HashMap<String, JdbcEnviroment>(2);
	
	private JdbcEnviroment defaultEnviroment;
	
	protected ApplicationContext ctx;
	
	public JdbcEnviroment getEnviroment(String name) {
		if (StringUtils.isNotEmpty(name)) {
			JdbcEnviroment env = enviroments.get(name);
			if (env == null)
				throw new IllegalArgumentException("no JdbcEnviroment be found, named '" + name + "'.");
			
			return env;
		} else {
			return getDefault();
		}
	}

	public void register(JdbcEnviroment env) {
		if (env.isDefault()) {
			if (defaultEnviroment == null) {
				defaultEnviroment = env;
			} else {
				throw new IllegalArgumentException("default JdbcEnviroment can't be override. [" + 
						defaultEnviroment.getName() + "," + env.getName() + "]");
			}
		}
		enviroments.put(env.getName(), env);
	}

	public JdbcEnviroment getDefault() {
		if (defaultEnviroment == null) {
			if (enviroments.size() == 1) {
				defaultEnviroment = enviroments.values().iterator().next();
			} else {
				throw new IllegalArgumentException("no default JdbcEnviroment can be found.");
			}
		}
		return defaultEnviroment;
	}

	@Override
	public void initialize() {
		Map<String, JdbcEnviroment> envMap = ctx.getBeansOfType(JdbcEnviroment.class);
		for (JdbcEnviroment env: envMap.values()) {
			this.register(env);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.ctx = applicationContext;
	}

}
