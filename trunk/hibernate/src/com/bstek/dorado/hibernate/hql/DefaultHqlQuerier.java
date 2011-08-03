package com.bstek.dorado.hibernate.hql;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

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
	
	public Object query(Session session, Object parameter, 
			Hql hql, HqlDataProvider provider) throws Exception {
		Query query = createQuery(session, parameter, hql);
		if (!provider.isUnique()) {
			return query.list();
		} else {
			return query.uniqueResult();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void query(Session session, Object parameter, Hql hql, 
			Page<?> page, HqlDataProvider provider) throws Exception {
		if (provider.isUnique()) {
			Query query = createQuery(session, parameter, hql);
			Object object = query.uniqueResult();
			if (object != null) {
				List list = new ArrayList(1);
				list.add(object);
				page.setEntities(list);
				page.setEntityCount(list.size());
			}
		} else {
			if (page.getPageSize() > 0) {
				Query query = createQuery(session, parameter, hql);
				query.setFirstResult(page.getFirstEntityIndex());
				query.setFetchSize(page.getPageSize());
				
				List entities = query.list();
				page.setEntities(entities);
				
				int entityCount = count(session, parameter, hql, provider);
				page.setEntityCount(entityCount);	
			} else {
				Query query = createQuery(session, parameter, hql);
				List list = query.list();
				page.setEntities(list);
				page.setEntityCount(list.size());
			}
		}
	}

	protected Query createQuery(Session session, Object parameter, Hql hql) 
	 	throws Exception{
		Assert.notNull(hql);
		
		AutoFilterVar filter = hql.getFilter();
		String hqlClause = hql.getClause();
		Query query = session.createQuery(hqlClause);
		List<HqlParameter> hqlParameters = hql.getParameters();
		if (!hqlParameters.isEmpty()) {
			for (HqlParameter hp: hqlParameters) {
				int i = hp.getIndex();
				String expr = hp.getExpr();
				if (filter != null && AutoFilterVar.isHolder(expr)) {
					Object v = parameterResolver.filterValue(filter, hp);
					query.setParameter(i, v);
				} else {
					Object v = parameterResolver.parameterValue(parameter, hp);
					query.setParameter(i, v);
				}
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
		List<HqlParameter> hqlParameters = hql.getParameters();
		if (!hqlParameters.isEmpty()) {
			for (HqlParameter hp: hqlParameters) {
				countHql.addParameter(hp);
			}
		}
		Query query = createQuery(session, parameter, countHql);
		Number c = (Number)query.uniqueResult();
		return c.intValue();
	}

}
