package com.bstek.dorado.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bstek.dorado.core.el.ContextVarsInitializer;
import com.bstek.dorado.util.SingletonBeanFactory;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-11-9
 */
public class WebContextVarsInitializer implements ContextVarsInitializer {
	private static final Log logger = LogFactory
			.getLog(WebContextVarsInitializer.class);

	public void initializeContext(Map<String, Object> vars) {
		try {
			vars.put("configure", WebConfigure.getStore());
			HttpServletRequest request = DoradoContext.getAttachedRequest();
			vars.put("request", request);
			vars.put("session", (request != null) ? request.getSession(false)
					: null);
			vars.put("servletContext", DoradoContext.getAttachedServletContext());
			vars.put("web", SingletonBeanFactory
					.getInstance(WebExpressionUtilsObject.class));
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

}
