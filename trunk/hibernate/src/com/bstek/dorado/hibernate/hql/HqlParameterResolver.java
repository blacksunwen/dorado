package com.bstek.dorado.hibernate.hql;

public interface HqlParameterResolver {
	
	Object parameterValue(Object parameterObj, HqlParameter hqlParameter) 
		throws Exception;
}
