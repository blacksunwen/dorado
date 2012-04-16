package com.bstek.dorado.hibernate.criteria.criterion;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class DefaultMisValueStrategy implements MisValueStrategy {

	public Criterion criterion(IdEqCriterion defCri) {
		return null;
	}

	public Criterion criterion(SingleCriterion defCri) {
		return null;
	}

	public Criterion criterion(InCriterion defCri) {
		return null;
	}

	public Criterion criterion(SizeCriterion defCri) {
		return null;
	}

	public Criterion criterion(BetweenCriterion defCri, Object value1, Object value2) {
		if (value1 == null && value2 == null) {
			return null;
		}
		
		if (value1 != null) {
			return Restrictions.ge(defCri.getPropertyName(), value1);
		}
		if (value2 != null) {
			return Restrictions.le(defCri.getPropertyName(), value2);
		}
		return null;
	}

}
