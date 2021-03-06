package com.bstek.dorado.web.filter;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Benny Bao (mailto:benny.bao@bstek.com)
 * @since 2014年12月5日
 */
public class FilterProxy extends DelegatingFilter {
	private Filter filter;
	private String name;
	private Properties initParameters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Properties getInitParameters() {
		return initParameters;
	}

	public void setInitParameters(Properties initParameters) {
		this.initParameters = initParameters;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		filter.init(filterConfig);
	}

	@Override
	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		filter.doFilter(request, response, chain);
	}

}
