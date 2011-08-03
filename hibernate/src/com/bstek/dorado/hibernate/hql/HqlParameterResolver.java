package com.bstek.dorado.hibernate.hql;

public interface HqlParameterResolver {

	Object filterValue(AutoFilterVar filterVar, HqlParameter hqlParameter) 
	throws Exception;
	
	Object parameterValue(Object parameterObj, HqlParameter hqlParameter) 
		throws Exception;
}
