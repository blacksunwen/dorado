package com.bstek.dorado.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.filter.AdvanceFilterCriterionParser;
import com.bstek.dorado.data.provider.filter.SingleValueFilterCriterion;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.hibernate.provider.SessionFactoryManager;
import com.bstek.dorado.view.ViewContextTestCase;

public abstract class HibernateContextTestCase extends ViewContextTestCase {
	public HibernateContextTestCase() {
		super();
		addExtensionContextConfigLocation("com/bstek/dorado/hibernate/context.xml");
		addExtensionContextConfigLocation("com/bstek/dorado/hibernate/test-context.xml");
	}

	protected SingleValueFilterCriterion createFilterCriterion(String property, DataType dataType,
			String expression) throws Exception {
		Context context = Context.getCurrent();
		AdvanceFilterCriterionParser parser = (AdvanceFilterCriterionParser)context.getServiceBean("filterCriterionParser");
		
		return (SingleValueFilterCriterion)parser.createFilterCriterion(property, dataType, expression);
	}

	private SessionFactory getSessionFactory() throws Exception {
		Context context = Context.getCurrent();
		SessionFactoryManager m = (SessionFactoryManager)context.getServiceBean("hibernateSessionFactoryManager");
		return m.getSessionFactory(null);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		SessionFactory sessionFactory = getSessionFactory();
		if (!TransactionSynchronizationManager.hasResource(sessionFactory)) {
			Session session = SessionFactoryUtils.doGetSession(sessionFactory, true);
			TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		}
	}

	@Override
	protected void tearDown() throws Exception {
		SessionFactory sessionFactory = getSessionFactory();
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(sessionHolder.getSession());
		
		super.tearDown();
	}
}
