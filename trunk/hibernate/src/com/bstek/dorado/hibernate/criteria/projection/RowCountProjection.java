package com.bstek.dorado.hibernate.criteria.projection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;

import com.bstek.dorado.annotation.XmlNode;

@XmlNode(nodeName="RowCountProjection")
public class RowCountProjection extends BaseProjection {
	
	@Override
	public Projection toHibernate(SessionFactory sessionFactory) {
		return Projections.rowCount();
	}
}
