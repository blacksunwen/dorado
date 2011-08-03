package com.bstek.dorado.hibernate.criteria.projection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import com.bstek.dorado.annotation.XmlNode;

@XmlNode(nodeName="PropertyProjection")
public class PropertyProjection extends SinglePropertyProjection {

	@Override
	public Projection toHibernate(SessionFactory sessionFactory) {
		String propertyName = this.getPropertyName();
		return Projections.property(propertyName);
	}
}
