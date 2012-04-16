package com.bstek.dorado.hibernate.criteria.criterion;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Subqueries;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.annotation.XmlSubNode;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;
import com.bstek.dorado.hibernate.criteria.TopCriteria;

public class SubQueryValueCriterion extends BaseCriterion {
	private Object value;
	private String dataType;
	private OP op;
	private TopCriteria innerQuery;

	public static enum OP {
		eq {
			public String toString() {
				return "=";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.eq(value, dc);
			}
		},
		eqAll {
			public String toString() {
				return "=all";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.eqAll(value, dc);
			}
		},
		ge {
			public String toString() {
				return ">=";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.ge(value, dc);
			}
		},
		geAll {
			public String toString() {
				return ">=all";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.geAll(value, dc);
			}
		},
		geSome {
			public String toString() {
				return ">=some";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.geSome(value, dc);
			}
		},
		gt {
			public String toString() {
				return ">";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.gt(value, dc);
			}
		},
		gtAll {
			public String toString() {
				return ">all";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.gtAll(value, dc);
			}
		},
		gtSome {
			public String toString() {
				return ">some";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.gtSome(value, dc);
			}
		},
		in {
			public String toString() {
				return "in";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.in(value, dc);
			}
		},
		le {
			public String toString() {
				return "<=";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.le(value, dc);
			}
		},
		leAll {
			public String toString() {
				return "<=all";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.leAll(value, dc);
			}
		},
		leSome {
			public String toString() {
				return "<=some";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.leSome(value, dc);
			}
		},
		lt {
			public String toString() {
				return "<";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.lt(value, dc);
			}
		},
		ltAll {
			public String toString() {
				return "<all";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.ltAll(value, dc);
			}
		},
		ltSome {
			public String toString() {
				return "<some";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.ltSome(value, dc);
			}
		},
		ne {
			public String toString() {
				return "<>";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.ne(value, dc);
			}
		},
		notIn {
			public String toString() {
				return "!in";
			}

			public Criterion criterion(Object value, DetachedCriteria dc) {
				return Subqueries.notIn(value, dc);
			}
		};

		public abstract Criterion criterion(Object value, DetachedCriteria dc);

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

	@XmlProperty(parser = "spring:dorado.hibernate.subQueryValueCriterionOpParser")
	@IdeProperty(enumValues="=,=all,>=,>=all,>=some,>,>all,>some,in,<=,<=all,<=some,<,<all,<some,<>,!in")
	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	@XmlSubNode(fixed=true)
	public TopCriteria getCriteria() {
		return innerQuery;
	}

	public void setCriteria(TopCriteria innerQuery) {
		this.innerQuery = innerQuery;
	}

	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		String dataType = this.getDataType();
		Object v1 = this.getValue();
		Object value = transformer.getValueFromParameter(parameter, dataType,
				v1);

		TopCriteria innerCriteria = this.getCriteria();
		DetachedCriteria dc = transformer.toHibernate(innerCriteria, parameter,
				sessionFactory);

		SubQueryValueCriterion.OP op = this.getOp();
		if (op != null) {
			return op.criterion(value, dc);
		} else {
			return null;
		}
	}
}
