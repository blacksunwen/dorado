/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2011-2012 BSTEK Information Technology Limited. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial(http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.web.resolver;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

import com.bstek.dorado.util.Assert;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2010-7-15
 */
public class UriResolverMapping extends AbstractUrlHandlerMapping {

	@Override
	protected Object lookupHandler(String urlPath, HttpServletRequest request)
			throws Exception {
		Map<String, Object> handlerMap = getHandlerMap();
		Object handler = handlerMap.get(urlPath);
		if (handler == null) {
			for (Iterator<String> it = handlerMap.keySet().iterator(); it
					.hasNext();) {
				String registeredPath = it.next();
				if (getPathMatcher().match(registeredPath, urlPath)) {
					request.setAttribute("originalUrlPath", urlPath);
					handler = handlerMap.get(registeredPath);
					break;
				}
			}
		}
		return handler;
	}

	@Override
	public void registerHandler(String urlPath, Object handler) {
		Assert.notNull(urlPath, "URL path must not be null");
		if (!urlPath.startsWith("/")) {
			urlPath = "/" + urlPath;
		}
		super.registerHandler(urlPath, handler);
	}
}
