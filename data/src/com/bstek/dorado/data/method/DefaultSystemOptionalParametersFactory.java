package com.bstek.dorado.data.method;

import java.util.ArrayList;
import java.util.Collection;

import com.bstek.dorado.core.Context;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-11-23
 */
public class DefaultSystemOptionalParametersFactory implements
		SystemOptionalParametersFactory {
	private Collection<ParameterFactory> parametersFactory;

	public DefaultSystemOptionalParametersFactory() {
		parametersFactory = new ArrayList<ParameterFactory>();

		parametersFactory.add(new ParameterFactory() {
			public Object getParameter() {
				return Context.getCurrent();
			}

			public String getParameterName() {
				return "context";
			}

			public Class<?> getParameterType() {
				return Context.class;
			}
		});
	}

	public Collection<ParameterFactory> getOptionalParameters() {
		return parametersFactory;
	}

}
