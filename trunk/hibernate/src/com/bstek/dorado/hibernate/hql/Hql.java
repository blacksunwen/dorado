package com.bstek.dorado.hibernate.hql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bstek.dorado.util.Assert;

@SuppressWarnings( { "unchecked", "rawtypes" })
public class Hql {

	private static final List EMPTY_PARAMETER_MAP = Collections.EMPTY_LIST;

	private List<HqlParameter> parametets = EMPTY_PARAMETER_MAP;
	private String clause;
	
	public Hql(String clause) {
		Assert.notEmpty(clause);
		this.clause = clause;
	}

	public String getClause() {
		return clause;
	}

	public List<HqlParameter> getParameters() {
		return parametets;
	}

	public void addParameter(HqlParameter parameter) {
		if (parametets == EMPTY_PARAMETER_MAP) {
			parametets = new ArrayList<HqlParameter>();
		}
		parametets.add(parameter);
	}
}
