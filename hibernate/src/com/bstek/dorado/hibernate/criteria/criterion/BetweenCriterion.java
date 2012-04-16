package com.bstek.dorado.hibernate.criteria.criterion;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class BetweenCriterion extends SingleProperyCriterion {

	private Object value1;
	private Object value2;
	private String dataType;

	@XmlProperty
	public Object getValue1() {
		return value1;
	}

	public void setValue1(Object value) {
		this.value1 = value;
	}

	@XmlProperty
	public Object getValue2() {
		return value2;
	}

	public void setValue2(Object value) {
		this.value2 = value;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		String dataType = this.getDataType();
		String propertyName = this.getPropertyName();
		Object v1 = this.getValue1();
		Object value1 = transformer.getValueFromParameter(parameter, dataType, v1);
		Object v2 = this.getValue2();
		Object value2 = transformer.getValueFromParameter(parameter, dataType, v2);
		if (value1 != null && value2 != null) {
			return Restrictions.between(propertyName, value1, value2);
		} else {
			return transformer.getMisValueStrategy().criterion(this, value1, value2);
		}
	}
}
