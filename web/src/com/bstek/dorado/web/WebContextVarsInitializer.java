/*
 * This file is part of Dorado 7.x
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * http://dorado.bstek.com
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */
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
			try {
				HttpServletRequest request = DoradoContext.getAttachedRequest();
				vars.put("request", request);
				vars.put("req", new RequestWrapperMap(request));
				vars.put("param", new RequestParameterWrapperMap(request));
				vars.put("session",
						(request != null) ? request.getSession(false) : null);
				vars.put("servletContext",
						DoradoContext.getAttachedServletContext());
			} catch (UnsupportedOperationException e) {
				// do nothing
			}
			vars.put("web", SingletonBeanFactory
					.getInstance(WebExpressionUtilsObject.class));
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

}
