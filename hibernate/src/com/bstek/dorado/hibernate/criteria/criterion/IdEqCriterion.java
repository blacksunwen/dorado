package com.bstek.dorado.hibernate.criteria.criterion;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

@XmlNode(nodeName="IdEqCriterion")
public class IdEqCriterion extends BaseCriterion {

	private Object value;
	private String dataType;
	//private String misValue;

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
		Object value = this.getValue();
		if (value != null) {
			Object value2 = transformer.getValueFromParameter(parameter, dataType, value);
			if (value2 != null) {
				return Restrictions.idEq(value2);
			} else {
				return transformer.getMisValueStrategy().criterion(this);
			}
		} else {
			return transformer.getMisValueStrategy().criterion(this);
		}
	}
	
}