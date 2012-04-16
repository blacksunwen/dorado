package com.bstek.dorado.hibernate.criteria.criterion;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

@XmlNode(implTypes = "com.bstek.dorado.hibernate.criteria.criterion.*")
public abstract class BaseCriterion {

	private boolean available = true;
	private boolean not = false;

	@ClientProperty(escapeValue="true")
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@ClientProperty(escapeValue="false")
	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

	public abstract Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception;
}
