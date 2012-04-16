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

public class SubQueryPropertyCriterion extends SingleProperyCriterion {

	private OP op;
	private TopCriteria innerQuery;

	public static enum OP {
		eq {
			public String toString() {
				return "=";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyEq(propertyName, dc);
			}
		},
		eqAll {
			public String toString() {
				return "=all";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyEqAll(propertyName, dc);
			}
		},
		ge {
			public String toString() {
				return ">=";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyGe(propertyName, dc);
			}
		},
		geAll {
			public String toString() {
				return ">=all";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyGtAll(propertyName, dc);
			}
		},
		geSome {
			public String toString() {
				return ">=some";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyGeSome(propertyName, dc);
			}
		},
		gt {
			public String toString() {
				return ">";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyGt(propertyName, dc);
			}
		},
		gtAll {
			public String toString() {
				return ">all";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyGtAll(propertyName, dc);
			}
		},
		gtSome {
			public String toString() {
				return ">some";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyGtSome(propertyName, dc);
			}
		},
		in {
			public String toString() {
				return "in";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyIn(propertyName, dc);
			}
		},
		le {
			public String toString() {
				return "<=";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyLe(propertyName, dc);
			}
		},
		leAll {
			public String toString() {
				return "<=all";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyLeAll(propertyName, dc);
			}
		},
		leSome {
			public String toString() {
				return "<=some";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyLeSome(propertyName, dc);
			}
		},
		lt {
			public String toString() {
				return "<";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyLt(propertyName, dc);
			}
		},
		ltAll {
			public String toString() {
				return "<all";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyLtAll(propertyName, dc);
			}
		},
		ltSome {
			public String toString() {
				return "<some";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyLtSome(propertyName, dc);
			}
		},
		ne {
			public String toString() {
				return "<>";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyNe(propertyName, dc);
			}
		},
		notIn {
			public String toString() {
				return "!in";
			}

			public Criterion criterion(String propertyName, DetachedCriteria dc) {
				return Subqueries.propertyNotIn(propertyName, dc);
			}
		};

		public abstract Criterion criterion(String propertyName,
				DetachedCriteria dc);

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

	@XmlProperty(parser = "spring:dorado.hibernate.subQueryPropertyCriterionOpParser")
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
		String propertyName = this.getPropertyName();
		TopCriteria innerCriteria = this.getCriteria();
		DetachedCriteria dc = transformer.toHibernate(innerCriteria, parameter,
				sessionFactory);

		SubQueryPropertyCriterion.OP op = this.getOp();
		if (op != null) {
			return op.criterion(propertyName, dc);
		} else {
			return null;
		}
	}
}
