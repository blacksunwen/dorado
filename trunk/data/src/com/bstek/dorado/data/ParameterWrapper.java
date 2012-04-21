/**
 * 
 */
package com.bstek.dorado.data;

import java.util.Map;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2012-4-21
 */
public class ParameterWrapper {
	private Object parameter;
	private Map<String, Object> sysParameter;

	public ParameterWrapper(Object parameter, Map<String, Object> sysParameter) {
		this.parameter = parameter;
		this.sysParameter = sysParameter;
	}

	public Object getParameter() {
		return parameter;
	}

	public Map<String, Object> getSysParameter() {
		return sysParameter;
	}

}
