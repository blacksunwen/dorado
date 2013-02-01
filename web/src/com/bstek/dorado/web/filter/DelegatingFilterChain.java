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

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.PathMatcher;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2013-1-24
 */
public class DelegatingFilterChain implements FilterChain {
	private List<DelegatingFilter> targetFilters;
	private int index = 0;
	private PathMatcher pathMatcher;
	private FilterChain realFilterChain;

	public DelegatingFilterChain(List<DelegatingFilter> targetFilters,
			PathMatcher pathMatcher, FilterChain realFilterChain) {
		this.targetFilters = targetFilters;
		this.pathMatcher = pathMatcher;
		this.realFilterChain = realFilterChain;
	}

	private String getPath(HttpServletRequest request) {
		return request.getRequestURI().substring(
				request.getContextPath().length());
	}

	public void doFilter(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		boolean matched = false;
		while (index < targetFilters.size()) {
			DelegatingFilter targetFilter = targetFilters.get(index);
			index++;
			String urlPattern = targetFilter.getUrlPattern();

			if (StringUtils.isNotEmpty(urlPattern)) {
				String path = getPath((HttpServletRequest) request);
				matched = pathMatcher.match(urlPattern, path);
			} else {
				matched = true;
			}

			if (matched) {
				targetFilter.doFilter((HttpServletRequest) request,
						(HttpServletResponse) response, this);
				break;
			}
		}

		if (index >= targetFilters.size()) {
			realFilterChain.doFilter(request, response);
		}
	}
}
