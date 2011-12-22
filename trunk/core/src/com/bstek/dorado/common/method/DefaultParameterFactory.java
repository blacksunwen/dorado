package com.bstek.dorado.common.method;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-16
 */
public class DefaultParameterFactory implements ParameterFactory {
	public Object parameter;
	public String parameterName;
	public Class<?> parameterType;

	public DefaultParameterFactory(Object parameter, String parameterName,
			Class<?> parameterType) {
		this.parameter = parameter;
		this.parameterName = parameterName;
		this.parameterType = parameterType;
	}

	public Object getParameter() {
		return parameter;
	}

	public String getParameterName() {
		return parameterName;
	}

	public Class<?> getParameterType() {
		return parameterType;
	}

}
