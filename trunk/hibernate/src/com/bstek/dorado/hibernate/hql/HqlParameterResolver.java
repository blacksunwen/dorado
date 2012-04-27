package com.bstek.dorado.hibernate.hql;

public interface HqlParameterResolver {
	
	Object parameterValue(Object parameterObj, HqlVarExpr hqlParameter) 
		throws Exception;
}
