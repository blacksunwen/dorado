package com.bstek.dorado.util.method;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-16
 */
public interface ParameterFactory {
	public Object getParameter();

	public String getParameterName();

	public Class<?> getParameterType();
}
