package com.bstek.dorado.hibernate.criteria.projection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

public class CountProjection extends SinglePropertyProjection {

	private boolean distinct = false;

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	@Override
	public Projection toHibernate(SessionFactory sessionFactory) {
		String propertyName = this.getPropertyName();
		boolean distinct = this.isDistinct();
		
		if (!distinct) {
			return Projections.count(propertyName);
		} else {
			return Projections.countDistinct(propertyName);
		}
	}
}
