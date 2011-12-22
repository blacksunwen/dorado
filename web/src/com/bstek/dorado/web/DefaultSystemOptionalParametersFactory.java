/**
 * 
 */
package com.bstek.dorado.web;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.bstek.dorado.common.method.ParameterFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-16
 */
public class DefaultSystemOptionalParametersFactory extends
		com.bstek.dorado.common.method.DefaultSystemOptionalParametersFactory {

	public DefaultSystemOptionalParametersFactory() {
		Collection<ParameterFactory> parametersFactory = getOptionalParameters();

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

}
