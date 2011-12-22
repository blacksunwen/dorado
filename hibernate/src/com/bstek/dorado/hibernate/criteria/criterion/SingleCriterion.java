package com.bstek.dorado.hibernate.criteria.criterion;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class SingleCriterion extends SingleProperyCriterion {

	private Object value;
	private String dataType;
	private OP op;

	public static enum OP {
		eq {
			public String toString() {
				return "=";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.eq(propertyName, value);
			}
		},
		ne {
			public String toString() {
				return "<>";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.ne(propertyName, value);
			}
		},
		gt {
			public String toString() {
				return ">";
			}

			public Criterion criterion(String propertyName, Object value2) {
				return Restrictions.gt(propertyName, value2);
			}
		},
		lt {
			public String toString() {
				return "<";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.lt(propertyName, value);
			}
		},
		le {
			public String toString() {
				return "<=";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.le(propertyName, value);
			}
		},
		ge {
			public String toString() {
				return ">=";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.ge(propertyName, value);
			}
		},

		like {
			public String toString() {
				return "like";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.like(propertyName, String.valueOf(value));
			}
		},
		likeStart {
			public String toString() {
				return "like%";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.like(propertyName, String.valueOf(value),
						MatchMode.START);
			}
		},
		likeEnd {
			public String toString() {
				return "%like";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.like(propertyName, String.valueOf(value),
						MatchMode.END);
			}
		},
		likeAnyWhere {
			public String toString() {
				return "%like%";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.like(propertyName, String.valueOf(value),
						MatchMode.ANYWHERE);
			}
		},

		ilike {
			public String toString() {
				return "ilike";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.ilike(propertyName, String.valueOf(value));
			}
		},
		ilikeStart {
			public String toString() {
				return "ilike%";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.ilike(propertyName, String.valueOf(value),
						MatchMode.START);
			}
		},
		ilikeEnd {
			public String toString() {
				return "%ilike";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.ilike(propertyName, String.valueOf(value),
						MatchMode.END);
			}
		},
		ilikeAnyWhere {
			public String toString() {
				return "%ilike%";
			}

			public Criterion criterion(String propertyName, Object value) {
				return Restrictions.ilike(propertyName, String.valueOf(value),
						MatchMode.ANYWHERE);
			}
		};

		public abstract Criterion criterion(String propertyName, Object value);

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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@XmlProperty(
			parser = "com.bstek.dorado.hibernate.criteria.criterion.SingleCriterionOpParser")
	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		String dataType = this.getDataType();
		SingleCriterion.OP op = this.getOp();
		String propertyName = this.getPropertyName();
		Object v1 = this.getValue();
		if (v1 != null) {
			Object value = transformer.getValueFromParameter(parameter,
					dataType, v1);
			if (value != null) {
				if (op != null) {
					return op.criterion(propertyName, value);
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
