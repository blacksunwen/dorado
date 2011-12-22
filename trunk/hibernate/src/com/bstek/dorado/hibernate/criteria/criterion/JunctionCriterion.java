package com.bstek.dorado.hibernate.criteria.criterion;

import java.util.ArrayList;
import java.util.List;

import com.bstek.dorado.annotation.XmlSubNode;

public abstract class JunctionCriterion extends BaseCriterion {

	protected List<BaseCriterion> criterions = new ArrayList<BaseCriterion>();

	@XmlSubNode
	public List<BaseCriterion> getCriterions() {
		return criterions;
	}

	public void addCriterion(BaseCriterion criterion) {
		criterions.add(criterion);
	}
}
