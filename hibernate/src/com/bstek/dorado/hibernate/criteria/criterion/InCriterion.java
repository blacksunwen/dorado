package com.bstek.dorado.hibernate.criteria.criterion;

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class InCriterion extends SingleProperyCriterion {

	private Object value;
	private String dataType;
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public Criterion toHibernate(Object parameter, SessionFactory sessionFactory, 
			HibernateCriteriaTransformer transformer) throws Exception {
		String dataType = this.getDataType();
		String propertyName = this.getPropertyName();
		Object value = this.getValue();
		if (value != null) {
			Object value2 = transformer.getValueFromParameter(parameter, dataType, value);
			if (value2 != null) {
				if (value instanceof Collection) {
					return Restrictions.in(propertyName, (Collection<?>)value);
				} else if (value.getClass().isArray()) {
					return Restrictions.in(propertyName, (Object[])value);
				} else {
					return Restrictions.in(propertyName, new Object[]{value});
				}
			} else {
				return transformer.getMisValueStrategy().criterion(this);
			}
		} else {
			return transformer.getMisValueStrategy().criterion(this);
		}
	}
}
