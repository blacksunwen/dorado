package com.bstek.dorado.hibernate.criteria.projection;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;

import com.bstek.dorado.annotation.ClientProperty;
import com.bstek.dorado.annotation.XmlNode;

@XmlNode(implTypes = "com.bstek.dorado.hibernate.criteria.projection.*")
public abstract class BaseProjection {
	private boolean available = true;
	private String alias;

	@ClientProperty(escapeValue="true")
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public abstract Projection toHibernate(SessionFactory sessionFactory);
}
