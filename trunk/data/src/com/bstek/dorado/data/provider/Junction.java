package com.bstek.dorado.data.provider;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-9-5
 */
public abstract class Junction implements Criterion {
	private Collection<Criterion> criterions;

	public Collection<Criterion> getCriterions() {
		return criterions;
	}

	public void setCriterions(Collection<Criterion> criterions) {
		this.criterions = criterions;
	}

	public void addCriterion(Criterion criterion) {
		if (criterions == null) {
			criterions = new ArrayList<Criterion>();
		}
		criterions.add(criterion);
	}
}
