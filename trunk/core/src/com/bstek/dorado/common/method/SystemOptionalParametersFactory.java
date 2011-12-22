package com.bstek.dorado.common.method;

import java.util.Collection;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-16
 */
public interface SystemOptionalParametersFactory {
	
	public Collection<ParameterFactory> getOptionalParameters();
}
