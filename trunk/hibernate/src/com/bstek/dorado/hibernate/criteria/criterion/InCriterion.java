package com.bstek.dorado.hibernate.criteria.criterion;

import java.util.Collection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class InCriterion extends SingleProperyCriterion {

	private Object value;
	private String dataType;
	
	@XmlProperty
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
	
	@SuppressWarnings("unchecked")
	public Criterion toHibernate(Object parameter, SessionFactory sessionFactory, 
			HibernateCriteriaTransformer transformer) throws Exception {
		String dataType = this.getDataType();
		String propertyName = this.getPropertyName();
		Object value = this.getValue();
		if (value != null) {
			Object value2 = transformer.getValueFromParameter(parameter, dataType, value);
			if (value2 != null) {
				if (value instanceof Collection) {
					Collection<Object> cValue = (Collection<Object>) value;
					if (!cValue.isEmpty()) {
						return Restrictions.in(propertyName, (Collection<?>)value);
					}
				} else if (value.getClass().isArray()) {
					Object[] aValue = (Object[])value;
					if (aValue.length > 0) {
						return Restrictions.in(propertyName, aValue);
					}
				} else {
					return Restrictions.in(propertyName, new Object[]{value});
				}
			} 
		} 
		
		return transformer.getMisValueStrategy().criterion(this);
	}
}
