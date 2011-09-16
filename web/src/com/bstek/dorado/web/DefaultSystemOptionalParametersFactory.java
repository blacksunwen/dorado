/**
 * 
 */
package com.bstek.dorado.web;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.bstek.dorado.core.Context;
import com.bstek.dorado.util.method.ParameterFactory;
import com.bstek.dorado.util.method.SystemOptionalParametersFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-16
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

		parametersFactory.add(new ParameterFactory() {
			public Object getParameter() {
				return DoradoContext.getAttachedRequest();
			}

			public String getParameterName() {
				return "request";
			}

			public Class<?> getParameterType() {
				return HttpServletRequest.class;
			}
		});
	}

	public Collection<ParameterFactory> getOptionalParameters() {
		return parametersFactory;
	}

}
