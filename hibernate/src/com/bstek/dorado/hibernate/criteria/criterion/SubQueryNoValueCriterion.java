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

public class SubQueryNoValueCriterion extends BaseCriterion {
	private OP op;
	private TopCriteria innerCriteria;

	public static enum OP {
		exist {
			public String toString() {
				return "exists";
			}

			public Criterion criterion(DetachedCriteria dc) {
				return Subqueries.exists(dc);
			}
		},
		notExist {
			public String toString() {
				return "!exists";
			}

			public Criterion criterion(DetachedCriteria dc) {
				return Subqueries.notExists(dc);
			}
		};

		public abstract Criterion criterion(DetachedCriteria dc);

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

	@XmlProperty(parser = "spring:dorado.hibernate.subQueryNoValueCriterionOpParser")
	@IdeProperty(enumValues="exists,!exists")
	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	@XmlSubNode(fixed=true)
	public TopCriteria getCriteria() {
		return innerCriteria;
	}

	public void setCriteria(TopCriteria innerQuery) {
		this.innerCriteria = innerQuery;
	}

	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		TopCriteria innerCriteria = this.getCriteria();
		DetachedCriteria dc = transformer.toHibernate(innerCriteria, parameter,
				sessionFactory);

		SubQueryNoValueCriterion.OP op = this.getOp();
		if (op != null) {
			return op.criterion(dc);
		} else {
			return null;
		}
	}
}
