package com.bstek.dorado.hibernate.provider;

import org.hibernate.SessionFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-20
 */
public class TestSessionFactoryManager implements SessionFactoryManager {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory(String sessionFactoryName)
			throws Exception {
		return sessionFactory;
	}

}
