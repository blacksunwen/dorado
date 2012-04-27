package com.bstek.dorado.hibernate.hql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings( { "unchecked", "rawtypes" })
public class Hql {

	private static final List EMPTY_PARAMETER_MAP = Collections.EMPTY_LIST;

	private List<HqlVarExpr> vars = EMPTY_PARAMETER_MAP;
	private String clause;
	
	public Hql(String clause) {
		this.clause = clause;
	}

	public String getClause() {
		return clause;
	}

	public List<HqlVarExpr> getVarExprs() {
		return vars;
	}

	public void addVarExpr(HqlVarExpr var) {
		if (vars == EMPTY_PARAMETER_MAP) {
			vars = new ArrayList<HqlVarExpr>();
		}
		vars.add(var);
	}
}
