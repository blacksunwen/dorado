package com.bstek.dorado.hibernate.criteria.criterion;

import org.hibernate.criterion.Criterion;

public interface MisValueStrategy {
	Criterion criterion(IdEqCriterion defCri);
	Criterion criterion(SingleCriterion defCri);
	Criterion criterion(InCriterion defCri);
	Criterion criterion(SizeCriterion defCri);
	Criterion criterion(BetweenCriterion defCri, Object value1, Object value2);
}
