package com.bstek.dorado.jdbc.sql;

import java.util.Collections;
import java.util.List;

import com.bstek.dorado.jdbc.Dialect;

public class VarSql extends AbstractSql{

	private String clause;
	private List<VarExpr> varExprs = null;
	
	@SuppressWarnings("unchecked")
	public VarSql(String clause, List<VarExpr> varExprs, JdbcParameterSource parameterSource) {
		this.clause = clause;
		if (varExprs == null) {
			varExprs = Collections.EMPTY_LIST;
		}
		this.varExprs = varExprs;
		this.setParameterSource(parameterSource);
	}
	
	@Override
	public void setParameterSource(JdbcParameterSource parameterSource) {
		super.setParameterSource(parameterSource);
		JdbcParameterSource ps = this.getParameterSource();
		if (ps != null) {
			for (VarExpr varExpr: varExprs) {
				ps.setVarExpr(varExpr.getVarName(), varExpr);
			}
		}
	}

	public String getClause() {
		return clause;
	}

	public List<VarExpr> getVarExprs() {
		return varExprs;
	}

	@Override
	protected String toSQL(Dialect dialect) {
		return getClause();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clause == null) ? 0 : clause.hashCode());
		result = prime * result
				+ ((varExprs == null) ? 0 : varExprs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VarSql other = (VarSql) obj;
		if (clause == null) {
			if (other.clause != null)
				return false;
		} else if (!clause.equals(other.clause))
			return false;
		if (varExprs == null) {
			if (other.varExprs != null)
				return false;
		} else if (!varExprs.equals(other.varExprs))
			return false;
		return true;
	}

}
