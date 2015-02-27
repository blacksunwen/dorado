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

package com.bstek.dorado.web.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2015-2-28
 */
public class MockFilterConfig implements FilterConfig {
	private String filterName;
	private ServletContext servletContext;
	private Properties initParameters;

	public MockFilterConfig(String filterName, ServletContext servletContext,
			Properties initParameters) {
		this.filterName = filterName;
		this.servletContext = servletContext;
		this.initParameters = initParameters;
	}

	public String getFilterName() {
		return filterName;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getInitParameter(String name) {
		return (initParameters == null) ? null : initParameters
				.getProperty(name);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Enumeration getInitParameterNames() {
		return (initParameters == null) ? Collections
				.enumeration(Collections.EMPTY_LIST) : initParameters
				.propertyNames();
	}

}
