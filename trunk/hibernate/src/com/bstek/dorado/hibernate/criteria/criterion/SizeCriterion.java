package com.bstek.dorado.hibernate.criteria.criterion;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class SizeCriterion extends SingleProperyCriterion {

	private Object value;
	private OP op;

	public static enum OP {
		eq {
			public String toString() {
				return "=";
			}

			public Criterion criterion(String propertyName, int size) {
				return Restrictions.sizeEq(propertyName, size);
			}
		},
		ne {
			public String toString() {
				return "<>";
			}

			public Criterion criterion(String propertyName, int size) {
				return Restrictions.sizeNe(propertyName, size);
			}
		},
		gt {
			public String toString() {
				return ">";
			}

			public Criterion criterion(String propertyName, int size) {
				return Restrictions.sizeGt(propertyName, size);
			}
		},
		lt {
			public String toString() {
				return "<";
			}

			public Criterion criterion(String propertyName, int size) {
				return Restrictions.sizeLt(propertyName, size);
			}
		},
		le {
			public String toString() {
				return "<=";
			}

			public Criterion criterion(String propertyName, int size) {
				return Restrictions.sizeLe(propertyName, size);
			}
		},
		ge {
			public String toString() {
				return ">=";
			}

			public Criterion criterion(String propertyName, int size) {
				return Restrictions.sizeGe(propertyName, size);
			}
		};

		public abstract Criterion criterion(String propertyName, int size);

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

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@XmlProperty(parser = "spring:dorado.hibernate.sizeCriterionOpParser")
	@IdeProperty(enumValues="=,<>,>,<,>=,<=")
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
		Object sizeObj = this.getValue();
		if (sizeObj != null) {
			Integer size = (Integer) transformer.getValueFromParameter(
					parameter, "Integer", sizeObj);
			if (size != null) {
				SizeCriterion.OP op = this.getOp();
				if (op != null) {
					return op.criterion(propertyName, size);
				} else {
					return null;
				}
			} else {
				return transformer.getMisValueStrategy().criterion(this);
			}
		} else {
			return transformer.getMisValueStrategy().criterion(this);
		}
	}
}
