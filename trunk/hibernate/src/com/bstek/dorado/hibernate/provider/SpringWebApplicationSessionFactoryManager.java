package com.bstek.dorado.hibernate.provider;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.web.context.WebApplicationContext;

import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-20
 */
public class SpringWebApplicationSessionFactoryManager implements
		SessionFactoryManager {
	private String defaultSessionFactory;

	public String getDefaultSessionFactory() {
		return defaultSessionFactory;
	}

	public void setDefaultSessionFactory(String defaultSessionFactory) {
		this.defaultSessionFactory = defaultSessionFactory;
	}

	public SessionFactory getSessionFactory(String sessionFactory)
			throws Exception {
		WebApplicationContext applicationContext = DoradoContext
				.getAttachedWebApplicationContext();
		return (SessionFactory) applicationContext.getBean(StringUtils
				.defaultString(sessionFactory, defaultSessionFactory));
	}

}
