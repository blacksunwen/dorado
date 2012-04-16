package com.bstek.dorado.hibernate.criteria.criterion;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.bstek.dorado.annotation.IdeProperty;
import com.bstek.dorado.annotation.XmlProperty;
import com.bstek.dorado.hibernate.criteria.HibernateCriteriaTransformer;

public class DoublePropertyCriterion extends BaseCriterion {

	private String propertyName1;
	private String propertyName2;
	private OP op;

	public static enum OP {
		eq {
			public String toString() {
				return "=";
			}

			public Criterion criterion(String pn1, String pn2) {
				return Restrictions.eqProperty(pn1, pn2);
			}
		},
		ne {
			public String toString() {
				return "<>";
			}

			public Criterion criterion(String pn1, String pn2) {
				return Restrictions.neProperty(pn1, pn2);
			}
		},
		gt {
			public String toString() {
				return ">";
			}

			public Criterion criterion(String pn1, String pn2) {
				return Restrictions.gtProperty(pn1, pn2);
			}
		},
		lt {
			public String toString() {
				return "<";
			}

			public Criterion criterion(String pn1, String pn2) {
				return Restrictions.ltProperty(pn1, pn2);
			}
		},
		le {
			public String toString() {
				return "<=";
			}

			public Criterion criterion(String pn1, String pn2) {
				return Restrictions.leProperty(pn1, pn2);
			}
		},
		ge {
			public String toString() {
				return ">=";
			}

			public Criterion criterion(String pn1, String pn2) {
				return Restrictions.geProperty(pn1, pn2);
			}
		};

		public abstract Criterion criterion(String pn1, String pn2);

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

	public String getPropertyName1() {
		return propertyName1;
	}

	public void setPropertyName1(String propertyName1) {
		this.propertyName1 = propertyName1;
	}

	public String getPropertyName2() {
		return propertyName2;
	}

	public void setPropertyName2(String propertyName2) {
		this.propertyName2 = propertyName2;
	}

	@XmlProperty(parser = "spring:dorado.hibernate.doublePropertyCriterionOpParser")
	@IdeProperty(enumValues="=,<>,>,<,<=,>=")
	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	public Criterion toHibernate(Object parameter,
			SessionFactory sessionFactory,
			HibernateCriteriaTransformer transformer) throws Exception {
		String pn1 = this.getPropertyName1();
		String pn2 = this.getPropertyName2();
		DoublePropertyCriterion.OP op = this.getOp();
		if (op != null) {
			return op.criterion(pn1, pn2);
		} else {
			return null;
		}
	}
}
