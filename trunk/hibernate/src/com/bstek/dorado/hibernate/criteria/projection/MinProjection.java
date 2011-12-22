package com.bstek.dorado.hibernate.criteria.projection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

public class MinProjection extends SinglePropertyProjection {
	
	@Override
	public Projection toHibernate(SessionFactory sessionFactory) {
		String propertyName = this.getPropertyName();
		return Projections.min(propertyName);
	}
}
