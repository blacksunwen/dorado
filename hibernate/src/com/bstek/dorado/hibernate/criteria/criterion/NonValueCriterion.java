package com.bstek.dorado.hibernate.criteria.criterion;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class NonValueCriterion extends SingleProperyCriterion {

	private OP op;

	// isNull !isNull isEmpty !isEmpty
	public static enum OP {
		isNull {
			public String toString() {
				return "null";
			}

			public Criterion criterion(String propertyName) {
				return Restrictions.isNull(propertyName);
			}
		},
		isNotNull {
			public String toString() {
				return "!null";
			}

			public Criterion criterion(String propertyName) {
				return Restrictions.isNotNull(propertyName);
			}
		},
		isEmpty {
			public String toString() {
				return "empty";
			}

			public Criterion criterion(String propertyName) {
				return Restrictions.isEmpty(propertyName);
			}
		},
		isNotEmpty {
			public String toString() {
				return "!empty";
			}

			public Criterion criterion(String propertyName) {
				return Restrictions.isNotEmpty(propertyName);
			}
		};

		public abstract Criterion criterion(String propertyName);

		public static OP value(String str) {
			if (StringUtils.isEmpty(str))
				return null;

			for (OP op : OP.values()) {
				if (op.toString().equals(str))
					return op;
			}

			throw new IllegalArgumentException("unknown op '" + str + "'.");
		}
	}

	@XmlProperty(parser = "spring:dorado.hibernate.nonValueCriterionOpParser", attributeOnly=true)
	@IdeProperty(enumValues="null,!null,empty,!empty")
	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		String propertyName = this.getPropertyName();
		NonValueCriterion.OP op = this.getOp();
		if (op != null) {
			return op.criterion(propertyName);
		} else {
			return null;
		}
	}
}
