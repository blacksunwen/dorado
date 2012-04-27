package com.bstek.dorado.hibernate.hql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;

import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.hibernate.provider.HqlDataProvider;
import com.bstek.dorado.util.Assert;

public class DefaultHqlQuerier implements HqlQuerier {

	private HqlParameterResolver parameterResolver;
	
	public HqlParameterResolver getHqlParameterResolver() {
		return parameterResolver;
	}
	
	public void setHqlParameterResolver(HqlParameterResolver parameterResolver) {
		this.parameterResolver = parameterResolver;
	}
	
	@SuppressWarnings("unchecked")
	public Object query(Session session, Object parameter, 
			Hql hql, HqlDataProvider provider) throws Exception {
		Query query = createQuery(session, parameter, hql);
		ResultTransformer rtf = provider.getResultTransformer();
		if (rtf != null) {
			query.setResultTransformer(rtf);
		}
		
		if (!provider.isUnique()) {
			List<Object> entities = query.list();
			return entities;
		} else {
			return query.uniqueResult();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void query(Session session, Object parameter, Hql hql, 
			Page<?> page, HqlDataProvider provider) throws Exception {
		Query query = createQuery(session, parameter, hql);
		ResultTransformer rtf = provider.getResultTransformer();
		if (rtf != null) {
			query.setResultTransformer(rtf);
		}
		
		if (provider.isUnique()) {
			Object object = query.uniqueResult();
			if (object != null) {
				List list = new ArrayList(1);
				list.add(object);
				page.setEntities(list);
				page.setEntityCount(list.size());
			}
		} else {
			List entities = null;
			if (page.getPageSize() > 0) {
				query.setFirstResult(page.getFirstEntityIndex());
				query.setMaxResults(page.getPageSize());
				
				entities = query.list();
				page.setEntities(entities);
			} else {
				entities = query.list();
				page.setEntities(entities);
			}
			
			if (page.getPageSize() > 0) {
				int entityCount = count(session, parameter, hql, provider);
				page.setEntityCount(entityCount);
			} else {
				page.setEntityCount(entities.size());
			}
		}
	}

	protected Query createQuery(Session session, Object parameter, Hql hql) 
	 	throws Exception{
		Assert.notNull(hql, "Hql must not be null.");
		
		String hqlClause = hql.getClause();
		Query query = session.createQuery(hqlClause);
		List<HqlVarExpr> hqlParameters = hql.getVarExprs();
		if (!hqlParameters.isEmpty()) {
			for (HqlVarExpr hp: hqlParameters) {
				int i = hp.getIndex();
				Object v = parameterResolver.parameterValue(parameter, hp);
				query.setParameter(i, v);
			}
		}
		return query;
	}

	public int count(Session session, Object parameter, Hql hql, 
			HqlDataProvider provider) throws Exception {
		String hqlClause = hql.getClause();
		String fromHql = hqlClause;
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");
		String countHqlClause = "select count(*) " + fromHql;
		
		Hql countHql = new Hql(countHqlClause);
		List<HqlVarExpr> hqlParameters = hql.getVarExprs();
		if (!hqlParameters.isEmpty()) {
			for (HqlVarExpr hp: hqlParameters) {
				countHql.addVarExpr(hp);
			}
		}
		Query query = createQuery(session, parameter, countHql);
		Number c = (Number)query.uniqueResult();
		return c.intValue();
	}

}
