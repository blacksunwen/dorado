package com.bstek.dorado.hibernate.provider;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.core.Context;
import com.bstek.dorado.data.provider.AbstractDataProvider;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.type.DataType;
import com.bstek.dorado.hibernate.hql.Hql;
import com.bstek.dorado.hibernate.hql.HqlQuerier;
import com.bstek.dorado.hibernate.hql.HqlUtil;
import com.bstek.dorado.util.Assert;

/**
 * 利用Hibernate HQL功能实现的DataProvider
 * 
 * @author mark
 */
@XmlNode(fixedProperties = "type=hibernateHql")
public class HqlDataProvider extends AbstractDataProvider {
	private String sessionFactory;
	private boolean unique = false;
	private ResultTransformer resultTransformer;
	
	public String getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(String sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@ClientProperty(escapeValue="false")
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	
	@XmlProperty(parser="spring:dorado.hibernate.resultTransformerParser")
	@IdeProperty(enumValues="ALIAS_TO_ENTITY_MAP,ROOT_ENTITY,DISTINCT_ROOT_ENTITY,PROJECTION")
	public ResultTransformer getResultTransformer() {
		return resultTransformer;
	}

	public void setResultTransformer(ResultTransformer resultTransformer) {
		this.resultTransformer = resultTransformer;
	}
	
	protected SessionFactory getSessionFactoryOject() throws Exception {
		SessionFactoryManager sessionManager = (SessionFactoryManager) Context
				.getCurrent().getServiceBean("hibernateSessionFactoryManager");
		SessionFactory sessionFactoryBean = sessionManager
				.getSessionFactory(sessionFactory);
		Assert.notNull(sessionFactoryBean, "SessionFactory named [" + sessionFactory + "] cound not be found.");
		return sessionFactoryBean;
	}
	
	protected Session getSession() throws Exception{
		return this.getSessionFactoryOject().getCurrentSession();
	}
	
	private String hql;

	@IdeProperty(editor = "multiLines")
	public String getHql() {
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	protected Object internalGetResult(Object parameter, DataType resultDataType)
			throws Exception {
		Assert.notEmpty(this.hql, "Hql must not be empty.");

		Hql hql = createHql(this.hql, parameter, resultDataType);
		HqlQuerier querier = createHqlQuerier();

		Session session = getSession();
		return querier.query(session, parameter, hql, this);
	}

	protected void internalGetResult(Object parameter, Page<?> page,
			DataType resultDataType) throws Exception {
		Assert.notEmpty(this.hql, "Hql must not be empty.");

		Hql hql = createHql(this.hql, parameter, resultDataType);
		HqlQuerier querier = createHqlQuerier();

		Session session = getSession();
		querier.query(session, parameter, hql, page, this);
	}

	protected HqlQuerier createHqlQuerier() throws Exception {
		HqlQuerier querier = (HqlQuerier) Context.getCurrent().getServiceBean(
				"hqlQuerier");
		return querier;
	}

	protected Hql createHql(String hqlClause, Object parameter,
			DataType resultDataType) throws Exception {
		String clause = HqlUtil.build(hqlClause, parameter);
		Hql hql = HqlUtil.build(clause);
		return hql;
	}

}
