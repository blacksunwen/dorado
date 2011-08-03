package com.bstek.dorado.hibernate.criteria;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.hibernate.criteria.criterion.BaseCriterion;
import com.bstek.dorado.hibernate.criteria.criterion.MisValueStrategy;
import com.bstek.dorado.hibernate.criteria.order.Order;
import com.bstek.dorado.hibernate.criteria.parameter.ParameterExtractor;
import com.bstek.dorado.util.Assert;

public class DefaultHibernateCriteriaTransformer extends 
	BaseHibernateCriteriaTransformer {

	//-------------------------- Alias ---------------------------
	@Override
	protected void addAlias(DetachedCriteria criteria, Alias alias, 
			Object parameter, SessionFactory sessionFactory) 
		throws Exception{
		String associationPath = alias.getAssociationPath();
		String aliasName = alias.getAlias();
		JoinType joinType = alias.getJoinType();
		Criterion withClause = null;
		
		List<BaseCriterion> crs = alias.getCriterions();
		if (crs != null && crs.size() > 0) {
			List<Criterion> crs2 = new ArrayList<Criterion>(crs.size());
			for (BaseCriterion cr: crs) {
				if (cr.isAvailable()) {
					Criterion c = this.criterion(cr, parameter, sessionFactory);
					if (c != null) {
						crs2.add(c);
					}
				}
			}
			if (crs2.size() == 1) {
				withClause = crs2.get(0);
			} else if (crs2.size() > 1){
				Conjunction andJunc = Restrictions.conjunction();
				for (Criterion c: crs2) {
					andJunc.add(c);
				}
				withClause = andJunc;
			}
		}
		if (joinType != null) {
			joinType.alias(criteria, associationPath, aliasName, withClause);
		} else {
			criteria.createAlias(associationPath, aliasName);
		}
	}
	
	//-------------------------- Criterion ---------------------------
	private ParameterExtractor parameterExtractor;
	private MisValueStrategy misValueStrategy;
	
	public void setParameterExtractor(ParameterExtractor parameterExtractor) {
		this.parameterExtractor = parameterExtractor;
	}
	public ParameterExtractor getParameterExtractor () {
		return parameterExtractor;
	}
	
	public void setMisValueStrategy(MisValueStrategy misValueStrategy) {
		this.misValueStrategy = misValueStrategy;
	}
	public MisValueStrategy getMisValueStrategy() {
		return this.misValueStrategy;
	}
	
	public Object getValueFromParameter(Object parameter, String dataType, 
			Object value) throws Exception {
		Object value2 = null;
		String expr = parameterExtractor.getExpr(value);
		if (StringUtils.isNotEmpty(expr)) {
			value2 = parameterExtractor.expr(parameter, expr, dataType);
		} else {
			value2 = parameterExtractor.value(value, dataType);
		}
		return value2;
	}
	
	//-------------------------- Order ---------------------------
	@Override
	protected org.hibernate.criterion.Order toHibernate(Order def) {
		String propertyName = def.getPropertyName();
		Assert.notEmpty(propertyName, "parameter 'propertyName' must not be empty.");
		
		Order.Direction direction = def.getDirection();
		switch (direction) {
		case asc:
			return org.hibernate.criterion.Order.asc(propertyName);
		case desc:
			return org.hibernate.criterion.Order.desc(propertyName);
		}
		return null;
	}
	
	//-------------------------- FetchMode ---------------------------
	protected org.hibernate.FetchMode toHibernate(FetchMode def) {
		String associationPath = def.getAssociationPath();
		Assert.notEmpty(associationPath, "parameter 'associationPath' must not be empty.");
		
		FetchMode.Mode mode = def.getMode();
		switch (mode) {
		case DEFAULT:
			return org.hibernate.FetchMode.DEFAULT;
		case JOIN:
			return org.hibernate.FetchMode.JOIN;
		case SELECT:
			return org.hibernate.FetchMode.SELECT;
		}
		return null;
	}
}
