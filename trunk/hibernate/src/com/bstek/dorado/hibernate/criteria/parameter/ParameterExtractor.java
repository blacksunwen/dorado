package com.bstek.dorado.hibernate.criteria.parameter;

public interface ParameterExtractor {

	Object expr(Object parameter, String expr, String dataTypeName) throws Exception;
	
	Object expr(Object parameter, String expr) throws Exception;
	
	String getExpr(Object expr);
	
	Object value(Object value, String dataTypeName) throws Exception;
}
