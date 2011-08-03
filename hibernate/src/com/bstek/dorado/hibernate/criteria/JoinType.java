package com.bstek.dorado.hibernate.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

public enum JoinType {
	INNER_JOIN {
		public int getFlag() {
			return Criteria.INNER_JOIN;
		}
	}, FULL_JOIN {
		public int getFlag() {
			return Criteria.FULL_JOIN;
		}
	}, LEFT_JOIN {
		public int getFlag() {
			return Criteria.LEFT_JOIN;
		}
	}, RIGHT_JOIN {
		public int getFlag() {
			return org.hibernate.sql.JoinFragment.RIGHT_OUTER_JOIN;
		}
	};
	
	public abstract int getFlag();
	
	public DetachedCriteria alias(DetachedCriteria criteria, 
			String associationPath, String aliasName, Criterion withCriterion) {
		if (withCriterion != null) {
			return criteria.createAlias(associationPath, aliasName, getFlag(), withCriterion);
		} else {
			return criteria.createAlias(associationPath, aliasName, getFlag());
		}
	}
}
