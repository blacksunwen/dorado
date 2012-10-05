package com.bstek.dorado.view;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.bstek.dorado.data.method.ParameterFactory;
import com.bstek.dorado.web.DoradoContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2011-9-16
 */
public class DefaultSystemOptionalParametersFactory extends
		com.bstek.dorado.data.method.DefaultSystemOptionalParametersFactory {

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
