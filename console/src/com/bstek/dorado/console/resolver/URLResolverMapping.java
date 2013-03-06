/*
 * This file is part of Dorado 7.x (http://dorado7.bsdn.org).
 * 
 * Copyright (c) 2002-2012 BSTEK Corp. All rights reserved.
 * 
 * This file is dual-licensed under the AGPLv3 (http://www.gnu.org/licenses/agpl-3.0.html) 
 * and BSDN commercial (http://www.bsdn.org/licenses) licenses.
 * 
 * If you are unsure which license is appropriate for your use, please contact the sales department
 * at http://www.bstek.com/contact.
 */

package com.bstek.dorado.console.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;

/**
 * @author Alex Tong(mailto:alex.tong@bstek.com)
 * @since 2010-7-15
 */
public class URLResolverMapping extends AbstractUrlHandlerMapping {
	private final Map<String, Object> urlMap = new HashMap<String, Object>();

	public void setMappings(Properties mappings) {
		CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
	}

	public void setUrlMap(Map<String, ?> urlMap) {
		this.urlMap.putAll(urlMap);
	}

	public Map<String, ?> getUrlMap() {
		return this.urlMap;
	}

	@Override
	public void initApplicationContext() throws BeansException {
		super.initApplicationContext();
		registerHandlers(this.urlMap);
	}

	protected void registerHandlers(Map<String, Object> urlMap)
			throws BeansException {
		if (urlMap.isEmpty()) {
			logger.warn("Neither 'urlMap' nor 'mappings' set on ConsoleUrlHandlerMapping");
		} else {
			for (Map.Entry<String, Object> entry : urlMap.entrySet()) {
				String url = entry.getKey();
				Object handler = entry.getValue();
				// Prepend with slash if not already present.
				if (!url.startsWith("/")) {
					url = "/" + url;
				}
				// Remove whitespace from handler bean name.
				if (handler instanceof String) {
					handler = ((String) handler).trim();
				}
				registerHandler(url, handler);
			}
		}
	}

	@Override
	protected Object lookupHandler(String urlPath, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return super.lookupHandler(urlPath, request);
	}

}
