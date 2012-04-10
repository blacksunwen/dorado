package com.bstek.dorado.hibernate.provider;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.util.Assert;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.AbstractDataProvider;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-2-20
 */
public abstract class HibernateDataProviderSupport extends AbstractDataProvider {
	private String sessionFactory;
	private boolean unique = false;
	private boolean autoFilter = false;

	public String getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(String sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public void setAutoFilter(boolean autoFilter) {
		this.autoFilter = autoFilter;
	}
	public boolean isAutoFilter() {
		return this.autoFilter;
	}
	
	protected SessionFactory getSessionFactoryOject() throws Exception {
		SessionFactoryManager sessionManager = (SessionFactoryManager) Context
				.getCurrent().getServiceBean("hibernateSessionFactoryManager");
		SessionFactory sessionFactoryBean = sessionManager
				.getSessionFactory(sessionFactory);
		Assert.notNull(sessionFactoryBean, "SessionFactory named [" + sessionFactory + "] cound not be found.");
		return sessionFactoryBean;
	}
	
	protected Session openSession() throws Exception {
		SessionFactory sessionFactory = this.getSessionFactoryOject();
		return sessionFactory.openSession();
	}
	
	protected Session currentSession() throws Exception {
		SessionFactory sessionFactory = this.getSessionFactoryOject();
		return sessionFactory.getCurrentSession();
	}
	
	protected Session session() throws Exception{
		Session session = null;
		try {
			session = this.currentSession();
		} catch (Exception e) {
			session = this.openSession();
		}
		
		return session;
	}
}
