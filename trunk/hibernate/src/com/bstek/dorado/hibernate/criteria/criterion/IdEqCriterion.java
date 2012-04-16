package com.bstek.dorado.hibernate.criteria.criterion;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class IdEqCriterion extends BaseCriterion {

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

	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		String dataType = this.getDataType();
		Object value = this.getValue();
		if (value != null) {
			Object value2 = transformer.getValueFromParameter(parameter,
					dataType, value);
			if (value2 != null) {
				return Restrictions.idEq(value2);
			} 
		} 
		
		return transformer.getMisValueStrategy().criterion(this);
	}

}